package ua.rudniev.taxi.dao.jdbc.utils;

import ua.rudniev.taxi.dao.common.field.Field;
import ua.rudniev.taxi.dao.common.field.FieldType;
import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.dao.common.filter.FilterType;
import ua.rudniev.taxi.dao.common.filter.Value;
import ua.rudniev.taxi.dao.common.order.OrderBy;
import ua.rudniev.taxi.dao.trip.TripOrderField;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ua.rudniev.taxi.dao.common.field.FieldType.*;
import static ua.rudniev.taxi.dao.trip.TripOrderField.CREATED;

public class QueryBuilder {

    private final Map<FilterType, String> filterTypeStringMap;

    public QueryBuilder() {
        filterTypeStringMap = new HashMap<>();
        filterTypeStringMap.put(FilterType.EQUALS, "=");
        filterTypeStringMap.put(FilterType.NOT_EQUALS, "<>");
        filterTypeStringMap.put(FilterType.MORE, ">");
        filterTypeStringMap.put(FilterType.LESS, "<");
    }

    public <T extends Field> String getFilterOrderAndLimitPart(List<Filter<T>> filters, List<OrderBy<T>> orderByList, FieldMapper<T> fieldMapper) {
        return getFilterPart(filters, fieldMapper) + getOrderPart(orderByList, fieldMapper) + getLimitPart();
    }

    private String getLimitPart() {
        return " limit ? offset ?";
    }

    public <T extends Field> void fillParams(
            PreparedStatement preparedStatement,
            List<Filter<T>> filters,
            int pageIndex,
            int pageSize
    ) throws SQLException {
        fillParams(preparedStatement, filters);
        preparedStatement.setInt(filters.size() + 1, pageSize);
        preparedStatement.setInt(filters.size() + 2, pageIndex);
    }

    public <T extends Field> void fillParams(
            PreparedStatement preparedStatement,
            List<Filter<T>> filters
    ) throws SQLException {
        for (int i = 0; i < filters.size(); i++) {
            Filter<T> filter = filters.get(i);
            fillStatement(preparedStatement, filter, i + 1);
        }
    }


    private <T extends Field> void fillStatement(PreparedStatement preparedStatement, Filter<T> filter, int index) throws SQLException {
        FieldType fieldType = filter.getField().getFieldType();
        Value value = filter.getValue();
        if (fieldType == INTEGER) {
            preparedStatement.setInt(index, value.getInteger());
        } else if (fieldType == STRING) {
            preparedStatement.setString(index, value.getString());
        } else if (fieldType == BIG_DECIMAL) {
            preparedStatement.setBigDecimal(index, value.getBigDecimal());
        } else if (fieldType == INSTANT) {
            Timestamp timestamp = Timestamp.from(value.getInstant());
            preparedStatement.setTimestamp(index, timestamp);
        }
    }

    public <T extends Field> String getFilterPart(List<Filter<T>> filters, FieldMapper<T> fieldMapper) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < filters.size(); i++) {
            Filter<T> filter = filters.get(i);
            if (i == 0) {
                sb.append(" WHERE");
            } else {
                sb.append(" AND");
            }
            String fieldName = fieldMapper.mapToSqlField(filter.getField());
            String operator = filterTypeStringMap.get(filter.getFilterType());
            sb.append(" ").append(fieldName).append(" ").append(operator).append(" ?");
        }
        return sb.toString();
    }

    private <T extends Field> String getOrderPart(List<OrderBy<T>> orderByList, FieldMapper<T> fieldMapper) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < orderByList.size(); i++) {
            OrderBy<T> orderBy = orderByList.get(0);
            if (i == 0) {
                sb.append(" ORDER BY");
            } else {
                sb.append(" ,");
            }
            String fieldName = fieldMapper.mapToSqlField(orderBy.getField());
            sb.append(" ").append(fieldName).append(" ").append(orderBy.getOrderByType());
        }
        return sb.toString();
    }


}

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

/**
 * This class has fields and methods for query building
 */
public class QueryBuilder {

    private final Map<FilterType, String> filterTypeStringMap;

    /**
     * This constructors creates HashMap of FilterType keys and values needed for jdbc
     */
    public QueryBuilder() {
        filterTypeStringMap = new HashMap<>();
        filterTypeStringMap.put(FilterType.EQUALS, "=");
        filterTypeStringMap.put(FilterType.NOT_EQUALS, "<>");
        filterTypeStringMap.put(FilterType.MORE, ">");
        filterTypeStringMap.put(FilterType.LESS, "<");
        filterTypeStringMap.put(FilterType.LESS_OR_EQUALS, "<=");
        filterTypeStringMap.put(FilterType.MORE_OR_EQUALS, ">=");
    }

    /**
     * This method appending filter, order and limit part of the query
     * @param filters This parameter has needed filters for the query
     * @param orderByList This parameter has needed sorting for the query
     * @param fieldMapper This parameter has needed field string for the query
     * @param <T> This parameter indicates which field needed
     * @return full query
     */
    public <T extends Field> String getFilterOrderAndLimitPart(List<Filter<T>> filters, List<OrderBy<T>> orderByList, FieldMapper<T> fieldMapper) {
        return getFilterPart(filters, fieldMapper) + getOrderPart(orderByList, fieldMapper) + getLimitPart();
    }

    /**
     * This method appending limit part to the query
     * @return String 'limit' and 'offset' part of the query
     */
    private String getLimitPart() {
        return " limit ? offset ?";
    }

    /**
     * This method calls another method and sets prepared statement values
     * @param preparedStatement This parameter is an unfilled prepared statement
     * @param filters This parameter has needed filters for the query
     * @param pageIndex This parameter indicates which page is needed (offset in SQL)
     * @param pageSize This parameter indicates quantity of return result (limit in SQL)
     * @param <T> This parameter indicates which field needed
     * @throws SQLException throws if something went wrong in jdbc
     */
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

    /**
     * This method calls another method
     * @param preparedStatement This parameter is an unfilled prepared statement
     * @param filters This parameter has needed filters for the query
     * @param <T> This parameter indicates which field needed
     * @throws SQLException throws if something went wrong in jdbc
     */
    public <T extends Field> void fillParams(
            PreparedStatement preparedStatement,
            List<Filter<T>> filters
    ) throws SQLException {
        for (int i = 0; i < filters.size(); i++) {
            Filter<T> filter = filters.get(i);
            fillStatement(preparedStatement, filter, i + 1);
        }
    }

    /**
     * This method sets values of prepared statement
     * @param preparedStatement This parameter is an unfilled prepared statement
     * @param filter This parameter indicates which set need to be used
     * @param index This parameter indicates which value need to be set
     * @param <T> This parameter indicates which field needed
     * @throws SQLException throws if something went wrong in jdbc
     */
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

    /**
     * This method building filter part of the query
     * @param filters This parameter has needed filters for the query
     * @param fieldMapper This parameter has needed field string for the query
     * @param <T> This parameter indicates which field needed
     * @return String 'where' part of the query
     */
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


    /**
     * This method building sorting part of the query
     * @param orderByList This parameter has needed sorting for the query
     * @param fieldMapper This parameter has needed field string for the query
     * @param <T> This parameter indicates which field needed
     * @return String 'order by' part of the query
     */
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

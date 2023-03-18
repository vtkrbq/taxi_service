package ua.rudniev.taxi.dao.jdbc.trip;

import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.dao.common.order.OrderBy;
import ua.rudniev.taxi.dao.jdbc.car.CarJdbcHelper;
import ua.rudniev.taxi.dao.jdbc.user.UserJdbcHelper;
import ua.rudniev.taxi.dao.jdbc.utils.PrepareStatementProvider;
import ua.rudniev.taxi.dao.jdbc.utils.QueryBuilder;
import ua.rudniev.taxi.dao.trip.TripOrderField;
import ua.rudniev.taxi.dao.trip.TripOrderDao;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.trip.AddressPoint;
import ua.rudniev.taxi.model.trip.TripOrder;
import ua.rudniev.taxi.model.user.User;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * This is implementation class of TripOrderDao interface that has fields and methods for working with jdbc
 */
public class TripOrderDaoImpl implements TripOrderDao {

    private final TripOrderFieldMapper tripOrderFieldMapper;

    private final CarJdbcHelper carJdbcHelper;

    private final UserJdbcHelper userJdbcHelper;

    private final QueryBuilder queryBuilder;

    private final PrepareStatementProvider prepareStatementProvider;

    public TripOrderDaoImpl(TripOrderFieldMapper tripOrderFieldMapper, CarJdbcHelper carJdbcHelper, UserJdbcHelper userJdbcHelper, QueryBuilder queryBuilder, PrepareStatementProvider prepareStatementProvider) {
        this.tripOrderFieldMapper = tripOrderFieldMapper;
        this.carJdbcHelper = carJdbcHelper;
        this.userJdbcHelper = userJdbcHelper;
        this.queryBuilder = queryBuilder;
        this.prepareStatementProvider = prepareStatementProvider;
    }

    @Override
    public List<TripOrder> findAllTripOrders(
            int pageIndex,
            int pageSize,
            List<OrderBy<TripOrderField>> orderByList,
            List<Filter<TripOrderField>> filters
    ) {
        String query = TripOrderSqlConstants.FIND_ALL_TRIP_ORDERS + queryBuilder.getFilterOrderAndLimitPart(filters, orderByList, tripOrderFieldMapper);
        return prepareStatementProvider.withPrepareStatement(query, (preparedStatement) -> {
            queryBuilder.fillParams(preparedStatement, filters, pageIndex, pageSize);
            ResultSet rs = preparedStatement.executeQuery();
            List<TripOrder> tripOrders = new ArrayList<>();
            while (rs.next()) {
                tripOrders.add(convertRowToTripOrder(rs));
            }
            return tripOrders;
        });
    }

    @Override
    public int getCountOfRecords(List<Filter<TripOrderField>> filters) {
        String query = TripOrderSqlConstants.GET_COUNT_OF_TRIP_ORDERS + queryBuilder.getFilterPart(filters, tripOrderFieldMapper);
        return prepareStatementProvider.withPrepareStatement(query, (preparedStatement) -> {
            queryBuilder.fillParams(preparedStatement, filters);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            return rs.getInt("count");
        });
    }

    @Override
    public void insert(TripOrder tripOrder) {
        prepareStatementProvider.withPrepareStatement(TripOrderSqlConstants.INSERT_TRIP_ORDER, (stmt) -> {
            stmt.setString(1, tripOrder.getDeparture().getAddress());
            stmt.setDouble(2, tripOrder.getDeparture().getX());
            stmt.setDouble(3, tripOrder.getDeparture().getY());
            stmt.setString(4, tripOrder.getDestination().getAddress());
            stmt.setDouble(5, tripOrder.getDestination().getX());
            stmt.setDouble(6, tripOrder.getDestination().getY());
            stmt.setString(7, tripOrder.getUser().getLogin());
            stmt.setInt(8, tripOrder.getCar().getId());
            stmt.setString(9, tripOrder.getCategory().toString());
            stmt.setInt(10, tripOrder.getCapacity());
            stmt.setBigDecimal(11, tripOrder.getPrice());
            stmt.setTimestamp(12, Timestamp.from(tripOrder.getTimestampCreated()));
            stmt.executeUpdate();
            return null;
        });
    }

    private TripOrder convertRowToTripOrder(ResultSet rs) throws SQLException {

        Instant timestampEnd;
        if (rs.getTimestamp(TripOrderSqlConstants.TripOrderFields.END_OF_TRIP) == null) timestampEnd = Instant.MIN;
        else timestampEnd = rs.getTimestamp(TripOrderSqlConstants.TripOrderFields.END_OF_TRIP).toInstant();

        AddressPoint departureAddressPoint = new AddressPoint(
                rs.getDouble(TripOrderSqlConstants.TripOrderFields.DEPARTURE_X),
                rs.getDouble(TripOrderSqlConstants.TripOrderFields.DEPARTURE_Y),
                rs.getString(TripOrderSqlConstants.TripOrderFields.DEPARTURE_ADDRESS)
        );
        AddressPoint destinationAddressPoint = new AddressPoint(
                rs.getDouble(TripOrderSqlConstants.TripOrderFields.DESTINATION_X),
                rs.getDouble(TripOrderSqlConstants.TripOrderFields.DESTINATION_Y),
                rs.getString(TripOrderSqlConstants.TripOrderFields.DESTINATION_ADDRESS)
        );
        Category category =  Category.valueOf(rs.getString(TripOrderSqlConstants.TripOrderFields.CATEGORY));
        int capacity = rs.getInt(TripOrderSqlConstants.TripOrderFields.CAPACITY);
        User client = userJdbcHelper.fillUser(rs, TripOrderSqlConstants.CLIENT_PREFIX);
        User driver = userJdbcHelper.fillUser(rs, TripOrderSqlConstants.DRIVER_PREFIX);
        Car car = carJdbcHelper.fillCar(rs, driver, TripOrderSqlConstants.CAR_PREFIX);
        BigDecimal price = rs.getBigDecimal(TripOrderSqlConstants.TripOrderFields.PRICE);
        Instant created = rs.getTimestamp(TripOrderSqlConstants.TripOrderFields.CREATED).toInstant();

        TripOrder tripOrder = new TripOrder(departureAddressPoint, destinationAddressPoint, category, capacity, client, car, price, created, timestampEnd);
        tripOrder.setId(rs.getInt(TripOrderSqlConstants.TripOrderFields.ID));

        return tripOrder;
    }

    @Override
    public void completeTripOrder(int id) {
        prepareStatementProvider.withPrepareStatement(TripOrderSqlConstants.COMPLETE_TRIP_VIA_TO, (stmt) -> {
            stmt.setTimestamp(1, Timestamp.from(Instant.now()));
            stmt.setInt(2, id);
            stmt.executeUpdate();
            return null;
        });
    }
}


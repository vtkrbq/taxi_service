package ua.rudniev.taxi.dao.trip;

import ua.rudniev.taxi.connection.SQLConstants;
import ua.rudniev.taxi.dao.utils.JdbcDaoUtils;
import ua.rudniev.taxi.exception.DbException;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.trip.AddressPoint;
import ua.rudniev.taxi.model.trip.TripOrder;
import ua.rudniev.taxi.model.user.User;
import ua.rudniev.taxi.transaction.HikariTransactionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripOrderDaoImpl implements TripOrderDao {

    @Override
    public List<TripOrder> findAllTripOrders(int pageIndex, int pageSize, String orderType, String orderBy, String filterBy, String filterKey) {
        if (orderType == null && orderBy == null && filterBy == null && filterKey == null)
            return findAllTripOrders(pageIndex, pageSize);
        List<TripOrder> tripOrders = new ArrayList<>();
        String query = createQueryWithSortAndFilter(orderType, orderBy, filterBy, filterKey);
        try (PreparedStatement stmt = JdbcDaoUtils.wrapSqlException(() ->
                HikariTransactionManager.getCurrentConnection().prepareStatement(query))) {
            if (filterBy == null && filterKey == null) {
                stmt.setInt(1, pageSize);
                stmt.setInt(2, pageIndex);
            } else {
                try {
                    int key = Integer.parseInt(filterKey);
                    stmt.setInt(1, key);
                    stmt.setInt(2, pageSize);
                    stmt.setInt(3, pageIndex);
                } catch (NumberFormatException nfe) {
                    stmt.setString(1, filterKey);
                    stmt.setInt(2, pageSize);
                    stmt.setInt(3, pageIndex);
                }
            }
            ResultSet rs = stmt.executeQuery();
            fillTripOrders(rs, tripOrders);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tripOrders;
    }


    @Override
    public int getCountOfRecords(String filterBy, String filterKey) {//TODO remake shit code
        int count = 0;
        if (filterKey == null) {
            try (PreparedStatement stmt = JdbcDaoUtils.wrapSqlException(() ->
                    HikariTransactionManager.getCurrentConnection().prepareStatement(SQLConstants.GET_COUNT_OF_TRIP_ORDERS))) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    count = rs.getInt("count");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            StringBuilder createQuery = new StringBuilder();
            createQuery.append(SQLConstants.GET_COUNT_OF_TRIP_ORDERS);
            if (checkSortAndFilterFields(filterBy)) createQuery.append(" where ").append(filterBy).append(" = ?");
            try (PreparedStatement stmt = JdbcDaoUtils.wrapSqlException(() ->
                    HikariTransactionManager.getCurrentConnection().prepareStatement(createQuery.toString()))) {
                try {
                    int key = Integer.parseInt(filterKey);
                    stmt.setInt(1, key);
                } catch (NumberFormatException nfe) {
                    stmt.setString(1, filterKey);
                }
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    count = rs.getInt("count");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    @Override
    public void insert(TripOrder tripOrder) {
        try (PreparedStatement stmt = JdbcDaoUtils.wrapSqlException(() ->
                HikariTransactionManager.getCurrentConnection().prepareStatement(SQLConstants.INSERT_TRIP_ORDER))) {
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
            stmt.setTimestamp(12, Timestamp.from(tripOrder.getTimestamp()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<TripOrder> findAllTripOrders(int pageIndex, int pageSize) {
        List<TripOrder> tripOrders = new ArrayList<>();
        try (PreparedStatement stmt = JdbcDaoUtils.wrapSqlException(() ->
                HikariTransactionManager
                        .getCurrentConnection()
                        .prepareStatement(
                                SQLConstants.FIND_ALL_TRIP_ORDERS))) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, pageIndex);
            ResultSet rs = stmt.executeQuery();
            fillTripOrders(rs, tripOrders);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tripOrders;
    }

    private void fillTripOrders(ResultSet rs, List<TripOrder> tripOrders) throws SQLException {
        while (rs.next()) {
            TripOrder tripOrder = new TripOrder(
                    new AddressPoint(
                            rs.getDouble(SQLConstants.TripOrderFields.DEPARTURE_X),
                            rs.getDouble(SQLConstants.TripOrderFields.DEPARTURE_Y),
                            rs.getString(SQLConstants.TripOrderFields.DEPARTURE_ADDRESS)),
                    new AddressPoint(
                            rs.getDouble(SQLConstants.TripOrderFields.DESTINATION_X),
                            rs.getDouble(SQLConstants.TripOrderFields.DESTINATION_Y),
                            rs.getString(SQLConstants.TripOrderFields.DESTINATION_ADDRESS)),
                    Category.valueOf(rs.getString(SQLConstants.TripOrderFields.CATEGORY)),
                    rs.getInt(SQLConstants.TripOrderFields.CAPACITY),
                    new User(
                            rs.getString(SQLConstants.TripOrderFields.USER_LOGIN),
                            rs.getString(SQLConstants.UserFields.FIRSTNAME),
                            rs.getString(SQLConstants.UserFields.LASTNAME),
                            rs.getString(SQLConstants.UserFields.PHONE),
                            rs.getString(SQLConstants.UserFields.EMAIL)
                    ),
                    new Car(
                            rs.getInt(SQLConstants.CarFields.ID),
                            new User(
                                    rs.getString(SQLConstants.CarFields.DRIVER_LOGIN),
                                    rs.getString(SQLConstants.UserFields.FIRSTNAME),
                                    rs.getString(SQLConstants.UserFields.LASTNAME),
                                    rs.getString(SQLConstants.UserFields.PHONE),
                                    rs.getString(SQLConstants.UserFields.EMAIL)
                            ),
                            rs.getString(SQLConstants.CarFields.CAR_NAME),
                            Category.valueOf(rs.getString(SQLConstants.CarFields.CAR_CATEGORY)),
                            rs.getInt(SQLConstants.CarFields.CAR_CAPACITY),
                            rs.getString(SQLConstants.CarFields.LICENSE_PLATE)
                    ),
                    rs.getBigDecimal(SQLConstants.TripOrderFields.PRICE),
                    rs.getTimestamp(SQLConstants.TripOrderFields.CREATED).toInstant());
            tripOrders.add(tripOrder);
        }
    }

    private boolean checkSortAndFilterFields(String...fields) {
        int count = 0;

        for (TableFields tf : TableFields.values()) {
            for (String f : fields) {
                if (f == null) count++;
                else if (f.equals(tf.getValue())) count++;
            }
        }
        return count == fields.length;
    }

    private String createQueryWithSortAndFilter(String orderType, String orderBy, String filterBy, String filterKey) {
        String str = "";
        if (orderBy != null && filterBy == null) {
            if (orderType != null) str = " order by " + orderBy + " " + orderType + " limit ? offset ?";
            else str = " order by " + orderBy + " asc " + " limit ? offset ?";
        }
        if (orderBy != null && filterBy != null && filterKey != null) {
            if (orderType != null)
                str = " where " + filterBy + " = ?" + " order by " + orderBy + " " + orderType + " limit ? offset ?";
            else str = " where " + filterBy + " = ?" + " order by " + orderBy + " asc " + " limit ? offset ?";
        }
        StringBuilder createFullQuery = new StringBuilder();
        createFullQuery.append(SQLConstants.FIND_ALL_TRIP_ORDERS_FOR_SORT_AND_FILTER);
        if (checkSortAndFilterFields(orderBy, filterBy)) {
            createFullQuery.append(str);
        } else throw new DbException("Cannot execute query");
        return createFullQuery.toString();
    }
}


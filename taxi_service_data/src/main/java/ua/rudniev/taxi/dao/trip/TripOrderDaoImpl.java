package ua.rudniev.taxi.dao.trip;

import ua.rudniev.taxi.connection.SQLConstants;
import ua.rudniev.taxi.dao.utils.JdbcDaoUtils;
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
    public List<TripOrder> findAllTripOrders(int pageIndex, int pageSize, String sortType, String sortBy, String filterBy, String filterKey) {
        List<TripOrder> tripOrders = new ArrayList<>();
        if (sortBy == null && sortType == null && filterBy == null & filterKey == null) {
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
        }
        return tripOrders;
    }

    @Override
    public int getCountOfRecords() {
        int count = 0;
        try (PreparedStatement stmt = JdbcDaoUtils.wrapSqlException(() ->
                HikariTransactionManager.getCurrentConnection().prepareStatement(SQLConstants.GET_COUNT_OF_TRIP_ORDERS))) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

    private void fillTripOrders(ResultSet rs, List<TripOrder> tripOrders) throws SQLException {
        while (rs.next()) {
            TripOrder tripOrder = new TripOrder();
            AddressPoint departure = new AddressPoint(
                    rs.getDouble(SQLConstants.TripOrderFields.DEPARTURE_X),
                    rs.getDouble(SQLConstants.TripOrderFields.DEPARTURE_Y),
                    rs.getString(SQLConstants.TripOrderFields.DEPARTURE_ADDRESS));
            tripOrder.setDeparture(departure);
            AddressPoint destination = new AddressPoint(
                    rs.getDouble(SQLConstants.TripOrderFields.DESTINATION_X),
                    rs.getDouble(SQLConstants.TripOrderFields.DESTINATION_Y),
                    rs.getString(SQLConstants.TripOrderFields.DESTINATION_ADDRESS));
            tripOrder.setDestination(destination);

            tripOrder.setCategory(Category.valueOf(rs.getString(SQLConstants.TripOrderFields.CATEGORY)));
            tripOrder.setCapacity(rs.getInt(SQLConstants.TripOrderFields.CAPACITY));

            User user = new User();
            user.setLogin(rs.getString(SQLConstants.TripOrderFields.USER_LOGIN));
            user.setFirstname(rs.getString(SQLConstants.UserFields.FIRSTNAME));
            user.setLastname(rs.getString(SQLConstants.UserFields.LASTNAME));
            user.setPhone(rs.getString(SQLConstants.UserFields.PHONE));
            user.setEmail(rs.getString(SQLConstants.UserFields.EMAIL));
            tripOrder.setUser(user);

            Car car = new Car();
            User driver = new User();
            driver.setLogin(rs.getString(SQLConstants.CarFields.DRIVER_LOGIN));
            driver.setFirstname(rs.getString(SQLConstants.UserFields.FIRSTNAME));
            driver.setLastname(rs.getString(SQLConstants.UserFields.LASTNAME));
            driver.setPhone(rs.getString(SQLConstants.UserFields.PHONE));
            driver.setEmail(rs.getString(SQLConstants.UserFields.EMAIL));
            car.setDriver(driver);
            car.setId(rs.getInt(SQLConstants.CarFields.ID));
            car.setCarName(rs.getString(SQLConstants.CarFields.CAR_NAME));
            car.setCarCategory(Category.valueOf(rs.getString(SQLConstants.CarFields.CAR_CATEGORY)));
            car.setCarCapacity(rs.getInt(SQLConstants.CarFields.CAR_CAPACITY));
            car.setLicensePlate(rs.getString(SQLConstants.CarFields.LICENSE_PLATE));
            tripOrder.setCar(car);


            tripOrder.setPrice(rs.getBigDecimal(SQLConstants.TripOrderFields.PRICE));
            tripOrder.setTimestamp(rs.getTimestamp(SQLConstants.TripOrderFields.CREATED).toInstant());
            tripOrders.add(tripOrder);
        }
    }
}


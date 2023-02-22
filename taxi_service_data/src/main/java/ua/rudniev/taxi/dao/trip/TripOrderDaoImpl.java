package ua.rudniev.taxi.dao.trip;

import ua.rudniev.taxi.dao.car.CarDaoImpl;
import ua.rudniev.taxi.dao.user.UserDaoImpl;
import ua.rudniev.taxi.dao.utils.JdbcDaoUtils;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.trip.AddressPoint;
import ua.rudniev.taxi.model.trip.TripOrder;
import ua.rudniev.taxi.model.user.Role;
import ua.rudniev.taxi.model.user.User;
import ua.rudniev.taxi.transaction.HikariTransactionManager;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TripOrderDaoImpl implements TripOrderDao {

    public static final String TABLE_NAME = "trip_order";

    public static class Fields {

        public static final String ID = "ID";

        public static final String DEPARTURE_ADDRESS = "DEPARTURE_ADDRESS";

        public static final String DEPARTURE_X = "DEPARTURE_X";

        public static final String DEPARTURE_Y = "DEPARTURE_Y";

        public static final String DESTINATION_ADDRESS = "DESTINATION_ADDRESS";

        public static final String DESTINATION_X = "DESTINATION_X";

        public static final String DESTINATION_Y = "DESTINATION_Y";

        public static final String CATEGORY = "TRIP_CATEGORY";

        public static final String CAPACITY = "TRIP_CAPACITY";

        public static final String USER_LOGIN = "USER_LOGIN";

        public static final String CAR_ID = "CAR_ID";

        public static final String PRICE = "PRICE";

        public static final String CREATED = "CREATED";
    }

    private static class Queries {
        private static final String INSERT_TRIP_ORDER = "INSERT INTO trip_order " +
                "(" + Fields.DEPARTURE_ADDRESS + ", " +
                Fields.DESTINATION_ADDRESS + ", " +
                Fields.USER_LOGIN + ", " +
                Fields.CAR_ID + ", " +
                Fields.CATEGORY + ", " +
                Fields.CAPACITY + ", " +
                Fields.PRICE + ", " +
                Fields.CREATED + ") " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        private static final String FIND_ALL_TRIP_ORDERS = "select " +
                TABLE_NAME + "." + Fields.DEPARTURE_ADDRESS + ", " +
                TABLE_NAME + "." + Fields.DEPARTURE_X + ", " +
                TABLE_NAME + "." + Fields.DEPARTURE_Y + ", " +
                TABLE_NAME + "." + Fields.DESTINATION_ADDRESS + ", " +
                TABLE_NAME + "." + Fields.DESTINATION_X + ", " +
                TABLE_NAME + "." + Fields.DESTINATION_Y + ", " +
                TABLE_NAME + "." + Fields.CATEGORY + ", " +
                TABLE_NAME + "." + Fields.CAPACITY + ", " +
                TABLE_NAME + "." + Fields.USER_LOGIN + ", " +
                "client." + UserDaoImpl.Fields.FIRSTNAME + ", " +
                "client." + UserDaoImpl.Fields.LASTNAME + ", " +
                "client." + UserDaoImpl.Fields.PHONE + ", " +
                "client." + UserDaoImpl.Fields.EMAIL + ", " +
                CarDaoImpl.TABLE_NAME + "." + CarDaoImpl.Fields.CAR_NAME + ", " +
                CarDaoImpl.TABLE_NAME + "." + CarDaoImpl.Fields.CAR_CATEGORY + ", " +
                CarDaoImpl.TABLE_NAME + "." + CarDaoImpl.Fields.CAR_CAPACITY + ", " +
                CarDaoImpl.TABLE_NAME + "." + CarDaoImpl.Fields.LICENSE_PLATE + ", " +
                CarDaoImpl.TABLE_NAME + "." + CarDaoImpl.Fields.DRIVER_LOGIN + ", " +
                "driver." + UserDaoImpl.Fields.FIRSTNAME + ", " +
                "driver." + UserDaoImpl.Fields.LASTNAME + ", " +
                "driver." + UserDaoImpl.Fields.PHONE + ", " +
                "driver." + UserDaoImpl.Fields.EMAIL + ", " +
                TABLE_NAME + "." + Fields.PRICE + ", " +
                TABLE_NAME + "." + Fields.CREATED +
                " from " + TABLE_NAME +
                " inner join " + CarDaoImpl.TABLE_NAME + " on " + TABLE_NAME + "." + Fields.CAR_ID + " = car.id " +
                " inner join " + UserDaoImpl.TABLE_NAME + " as client" +
                " on " + TABLE_NAME + "." + Fields.USER_LOGIN + " = client.login" +
                " inner join " + UserDaoImpl.TABLE_NAME + " as driver" +
                " on " + CarDaoImpl.TABLE_NAME + "." + CarDaoImpl.Fields.DRIVER_LOGIN + " = driver.login" +
                " limit ? " + "offset ?";
        private static final String GET_COUNT_OF_RECORDS = "select count(distinct(id)) from " + TABLE_NAME;

    }

    @Override
    public List<TripOrder> findAllTripOrders(int pageIndex, int pageSize) {
        List<TripOrder> tripOrders = new ArrayList<>();
        try (PreparedStatement stmt = JdbcDaoUtils.wrapSqlException(() ->
                HikariTransactionManager.getCurrentConnection().prepareStatement(Queries.FIND_ALL_TRIP_ORDERS))) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, pageIndex);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TripOrder tripOrder = new TripOrder();
                AddressPoint departure = new AddressPoint(
                        rs.getDouble(Fields.DEPARTURE_X),
                        rs.getDouble(Fields.DEPARTURE_Y),
                        rs.getString(Fields.DEPARTURE_ADDRESS));
                tripOrder.setDeparture(departure);
                AddressPoint destination = new AddressPoint(
                        rs.getDouble(Fields.DESTINATION_X),
                        rs.getDouble(Fields.DESTINATION_Y),
                        rs.getString(Fields.DESTINATION_ADDRESS));
                tripOrder.setDestination(destination);

                tripOrder.setCategory(Category.valueOf(rs.getString(Fields.CATEGORY)));
                tripOrder.setCapacity(rs.getInt(Fields.CAPACITY));

                User user = new User();
                user.setLogin(rs.getString(Fields.USER_LOGIN));
                user.setFirstname(rs.getString(UserDaoImpl.Fields.FIRSTNAME));
                user.setLastname(rs.getString(UserDaoImpl.Fields.LASTNAME));
                user.setPhone(rs.getString(UserDaoImpl.Fields.PHONE));
                user.setEmail(rs.getString(UserDaoImpl.Fields.EMAIL));
                tripOrder.setUser(user);

                Car car = new Car();
                User driver = new User();
                driver.setLogin(rs.getString(CarDaoImpl.Fields.DRIVER_LOGIN));
                driver.setFirstname(rs.getString(UserDaoImpl.Fields.FIRSTNAME));
                driver.setLastname(rs.getString(UserDaoImpl.Fields.LASTNAME));
                driver.setPhone(rs.getString(UserDaoImpl.Fields.PHONE));
                driver.setEmail(rs.getString(UserDaoImpl.Fields.EMAIL));
                car.setDriver(driver);
                car.setCarName(rs.getString(CarDaoImpl.Fields.CAR_NAME));
                car.setCarCategory(Category.valueOf(rs.getString(CarDaoImpl.Fields.CAR_CATEGORY)));
                car.setCarCapacity(rs.getInt(CarDaoImpl.Fields.CAR_CAPACITY));
                car.setLicensePlate(rs.getString(CarDaoImpl.Fields.LICENSE_PLATE));
                tripOrder.setCar(car);


                tripOrder.setPrice(rs.getBigDecimal(Fields.PRICE));
                tripOrder.setTimestamp(rs.getTimestamp(Fields.CREATED).toInstant());

                tripOrders.add(tripOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tripOrders;
    }

    @Override
    public int getCountOfRecords() {
        int count = 0;
        try (PreparedStatement stmt = JdbcDaoUtils.wrapSqlException(() ->
                HikariTransactionManager.getCurrentConnection().prepareStatement(Queries.GET_COUNT_OF_RECORDS))) {
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
                HikariTransactionManager.getCurrentConnection().prepareStatement(Queries.INSERT_TRIP_ORDER))) {
            stmt.setString(1, tripOrder.getDeparture().getAddress());
            stmt.setString(2, tripOrder.getDestination().getAddress());
            stmt.setString(3, tripOrder.getUser().getLogin());
            stmt.setInt(4, tripOrder.getCar().getId());
            stmt.setString(5, tripOrder.getCategory().toString());
            stmt.setInt(6, tripOrder.getCapacity());
            stmt.setBigDecimal(7, tripOrder.getPrice());
            stmt.setTimestamp(8, Timestamp.from(tripOrder.getTimestamp()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

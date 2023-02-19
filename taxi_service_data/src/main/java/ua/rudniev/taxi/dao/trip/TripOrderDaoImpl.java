package ua.rudniev.taxi.dao.trip;

import ua.rudniev.taxi.dao.utils.JdbcDaoUtils;
import ua.rudniev.taxi.model.trip.TripOrder;
import ua.rudniev.taxi.transaction.HikariTransactionManager;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TripOrderDaoImpl implements TripOrderDao {

    public static final String TABLE_NAME = "trip_order";

    public static class Fields {

        public static final String ID = "ID";

        public static final String DEPARTURE_ADDRESS = "DEPARTURE_ADDRESS";

        public static final String DESTINATION_ADDRESS = "DESTINATION_ADDRESS";

        public static final String USER_LOGIN = "USER_LOGIN";

        public static final String CAR_ID = "CAR_ID";

        public static final String CATEGORY = "TRIP_CATEGORY";

        public static final String CAPACITY = "TRIP_CAPACITY";

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
            HikariTransactionManager.getCurrentConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

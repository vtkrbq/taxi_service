package ua.rudniev.taxi.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class has fields and methods that provides connection to PostgreSQL Data Base
**/
public class ConnectionProvider {
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        config.setDriverClassName("org.postgresql.Driver"); //TODO: Пирог: вынести в проперти. Это вроде нужно было в условиях
        config.setJdbcUrl(System.getenv("TAXI_SERVICE_DB_URL"));
        config.setAutoCommit(false);
        config.setUsername(System.getenv("TAXI_SERVICE_DB_LOGIN"));
        config.setPassword(System.getenv("TAXI_SERVICE_DB_PASSWORD"));
        ds = new HikariDataSource(config);
    }
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private ConnectionProvider() {}
}

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
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/taxi_service");
        config.setAutoCommit(false);
        config.setUsername("vtkrbq");
        config.setPassword("PolotencE123");
        ds = new HikariDataSource(config);
    }
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private ConnectionProvider() {}
}

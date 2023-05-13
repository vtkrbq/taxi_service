package ua.rudniev.taxi.transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.rudniev.taxi.connection.ConnectionProvider;
import ua.rudniev.taxi.exception.DbException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class has fields and methods that are enable execute code in one transaction in jdbc via Hikari Connection Pool
 */
public class HikariTransactionManager implements TransactionManager {

    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    private static final Logger LOGGER = LogManager.getLogger(HikariTransactionManager.class);

    @Override
    public <T> T doInTransaction(Execution<T> execution, boolean readOnly) {
        Connection conn = getConnection();
        connectionHolder.set(conn);
        try (conn) {
            conn.setReadOnly(readOnly);
            T result;
            try {
                result = execution.execute();
            } catch (RuntimeException e) {
                LOGGER.error("An error occurred in transaction", e);
                conn.rollback();
                throw e;
            }
            conn.commit();
            return result;
        } catch (SQLException e) {
            LOGGER.error("An DB error occurred", e);
            throw new DbException("An DB error occurred", e);
        } finally {
            connectionHolder.remove();
        }
    }

    private Connection getConnection() {
        try {
            return ConnectionProvider.getConnection();
        } catch (SQLException e) {
            LOGGER.error("An error occurred with getting connection", e);
            throw new DbException("An error occurred with getting connection", e);
        }
    }

    public static Connection getCurrentConnection() {
        return connectionHolder.get();
    }

}

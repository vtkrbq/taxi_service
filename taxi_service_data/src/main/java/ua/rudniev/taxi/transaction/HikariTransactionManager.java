package ua.rudniev.taxi.transaction;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.rudniev.taxi.connection.ConnectionProvider;
import ua.rudniev.taxi.exception.DbException;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class HikariTransactionManager implements TransactionManager {

    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    private static final Logger LOGGER = LogManager.getLogger(HikariTransactionManager.class);

    @Override
    public <T> T doInTransaction(Execution<T> execution, boolean readOnly) {
        Connection conn = getConnection();
        connectionHolder.set(conn);
        try(conn) {
            conn.setReadOnly(readOnly);
            T result = execution.execute();
            conn.commit();
            return result;
        } catch (Exception e) {
            try {
                LOGGER.error("An error occurred in transaction", e);
                conn.rollback();
                if (e instanceof RuntimeException) throw (RuntimeException) e;
                LOGGER.error("An error occurred with DB", e);
                throw new DbException("An error occurred with DB", e);
            } catch (SQLException ex) {
                LOGGER.error("An error occurred while trying to rollback", ex);
                throw new DbException("An error occurred while trying to rollback", ex);
            }
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

    public static Connection getCurrentConnection()  {
        return connectionHolder.get();
    }

}

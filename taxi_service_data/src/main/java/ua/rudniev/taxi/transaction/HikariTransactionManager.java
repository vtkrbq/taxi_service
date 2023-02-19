package ua.rudniev.taxi.transaction;

import ua.rudniev.taxi.connection.ConnectionProvider;
import ua.rudniev.taxi.exception.DbException;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariTransactionManager implements TransactionManager {

    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();
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
                conn.rollback();
                throw new DbException("An error occurred with DB", e);
            } catch (SQLException ex) {
                throw new DbException("An error occurred with rollback", ex);
            }
        } finally {
            connectionHolder.remove();
        }
    }

    private Connection getConnection() {
        try {
            return ConnectionProvider.getConnection();
        } catch (SQLException e) {
            throw new DbException("An error occurred with getting connection", e);
        }
    }

    public static Connection getCurrentConnection()  {
        return connectionHolder.get();
    }

}

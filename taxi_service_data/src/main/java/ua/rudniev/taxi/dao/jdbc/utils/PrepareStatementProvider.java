package ua.rudniev.taxi.dao.jdbc.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.rudniev.taxi.exception.DbException;
import ua.rudniev.taxi.transaction.HikariTransactionManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class has fields and methods that provides other classes with result of prepared statement execution
 */
public class PrepareStatementProvider {
    private static final Logger LOGGER = LogManager.getLogger(PrepareStatementProvider.class);

    /**
     * This method executes given query in prepared statement
     * @param sql This parameter indicates query
     * @param execution block of code which will be executed
     * @param <T> class which will be returned after execution
     * @return result of prepared statement execution
     */
    public <T> T withPrepareStatement(String sql, Execution<T> execution) {
        try (PreparedStatement preparedStatement = HikariTransactionManager.getCurrentConnection().prepareStatement(sql)) {
             return execution.execute(preparedStatement);
        } catch (SQLException e) {
            LOGGER.error("Error while executing sql: " + sql, e);
            throw new DbException(e);
        }
    }

    /**
     * This interface is a implementation of Command Pattern
     * @param <T> class which will be returned after execution
     */
    public interface Execution<T> {
        T execute(PreparedStatement preparedStatement) throws SQLException;
    }
}

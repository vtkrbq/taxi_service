package ua.rudniev.taxi.dao.jdbc.utils;

import lombok.extern.slf4j.Slf4j;
import ua.rudniev.taxi.exception.DbException;
import ua.rudniev.taxi.transaction.HikariTransactionManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class PrepareStatementProvider {

    public <T> T withPrepareStatement(String sql, Execution<T> execution) {
        try (PreparedStatement preparedStatement = HikariTransactionManager.getCurrentConnection().prepareStatement(sql)) {
            return execution.execute(preparedStatement);
        } catch (SQLException e) {
            log.error("Error while executing sql: " + sql, e);
            throw new DbException(e);
        }
    }

    public interface Execution<T> {
        T execute(PreparedStatement preparedStatement) throws SQLException;
    }
}

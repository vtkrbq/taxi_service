package ua.rudniev.taxi.dao.utils;

import java.sql.SQLException;

public class JdbcDaoUtils {
    public static <T> T wrapSqlException(ExecutionWithSqlException<T> wrapper) {
        try {
            return wrapper.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public interface ExecutionWithSqlException<T> {
        T execute() throws SQLException;
    }
}

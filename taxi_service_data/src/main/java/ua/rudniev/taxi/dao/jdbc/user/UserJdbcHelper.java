package ua.rudniev.taxi.dao.jdbc.user;

import ua.rudniev.taxi.model.user.Role;
import ua.rudniev.taxi.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserJdbcHelper {

    public User fillUser(ResultSet resultSet, boolean fillRoles) throws SQLException {
        return fillUser(resultSet, fillRoles, "");
    }

    //TODO: Пирог: я думаю нужно будет убрать флаг fillRoles и всегда забирать все роли из БД
    public User fillUser(ResultSet resultSet, boolean fillRoles, String prefix) throws SQLException {
        User user = new User();
        user.setLogin(resultSet.getString(prefix + UserSqlConstants.UserFields.LOGIN));
        user.setFirstname(resultSet.getString(prefix + UserSqlConstants.UserFields.FIRSTNAME));
        user.setLastname(resultSet.getString(prefix + UserSqlConstants.UserFields.LASTNAME));
        user.setPhone(resultSet.getString(prefix + UserSqlConstants.UserFields.PHONE));
        user.setEmail(resultSet.getString(prefix + UserSqlConstants.UserFields.EMAIL));
        if (fillRoles) {
            String[] roles = resultSet.getString(prefix + UserSqlConstants.UserFields.ROLES).split(",");
            for (String r : roles) {
                user.addRole(Role.valueOf(r));
            }
        }
        return user;
    }

    public User fillUser(ResultSet resultSet) throws SQLException {
        return fillUser(resultSet, true);
    }

}

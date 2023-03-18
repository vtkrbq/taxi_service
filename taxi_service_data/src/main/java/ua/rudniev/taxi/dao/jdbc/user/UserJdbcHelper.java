package ua.rudniev.taxi.dao.jdbc.user;

import ua.rudniev.taxi.model.user.Role;
import ua.rudniev.taxi.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class has methods that filling object user with data from database
 */
public class UserJdbcHelper {

    /**
     * This method calling another method in this class with empty prefix
     *
     * @param resultSet This parameter has data of an object from database
     * @return filled object of a class User
     * @throws SQLException throws if something went wrong in database
     */
    public User fillUser(ResultSet resultSet) throws SQLException {
        return fillUser(resultSet, "");
    }

    /**
     * This method filling object user with data from database
     * @param resultSet This parameter has data of an object from database
     * @param prefix This parameter has string needed for database query
     * @return filled object of a class User
     * @throws SQLException throws if something went wrong in database
     */
    public User fillUser(ResultSet resultSet, String prefix) throws SQLException {
        User user = new User();
        user.setLogin(resultSet.getString(prefix + UserSqlConstants.UserFields.LOGIN));
        user.setFirstname(resultSet.getString(prefix + UserSqlConstants.UserFields.FIRSTNAME));
        user.setLastname(resultSet.getString(prefix + UserSqlConstants.UserFields.LASTNAME));
        user.setPhone(resultSet.getString(prefix + UserSqlConstants.UserFields.PHONE));
        user.setEmail(resultSet.getString(prefix + UserSqlConstants.UserFields.EMAIL));
        user.setDiscount(resultSet.getInt(prefix + UserSqlConstants.UserFields.DISCOUNT));
        String[] roles = resultSet.getString(prefix + UserSqlConstants.UserFields.ROLES).split(",");
        for (String r : roles) {
            user.addRole(Role.valueOf(r));
        }
        return user;
    }
}

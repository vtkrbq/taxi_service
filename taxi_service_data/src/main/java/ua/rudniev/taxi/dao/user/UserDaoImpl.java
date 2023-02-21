package ua.rudniev.taxi.dao.user;

import ua.rudniev.taxi.dao.utils.JdbcDaoUtils;
import ua.rudniev.taxi.dao.utils.PasswordEncryptionService;
import ua.rudniev.taxi.exception.DbException;
import ua.rudniev.taxi.exception.PasswordDoesNotMatchException;
import ua.rudniev.taxi.exception.UserAlreadyExistsException;
import ua.rudniev.taxi.model.user.Role;
import ua.rudniev.taxi.model.user.User;
import ua.rudniev.taxi.transaction.HikariTransactionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserDaoImpl implements UserDao {

    public static final String TABLE_NAME = "app_user";

    public static class Fields {

        public static final String LOGIN = "LOGIN";

        public static final String PASSHASH = "PASSHASH";

        public static final String FIRSTNAME = "FIRSTNAME";

        public static final String LASTNAME = "LASTNAME";

        public static final String PHONE = "PHONE";

        public static final String EMAIL = "EMAIL";

        public static final String ROLES = "ROLES";
    }

    private static class Queries {
        private static final String FIND_USER = "select " +
                Fields.LOGIN + ", " +
                Fields.FIRSTNAME + ", " +
                Fields.LASTNAME + ", " +
                Fields.PHONE + ", " +
                Fields.EMAIL + ", " +
                Fields.ROLES +
                " from " + TABLE_NAME + " where login=? and passhash=?";
        private static final String CREATE_USER = "insert into " + TABLE_NAME +
                " (" + Fields.LOGIN + ", " +
                Fields.PASSHASH + ", " +
                Fields.FIRSTNAME + ", " +
                Fields.LASTNAME + ", " +
                Fields.PHONE + ", " +
                Fields.EMAIL + ", " +
                Fields.ROLES + ") " +
                "values (?, ?, ?, ?, ?, ?, ?)";
        private static final String UPDATE_USER = "update " + TABLE_NAME +
                " set " + Fields.LOGIN + " = " + "?, " +
                Fields.FIRSTNAME + " = " + "?, " +
                Fields.LASTNAME + " = " + "?, " +
                Fields.PHONE + " = " + "?, " +
                Fields.EMAIL + " = " + "? " +
                "where login=?";
        private static final String UPDATE_PASSHASH = "update " + TABLE_NAME +
                " set " + Fields.PASSHASH + " = ? " + "where login=? and passhash=?";
        private static final String FIND_ALL_LOGINS = "select " + Fields.LOGIN +
                " from " + TABLE_NAME;
        private static final String CHECK_PASSWORD = "select " +
                Fields.LOGIN + ", " +
                Fields.PASSHASH +
                " from " + TABLE_NAME + " where login=? and passhash=?";
    }

    @Override
    public Optional<User> findUser(String login, String password) {
        User user = null;
        try (PreparedStatement stmt = JdbcDaoUtils.wrapSqlException(() ->
                HikariTransactionManager.getCurrentConnection().prepareStatement(Queries.FIND_USER))) {
            String encryptedPassword = new String(Base64.getEncoder().encode(PasswordEncryptionService.getEncryptedPassword(password)));
            stmt.setString(1, login);
            stmt.setString(2, encryptedPassword);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                user = new User();
                user.setLogin(rs.getString(Fields.LOGIN));
                user.setFirstname(rs.getString(Fields.FIRSTNAME));
                user.setLastname(rs.getString(Fields.LASTNAME));
                user.setPhone(rs.getString(Fields.PHONE));
                user.setEmail(rs.getString(Fields.EMAIL));
                String[] roles = rs.getString(Fields.ROLES).split(",");
                for (String r : roles) {
                    user.addRole(Role.valueOf(r));
                }
            }
        } catch (Exception e) {
            throw new DbException(e);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public void createUser(User user, String password) {
        try (PreparedStatement stmt = JdbcDaoUtils.wrapSqlException(() ->
                HikariTransactionManager.getCurrentConnection().prepareStatement(Queries.CREATE_USER))) {
            if (checkForExistingLogin(user.getLogin())) throw new UserAlreadyExistsException(user.getLogin());//check
            String encryptedPassword = new String(Base64.getEncoder().encode(PasswordEncryptionService.getEncryptedPassword(password)));
            stmt.setString(1, user.getLogin());
            stmt.setString(2, encryptedPassword);
            stmt.setString(3, user.getFirstname());
            stmt.setString(4, user.getLastname());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getEmail());
            stmt.setString(7, user.rolesToString());
            stmt.executeUpdate();
            HikariTransactionManager.getCurrentConnection().commit();
            //TODO доавить экеспешн
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    public void updateUser(User user, String login) {//TODO сделать лучше
        try (PreparedStatement stmt = JdbcDaoUtils.wrapSqlException(() ->
                HikariTransactionManager.getCurrentConnection().prepareStatement(Queries.UPDATE_USER))) {
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getFirstname());
            stmt.setString(3, user.getLastname());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, login);
            stmt.executeUpdate();
            HikariTransactionManager.getCurrentConnection().commit();
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    @Override
    public void updatePassword(User user, String oldPassword, String newPassword) {
        try (PreparedStatement stmt = JdbcDaoUtils.wrapSqlException(() ->
                HikariTransactionManager.getCurrentConnection().prepareStatement(Queries.UPDATE_PASSHASH))) {
            String encryptedOldPassword = new String(Base64.getEncoder().encode(PasswordEncryptionService.getEncryptedPassword(oldPassword)));
            String encryptedNewPassword = new String(Base64.getEncoder().encode(PasswordEncryptionService.getEncryptedPassword(newPassword)));
            stmt.setString(1, encryptedNewPassword);
            stmt.setString(2, user.getLogin());
            stmt.setString(3, encryptedOldPassword);
            stmt.executeUpdate();
            if (stmt.executeUpdate() == 0) throw new PasswordDoesNotMatchException();
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    //check
    private static boolean checkForExistingLogin(String login) {
        try (PreparedStatement stmt = JdbcDaoUtils.wrapSqlException(() ->
                HikariTransactionManager.getCurrentConnection().prepareStatement(Queries.FIND_ALL_LOGINS))){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getString(Fields.LOGIN).equals(login)) return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

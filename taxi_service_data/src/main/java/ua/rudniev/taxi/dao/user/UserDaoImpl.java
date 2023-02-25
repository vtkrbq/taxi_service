package ua.rudniev.taxi.dao.user;

import ua.rudniev.taxi.connection.SQLConstants;
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
    @Override
    public Optional<User> findUser(String login) {
        User user = null;
        try (PreparedStatement stmt = JdbcDaoUtils.wrapSqlException(() ->
                HikariTransactionManager.getCurrentConnection().prepareStatement(SQLConstants.FIND_USER))) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                user = new User();
                user.setLogin(rs.getString(SQLConstants.UserFields.LOGIN));
                user.setFirstname(rs.getString(SQLConstants.UserFields.FIRSTNAME));
                user.setLastname(rs.getString(SQLConstants.UserFields.LASTNAME));
                user.setPhone(rs.getString(SQLConstants.UserFields.PHONE));
                user.setEmail(rs.getString(SQLConstants.UserFields.EMAIL));
                String[] roles = rs.getString(SQLConstants.UserFields.ROLES).split(",");
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
    public Optional<User> authUser(String login, String password) {
        User user = null;
        try (PreparedStatement stmt = JdbcDaoUtils.wrapSqlException(() ->
                HikariTransactionManager.getCurrentConnection().prepareStatement(SQLConstants.AUTH_USER))) {
            String encryptedPassword = new String(Base64.getEncoder().encode(PasswordEncryptionService.getEncryptedPassword(password)));
            stmt.setString(1, login);
            stmt.setString(2, encryptedPassword);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                user = new User();
                user.setLogin(rs.getString(SQLConstants.UserFields.LOGIN));
                user.setFirstname(rs.getString(SQLConstants.UserFields.FIRSTNAME));
                user.setLastname(rs.getString(SQLConstants.UserFields.LASTNAME));
                user.setPhone(rs.getString(SQLConstants.UserFields.PHONE));
                user.setEmail(rs.getString(SQLConstants.UserFields.EMAIL));
                String[] roles = rs.getString(SQLConstants.UserFields.ROLES).split(",");
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
                HikariTransactionManager.getCurrentConnection().prepareStatement(SQLConstants.CREATE_USER))) {
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
                HikariTransactionManager.getCurrentConnection().prepareStatement(SQLConstants.UPDATE_USER))) {
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
                HikariTransactionManager.getCurrentConnection().prepareStatement(SQLConstants.UPDATE_PASSHASH))) {
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
                HikariTransactionManager.getCurrentConnection().prepareStatement(SQLConstants.FIND_ALL_LOGINS))){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getString(SQLConstants.UserFields.LOGIN).equals(login)) return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

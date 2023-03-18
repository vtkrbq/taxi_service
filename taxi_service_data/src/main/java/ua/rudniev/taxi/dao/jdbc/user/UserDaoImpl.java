package ua.rudniev.taxi.dao.jdbc.user;

import ua.rudniev.taxi.dao.jdbc.utils.PasswordEncryptionService;
import ua.rudniev.taxi.dao.jdbc.utils.PrepareStatementProvider;
import ua.rudniev.taxi.dao.user.UserDao;
import ua.rudniev.taxi.exception.PasswordDoesNotMatchException;
import ua.rudniev.taxi.exception.UserAlreadyExistsException;
import ua.rudniev.taxi.model.user.User;

import java.sql.ResultSet;
import java.util.*;

/**
 * This is implementation class of UserDao interface that has fields and methods for working with jdbc
 */
public class UserDaoImpl implements UserDao {

    private final UserJdbcHelper userJdbcHelper;

    private final PrepareStatementProvider prepareStatementProvider;

    private final PasswordEncryptionService passwordEncryptionService;

    public UserDaoImpl(UserJdbcHelper userJdbcHelper, PrepareStatementProvider prepareStatementProvider, PasswordEncryptionService passwordEncryptionService) {
        this.userJdbcHelper = userJdbcHelper;
        this.prepareStatementProvider = prepareStatementProvider;
        this.passwordEncryptionService = passwordEncryptionService;
    }

    @Override
    public Optional<User> findUser(String login) {
        return prepareStatementProvider.withPrepareStatement(UserSqlConstants.FIND_USER, stmt -> {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            User user = null;
            while (rs.next()) {
                user = userJdbcHelper.fillUser(rs);
            }
            return Optional.ofNullable(user);
        });
    }

    @Override
    public Optional<User> findUserByLoginAndPassword(String login, String password) {
        return prepareStatementProvider.withPrepareStatement(UserSqlConstants.AUTH_USER, stmt -> {
            String encryptedPassword = new String(Base64.getEncoder().encode(passwordEncryptionService.getEncryptedPassword(password)));
            stmt.setString(1, login);
            stmt.setString(2, encryptedPassword);
            ResultSet rs = stmt.executeQuery();
            User user = null;
            while (rs.next()) {
                user = userJdbcHelper.fillUser(rs);
            }
            return Optional.ofNullable(user);
        });
    }

    @Override
    public void createUser(User user, String password) {
        prepareStatementProvider.withPrepareStatement(UserSqlConstants.CREATE_USER, stmt -> {
            if (checkForExistingLogin(user.getLogin())) throw new UserAlreadyExistsException(user.getLogin());//check
            String encryptedPassword = new String(Base64.getEncoder().encode(passwordEncryptionService.getEncryptedPassword(password)));
            stmt.setString(1, user.getLogin());
            stmt.setString(2, encryptedPassword);
            stmt.setString(3, user.getFirstname());
            stmt.setString(4, user.getLastname());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getEmail());
            stmt.setString(7, user.rolesToString());
            stmt.executeUpdate();
            return null;
        });
    }

    public void updateUser(User user, String login) {
        prepareStatementProvider.withPrepareStatement(UserSqlConstants.UPDATE_USER, stmt -> {
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getFirstname());
            stmt.setString(3, user.getLastname());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.rolesToString());
            stmt.setInt(7, user.getDiscount());
            stmt.setString(8, login);
            stmt.executeUpdate();
            return null;
        });
    }

    @Override
    public void updatePassword(User user, String oldPassword, String newPassword) {
        prepareStatementProvider.withPrepareStatement(UserSqlConstants.UPDATE_PASSHASH, stmt -> {
            String encryptedOldPassword = new String(Base64.getEncoder().encode(passwordEncryptionService.getEncryptedPassword(oldPassword)));
            String encryptedNewPassword = new String(Base64.getEncoder().encode(passwordEncryptionService.getEncryptedPassword(newPassword)));
            stmt.setString(1, encryptedNewPassword);
            stmt.setString(2, user.getLogin());
            stmt.setString(3, encryptedOldPassword);
            stmt.executeUpdate();
            if (stmt.executeUpdate() == 0) throw new PasswordDoesNotMatchException();
            return null;
        });
    }

    //check
    private boolean checkForExistingLogin(String login) { // TODO: пирог: удали этот метод (БД по идее сама должна кидать ошибку,
        // если  мы попытеамся добавить пользователя с таким же логином) Тем более тут мы перебираем все записи - так делать не стоит
        // , если мы хотим проверить пользователя запросом, можем написать что-то типа SELECT * FROM USERS where login = ?.
        // И если будет хоть одна запись, значит пользователь существует
        return prepareStatementProvider.withPrepareStatement(UserSqlConstants.FIND_ALL_LOGINS, stmt -> {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getString(UserSqlConstants.UserFields.LOGIN).equals(login)) return true;
            }
            return false;
        });
    }

}

package ua.rudniev.taxi.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.rudniev.taxi.dao.user.UserDao;
import ua.rudniev.taxi.model.user.Role;
import ua.rudniev.taxi.model.user.User;
import ua.rudniev.taxi.transaction.TransactionManager;

import java.util.Optional;

/**
 * This class provides the flow between servlets and Dao implementation classes
 */
public class UserService {
    private final UserDao userDao;

    private final TransactionManager transactionManager;

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public UserService(
            UserDao userDao,
            TransactionManager transactionManager
    ) {
        this.userDao = userDao;
        this.transactionManager = transactionManager;
    }

    public Optional<User> findUser(String login) {
        return transactionManager.doInTransaction(() ->
            userDao.findUser(login), true);
    }

    public Optional<User> findUserByLoginAndPassword(String login, String password) {
        return transactionManager.doInTransaction(() ->
            userDao.findUserByLoginAndPassword(login, password), true);
    }

    public void createUser(User user, String password, boolean isDriver) {
        transactionManager.doInTransaction(() -> {
            user.addRole(Role.USER);
            if(isDriver) {
                user.addRole(Role.DRIVER);
            }
            userDao.createUser(user, password);
            LOGGER.info("New user " + user.getLogin() + " has been registered");
            return null;
        }, false);
    }

    public void updateUser(User user, String login) {
        transactionManager.doInTransaction(() -> {
            userDao.updateUser(user, login);
            LOGGER.info("User " + user.getLogin() + " has been updated");
            return null;
        }, false);
    }

    public void updatePassword(User user, String oldPassword, String newPassword) {
        transactionManager.doInTransaction(() -> {
            userDao.updatePassword(user, oldPassword, newPassword);
            LOGGER.info("Password for user " + user.getLogin() + " has been changed");
            return null;
        }, false);
    }
}

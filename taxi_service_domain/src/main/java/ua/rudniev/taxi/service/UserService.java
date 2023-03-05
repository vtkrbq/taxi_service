package ua.rudniev.taxi.service;

import ua.rudniev.taxi.model.user.Role;
import ua.rudniev.taxi.model.user.User;
import ua.rudniev.taxi.dao.user.UserDao;
import ua.rudniev.taxi.transaction.TransactionManager;

import java.util.Optional;

public class UserService {
    private final UserDao userDao;

    private final TransactionManager transactionManager;
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

    public void createUser(User user, String password) {
        transactionManager.doInTransaction(() -> {
            user.addRole(Role.USER);
            userDao.createUser(user, password);
            return null;
        }, false);
    }

    public void updateUser(User user, String login) {
        transactionManager.doInTransaction(() -> {
            userDao.updateUser(user, login);
            return null;
        }, false);
    }

    public void updatePassword(User user, String oldPassword, String newPassword) {
        transactionManager.doInTransaction(() -> {
            userDao.updatePassword(user, oldPassword, newPassword);
            return null;
        }, false);
    }
}

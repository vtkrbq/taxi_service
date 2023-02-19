package ua.rudniev.taxi.service;

import ua.rudniev.taxi.dao.car.CarDao;
import ua.rudniev.taxi.dao.trip.TripOrderDao;
import ua.rudniev.taxi.model.user.Role;
import ua.rudniev.taxi.model.user.User;
import ua.rudniev.taxi.dao.user.UserDao;
import ua.rudniev.taxi.service.price.PriceStrategy;
import ua.rudniev.taxi.transaction.TransactionManager;

import java.util.Optional;

public class UserService {
    private UserDao userDao;

    private TransactionManager transactionManager;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }
    public UserService(
            UserDao userDao,
            TransactionManager transactionManager
    ) {
        this.userDao = userDao;
        this.transactionManager = transactionManager;
    }

    public Optional<User> findUser(String login, String password) {
        return transactionManager.doInTransaction(() ->
            userDao.findUser(login, password), true);
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

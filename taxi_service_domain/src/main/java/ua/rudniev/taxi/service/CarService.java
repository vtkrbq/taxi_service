package ua.rudniev.taxi.service;

import ua.rudniev.taxi.dao.car.CarDao;
import ua.rudniev.taxi.dao.user.UserDao;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.user.Role;
import ua.rudniev.taxi.model.user.User;
import ua.rudniev.taxi.transaction.TransactionManager;

import java.util.List;

public class CarService {
    private CarDao carDao;

    private TransactionManager transactionManager;

    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }
    public CarService(
            CarDao carDao,
            TransactionManager transactionManager
    ) {
        this.carDao = carDao;
        this.transactionManager = transactionManager;
    }

    public List<Car> findAvailableCars(int capacity, Category category) {
        return transactionManager.doInTransaction(() -> carDao.findAvailableCars(capacity, category));
    }

    public void createCar(Car car, User user) {
        transactionManager.doInTransaction(() -> {
            carDao.createCar(car, user);
            return null;
        }, false);
    }

}

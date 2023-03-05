package ua.rudniev.taxi.service;

import ua.rudniev.taxi.dao.car.CarDao;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.transaction.TransactionManager;

import java.util.List;
import java.util.Optional;

public class CarService {
    private final CarDao carDao;

    private final TransactionManager transactionManager;

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

    public void createCar(Car car) {
        transactionManager.doInTransaction(() -> {
            carDao.createCar(car);
            return null;
        }, false);
    }

    public Optional<Car> findCarById(int id) {
        return transactionManager.doInTransaction(() ->
                carDao.findCarById(id), true);
    }

}

package ua.rudniev.taxi.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.rudniev.taxi.dao.car.CarDao;
import ua.rudniev.taxi.dao.car.CarField;
import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.trip.TripOrder;
import ua.rudniev.taxi.transaction.TransactionManager;

import java.util.List;
import java.util.Optional;

public class CarService {
    private final CarDao carDao;

    private final TransactionManager transactionManager;

    private static final Logger LOGGER = LogManager.getLogger(CarService.class);


    public CarService(
            CarDao carDao,
            TransactionManager transactionManager
    ) {
        this.carDao = carDao;
        this.transactionManager = transactionManager;
    }

    public List<Car> findAvailableCars(List<Filter<CarField>> filters) {
        return transactionManager.doInTransaction(() -> carDao.findAvailableCars(filters));
    }

    public void createCar(Car car) {
        transactionManager.doInTransaction(() -> {
            carDao.createCar(car);
            LOGGER.info("New car " + car + " has been registered");
            return null;
        }, false);
    }

    public Optional<Car> findCarById(int id) {
        return transactionManager.doInTransaction(() ->
                carDao.findCarById(id), true);
    }
}

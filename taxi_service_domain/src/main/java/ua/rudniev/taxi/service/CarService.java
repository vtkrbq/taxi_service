package ua.rudniev.taxi.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.rudniev.taxi.dao.car.CarDao;
import ua.rudniev.taxi.dao.car.CarField;
import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.trip.AddressPoint;
import ua.rudniev.taxi.transaction.TransactionManager;

import java.util.List;
import java.util.Optional;

/**
 * This class realize business logic related to cars
 */
public class CarService {
    private final CarDao carDao;

    private final TransactionManager transactionManager;

    private static final Logger LOGGER = LogManager.getLogger(CarService.class);

    private final double STARTING_POINT_X = 49.991981;
    private final double STARTING_POINT_Y = 36.328126;
    private final String STARTING_POINT_ADDRESS = "Харків, вул. Гвардійців-Широнінців, 5";

    public CarService(
            CarDao carDao,
            TransactionManager transactionManager
    ) {
        this.carDao = carDao;
        this.transactionManager = transactionManager;
    }

    /**
     * This method sets starting AddressPoint of a car and call method from CarDaoImplementation for creating car
     * @param car This parameter provides data for created car
     */
    public void createCar(Car car) {
        transactionManager.doInTransaction(() -> {
            car.setCurrentAddress(new AddressPoint(
                    STARTING_POINT_X,
                    STARTING_POINT_Y,
                    STARTING_POINT_ADDRESS));
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

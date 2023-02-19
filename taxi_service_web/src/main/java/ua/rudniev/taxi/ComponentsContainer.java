package ua.rudniev.taxi;

import ua.rudniev.taxi.dao.car.CarDao;
import ua.rudniev.taxi.dao.car.CarDaoImpl;
import ua.rudniev.taxi.dao.trip.TripOrderDao;
import ua.rudniev.taxi.dao.trip.TripOrderDaoImpl;
import ua.rudniev.taxi.dao.user.UserDao;
import ua.rudniev.taxi.dao.user.UserDaoImpl;
import ua.rudniev.taxi.service.CarService;
import ua.rudniev.taxi.service.OrderingService;
import ua.rudniev.taxi.service.UserService;
import ua.rudniev.taxi.service.price.PriceStrategy;
import ua.rudniev.taxi.service.price.PythagorasPriceStrategy;
import ua.rudniev.taxi.transaction.HikariTransactionManager;
import ua.rudniev.taxi.transaction.TransactionManager;

public class ComponentsContainer {
    private static ComponentsContainer instance;

    private UserService userService;

    private CarService carService;

    private UserDao userDao;

    private CarDao carDao;

    private PriceStrategy priceStrategy;

    private TransactionManager transactionManager;

    private OrderingService orderingService;

    private TripOrderDao tripOrderDao;

    private ComponentsContainer() {
    }

    public static synchronized ComponentsContainer getInstance() {
        if (instance == null) {
            instance = new ComponentsContainer();
        }
        return instance;
    }

    public synchronized UserService getUserService() {
        if (userService == null) {
            userService = new UserService(
                    getUserDao(),
                    getTransactionManager());
        }
        return userService;
    }

    public synchronized UserDao getUserDao() {
        if (userDao == null) {
            userDao = new UserDaoImpl();
        }
        return userDao;
    }

    public synchronized CarService getCarService() {
        if (carService == null) {
            carService = new CarService(
                    getCarDao(),
                    getTransactionManager());
        }
        return carService;
    }

    public synchronized CarDao getCarDao() {
        if (carDao == null) {
            carDao = new CarDaoImpl();
        }
        return carDao;
    }

    public CarDao getCar() {
        if (carDao == null) {
            carDao = new CarDaoImpl();
        }
        return carDao;
    }

    public PriceStrategy getPriceStrategy() {
        if (priceStrategy == null) {
            priceStrategy = new PythagorasPriceStrategy();
        }
        return priceStrategy;
    }

    public TransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = new HikariTransactionManager();
        }
        return transactionManager;
    }

    public synchronized OrderingService getOrderingService() {
        if (orderingService == null) {
            orderingService = new OrderingService(
                    getCar(),
                    getPriceStrategy(),
                    getTransactionManager(),
                    getTripOrderDao()
            );
        }
        return orderingService;
    }

    private TripOrderDao getTripOrderDao() {
        if (tripOrderDao == null) {
            tripOrderDao = new TripOrderDaoImpl();
        }
        return tripOrderDao;
    }
}

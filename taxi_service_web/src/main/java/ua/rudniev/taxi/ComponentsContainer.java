package ua.rudniev.taxi;

import ua.rudniev.taxi.dao.car.CarDao;
import ua.rudniev.taxi.dao.jdbc.car.CarDaoImpl;
import ua.rudniev.taxi.dao.jdbc.car.CarJdbcHelper;
import ua.rudniev.taxi.dao.jdbc.trip.TripOrderFieldMapper;
import ua.rudniev.taxi.dao.jdbc.user.UserJdbcHelper;
import ua.rudniev.taxi.dao.jdbc.utils.PasswordEncryptionService;
import ua.rudniev.taxi.dao.jdbc.utils.PrepareStatementProvider;
import ua.rudniev.taxi.dao.jdbc.utils.QueryBuilder;
import ua.rudniev.taxi.dao.trip.TripOrderDao;
import ua.rudniev.taxi.dao.jdbc.trip.TripOrderDaoImpl;
import ua.rudniev.taxi.dao.user.UserDao;
import ua.rudniev.taxi.dao.jdbc.user.UserDaoImpl;
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

    private UserJdbcHelper userJdbcHelper;

    private CarJdbcHelper carJdbcHelper;

    private PrepareStatementProvider prepareStatementProvider;

    private PasswordEncryptionService passwordEncryptionService;

    private TripOrderFieldMapper tripOrderFieldMapper;

    private QueryBuilder queryBuilder;

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
            userDao = new UserDaoImpl(
                    getUserJdbcHelper(),
                    getPrepareStatementProvider(),
                    getPasswordEncryptionService()
            );
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
            carDao = new CarDaoImpl(
                    getUserJdbcHelper(),
                    getCarJdbcHelper(),
                    getPrepareStatementProvider()
            );
        }
        return carDao;
    }

    public synchronized PriceStrategy getPriceStrategy() {
        if (priceStrategy == null) {
            priceStrategy = new PythagorasPriceStrategy();
        }
        return priceStrategy;
    }

    public synchronized TransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = new HikariTransactionManager();
        }
        return transactionManager;
    }

    public synchronized OrderingService getOrderingService() {
        if (orderingService == null) {
            orderingService = new OrderingService(
                    getCarDao(),
                    getPriceStrategy(),
                    getTransactionManager(),
                    getTripOrderDao()
            );
        }
        return orderingService;
    }

    private synchronized TripOrderDao getTripOrderDao() {
        if (tripOrderDao == null) {
            tripOrderDao = new TripOrderDaoImpl(
                    getTripOrderFieldMapper(),
                    getCarJdbcHelper(),
                    getUserJdbcHelper(),
                    getQueryBuilder(),
                    getPrepareStatementProvider()
            );
        }
        return tripOrderDao;
    }

    public synchronized UserJdbcHelper getUserJdbcHelper() {
        if (userJdbcHelper == null) {
            userJdbcHelper = new UserJdbcHelper();
        }
        return userJdbcHelper;
    }

    public synchronized CarJdbcHelper getCarJdbcHelper() {
        if (carJdbcHelper == null) {
            carJdbcHelper = new CarJdbcHelper();
        }
        return carJdbcHelper;
    }

    public synchronized PrepareStatementProvider getPrepareStatementProvider() {
        if (prepareStatementProvider == null) {
            prepareStatementProvider = new PrepareStatementProvider();
        }
        return prepareStatementProvider;
    }

    public synchronized PasswordEncryptionService getPasswordEncryptionService() {
        if (passwordEncryptionService == null) {
            passwordEncryptionService = new PasswordEncryptionService();
        }
        return passwordEncryptionService;
    }

    public synchronized TripOrderFieldMapper getTripOrderFieldMapper() {
        if (tripOrderFieldMapper == null) {
            tripOrderFieldMapper = new TripOrderFieldMapper();
        }
        return tripOrderFieldMapper;
    }

    public synchronized QueryBuilder getQueryBuilder() {
        if (queryBuilder == null) {
            queryBuilder = new QueryBuilder();
        }
        return queryBuilder;
    }
}

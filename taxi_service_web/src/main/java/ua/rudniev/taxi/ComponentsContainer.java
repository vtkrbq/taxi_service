package ua.rudniev.taxi;

import ua.rudniev.taxi.dao.car.CarDao;
import ua.rudniev.taxi.dao.jdbc.car.CarDaoImpl;
import ua.rudniev.taxi.dao.jdbc.car.CarFieldMapper;
import ua.rudniev.taxi.dao.jdbc.car.CarJdbcHelper;
import ua.rudniev.taxi.dao.jdbc.trip.TripOrderDaoImpl;
import ua.rudniev.taxi.dao.jdbc.trip.TripOrderFieldMapper;
import ua.rudniev.taxi.dao.jdbc.user.UserDaoImpl;
import ua.rudniev.taxi.dao.jdbc.user.UserJdbcHelper;
import ua.rudniev.taxi.dao.jdbc.utils.PasswordEncryptionService;
import ua.rudniev.taxi.dao.jdbc.utils.PrepareStatementProvider;
import ua.rudniev.taxi.dao.jdbc.utils.QueryBuilder;
import ua.rudniev.taxi.dao.trip.TripOrderDao;
import ua.rudniev.taxi.dao.user.UserDao;
import ua.rudniev.taxi.service.CarService;
import ua.rudniev.taxi.service.OrderingService;
import ua.rudniev.taxi.service.UserService;
import ua.rudniev.taxi.service.price.PriceStrategy;
import ua.rudniev.taxi.service.price.PythagorasPriceStrategy;
import ua.rudniev.taxi.service.time.TimeStrategy;
import ua.rudniev.taxi.servlet.utils.TripOrderServletUtils;
import ua.rudniev.taxi.transaction.HikariTransactionManager;
import ua.rudniev.taxi.transaction.TransactionManager;

/**
 * This is implementation of the Dependency-Injection pattern. This container holds instances of application components,
 * and provide methods to access them.
 * This class is a singleton.
 */
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

    private CarFieldMapper carFieldMapper;

    private QueryBuilder queryBuilder;

    private TripOrderServletUtils tripOrderServletUtils;

    private TimeStrategy timeStrategy;

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
                    getPrepareStatementProvider(),
                    getQueryBuilder(),
                    getCarFieldMapper()
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
                    getTimeStrategy(),
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

    public synchronized CarFieldMapper getCarFieldMapper() {
        if (carFieldMapper == null) {
            carFieldMapper = new CarFieldMapper();
        }
        return carFieldMapper;
    }

    public synchronized QueryBuilder getQueryBuilder() {
        if (queryBuilder == null) {
            queryBuilder = new QueryBuilder();
        }
        return queryBuilder;
    }

    public synchronized TripOrderServletUtils getTripOrderServletUtils() {
        if (tripOrderServletUtils == null) {
            tripOrderServletUtils = new TripOrderServletUtils(getOrderingService());
        }
        return tripOrderServletUtils;
    }

    public synchronized TimeStrategy getTimeStrategy() {
        if (timeStrategy == null) {
            timeStrategy = new TimeStrategy();
        }
        return timeStrategy;
    }
}

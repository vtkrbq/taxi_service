package ua.rudniev.taxi.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.rudniev.taxi.dao.car.CarField;
import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.dao.common.order.OrderBy;
import ua.rudniev.taxi.dao.jdbc.car.CarDaoImpl;
import ua.rudniev.taxi.dao.trip.TripOrderField;
import ua.rudniev.taxi.model.NewTripInfo;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Status;
import ua.rudniev.taxi.model.trip.AddressPoint;
import ua.rudniev.taxi.model.trip.TripOrder;
import ua.rudniev.taxi.dao.car.CarDao;
import ua.rudniev.taxi.dao.trip.TripOrderDao;
import ua.rudniev.taxi.service.price.PriceStrategy;
import ua.rudniev.taxi.transaction.TransactionManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * This class provides the flow between servlets and Dao implementation classes
 */
public class OrderingService {
    private final CarDao carDao;

    private final PriceStrategy priceStrategy;

    private final TransactionManager transactionManager;

    private final TripOrderDao tripOrderDao;

    private static final Logger LOGGER = LogManager.getLogger(OrderingService.class);

    public OrderingService(
            CarDao carRepository,
            PriceStrategy priceStrategy,
            TransactionManager transactionManager,
            TripOrderDao tripOrderDao
    ) {
        this.carDao = carRepository;
        this.priceStrategy = priceStrategy;
        this.transactionManager = transactionManager;
        this.tripOrderDao = tripOrderDao;
    }

    /**
     * This method find list of available cars, creating trip order via dao and creating info object for servlet
     * @param tripOrder This parameter provides data of trip order
     * @param distance This parameter provides distance of trip
     * @param departureAddress This parameter provides client location for eta
     * @param filters This parameter provides filters for trip order
     * @return Optional of NewTripInfo that contains full information of trip order
     */
    public Optional<NewTripInfo> findAndOrder(TripOrder tripOrder, double distance, AddressPoint departureAddress, List<Filter<CarField>> filters) {
        return transactionManager.doInTransaction(() -> {
            List<Car> cars = carDao.findAvailableCars(filters);
            Optional<NewTripInfo> newTripInfoOptional = cars
                    .stream()
                    .map(car -> new NewTripInfo(
                            car,
                            priceStrategy.calculatePrice(
                                distance,
                                car
                            ),
                            calculateEta(departureAddress, car.getCurrentAddress())))
                    .min(Comparator.comparing(NewTripInfo::getPrice));
            newTripInfoOptional.ifPresent(newTripInfo -> {
                        Car car = newTripInfo.getCar();
                        car.setStatus(Status.ON_ROUTE);
                        carDao.update(car);
                        tripOrder.setCar(car);
                        BigDecimal discount = newTripInfo.getPrice().multiply(BigDecimal.valueOf(tripOrder.getUser().getDiscount())).divide(BigDecimal.valueOf(100), RoundingMode.DOWN);
                        tripOrder.setPrice(newTripInfo.getPrice().subtract(discount));
                        tripOrderDao.insert(tripOrder);
                        LOGGER.info("Order " + tripOrder + " has been created" + newTripInfoOptional.get().getCar().getDriver().getLogin());
                    }
            );
            return newTripInfoOptional;
        }, false);
    }

    public List<TripOrder> findAllTripOrders(int pageIndex,
                                             int pageSize,
                                             List<OrderBy<TripOrderField>> orderByList,
                                             List<Filter<TripOrderField>> filters) {
        return transactionManager.doInTransaction(() ->
                tripOrderDao.findAllTripOrders(pageIndex, pageSize, orderByList, filters), true);
    }

    public int getCountOfRecords(List<Filter<TripOrderField>> filters) {
        return transactionManager.doInTransaction(() -> tripOrderDao.getCountOfRecords(filters), true);
    }

    public void completeTripOrder (int id, int carId) {
        transactionManager.doInTransaction(() -> {
            tripOrderDao.completeTripOrder(id);
            carDao.completeTrip(carId);
            LOGGER.info("Trip with id " + id + " has been completed");
            return null;
        }, false);
    }

    /**
     * This method calculate estimated time arrival of a car
     * @param client This parameter provides user of a trip order
     * @param driver This parameter provides driver of a trip order
     * @return int estimated time arrival in minutes
     */
    private int calculateEta(AddressPoint client, AddressPoint driver) {//TODO tyt??
        double t = ((Math.sqrt(
                Math.pow(driver.getX() - client.getX(), 2)
                        + Math.pow(driver.getY() - client.getY(), 2)
        ) * 100) / 60) * 15;
        return (int) (t * (Math.random() * (5 - 2) + 2));
    }
}

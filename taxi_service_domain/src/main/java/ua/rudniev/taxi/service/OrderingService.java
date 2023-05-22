package ua.rudniev.taxi.service;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.rudniev.taxi.dao.car.CarDao;
import ua.rudniev.taxi.dao.car.CarField;
import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.dao.common.order.OrderBy;
import ua.rudniev.taxi.dao.trip.TripOrderDao;
import ua.rudniev.taxi.dao.trip.TripOrderField;
import ua.rudniev.taxi.model.NewTripInfo;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Status;
import ua.rudniev.taxi.model.trip.TripOrder;
import ua.rudniev.taxi.service.price.PriceStrategy;
import ua.rudniev.taxi.service.time.TimeStrategy;
import ua.rudniev.taxi.transaction.TransactionManager;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * This class realize business logic related to trip orders
 */
public class OrderingService {
    private final CarDao carDao;

    private final PriceStrategy priceStrategy;

    private final TimeStrategy timeStrategy;

    private final TransactionManager transactionManager;

    private final TripOrderDao tripOrderDao;

    private static final Logger LOGGER = LogManager.getLogger(OrderingService.class);

    public OrderingService(
            CarDao carRepository,
            PriceStrategy priceStrategy,
            TimeStrategy timeStrategy,
            TransactionManager transactionManager,
            TripOrderDao tripOrderDao
    ) {
        this.carDao = carRepository;
        this.priceStrategy = priceStrategy;
        this.timeStrategy = timeStrategy;
        this.transactionManager = transactionManager;
        this.tripOrderDao = tripOrderDao;
    }

    /**
     * This method find list of available cars, creating trip order via dao and creating info object for servlet
     * @param tripOrder This parameter provides data of trip order
     * @param filters This parameter provides filters for trip order
     * @return Optional of NewTripInfo that contains full information of trip order
     */
    public Optional<NewTripInfo> findAndOrder(
            TripOrder tripOrder,
            List<Filter<CarField>> filters) {
        return transactionManager.doInTransaction(() -> {
            List<Car> cars = carDao.findCars(filters);
            Optional<NewTripInfo> newTripInfoOptional = cars
                    .stream()
                    .map(car -> new NewTripInfo(
                            car,
                            priceStrategy.calculatePrice(
                                tripOrder.getDistance(),
                                car
                            ),
                            timeStrategy.calculateEta(tripOrder.getDeparture(), car.getCurrentAddress())))
                    .min(Comparator.comparing(NewTripInfo::getPriceWithoutDiscount));
            newTripInfoOptional.ifPresent(newTripInfo -> {
                        Car car = newTripInfo.getCar();
                        car.setStatus(Status.ON_ROUTE);
                        carDao.update(car);
                        tripOrder.setCar(car);
                        BigDecimal discount = priceStrategy.calculateDiscountVolume(tripOrder.getUser(), newTripInfo);
                        BigDecimal finalPrice = newTripInfo.getPriceWithoutDiscount().subtract(discount);
                        tripOrder.setPrice(finalPrice);
                        newTripInfo.setPriceWithDiscount(finalPrice);
                        tripOrderDao.insert(tripOrder);
                        LOGGER.info("Order " + tripOrder + " has been created" + newTripInfoOptional.get().getCar()
                                .getDriver().getLogin());
                    }
            );
            return newTripInfoOptional;
        }, false);
    }

    public ImmutablePair<List<TripOrder>, Integer> findAllTripOrders(int pageIndex,
                                                                     int pageSize,
                                                                     List<OrderBy<TripOrderField>> orderByList,
                                                                     List<Filter<TripOrderField>> filters) {
        return transactionManager.doInTransaction(() -> {
            List<TripOrder> list = tripOrderDao.findAllTripOrders(pageIndex, pageSize, orderByList, filters);
            Integer count = tripOrderDao.getCountOfRecords(filters);
            return new ImmutablePair<>(list, count);
        }, true);
    }

    public void completeTripOrder(int id, int carId) {
        transactionManager.doInTransaction(() -> {
            Optional<TripOrder> tripOrderOptional = tripOrderDao.findTripOrderById(id);
            if (tripOrderOptional.isEmpty()) throw new RuntimeException();
            if (tripOrderOptional.get().getTimestampEnd() == null) {
                tripOrderOptional.get().setTimestampEnd(Instant.now());
                tripOrderDao.updateTripOrder(tripOrderOptional.get(), id);
            }
            Optional<Car> carOptional = carDao.findCarById(carId);
            if (carOptional.isEmpty()) throw new RuntimeException();
            if (carOptional.get().getStatus().equals(Status.ON_ROUTE)) {
                carOptional.get().setStatus(Status.AVAILABLE);
                carDao.update(carOptional.get());
            }
            LOGGER.info("Trip with id " + id + " has been completed");
            return null;
        }, false);
    }
}

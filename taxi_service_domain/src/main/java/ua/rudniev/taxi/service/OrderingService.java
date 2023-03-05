package ua.rudniev.taxi.service;

import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.dao.common.order.OrderBy;
import ua.rudniev.taxi.dao.trip.TripOrderField;
import ua.rudniev.taxi.model.NewTripInfo;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Status;
import ua.rudniev.taxi.model.trip.TripOrder;
import ua.rudniev.taxi.dao.car.CarDao;
import ua.rudniev.taxi.dao.trip.TripOrderDao;
import ua.rudniev.taxi.service.price.PriceStrategy;
import ua.rudniev.taxi.transaction.TransactionManager;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class OrderingService {
    private final CarDao carDao;

    private final PriceStrategy priceStrategy;

    private final TransactionManager transactionManager;

    private final TripOrderDao tripOrderDao;

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

    public Optional<NewTripInfo> findAndOrder(TripOrder tripOrder, double distance) {
        return transactionManager.doInTransaction(() -> {
            List<Car> cars = carDao.findAvailableCars(tripOrder.getCapacity(), tripOrder.getCategory());
            Optional<NewTripInfo> newTripInfoOptional = cars
                    .stream()
                    .map(car -> new NewTripInfo(car, priceStrategy.calculatePrice(
                            distance,
                            car
                    )))
                    .min(Comparator.comparing(NewTripInfo::getPrice));
            newTripInfoOptional.ifPresent(newTripInfo -> {
                        Car car = newTripInfo.getCar();
                        car.setStatus(Status.ON_ROUTE);
                        carDao.update(car);
                        tripOrder.setCar(car);
                        tripOrder.setPrice(newTripInfo.getPrice());
                        tripOrderDao.insert(tripOrder);
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
}

package ua.rudniev.taxi.dao.trip;

import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.dao.common.order.OrderBy;
import ua.rudniev.taxi.model.trip.TripOrder;

import java.util.List;

public interface TripOrderDao {
    List<TripOrder> findAllTripOrders(
            int pageIndex,
            int pageSize,
            List<OrderBy<TripOrderField>> orderByList,
            List<Filter<TripOrderField>> filters
    );
    void insert(TripOrder tripOrder);
    int getCountOfRecords(List<Filter<TripOrderField>> filters);
    void completeTripOrder (int id);
}

package ua.rudniev.taxi.dao.trip;

import ua.rudniev.taxi.model.trip.TripOrder;

import java.util.List;

public interface TripOrderDao {
    List<TripOrder> findAllTripOrders(int pageIndex, int pageSize);
    void insert(TripOrder tripOrder);
    int getCountOfRecords();
}

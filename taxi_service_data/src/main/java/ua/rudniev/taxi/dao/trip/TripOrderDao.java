package ua.rudniev.taxi.dao.trip;

import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.dao.common.order.OrderBy;
import ua.rudniev.taxi.model.trip.TripOrder;

import java.util.List;

/**
 * Interface class that has methods for working with repositories
 */
public interface TripOrderDao {
    /**
     * This method searching all trip orders in repository with pagination
     * @param pageIndex This parameter indicates which page will return (offset in SQL)
     * @param pageSize This parameter indicates quantity of result (limit in SQL)
     * @param orderByList This parameter has needed sorting for the query
     * @param filters This parameter has needed filters for the query
     * @return List of objects TripOrder from database
     */
    List<TripOrder> findAllTripOrders(
            int pageIndex,
            int pageSize,
            List<OrderBy<TripOrderField>> orderByList,
            List<Filter<TripOrderField>> filters
    );

    /**
     * This method creates trip order in repository
     * @param tripOrder This parameter contains data about created trip order
     */
    void insert(TripOrder tripOrder);

    /**
     * This method counts records from database
     * @param filters This parameter has filters for the query
     * @return int number of returning records from database
     */
    int getCountOfRecords(List<Filter<TripOrderField>> filters);

    /**
     * This method will complete trip for indicated trip order
     *
     * @param id This parameter indicates which trip order will be completed
     */
    void completeTripOrder (int id);
}

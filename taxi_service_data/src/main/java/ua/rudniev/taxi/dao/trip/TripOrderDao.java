package ua.rudniev.taxi.dao.trip;

import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.dao.common.order.OrderBy;
import ua.rudniev.taxi.model.trip.TripOrder;
import ua.rudniev.taxi.model.user.User;

import java.util.List;
import java.util.Optional;

/**
 * Interface class that has methods for working with repositories
 */
public interface TripOrderDao {

    /**
     * This method searching for trip order in repository with indicated id
     *
     * @param id This parameter is indicating which trip order will be returned
     * @return Optional of trip order with indicated id
     */
    Optional<TripOrder> findTripOrderById(int id);

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
     * This method updates trip order in repository
     * @param tripOrder This parameter contains data about updated trip order
     * @param id This parameter contains id of trip order which has to be updated
     */
    void updateTripOrder(TripOrder tripOrder, int id);

    /**
     * This method counts records from database
     * @param filters This parameter has filters for the query
     * @return int number of returning records from database
     */
    int getCountOfRecords(List<Filter<TripOrderField>> filters);
}

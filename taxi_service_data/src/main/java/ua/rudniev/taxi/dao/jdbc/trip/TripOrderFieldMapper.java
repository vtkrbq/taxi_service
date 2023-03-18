package ua.rudniev.taxi.dao.jdbc.trip;

import ua.rudniev.taxi.dao.jdbc.utils.FieldMapper;
import ua.rudniev.taxi.dao.trip.TripOrderField;

import java.util.HashMap;
import java.util.Map;

/**
 * This is implementation class of FieldMapper interface that has fields and methods for working with jdbc
 */
public class TripOrderFieldMapper implements FieldMapper<TripOrderField> {

    private final Map<TripOrderField, String> tripOrderFieldMap;

    public TripOrderFieldMapper() {
        tripOrderFieldMap = new HashMap<>();
        tripOrderFieldMap.put(TripOrderField.DEPARTURE_ADDRESS, "departure_address");
        tripOrderFieldMap.put(TripOrderField.DESTINATION_ADDRESS, "destination_address");
        tripOrderFieldMap.put(TripOrderField.CATEGORY, "trip_category");
        tripOrderFieldMap.put(TripOrderField.CAPACITY, "trip_capacity");
        tripOrderFieldMap.put(TripOrderField.CLIENT_LAST_NAME, "client.lastname");
        tripOrderFieldMap.put(TripOrderField.CAR_NAME, "car.name");
        tripOrderFieldMap.put(TripOrderField.PRICE, "trip_order.price");
        tripOrderFieldMap.put(TripOrderField.CREATED, "trip_order.created");
        tripOrderFieldMap.put(TripOrderField.DRIVER_LOGIN, "car.driver_login");
        tripOrderFieldMap.put(TripOrderField.CLIENT_LOGIN, "trip_order.user_login");
    }

    @Override
    public String mapToSqlField(TripOrderField tripOrderField) {
        return tripOrderFieldMap.get(tripOrderField);
    }

}

package ua.rudniev.taxi.dao.jdbc.car;

import ua.rudniev.taxi.dao.car.CarField;
import ua.rudniev.taxi.dao.jdbc.utils.FieldMapper;
import ua.rudniev.taxi.dao.trip.TripOrderField;

import java.util.HashMap;
import java.util.Map;

public class CarFieldMapper implements FieldMapper<CarField> {

    private final Map<CarField, String> carFieldMap;

    public CarFieldMapper() {
        carFieldMap = new HashMap<>();
        carFieldMap.put(CarField.NAME, "name");
        carFieldMap.put(CarField.CATEGORY, "category");
        carFieldMap.put(CarField.CAPACITY, "capacity");
        carFieldMap.put(CarField.LICENSE_PLATE, "license_plate");
        carFieldMap.put(CarField.DRIVER_LOGIN, "driver_login");
        carFieldMap.put(CarField.STATUS, "status");
        carFieldMap.put(CarField.CURRENT_ADDRESS, "current_address");
        carFieldMap.put(CarField.CURRENT_POINT_X, "current_point_x");
        carFieldMap.put(CarField.CURRENT_POINT_Y, "current_point_y");
        carFieldMap.put(CarField.DISCOUNT, "discount");
    }

    @Override
    public String mapToSqlField(CarField field) {
        return carFieldMap.get(field);
    }
}

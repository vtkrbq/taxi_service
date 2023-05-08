package ua.rudniev.taxi.dao.jdbc.car;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.rudniev.taxi.dao.car.CarField;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CarFieldMapperTests {

    @Test
    public void testMapToSqlField() {
        CarFieldMapper mapper = new CarFieldMapper();

        assertNotNull(mapper);
        assertEquals("name", mapper.mapToSqlField(CarField.NAME));
        assertEquals("category", mapper.mapToSqlField(CarField.CATEGORY));
        assertEquals("capacity", mapper.mapToSqlField(CarField.CAPACITY));
        assertEquals("license_plate", mapper.mapToSqlField(CarField.LICENSE_PLATE));
        assertEquals("driver_login", mapper.mapToSqlField(CarField.DRIVER_LOGIN));
        assertEquals("status", mapper.mapToSqlField(CarField.STATUS));
        assertEquals("current_address", mapper.mapToSqlField(CarField.CURRENT_ADDRESS));
        assertEquals("current_point_x", mapper.mapToSqlField(CarField.CURRENT_POINT_X));
        assertEquals("current_point_y", mapper.mapToSqlField(CarField.CURRENT_POINT_Y));
        assertEquals("discount", mapper.mapToSqlField(CarField.DISCOUNT));
    }
}

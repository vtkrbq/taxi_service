package ua.rudniev.taxi.dao.jdbc.car;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.rudniev.taxi.dao.car.CarField;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CarFieldMapperTests {

    private final CarFieldMapper mapper = new CarFieldMapper();

    public static Stream<Arguments> testMapToSqlFieldSource() {
        return Stream.of(
                Arguments.of("name", CarField.NAME),
                Arguments.of("category", CarField.CATEGORY),
                Arguments.of("capacity", CarField.CAPACITY),
                Arguments.of("license_plate", CarField.LICENSE_PLATE),
                Arguments.of("driver_login", CarField.DRIVER_LOGIN),
                Arguments.of("status", CarField.STATUS),
                Arguments.of("current_address", CarField.CURRENT_ADDRESS),
                Arguments.of("current_point_x", CarField.CURRENT_POINT_X),
                Arguments.of("current_point_y", CarField.CURRENT_POINT_Y),
                Arguments.of("discount", CarField.DISCOUNT)
        );
    }

    @MethodSource("testMapToSqlFieldSource")
    @ParameterizedTest
    public void testMapToSqlField(String name, CarField field) {
        assertThat(mapper.mapToSqlField(field)).isEqualTo(name);
    }
}

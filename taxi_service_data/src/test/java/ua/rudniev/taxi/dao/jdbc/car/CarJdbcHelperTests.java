package ua.rudniev.taxi.dao.jdbc.car;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.rudniev.taxi.dao.testUtils.CarDataProvider;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.user.User;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarJdbcHelperTests {
    CarDataProvider dp = new CarDataProvider();

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private CarJdbcHelper carJdbcHelper;

    @Test
    public void testFillCarWithPrefix() throws SQLException {
        User driver = dp.createUser();
        String prefix = "car_";
        Car car = dp.createCar();
        when(resultSet.getInt(prefix + CarSqlConstants.CarFields.ID)).thenReturn(dp.CAR_ID);
        when(resultSet.getString(prefix + CarSqlConstants.CarFields.NAME)).thenReturn(dp.CAR_NAME);
        when(resultSet.getString(prefix + CarSqlConstants.CarFields.CATEGORY)).thenReturn(dp.CAR_CATEGORY.toString());
        when(resultSet.getInt(prefix + CarSqlConstants.CarFields.CAPACITY)).thenReturn(dp.CAR_CAPACITY);
        when(resultSet.getString(prefix + CarSqlConstants.CarFields.LICENSE_PLATE)).thenReturn(dp.CAR_LICENSE_PLATE);
        when(resultSet.getDouble(prefix + CarSqlConstants.CarFields.CURRENT_POINT_X)).thenReturn(dp.CAR_ADDRESS_POINT.getX());
        when(resultSet.getDouble(prefix + CarSqlConstants.CarFields.CURRENT_POINT_Y)).thenReturn(dp.CAR_ADDRESS_POINT.getY());

        when(resultSet.getString(prefix + CarSqlConstants.CarFields.CURRENT_ADDRESS)).thenReturn(dp.CAR_ADDRESS_POINT.getAddress());
        Car resultCar = carJdbcHelper.fillCar(resultSet, driver, prefix);
        assertThat(resultCar.getId()).isEqualTo(dp.CAR_ID);
        assertThat(resultCar.getCarName()).isEqualTo(car.getCarName());
        assertThat(resultCar.getCarCapacity()).isEqualTo(car.getCarCapacity());
        assertThat(resultCar.getLicensePlate()).isEqualTo(car.getLicensePlate());
        assertThat(resultCar.getCarCategory()).isEqualTo(car.getCarCategory());
        assertThat(resultCar.getDriver()).isEqualTo(car.getDriver());
        assertThat(resultCar.getCurrentAddress()).isEqualTo(car.getCurrentAddress());
    }

    @Test
    public void testFillCarWithoutPrefix() throws SQLException {
        User driver = dp.createUser();
        Car car = dp.createCar();
        when(resultSet.getInt(CarSqlConstants.CarFields.ID)).thenReturn(dp.CAR_ID);
        when(resultSet.getString(CarSqlConstants.CarFields.NAME)).thenReturn(dp.CAR_NAME);
        when(resultSet.getString(CarSqlConstants.CarFields.CATEGORY)).thenReturn(dp.CAR_CATEGORY.toString());
        when(resultSet.getInt(CarSqlConstants.CarFields.CAPACITY)).thenReturn(dp.CAR_CAPACITY);
        when(resultSet.getString(CarSqlConstants.CarFields.LICENSE_PLATE)).thenReturn(dp.CAR_LICENSE_PLATE);
        when(resultSet.getDouble(CarSqlConstants.CarFields.CURRENT_POINT_X)).thenReturn(dp.CAR_ADDRESS_POINT.getX());
        when(resultSet.getDouble(CarSqlConstants.CarFields.CURRENT_POINT_Y)).thenReturn(dp.CAR_ADDRESS_POINT.getY());
        when(resultSet.getString(CarSqlConstants.CarFields.CURRENT_ADDRESS)).thenReturn(dp.CAR_ADDRESS_POINT.getAddress());
        Car resultCar = carJdbcHelper.fillCar(resultSet, driver);

        assertThat(resultCar.getId()).isEqualTo(dp.CAR_ID);
        assertThat(resultCar.getCarName()).isEqualTo(car.getCarName());
        assertThat(resultCar.getCarCapacity()).isEqualTo(car.getCarCapacity());
        assertThat(resultCar.getLicensePlate()).isEqualTo(car.getLicensePlate());
        assertThat(resultCar.getCarCategory()).isEqualTo(car.getCarCategory());
        assertThat(resultCar.getDriver()).isEqualTo(car.getDriver());
        assertThat(resultCar.getCurrentAddress()).isEqualTo(car.getCurrentAddress());
    }
}

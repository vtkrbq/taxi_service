package ua.rudniev.taxi.dao.jdbc.car;

import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CarJdbcHelper {

    public Car fillCar(ResultSet rs, User driver) throws SQLException {
        return fillCar(rs, driver, "");
    }

    public Car fillCar(ResultSet rs, User driver, String prefix) throws SQLException {
        Car car = new Car();
        car.setId(rs.getInt(prefix + CarSqlConstants.CarFields.ID));
        car.setCarName(rs.getString(prefix + CarSqlConstants.CarFields.CAR_NAME));
        car.setCarCategory(Category.valueOf(rs.getString(prefix + CarSqlConstants.CarFields.CAR_CATEGORY)));
        car.setCarCapacity(rs.getInt(prefix + CarSqlConstants.CarFields.CAR_CAPACITY));
        car.setLicensePlate(rs.getString(prefix + CarSqlConstants.CarFields.LICENSE_PLATE));
        car.setDriver(driver);
        return car;
    }
}
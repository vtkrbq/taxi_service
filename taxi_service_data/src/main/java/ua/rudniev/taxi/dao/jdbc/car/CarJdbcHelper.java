package ua.rudniev.taxi.dao.jdbc.car;

import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.trip.AddressPoint;
import ua.rudniev.taxi.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class has methods that filling object car with data from database
 */
public class CarJdbcHelper {

    /**
     * This method calling another method in this class with empty prefix
     * @param rs This parameter has data of an object from database
     * @param driver This parameter has data of an object from database
     * @return filled object of a class Car
     * @throws SQLException throws if something went wrong in database
     */
    public Car fillCar(ResultSet rs, User driver) throws SQLException {
        return fillCar(rs, driver, "");
    }

    /**
     * This method filling object car with data from database
     * @param rs This parameter has data of an object from database
     * @param driver This parameter has data of an object from database
     * @param prefix This parameter has string needed for database query
     * @return filled object of a class Car
     * @throws SQLException throws if something went wrong in database
     */
    public Car fillCar(ResultSet rs, User driver, String prefix) throws SQLException {
        Car car = new Car();
        car.setId(rs.getInt(prefix + CarSqlConstants.CarFields.ID));
        car.setCarName(rs.getString(prefix + CarSqlConstants.CarFields.NAME));
        car.setCarCategory(Category.valueOf(rs.getString(prefix + CarSqlConstants.CarFields.CATEGORY)));
        car.setCarCapacity(rs.getInt(prefix + CarSqlConstants.CarFields.CAPACITY));
        car.setLicensePlate(rs.getString(prefix + CarSqlConstants.CarFields.LICENSE_PLATE));
        //*
        car.setCurrentAddress(new AddressPoint(
                Double.parseDouble(rs.getString(prefix + CarSqlConstants.CarFields.CURRENT_POINT_X)),
                Double.parseDouble(rs.getString(prefix + CarSqlConstants.CarFields.CURRENT_POINT_Y)),
                rs.getString(prefix + CarSqlConstants.CarFields.CURRENT_ADDRESS)
        ));
        car.setDriver(driver);
        return car;
    }
}

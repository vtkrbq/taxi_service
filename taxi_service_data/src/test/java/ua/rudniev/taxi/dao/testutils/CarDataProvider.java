package ua.rudniev.taxi.dao.testutils;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.rudniev.taxi.dao.jdbc.car.CarSqlConstants;
import ua.rudniev.taxi.dao.jdbc.user.UserSqlConstants;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.car.Status;
import ua.rudniev.taxi.model.trip.AddressPoint;
import ua.rudniev.taxi.model.user.Role;
import ua.rudniev.taxi.model.user.User;

import static ua.rudniev.taxi.dao.jdbc.car.CarSqlConstants.CAR_TABLE_NAME;

@Data
@NoArgsConstructor
public class CarDataProvider {
    public final int CAR_ID = 1;
    public final String CAR_NAME = "Lada Priora";
    public final Category CAR_CATEGORY = Category.COMFORT;
    public final int CAR_CAPACITY = 3;
    public final Status CAR_STATUS = Status.AVAILABLE;
    public final String CAR_LICENSE_PLATE = "AK1345OK";
    public final AddressPoint CAR_ADDRESS_POINT  = new AddressPoint(
            49.991981,
            36.328126,
            "Харків, вул. Гвардійців-Широнінців, 5");
    public final String DRIVER_LOGIN = "driver";
    public final String DRIVER_NAME = "Volodymir";
    public final String DRIVER_LASTNAME = "Slobozhanskiy";
    public final String DRIVER_PHONE = "+380680000009";
    public final String DRIVER_EMAIL = "vodila_bombit@gmail.com";
    public final int DRIVER_DISCOUNT = 0;

    public final String FIND_ALL_AVAILABLE_CARS = "select " + CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.ID + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.DRIVER_LOGIN + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.NAME + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.CATEGORY + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.STATUS + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.CAPACITY + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.LICENSE_PLATE + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.CURRENT_ADDRESS + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.CURRENT_POINT_X + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.CURRENT_POINT_Y + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.LOGIN + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.FIRSTNAME + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.LASTNAME + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.PHONE + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.EMAIL + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.DISCOUNT + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.ROLES +
            " from car inner join app_user on app_user.login=car.driver_login " +
            "where status='AVAILABLE'";

    public Car createCar() {
        Car car = new Car();
        car.setId(CAR_ID);
        car.setCarName(CAR_NAME);
        car.setCarCategory(CAR_CATEGORY);
        car.setCarCapacity(CAR_CAPACITY);
        car.setStatus(CAR_STATUS);
        car.setLicensePlate(CAR_LICENSE_PLATE);
        car.setCurrentAddress(CAR_ADDRESS_POINT);
        car.setDriver(createUser());
        return car;
    }

    public User createUser() {
        User driver = new User();
        driver.setLogin(DRIVER_LOGIN);
        driver.setFirstname(DRIVER_NAME);
        driver.setLastname(DRIVER_LASTNAME);
        driver.setPhone(DRIVER_PHONE);
        driver.setEmail(DRIVER_EMAIL);
        driver.setDiscount(DRIVER_DISCOUNT);
        driver.addRole(Role.USER);
        driver.addRole(Role.DRIVER);
        return driver;
    }
}

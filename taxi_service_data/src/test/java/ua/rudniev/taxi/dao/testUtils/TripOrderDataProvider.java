package ua.rudniev.taxi.dao.testUtils;

import ua.rudniev.taxi.dao.jdbc.car.CarSqlConstants;
import ua.rudniev.taxi.dao.jdbc.trip.TripOrderSqlConstants;
import ua.rudniev.taxi.dao.jdbc.user.UserSqlConstants;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.trip.AddressPoint;
import ua.rudniev.taxi.model.trip.TripOrder;
import ua.rudniev.taxi.model.user.Role;
import ua.rudniev.taxi.model.user.User;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static ua.rudniev.taxi.dao.jdbc.car.CarSqlConstants.CAR_TABLE_NAME;
import static ua.rudniev.taxi.dao.jdbc.trip.TripOrderSqlConstants.*;
import static ua.rudniev.taxi.dao.jdbc.user.UserSqlConstants.USER_TABLE_NAME;

public class TripOrderDataProvider {
    public final String USER_LOGIN = "user";
    public final String USER_NAME = "Aleksandr";
    public final String USER_LASTNAME = "Kornevoi";
    public final String USER_PHONE = "+380680000010";
    public final String USER_EMAIL = "user_ezdit@gmail.com";
    public final int USER_DISCOUNT = 0;
    CarDataProvider dp = new CarDataProvider();
    public final int TRIP_ORDER_ID = 1;
    public final AddressPoint TRIP_ORDER_DEPARTURE = new AddressPoint(49.9319220, 36.3944980, "Харків, пров. Луї Пастера, 3");
    public final AddressPoint TRIP_ORDER_DESTINATION = new AddressPoint(49.9508670, 36.3591440, "Харків, вул. Біблика (П'ятирічки 2-ї), 2а");
    public final Category TRIP_ORDER_CATEGORY = Category.COMFORT;
    public final int TRIP_ORDER_CAPACITY = 2;
    public final User TRIP_ORDER_USER = createUser();
    public final Instant TRIP_ORDER_TIMESTAMP_CREATED = Instant.now().minus(15, ChronoUnit.MINUTES);
    public final Instant TRIP_ORDER_TIMESTAMP_END = Instant.now().plus(15, ChronoUnit.MINUTES);

    public final Car TRIP_ORDER_CAR = dp.createCar();

    public final BigDecimal TRIP_ORDER_PRICE = BigDecimal.valueOf(66.24);

    public final String FIND_ALL_TRIP_ORDERS = "select " +
            TO_TABLE_NAME + "." + TripOrderSqlConstants.TripOrderFields.ID + ", " +
            TO_TABLE_NAME + "." + TripOrderSqlConstants.TripOrderFields.DEPARTURE_ADDRESS + ", " +
            TO_TABLE_NAME + "." + TripOrderSqlConstants.TripOrderFields.DEPARTURE_X + ", " +
            TO_TABLE_NAME + "." + TripOrderSqlConstants.TripOrderFields.DEPARTURE_Y + ", " +
            TO_TABLE_NAME + "." + TripOrderSqlConstants.TripOrderFields.DESTINATION_ADDRESS + ", " +
            TO_TABLE_NAME + "." + TripOrderSqlConstants.TripOrderFields.DESTINATION_X + ", " +
            TO_TABLE_NAME + "." + TripOrderSqlConstants.TripOrderFields.DESTINATION_Y + ", " +
            TO_TABLE_NAME + "." + TripOrderSqlConstants.TripOrderFields.CATEGORY + ", " +
            TO_TABLE_NAME + "." + TripOrderSqlConstants.TripOrderFields.CAPACITY + ", " +
            CLIENT_ALIAS + "." + UserSqlConstants.UserFields.LOGIN + " as " + CLIENT_PREFIX + UserSqlConstants.UserFields.LOGIN + ", " + //TODO: подумать, может можно сдлелать лучше
            CLIENT_ALIAS + "." + UserSqlConstants.UserFields.FIRSTNAME + " as " + CLIENT_PREFIX + UserSqlConstants.UserFields.FIRSTNAME + ", " +
            CLIENT_ALIAS + "." + UserSqlConstants.UserFields.LASTNAME + " as " + CLIENT_PREFIX + UserSqlConstants.UserFields.LASTNAME + ", " +
            CLIENT_ALIAS + "." + UserSqlConstants.UserFields.PHONE + " as " + CLIENT_PREFIX + UserSqlConstants.UserFields.PHONE + ", " +
            CLIENT_ALIAS + "." + UserSqlConstants.UserFields.EMAIL + " as " + CLIENT_PREFIX + UserSqlConstants.UserFields.EMAIL + ", " +
            CLIENT_ALIAS + "." + UserSqlConstants.UserFields.DISCOUNT + " as " + CLIENT_PREFIX + UserSqlConstants.UserFields.DISCOUNT + ", " +
            CLIENT_ALIAS + "." + UserSqlConstants.UserFields.ROLES + " as " + CLIENT_PREFIX + UserSqlConstants.UserFields.ROLES + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.ID + " as " + CAR_PREFIX + CarSqlConstants.CarFields.ID + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.NAME + " as " + CAR_PREFIX + CarSqlConstants.CarFields.NAME + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.CATEGORY + " as " + CAR_PREFIX + CarSqlConstants.CarFields.CATEGORY + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.CAPACITY + " as " + CAR_PREFIX + CarSqlConstants.CarFields.CAPACITY + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.LICENSE_PLATE + " as " + CAR_PREFIX + CarSqlConstants.CarFields.LICENSE_PLATE + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.CURRENT_ADDRESS + " as " + CAR_PREFIX + CarSqlConstants.CarFields.CURRENT_ADDRESS + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.CURRENT_POINT_X + " as " + CAR_PREFIX + CarSqlConstants.CarFields.CURRENT_POINT_X + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.CURRENT_POINT_Y + " as " + CAR_PREFIX + CarSqlConstants.CarFields.CURRENT_POINT_Y + ", " +
            DRIVER_ALIAS + "." + UserSqlConstants.UserFields.LOGIN + " as " + DRIVER_PREFIX + UserSqlConstants.UserFields.LOGIN + ", " +
            DRIVER_ALIAS + "." + UserSqlConstants.UserFields.FIRSTNAME + " as " + DRIVER_PREFIX + UserSqlConstants.UserFields.FIRSTNAME + ", " +
            DRIVER_ALIAS + "." + UserSqlConstants.UserFields.LASTNAME + " as " + DRIVER_PREFIX + UserSqlConstants.UserFields.LASTNAME + ", " +
            DRIVER_ALIAS + "." + UserSqlConstants.UserFields.PHONE + " as " + DRIVER_PREFIX + UserSqlConstants.UserFields.PHONE + ", " +
            DRIVER_ALIAS + "." + UserSqlConstants.UserFields.EMAIL + " as " + DRIVER_PREFIX + UserSqlConstants.UserFields.EMAIL + ", " +
            DRIVER_ALIAS + "." + UserSqlConstants.UserFields.DISCOUNT + " as " + DRIVER_PREFIX + UserSqlConstants.UserFields.DISCOUNT + ", " +
            DRIVER_ALIAS + "." + UserSqlConstants.UserFields.ROLES + " as " + DRIVER_PREFIX + UserSqlConstants.UserFields.ROLES + ", " +
            TO_TABLE_NAME + "." + TripOrderSqlConstants.TripOrderFields.PRICE + ", " +
            TO_TABLE_NAME + "." + TripOrderSqlConstants.TripOrderFields.CREATED + ", " +
            TO_TABLE_NAME + "." + TripOrderSqlConstants.TripOrderFields.END_OF_TRIP +
            " from " + TO_TABLE_NAME +
            " inner join " + CAR_TABLE_NAME + " on " + TO_TABLE_NAME + "." + TripOrderSqlConstants.TripOrderFields.CAR_ID + " = " + CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.ID +
            " inner join " + USER_TABLE_NAME + " as " + CLIENT_ALIAS +
            " on " + TO_TABLE_NAME + "." + TripOrderSqlConstants.TripOrderFields.USER_LOGIN + " = client.login" +
            " inner join " + USER_TABLE_NAME + " as " + DRIVER_ALIAS +
            " on " + CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.DRIVER_LOGIN + " = driver.login" +
            " limit 1 offset 1";

    public TripOrder createTripOrder() {
        TripOrder tripOrder = new TripOrder();
        tripOrder.setId(TRIP_ORDER_ID);
        tripOrder.setDeparture(new AddressPoint(TRIP_ORDER_DEPARTURE.getX(), TRIP_ORDER_DEPARTURE.getY(), TRIP_ORDER_DEPARTURE.getAddress()));
        tripOrder.setDestination(new AddressPoint(TRIP_ORDER_DESTINATION.getX(), TRIP_ORDER_DESTINATION.getY(), TRIP_ORDER_DESTINATION.getAddress()));
        tripOrder.setCategory(TRIP_ORDER_CATEGORY);
        tripOrder.setCapacity(TRIP_ORDER_CAPACITY);
        tripOrder.setUser(TRIP_ORDER_USER);
        tripOrder.setTimestampCreated(TRIP_ORDER_TIMESTAMP_CREATED);
        tripOrder.setTimestampEnd(TRIP_ORDER_TIMESTAMP_END);
        tripOrder.setCar(TRIP_ORDER_CAR);
        tripOrder.setPrice(TRIP_ORDER_PRICE);
        return tripOrder;
    }

    public User createUser() {
        User driver = new User();
        driver.setLogin(USER_LOGIN);
        driver.setFirstname(USER_NAME);
        driver.setLastname(USER_LASTNAME);
        driver.setPhone(USER_PHONE);
        driver.setEmail(USER_EMAIL);
        driver.setDiscount(USER_DISCOUNT);
        driver.addRole(Role.USER);
        return driver;
    }
}

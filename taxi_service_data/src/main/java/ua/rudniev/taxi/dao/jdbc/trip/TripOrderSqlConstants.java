package ua.rudniev.taxi.dao.jdbc.trip;

import ua.rudniev.taxi.dao.jdbc.car.CarSqlConstants;
import ua.rudniev.taxi.dao.jdbc.user.UserSqlConstants;

import static ua.rudniev.taxi.dao.jdbc.car.CarSqlConstants.CAR_TABLE_NAME;
import static ua.rudniev.taxi.dao.jdbc.user.UserSqlConstants.USER_TABLE_NAME;

public class TripOrderSqlConstants {

    private static final String TO_TABLE_NAME = "trip_order";

    public static final String CLIENT_ALIAS = "client";

    public static final String CLIENT_PREFIX = "CLIENT_";

    public static final String DRIVER_ALIAS = "driver";

    public static final String DRIVER_PREFIX = "DRIVER_";

    public static final String CAR_PREFIX = "CAR_";

    public static class TripOrderFields {

        public static final String ID = "ID";

        public static final String DEPARTURE_ADDRESS = "DEPARTURE_ADDRESS";

        public static final String DEPARTURE_X = "DEPARTURE_X";

        public static final String DEPARTURE_Y = "DEPARTURE_Y";

        public static final String DESTINATION_ADDRESS = "DESTINATION_ADDRESS";

        public static final String DESTINATION_X = "DESTINATION_X";

        public static final String DESTINATION_Y = "DESTINATION_Y";

        public static final String CATEGORY = "TRIP_CATEGORY";

        public static final String CAPACITY = "TRIP_CAPACITY";

        public static final String USER_LOGIN = "USER_LOGIN";

        public static final String CAR_ID = "CAR_ID";

        public static final String PRICE = "PRICE";

        public static final String CREATED = "CREATED";
    }

    public static final String FIND_ALL_TRIP_ORDERS = "select " +
            TO_TABLE_NAME + "." + TripOrderFields.DEPARTURE_ADDRESS + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.DEPARTURE_X + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.DEPARTURE_Y + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.DESTINATION_ADDRESS + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.DESTINATION_X + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.DESTINATION_Y + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.CATEGORY + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.CAPACITY + ", " +
            CLIENT_ALIAS + "." + UserSqlConstants.UserFields.LOGIN + " as " + CLIENT_PREFIX + UserSqlConstants.UserFields.LOGIN + ", " + //TODO: подумать, может можно сдлелать лучше
            CLIENT_ALIAS + "." + UserSqlConstants.UserFields.FIRSTNAME + " as " + CLIENT_PREFIX + UserSqlConstants.UserFields.FIRSTNAME + ", " +
            CLIENT_ALIAS + "." + UserSqlConstants.UserFields.LASTNAME + " as " + CLIENT_PREFIX + UserSqlConstants.UserFields.LASTNAME + ", " +
            CLIENT_ALIAS + "." + UserSqlConstants.UserFields.PHONE + " as " + CLIENT_PREFIX + UserSqlConstants.UserFields.PHONE + ", " +
            CLIENT_ALIAS + "." + UserSqlConstants.UserFields.EMAIL + " as " + CLIENT_PREFIX + UserSqlConstants.UserFields.EMAIL + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.ID + " as " + CAR_PREFIX + CarSqlConstants.CarFields.ID + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.CAR_NAME + " as " + CAR_PREFIX + CarSqlConstants.CarFields.CAR_NAME + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.CAR_CATEGORY + " as " + CAR_PREFIX + CarSqlConstants.CarFields.CAR_CATEGORY + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.CAR_CAPACITY + " as " + CAR_PREFIX + CarSqlConstants.CarFields.CAR_CAPACITY + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.LICENSE_PLATE + " as " + CAR_PREFIX + CarSqlConstants.CarFields.LICENSE_PLATE + ", " +
            DRIVER_ALIAS + "." + UserSqlConstants.UserFields.LOGIN + " as " + DRIVER_PREFIX + UserSqlConstants.UserFields.LOGIN + ", " +
            DRIVER_ALIAS + "." + UserSqlConstants.UserFields.FIRSTNAME + " as " + DRIVER_PREFIX + UserSqlConstants.UserFields.FIRSTNAME + ", " +
            DRIVER_ALIAS + "." + UserSqlConstants.UserFields.LASTNAME + " as " + DRIVER_PREFIX + UserSqlConstants.UserFields.LASTNAME + ", " +
            DRIVER_ALIAS + "." + UserSqlConstants.UserFields.PHONE + " as " + DRIVER_PREFIX + UserSqlConstants.UserFields.PHONE + ", " +
            DRIVER_ALIAS + "." + UserSqlConstants.UserFields.EMAIL + " as " + DRIVER_PREFIX + UserSqlConstants.UserFields.EMAIL + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.PRICE + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.CREATED +
            " from " + TO_TABLE_NAME +
            " inner join " + CAR_TABLE_NAME + " on " + TO_TABLE_NAME + "." + TripOrderFields.CAR_ID + " = car.id " +
            " inner join " + USER_TABLE_NAME + " as " + CLIENT_ALIAS +
            " on " + TO_TABLE_NAME + "." + TripOrderFields.USER_LOGIN + " = client.login" +
            " inner join " + USER_TABLE_NAME + " as " + DRIVER_ALIAS +
            " on " + CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.DRIVER_LOGIN + " = driver.login";

    public static final String INSERT_TRIP_ORDER = "INSERT INTO trip_order " +
            "(" + TripOrderFields.DEPARTURE_ADDRESS + ", " +
            TripOrderFields.DEPARTURE_X + ", " +
            TripOrderFields.DEPARTURE_Y + ", " +
            TripOrderFields.DESTINATION_ADDRESS + ", " +
            TripOrderFields.DESTINATION_X + ", " +
            TripOrderFields.DESTINATION_Y + ", " +
            TripOrderFields.USER_LOGIN + ", " +
            TripOrderFields.CAR_ID + ", " +
            TripOrderFields.CATEGORY + ", " +
            TripOrderFields.CAPACITY + ", " +
            TripOrderFields.PRICE + ", " +
            TripOrderFields.CREATED + ") " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String GET_COUNT_OF_TRIP_ORDERS = "select count(distinct(trip_order.id)) from " + TO_TABLE_NAME +
            " inner join " + CAR_TABLE_NAME + " on " + TO_TABLE_NAME + "." + TripOrderFields.CAR_ID + " = car.id " +
            " inner join " + USER_TABLE_NAME + " as client" +
            " on " + TO_TABLE_NAME + "." + TripOrderFields.USER_LOGIN + " = client.login" +
            " inner join " + USER_TABLE_NAME + " as driver" +
            " on " + CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.DRIVER_LOGIN + " = driver.login";
}

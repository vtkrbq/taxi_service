package ua.rudniev.taxi.connection;

import ua.rudniev.taxi.dao.car.CarDaoImpl;
import ua.rudniev.taxi.dao.user.UserDaoImpl;

public class SQLConstants {
    private static final String CAR_TABLE_NAME = "car";
    private static final String TO_TABLE_NAME = "trip_order";
    private static final String AU_TABLE_NAME = "app_user";

    public static class UserFields {

        public static final String LOGIN = "LOGIN";

        public static final String PASSHASH = "PASSHASH";

        public static final String FIRSTNAME = "FIRSTNAME";

        public static final String LASTNAME = "LASTNAME";

        public static final String PHONE = "PHONE";

        public static final String EMAIL = "EMAIL";

        public static final String ROLES = "ROLES";
    }

    public static class CarFields {

        public static final String ID = "ID";

        public static final String DRIVER_LOGIN = "DRIVER_LOGIN";

        public static final String CAR_NAME = "CAR_NAME";

        public static final String CAR_CATEGORY = "CAR_CATEGORY";

        public static final String STATUS = "STATUS";

        public static final String CAR_CAPACITY = "CAR_CAPACITY";

        public static final String LICENSE_PLATE = "LICENSE_PLATE";
    }

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

    public static final String AUTH_USER = "select " +
            UserFields.LOGIN + ", " +
            UserFields.FIRSTNAME + ", " +
            UserFields.LASTNAME + ", " +
            UserFields.PHONE + ", " +
            UserFields.EMAIL + ", " +
            UserFields.ROLES +
            " from " + AU_TABLE_NAME + " where login=? and passhash=?";

    public static final String FIND_USER = "select " +
            UserFields.LOGIN + ", " +
            UserFields.FIRSTNAME + ", " +
            UserFields.LASTNAME + ", " +
            UserFields.PHONE + ", " +
            UserFields.EMAIL + ", " +
            UserFields.ROLES +
            " from " + AU_TABLE_NAME + " where login=?";

    public static final String CREATE_USER = "insert into " + AU_TABLE_NAME +
            " (" + UserFields.LOGIN + ", " +
            UserFields.PASSHASH + ", " +
            UserFields.FIRSTNAME + ", " +
            UserFields.LASTNAME + ", " +
            UserFields.PHONE + ", " +
            UserFields.EMAIL + ", " +
            UserFields.ROLES + ") " +
            "values (?, ?, ?, ?, ?, ?, ?)";

    public static final String UPDATE_USER = "update " + AU_TABLE_NAME +
            " set " + UserFields.LOGIN + " = " + "?, " +
            UserFields.FIRSTNAME + " = " + "?, " +
            UserFields.LASTNAME + " = " + "?, " +
            UserFields.PHONE + " = " + "?, " +
            UserFields.EMAIL + " = " + "? " +
            "where login=?";

    public static final String UPDATE_PASSHASH = "update " + AU_TABLE_NAME +
            " set " + UserFields.PASSHASH + " = ? " + "where login=? and passhash=?";

    public static final String FIND_ALL_LOGINS = "select " + UserFields.LOGIN +
            " from " + AU_TABLE_NAME;

    public static final String CHECK_PASSWORD = "select " +
            UserFields.LOGIN + ", " +
            UserFields.PASSHASH +
            " from " + AU_TABLE_NAME + " where login=? and passhash=?";

    public static final String SELECT_AVAILABLE_CARS_WITH_DRIVERS = "select " +
            CAR_TABLE_NAME + "." + CarFields.ID + ", " +
            CAR_TABLE_NAME + "." + CarFields.DRIVER_LOGIN + ", " +
            CAR_TABLE_NAME + "." + CarFields.CAR_NAME + ", " +
            CAR_TABLE_NAME + "." + CarFields.CAR_CATEGORY + ", " +
            CAR_TABLE_NAME + "." + CarFields.STATUS + ", " +
            CAR_TABLE_NAME + "." + CarFields.CAR_CAPACITY + ", " +
            CAR_TABLE_NAME + "." + CarFields.LICENSE_PLATE + ", " +
            AU_TABLE_NAME + "." + UserFields.LOGIN + ", " +
            AU_TABLE_NAME + "." + UserFields.FIRSTNAME + ", " +
            AU_TABLE_NAME + "." + UserFields.LASTNAME + ", " +
            AU_TABLE_NAME + "." + UserFields.PHONE + ", " +
            AU_TABLE_NAME + "." + UserFields.EMAIL + ", " +
            AU_TABLE_NAME + "." + UserFields.ROLES +
            " from car inner join app_user on app_user.login=car.driver_login where status='AVAILABLE'";

    public static final String CREATE_CAR = "insert into " + CAR_TABLE_NAME +
            " (" + CarFields.CAR_NAME + ", " +
            CarFields.CAR_CATEGORY + ", " +
            CarFields.CAR_CAPACITY + ", " +
            CarFields.LICENSE_PLATE + ", " +
            CarFields.DRIVER_LOGIN + ") " +
            "values (?, ?, ?, ?, ?)";

    public static final String FIND_CAR_WITH_DRIVER = "select " +
            CAR_TABLE_NAME + "." + CarFields.ID + ", " +
            CAR_TABLE_NAME + "." + CarFields.CAR_NAME + ", " +
            CAR_TABLE_NAME + "." + CarFields.CAR_CATEGORY + ", " +
            CAR_TABLE_NAME + "." + CarFields.CAR_CAPACITY + ", " +
            CAR_TABLE_NAME + "." + CarFields.LICENSE_PLATE + ", " +
            AU_TABLE_NAME + "." + UserFields.LOGIN + ", " +
            AU_TABLE_NAME + "." + UserFields.FIRSTNAME + ", " +
            AU_TABLE_NAME + "." + UserFields.LASTNAME + ", " +
            AU_TABLE_NAME + "." + UserFields.PHONE + ", " +
            AU_TABLE_NAME + "." + UserFields.EMAIL + " " +
            "from " + CAR_TABLE_NAME +
            " inner join " + AU_TABLE_NAME +
            " on " + CAR_TABLE_NAME + "." + CarFields.DRIVER_LOGIN + " = " + AU_TABLE_NAME + "." + UserFields.LOGIN +
            " where " + CarFields.ID + "=?";

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

    public static final String FIND_ALL_TRIP_ORDERS = "select " +
            TO_TABLE_NAME + "." + TripOrderFields.DEPARTURE_ADDRESS + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.DEPARTURE_X + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.DEPARTURE_Y + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.DESTINATION_ADDRESS + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.DESTINATION_X + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.DESTINATION_Y + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.CATEGORY + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.CAPACITY + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.USER_LOGIN + ", " +
            "client." + UserFields.FIRSTNAME + ", " +
            "client." + UserFields.LASTNAME + ", " +
            "client." + UserFields.PHONE + ", " +
            "client." + UserFields.EMAIL + ", " +
            CAR_TABLE_NAME + "." + CarFields.ID + ", " +
            CAR_TABLE_NAME + "." + CarFields.CAR_NAME + ", " +
            CAR_TABLE_NAME + "." + CarFields.CAR_CATEGORY + ", " +
            CAR_TABLE_NAME + "." + CarFields.CAR_CAPACITY + ", " +
            CAR_TABLE_NAME + "." + CarFields.LICENSE_PLATE + ", " +
            CAR_TABLE_NAME + "." + CarFields.DRIVER_LOGIN + ", " +
            "driver." + UserFields.FIRSTNAME + ", " +
            "driver." + UserFields.LASTNAME + ", " +
            "driver." + UserFields.PHONE + ", " +
            "driver." + UserFields.EMAIL + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.PRICE + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.CREATED +
            " from " + TO_TABLE_NAME +
            " inner join " + CAR_TABLE_NAME + " on " + TO_TABLE_NAME + "." + TripOrderFields.CAR_ID + " = car.id " +
            " inner join " + AU_TABLE_NAME + " as client" +
            " on " + TO_TABLE_NAME + "." + TripOrderFields.USER_LOGIN + " = client.login" +
            " inner join " + AU_TABLE_NAME + " as driver" +
            " on " + CAR_TABLE_NAME + "." + CarFields.DRIVER_LOGIN + " = driver.login" +
            " order by " + TripOrderFields.DEPARTURE_ADDRESS + " asc" +
            "limit ? offset ?";

    public static final String FIND_ALL_TRIP_ORDERS_WITH_FILTER = "select " +
            TO_TABLE_NAME + "." + TripOrderFields.DEPARTURE_ADDRESS + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.DEPARTURE_X + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.DEPARTURE_Y + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.DESTINATION_ADDRESS + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.DESTINATION_X + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.DESTINATION_Y + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.CATEGORY + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.CAPACITY + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.USER_LOGIN + ", " +
            "client." + UserFields.FIRSTNAME + ", " +
            "client." + UserFields.LASTNAME + ", " +
            "client." + UserFields.PHONE + ", " +
            "client." + UserFields.EMAIL + ", " +
            CAR_TABLE_NAME + "." + CarFields.ID + ", " +
            CAR_TABLE_NAME + "." + CarFields.CAR_NAME + ", " +
            CAR_TABLE_NAME + "." + CarFields.CAR_CATEGORY + ", " +
            CAR_TABLE_NAME + "." + CarFields.CAR_CAPACITY + ", " +
            CAR_TABLE_NAME + "." + CarFields.LICENSE_PLATE + ", " +
            CAR_TABLE_NAME + "." + CarFields.DRIVER_LOGIN + ", " +
            "driver." + UserFields.FIRSTNAME + ", " +
            "driver." + UserFields.LASTNAME + ", " +
            "driver." + UserFields.PHONE + ", " +
            "driver." + UserFields.EMAIL + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.PRICE + ", " +
            TO_TABLE_NAME + "." + TripOrderFields.CREATED +
            " from " + TO_TABLE_NAME +
            " inner join " + CAR_TABLE_NAME + " on " + TO_TABLE_NAME + "." + TripOrderFields.CAR_ID + " = car.id " +
            " inner join " + AU_TABLE_NAME + " as client" +
            " on " + TO_TABLE_NAME + "." + TripOrderFields.USER_LOGIN + " = client.login" +
            " inner join " + AU_TABLE_NAME + " as driver" +
            " on " + CAR_TABLE_NAME + "." + CarFields.DRIVER_LOGIN + " = driver.login" +
            " where ? = ?" +
            " order by ? ?" +
            " limit ? " + "offset ?";

    public static final String GET_COUNT_OF_TRIP_ORDERS = "select count(distinct(id)) from " + TO_TABLE_NAME;

}

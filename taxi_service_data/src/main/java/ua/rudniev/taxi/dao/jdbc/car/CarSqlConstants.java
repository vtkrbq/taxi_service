package ua.rudniev.taxi.dao.jdbc.car;

import ua.rudniev.taxi.dao.jdbc.trip.TripOrderSqlConstants;
import ua.rudniev.taxi.dao.jdbc.user.UserSqlConstants;

public class CarSqlConstants {

    public static final String CAR_TABLE_NAME = "car";

    public static class CarFields {

        public static final String ID = "ID";

        public static final String DRIVER_LOGIN = "DRIVER_LOGIN";

        public static final String NAME = "NAME";

        public static final String CATEGORY = "CATEGORY";

        public static final String STATUS = "STATUS";

        public static final String CAPACITY = "CAPACITY";

        public static final String LICENSE_PLATE = "LICENSE_PLATE";

        public static final String CURRENT_ADDRESS = "CURRENT_ADDRESS";

        public static final String CURRENT_POINT_X = "CURRENT_POINT_X";

        public static final String CURRENT_POINT_Y = "CURRENT_POINT_Y";
    }

    public static final String FIND_AVAILABLE_CARS = "select " +
            CAR_TABLE_NAME + "." + CarFields.ID + ", " +
            CAR_TABLE_NAME + "." + CarFields.DRIVER_LOGIN + ", " +
            CAR_TABLE_NAME + "." + CarFields.NAME + ", " +
            CAR_TABLE_NAME + "." + CarFields.CATEGORY + ", " +
            CAR_TABLE_NAME + "." + CarFields.STATUS + ", " +
            CAR_TABLE_NAME + "." + CarFields.CAPACITY + ", " +
            CAR_TABLE_NAME + "." + CarFields.LICENSE_PLATE + ", " +
            CAR_TABLE_NAME + "." + CarFields.CURRENT_ADDRESS + ", " +
            CAR_TABLE_NAME + "." + CarFields.CURRENT_POINT_X + ", " +
            CAR_TABLE_NAME + "." + CarFields.CURRENT_POINT_Y + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.LOGIN + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.FIRSTNAME + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.LASTNAME + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.PHONE + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.EMAIL + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.ROLES +
            " from car inner join app_user on app_user.login=car.driver_login where status='AVAILABLE'";

    public static final String CREATE_CAR = "insert into " + CAR_TABLE_NAME +
            " (" + CarFields.NAME + ", " +
            CarFields.CATEGORY + ", " +
            CarFields.CAPACITY + ", " +
            CarFields.LICENSE_PLATE + ", " +
            CarFields.DRIVER_LOGIN + ", " +
            CarFields.STATUS + ", " +
            CarFields.CURRENT_ADDRESS + ", " +
            CarFields.CURRENT_POINT_X + ", " +
            CarFields.CURRENT_POINT_Y + ") " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String FIND_CAR_BY_ID = "select " +
            CAR_TABLE_NAME + "." + CarFields.ID + ", " +
            CAR_TABLE_NAME + "." + CarFields.NAME + ", " +
            CAR_TABLE_NAME + "." + CarFields.CATEGORY + ", " +
            CAR_TABLE_NAME + "." + CarFields.CAPACITY + ", " +
            CAR_TABLE_NAME + "." + CarFields.LICENSE_PLATE + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.LOGIN + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.FIRSTNAME + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.LASTNAME + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.PHONE + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.EMAIL + " " +
            "from " + CAR_TABLE_NAME +
            " inner join " + UserSqlConstants.USER_TABLE_NAME +
            " on " + CAR_TABLE_NAME + "." + CarFields.DRIVER_LOGIN + " = " + UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.LOGIN +
            " where " + CarFields.ID + "=?";

    public static final String UPDATE_CAR = "update " + CAR_TABLE_NAME +
            " set " + CarFields.NAME + " = " + "?, " +
            CarFields.CATEGORY + " = " + "?, " +
            CarFields.CAPACITY + " = " + "?, " +
            CarFields.LICENSE_PLATE + " = " + "?, " +
            CarFields.DRIVER_LOGIN + " = " + "?, " +
            CarFields.STATUS + " = " + "?, " +
            CarFields.CURRENT_ADDRESS + " = " + "?, " +
            CarFields.CURRENT_POINT_X + " = " + "?, " +
            CarFields.CURRENT_POINT_Y + " = " + "? " +
            "where " + CarFields.ID + "=?";

    public static final String COMPLETE_TRIP_VIA_CAR = "update " + CAR_TABLE_NAME +
                    " set " + CarFields.STATUS + " = 'AVAILABLE' " +
                    "where " + CarFields.ID + "=?";
}

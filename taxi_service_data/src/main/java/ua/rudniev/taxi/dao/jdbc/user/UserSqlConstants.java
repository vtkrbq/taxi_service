package ua.rudniev.taxi.dao.jdbc.user;

/**
 * This class has fields with names of fields in database table 'app_user' and queries
 */
public class UserSqlConstants {

    public static final String USER_TABLE_NAME = "app_user";

    public static class UserFields {

        public static final String LOGIN = "LOGIN";

        public static final String PASSHASH = "PASSHASH";

        public static final String FIRSTNAME = "FIRSTNAME";

        public static final String LASTNAME = "LASTNAME";

        public static final String PHONE = "PHONE";

        public static final String EMAIL = "EMAIL";

        public static final String ROLES = "ROLES";

        public static final String DISCOUNT = "DISCOUNT";
    }

    public static final String AUTH_USER = "select " +
            UserFields.LOGIN + ", " +
            UserFields.FIRSTNAME + ", " +
            UserFields.LASTNAME + ", " +
            UserFields.PHONE + ", " +
            UserFields.EMAIL + ", " +
            UserFields.ROLES + ", " +
            UserFields.DISCOUNT +
            " from " + USER_TABLE_NAME + " where login=? and passhash=?";

    public static final String FIND_USER = "select " +
            UserFields.LOGIN + ", " +
            UserFields.FIRSTNAME + ", " +
            UserFields.LASTNAME + ", " +
            UserFields.PHONE + ", " +
            UserFields.EMAIL + ", " +
            UserFields.ROLES + ", " +
            UserFields.DISCOUNT +
            " from " + USER_TABLE_NAME + " where " + UserFields.LOGIN + "=?";

    public static final String CREATE_USER = "insert into " + USER_TABLE_NAME +
            " (" + UserFields.LOGIN + ", " +
            UserFields.PASSHASH + ", " +
            UserFields.FIRSTNAME + ", " +
            UserFields.LASTNAME + ", " +
            UserFields.PHONE + ", " +
            UserFields.EMAIL + ", " +
            UserFields.ROLES + ") " +
            "values (?, ?, ?, ?, ?, ?, ?)";

    public static final String UPDATE_USER = "update " + USER_TABLE_NAME +
            " set " + UserFields.LOGIN + " = " + "?, " +
            UserFields.FIRSTNAME + " = " + "?, " +
            UserFields.LASTNAME + " = " + "?, " +
            UserFields.PHONE + " = " + "?, " +
            UserFields.EMAIL + " = " + "?, " +
            UserFields.ROLES + " = " + "?, " +
            UserFields.DISCOUNT + " = " + "? " +
            "where login=?";

    public static final String UPDATE_PASSHASH = "update " + USER_TABLE_NAME +
            " set " + UserFields.PASSHASH + " = ? " + "where login=? and passhash=?";

    public static final String FIND_ALL_LOGINS = "select " + UserFields.LOGIN +
            " from " + USER_TABLE_NAME;
}

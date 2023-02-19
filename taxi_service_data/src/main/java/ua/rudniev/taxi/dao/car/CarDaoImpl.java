package ua.rudniev.taxi.dao.car;

import ua.rudniev.taxi.dao.user.UserDaoImpl;
import ua.rudniev.taxi.dao.utils.JdbcDaoUtils;
import ua.rudniev.taxi.exception.DbException;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.car.Status;
import ua.rudniev.taxi.model.user.Role;
import ua.rudniev.taxi.model.user.User;
import ua.rudniev.taxi.transaction.HikariTransactionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImpl implements CarDao {

    public static final String TABLE_NAME = "car";

    public static class Fields {

        public static final String ID = "ID";

        public static final String DRIVER_LOGIN = "DRIVER_LOGIN";

        public static final String CAR_NAME = "CAR_NAME";

        public static final String CAR_CATEGORY = "CAR_CATEGORY";

        public static final String STATUS = "STATUS";

        public static final String CAR_CAPACITY = "CAR_CAPACITY";

        public static final String LICENSE_PLATE = "LICENSE_PLATE";

    }

    private static class Queries {
        private static final String SELECT_AVAILABLE_CARS_WITH_DRIVERS = "select " +
                TABLE_NAME + "." + Fields.ID + ", " +
                TABLE_NAME + "." + Fields.DRIVER_LOGIN + ", " +
                TABLE_NAME + "." + Fields.CAR_NAME + ", " +
                TABLE_NAME + "." + Fields.CAR_CATEGORY + ", " +
                TABLE_NAME + "." + Fields.STATUS + ", " +
                TABLE_NAME + "." + Fields.CAR_CAPACITY + ", " +
                TABLE_NAME + "." + Fields.LICENSE_PLATE + ", " +
                UserDaoImpl.TABLE_NAME + "." + UserDaoImpl.Fields.LOGIN + ", " +
                UserDaoImpl.TABLE_NAME + "." + UserDaoImpl.Fields.FIRSTNAME + ", " +
                UserDaoImpl.TABLE_NAME + "." + UserDaoImpl.Fields.LASTNAME + ", " +
                UserDaoImpl.TABLE_NAME + "." + UserDaoImpl.Fields.PHONE + ", " +
                UserDaoImpl.TABLE_NAME + "." + UserDaoImpl.Fields.EMAIL + ", " +
                UserDaoImpl.TABLE_NAME + "." + UserDaoImpl.Fields.ROLES +
                " from car inner join app_user on app_user.login=car.driver_login where status='AVAILABLE'";
        private static final String CREATE_CAR = "insert into " + TABLE_NAME +
                " (" + Fields.CAR_NAME + ", " +
                Fields.CAR_CATEGORY + ", " +
                Fields.CAR_CAPACITY + ", " +
                Fields.LICENSE_PLATE + ", " +
                Fields.DRIVER_LOGIN + ") " +
                "values (?, ?, ?, ?, ?)";
    }

    @Override
    public List<Car> findAvailableCars(int capacity, Category category) {
        List<Car> cars = new ArrayList<>();
        User driver = new User();
        Car car = new Car();
        try (PreparedStatement stmt = JdbcDaoUtils.wrapSqlException(() ->
                HikariTransactionManager.getCurrentConnection().prepareStatement(Queries.SELECT_AVAILABLE_CARS_WITH_DRIVERS))) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                driver.setLogin(rs.getString(UserDaoImpl.Fields.LOGIN));
                driver.setFirstname(rs.getString(UserDaoImpl.Fields.FIRSTNAME));
                driver.setLastname(rs.getString(UserDaoImpl.Fields.LASTNAME));
                driver.setPhone(rs.getString(UserDaoImpl.Fields.PHONE));
                driver.setEmail(rs.getString(UserDaoImpl.Fields.EMAIL));
                String[] roles = rs.getString(UserDaoImpl.Fields.ROLES).split(",");
                for (String r : roles) {
                    driver.addRole(Role.valueOf(r));
                }
                car.setDriver(driver);
                car.setCarName(rs.getString(Fields.CAR_NAME));
                car.setCarCategory(Category.valueOf(rs.getString(Fields.CAR_CATEGORY)));
                car.setStatus(Status.valueOf(rs.getString(Fields.STATUS)));
                car.setLicensePlate(rs.getString(Fields.LICENSE_PLATE));
                car.setCarCapacity(Integer.parseInt(rs.getString(Fields.CAR_CAPACITY)));
                car.setId(rs.getInt(Fields.ID));
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    @Override
    public void update(Car car) {

    }

    @Override
    public void createCar(Car car, User user) {
        try (PreparedStatement stmt = JdbcDaoUtils.wrapSqlException(() ->
                HikariTransactionManager.getCurrentConnection().prepareStatement(Queries.CREATE_CAR))) {
            stmt.setString(1, car.getCarName());
            stmt.setString(2, car.getCarCategory().toString());
            stmt.setInt(3, car.getCarCapacity());
            stmt.setString(4, car.getLicensePlate());
            stmt.setString(5, user.getLogin());
            stmt.executeUpdate();
            HikariTransactionManager.getCurrentConnection().commit();
        } catch (Exception e) {
            throw new DbException(e);
        }
    }
}

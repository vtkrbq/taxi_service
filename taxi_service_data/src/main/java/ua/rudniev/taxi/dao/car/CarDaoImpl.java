package ua.rudniev.taxi.dao.car;

import ua.rudniev.taxi.connection.SQLConstants;
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
import java.util.Optional;

public class CarDaoImpl implements CarDao {

    @Override
    public Optional<Car> findCarById(int id) {
        Car car = null;
        try (PreparedStatement stmt = JdbcDaoUtils.wrapSqlException(() ->
                HikariTransactionManager.getCurrentConnection().prepareStatement(SQLConstants.FIND_CAR_WITH_DRIVER))) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                car = new Car();
                car.setId(rs.getInt(SQLConstants.CarFields.ID));
                car.setCarName(rs.getString(SQLConstants.CarFields.CAR_NAME));
                car.setCarCategory(Category.valueOf(rs.getString(SQLConstants.CarFields.CAR_CATEGORY)));
                car.setCarCapacity(rs.getInt(SQLConstants.CarFields.CAR_CAPACITY));
                car.setLicensePlate(rs.getString(SQLConstants.CarFields.LICENSE_PLATE));

                User driver = new User();
                driver.setLogin(rs.getString(SQLConstants.UserFields.LOGIN));
                driver.setFirstname(rs.getString(SQLConstants.UserFields.FIRSTNAME));
                driver.setLastname(rs.getString(SQLConstants.UserFields.LASTNAME));
                driver.setPhone(rs.getString(SQLConstants.UserFields.PHONE));
                driver.setEmail(rs.getString(SQLConstants.UserFields.EMAIL));


                car.setDriver(driver);
            }
        } catch (Exception e) {
            throw new DbException(e);
        }
        return Optional.ofNullable(car);
    }

    @Override
    public List<Car> findAvailableCars(int capacity, Category category) {
        List<Car> cars = new ArrayList<>();
        User driver = new User();
        Car car = new Car();
        try (PreparedStatement stmt = JdbcDaoUtils.wrapSqlException(() ->
                HikariTransactionManager.getCurrentConnection().prepareStatement(SQLConstants.SELECT_AVAILABLE_CARS_WITH_DRIVERS))) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                driver.setLogin(rs.getString(SQLConstants.UserFields.LOGIN));
                driver.setFirstname(rs.getString(SQLConstants.UserFields.FIRSTNAME));
                driver.setLastname(rs.getString(SQLConstants.UserFields.LASTNAME));
                driver.setPhone(rs.getString(SQLConstants.UserFields.PHONE));
                driver.setEmail(rs.getString(SQLConstants.UserFields.EMAIL));
                String[] roles = rs.getString(SQLConstants.UserFields.ROLES).split(",");
                for (String r : roles) {
                    driver.addRole(Role.valueOf(r));
                }
                car.setDriver(driver);
                car.setCarName(rs.getString(SQLConstants.CarFields.CAR_NAME));
                car.setCarCategory(Category.valueOf(rs.getString(SQLConstants.CarFields.CAR_CATEGORY)));
                car.setStatus(Status.valueOf(rs.getString(SQLConstants.CarFields.STATUS)));
                car.setLicensePlate(rs.getString(SQLConstants.CarFields.LICENSE_PLATE));
                car.setCarCapacity(Integer.parseInt(rs.getString(SQLConstants.CarFields.CAR_CAPACITY)));
                car.setId(rs.getInt(SQLConstants.CarFields.ID));
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
                HikariTransactionManager.getCurrentConnection().prepareStatement(SQLConstants.CREATE_CAR))) {
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

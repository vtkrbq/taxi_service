package ua.rudniev.taxi.dao.jdbc.car;

import ua.rudniev.taxi.dao.car.CarDao;
import ua.rudniev.taxi.dao.jdbc.user.UserJdbcHelper;
import ua.rudniev.taxi.dao.jdbc.utils.PrepareStatementProvider;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.user.User;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarDaoImpl implements CarDao {

    private final UserJdbcHelper userJdbcHelper;

    private final CarJdbcHelper carJdbcHelper;

    private final PrepareStatementProvider prepareStatementProvider;


    public CarDaoImpl(UserJdbcHelper userJdbcHelper, CarJdbcHelper carJdbcHelper, PrepareStatementProvider prepareStatementProvider) {
        this.userJdbcHelper = userJdbcHelper;
        this.carJdbcHelper = carJdbcHelper;
        this.prepareStatementProvider = prepareStatementProvider;
    }

    @Override
    public Optional<Car> findCarById(int id) {
        return prepareStatementProvider.withPrepareStatement(CarSqlConstants.FIND_CAR_BY_ID, stmt -> {
            Car car = null;
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User driver = userJdbcHelper.fillUser(rs, false);
                car = carJdbcHelper.fillCar(rs, driver);
            }
            return Optional.ofNullable(car);
        });
    }

    @Override
    public List<Car> findAvailableCars(int capacity, Category category) {
        return prepareStatementProvider.withPrepareStatement(CarSqlConstants.FIND_AVAILABLE_CARS, stmt -> {
            List<Car> cars = new ArrayList<>();
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User driver = userJdbcHelper.fillUser(rs, false);
                Car car = carJdbcHelper.fillCar(rs, driver);
                //TODO if capacity = category =
                cars.add(car);
            }
            return cars;
        });
    }

    @Override
    public void update(Car car) {
        prepareStatementProvider.withPrepareStatement(CarSqlConstants.UPDATE_CAR, stmt -> {
            stmt.setString(1, car.getCarName());
            stmt.setString(2, car.getCarCategory().toString());
            stmt.setInt(3, car.getCarCapacity());
            stmt.setString(4, car.getLicensePlate());
            stmt.setString(5, car.getDriver().getLogin());
            stmt.setString(6, car.getStatus().toString());
            stmt.setString(7, car.getCurrentAddress().getAddress());
            stmt.setDouble(8, car.getCurrentAddress().getX());
            stmt.setDouble(9, car.getCurrentAddress().getY());
            stmt.setDouble(10, car.getId());
            stmt.executeUpdate();
            return null;
        });
    }

    @Override
    public void createCar(Car car) {
        prepareStatementProvider.withPrepareStatement(CarSqlConstants.CREATE_CAR, stmt -> {
            stmt.setString(1, car.getCarName());
            stmt.setString(2, car.getCarCategory().name());
            stmt.setInt(3, car.getCarCapacity());
            stmt.setString(4, car.getLicensePlate());
            stmt.setString(5, car.getDriver().getLogin());
            stmt.setString(5, car.getStatus().name());
            stmt.setString(7, car.getCurrentAddress().getAddress());
            stmt.setDouble(8, car.getCurrentAddress().getX());
            stmt.setDouble(9, car.getCurrentAddress().getY());
            stmt.executeUpdate();
            return null;
        });
    }

    @Override
    public void completeTrip(int id) {
        prepareStatementProvider.withPrepareStatement(CarSqlConstants.COMPLETE_TRIP_VIA_CAR, stmt -> {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return null;
        });
    }
}

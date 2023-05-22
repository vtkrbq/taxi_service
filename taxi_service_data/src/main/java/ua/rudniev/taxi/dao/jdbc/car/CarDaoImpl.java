package ua.rudniev.taxi.dao.jdbc.car;

import ua.rudniev.taxi.dao.car.CarDao;
import ua.rudniev.taxi.dao.car.CarField;
import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.dao.jdbc.user.UserJdbcHelper;
import ua.rudniev.taxi.dao.jdbc.utils.PrepareStatementProvider;
import ua.rudniev.taxi.dao.jdbc.utils.QueryBuilder;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.user.User;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This is implementation class of CarDao interface that has fields and methods for working with jdbc
 */
public class CarDaoImpl implements CarDao {

    private final UserJdbcHelper userJdbcHelper;

    private final CarJdbcHelper carJdbcHelper;

    private final PrepareStatementProvider prepareStatementProvider;

    private final QueryBuilder queryBuilder;

    private final CarFieldMapper carFieldMapper;


    public CarDaoImpl(UserJdbcHelper userJdbcHelper, CarJdbcHelper carJdbcHelper, PrepareStatementProvider prepareStatementProvider, QueryBuilder queryBuilder, CarFieldMapper carFieldMapper) {
        this.userJdbcHelper = userJdbcHelper;
        this.carJdbcHelper = carJdbcHelper;
        this.queryBuilder = queryBuilder;
        this.prepareStatementProvider = prepareStatementProvider;
        this.carFieldMapper = carFieldMapper;
    }

    @Override
    public Optional<Car> findCarById(int id) {
        return prepareStatementProvider.withPrepareStatement(CarSqlConstants.FIND_CAR_BY_ID, stmt -> {
            Car car = null;
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User driver = userJdbcHelper.fillUser(rs);
                car = carJdbcHelper.fillCar(rs, driver);
            }
            return Optional.ofNullable(car);
        });
    }

    @Override
    public List<Car> findCars(List<Filter<CarField>> filters) {
        String query = CarSqlConstants.FIND_AVAILABLE_CARS + queryBuilder.getFilterPart(filters, carFieldMapper);
        return prepareStatementProvider.withPrepareStatement(query, (preparedStatement) -> {
            queryBuilder.fillParams(preparedStatement, filters);
            ResultSet rs = preparedStatement.executeQuery();
            List<Car> cars = new ArrayList<>();
            while (rs.next()) {
                User driver = userJdbcHelper.fillUser(rs);
                Car car = carJdbcHelper.fillCar(rs, driver);
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
            stmt.setString(6, car.getStatus().name());
            stmt.setString(7, car.getCurrentAddress().getAddress());
            stmt.setDouble(8, car.getCurrentAddress().getX());
            stmt.setDouble(9, car.getCurrentAddress().getY());
            stmt.executeUpdate();
            return null;
        });
    }
}

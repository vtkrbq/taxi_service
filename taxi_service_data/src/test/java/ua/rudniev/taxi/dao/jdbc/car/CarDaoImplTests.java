package ua.rudniev.taxi.dao.jdbc.car;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import ua.rudniev.taxi.dao.car.CarField;
import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.dao.common.filter.Value;
import ua.rudniev.taxi.dao.jdbc.user.UserJdbcHelper;
import ua.rudniev.taxi.dao.jdbc.utils.PrepareStatementProvider;
import ua.rudniev.taxi.dao.jdbc.utils.QueryBuilder;
import ua.rudniev.taxi.dao.testutils.CarDataProvider;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Status;
import ua.rudniev.taxi.model.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarDaoImplTests {
    CarDataProvider dp = new CarDataProvider();

    @Mock
    private QueryBuilder queryBuilder;

    @Mock
    private CarFieldMapper carFieldMapper;

    @Mock
    private UserJdbcHelper userJdbcHelper;

    @Mock
    private CarJdbcHelper carJdbcHelper;

    @Mock
    private PrepareStatementProvider prepareStatementProvider;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private CarDaoImpl carDao;

    @Mock
    private PreparedStatement preparedStatement;

    @BeforeEach
    public void beforeEach() {
        Answer<?> answer = invocationOnMock -> {
            PrepareStatementProvider.Execution<?> execution = invocationOnMock
                    .getArgument(1, PrepareStatementProvider.Execution.class);
            return execution.execute(preparedStatement);
        };
        when(prepareStatementProvider.withPrepareStatement(any(), any())).thenAnswer(answer);
    }

    @Test
    public void shouldReturnCarIfCarExistInDB() throws SQLException {
        // given
        Car car = dp.createCar();
        User user = dp.createUser();

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(userJdbcHelper.fillUser(resultSet)).thenReturn(user);
        when(carJdbcHelper.fillCar(resultSet, user)).thenReturn(car);

        // when
        Optional<Car> carOptional = carDao.findCarById(dp.CAR_ID);

        // then
        assertThat(carOptional.isPresent()).isTrue();
        Car resultCar = carOptional.get();
        assertThat(resultCar.getId()).isEqualTo(dp.CAR_ID);
        assertThat(resultCar.getCarName()).isEqualTo(car.getCarName());
        assertThat(resultCar.getCarCapacity()).isEqualTo(car.getCarCapacity());
        assertThat(resultCar.getLicensePlate()).isEqualTo(car.getLicensePlate());
        assertThat(resultCar.getCarCategory()).isEqualTo(car.getCarCategory());
        assertThat(resultCar.getDriver()).isEqualTo(car.getDriver());
        assertThat(resultCar.getStatus()).isEqualTo(car.getStatus());
        assertThat(resultCar.getCurrentAddress()).isEqualTo(car.getCurrentAddress());

        verify(prepareStatementProvider).withPrepareStatement(eq(CarSqlConstants.FIND_CAR_BY_ID), any());
        verify(preparedStatement).setInt(1, dp.CAR_ID);
        verify(preparedStatement).executeQuery();
        verify(resultSet, times(2)).next();
        verify(userJdbcHelper).fillUser(resultSet);
        verify(carJdbcHelper).fillCar(resultSet, user);
    }

    @Test
    public void shouldReturnEmptyOptionalIfCarDoesntExistInDB() throws SQLException {
        // given
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        // when
        Optional<Car> carOptional = carDao.findCarById(dp.CAR_ID);

        // then
        assertThat(carOptional.isPresent()).isFalse();

        verify(prepareStatementProvider).withPrepareStatement(eq(CarSqlConstants.FIND_CAR_BY_ID), any());
        verify(preparedStatement).setInt(1, dp.CAR_ID);
        verify(preparedStatement).executeQuery();
        verify(resultSet, times(1)).next();
        verify(userJdbcHelper, times(0)).fillUser(resultSet);
        verify(carJdbcHelper, times(0)).fillCar(any(), any());
    }

    @Test
    public void shouldReturnListOfCarsIfCarsAvailableInDB() throws SQLException {
        // given
        Car car = dp.createCar();
        User driver = dp.createUser();

        List<Filter<CarField>> filters = new ArrayList<>();

        Filter<CarField> filterStatus = new Filter<>(
                CarField.STATUS,
                new Value(Status.AVAILABLE.toString())
        );
        filters.add(filterStatus);


        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(userJdbcHelper.fillUser(resultSet)).thenReturn(driver);
        when(carJdbcHelper.fillCar(resultSet, driver)).thenReturn(car);
        when(queryBuilder.getFilterPart(filters, carFieldMapper)).thenReturn(" where status='AVAILABLE'");

        // when
        List<Car> carList = carDao.findCars(filters);

        // then
        assertThat(carList).isNotNull();
        assertThat(carList).isNotEmpty();
        Car resultCar = carList.get(0);
        assertThat(resultCar.getId()).isEqualTo(dp.CAR_ID);
        assertThat(resultCar.getCarName()).isEqualTo(car.getCarName());
        assertThat(resultCar.getCarCapacity()).isEqualTo(car.getCarCapacity());
        assertThat(resultCar.getLicensePlate()).isEqualTo(car.getLicensePlate());
        assertThat(resultCar.getCarCategory()).isEqualTo(car.getCarCategory());
        assertThat(resultCar.getDriver()).isEqualTo(car.getDriver());
        assertThat(resultCar.getStatus()).isEqualTo(car.getStatus());
        assertThat(resultCar.getCurrentAddress()).isEqualTo(car.getCurrentAddress());

        verify(prepareStatementProvider).withPrepareStatement(eq(dp.FIND_ALL_AVAILABLE_CARS), any());
        verify(preparedStatement).executeQuery();
        verify(resultSet, times(2)).next();
        verify(userJdbcHelper).fillUser(resultSet);
        verify(carJdbcHelper).fillCar(resultSet, driver);
    }

    @Test
    public void shouldReturnEmptyListIfNoCarsAvailableInDb() throws SQLException {
        // given
        List<Filter<CarField>> filters = new ArrayList<>();

        Filter<CarField> filterStatus = new Filter<>(
                CarField.STATUS,
                new Value(Status.AVAILABLE.toString())
        );
        filters.add(filterStatus);


        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        when(queryBuilder.getFilterPart(filters, carFieldMapper)).thenReturn(" where status='AVAILABLE'");

        // when
        List<Car> carList = carDao.findCars(filters);

        // then
        assertThat(carList).isNotNull();
        assertThat(carList).isEmpty();

        verify(prepareStatementProvider).withPrepareStatement(eq(dp.FIND_ALL_AVAILABLE_CARS), any());
        verify(preparedStatement).executeQuery();
        verify(resultSet, times(1)).next();
        verify(userJdbcHelper, times(0)).fillUser(resultSet);
        verify(carJdbcHelper, times(0)).fillCar(any(), any());
    }

    @Test
    public void updateCarShouldExecutePreparedStatement() throws SQLException {
        // given
        Car car = dp.createCar();
        // when
        carDao.update(car);
        // then
        verify(prepareStatementProvider).withPrepareStatement(eq(CarSqlConstants.UPDATE_CAR), any());
        verify(preparedStatement).setString(1, dp.CAR_NAME);
        verify(preparedStatement).setString(2, dp.CAR_CATEGORY.toString());
        verify(preparedStatement).setInt(3, dp.CAR_CAPACITY);
        verify(preparedStatement).setString(4, dp.CAR_LICENSE_PLATE);
        verify(preparedStatement).setString(5, dp.DRIVER_LOGIN);
        verify(preparedStatement).setString(6, dp.CAR_STATUS.toString());
        verify(preparedStatement).setString(7, dp.CAR_ADDRESS_POINT.getAddress());
        verify(preparedStatement).setDouble(8, dp.CAR_ADDRESS_POINT.getX());
        verify(preparedStatement).setDouble(9, dp.CAR_ADDRESS_POINT.getY());
        verify(preparedStatement).setDouble(10, dp.CAR_ID);
        verify(preparedStatement).executeUpdate();
    }
    @Test
    public void createCarShouldExecutePreparedStatement() throws SQLException {
        // given
        Car car = dp.createCar();
        // when
        carDao.createCar(car);
        // then
        verify(prepareStatementProvider).withPrepareStatement(eq(CarSqlConstants.CREATE_CAR), any());
        verify(preparedStatement).setString(1, dp.CAR_NAME);
        verify(preparedStatement).setString(2, dp.CAR_CATEGORY.toString());
        verify(preparedStatement).setInt(3, dp.CAR_CAPACITY);
        verify(preparedStatement).setString(4, dp.CAR_LICENSE_PLATE);
        verify(preparedStatement).setString(5, dp.DRIVER_LOGIN);
        verify(preparedStatement).setString(6, dp.CAR_STATUS.toString());
        verify(preparedStatement).setString(7, dp.CAR_ADDRESS_POINT.getAddress());
        verify(preparedStatement).setDouble(8, dp.CAR_ADDRESS_POINT.getX());
        verify(preparedStatement).setDouble(9, dp.CAR_ADDRESS_POINT.getY());
        verify(preparedStatement).executeUpdate();
    }

//    @Test
//    public void updateTripShouldExecutePreparedStatement() throws SQLException {
//        // given
//        int tripId = 1;
//        // when
//        carDao.completeTrip(tripId);
//        // then
//        verify(prepareStatementProvider).withPrepareStatement(eq(CarSqlConstants.COMPLETE_TRIP_VIA_CAR), any());
//        verify(preparedStatement).setInt(1, tripId);
//        verify(preparedStatement).executeUpdate();
//    }
}

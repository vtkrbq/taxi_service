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
import ua.rudniev.taxi.dao.jdbc.user.UserSqlConstants;
import ua.rudniev.taxi.dao.jdbc.utils.PrepareStatementProvider;
import ua.rudniev.taxi.dao.jdbc.utils.QueryBuilder;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.car.Status;
import ua.rudniev.taxi.model.trip.AddressPoint;
import ua.rudniev.taxi.model.user.Role;
import ua.rudniev.taxi.model.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ua.rudniev.taxi.dao.jdbc.car.CarSqlConstants.CAR_TABLE_NAME;

@ExtendWith(MockitoExtension.class)
public class CarDaoImplTest {
    private final int CAR_ID = 1;
    private final String CAR_NAME = "Lada Priora";
    private final Category CAR_CATEGORY = Category.COMFORT;
    private final int CAR_CAPACITY = 3;
    private final Status CAR_STATUS = Status.AVAILABLE;
    private final String CAR_LICENSE_PLATE = "AK1345OK";
    private final AddressPoint CAR_ADDRESS_POINT  = new AddressPoint(
            49.991981,
            36.328126,
            "Харків, вул. Гвардійців-Широнінців, 5");
    private final String DRIVER_LOGIN = "driver";
    private final String DRIVER_NAME = "Volodymir";
    private final String DRIVER_LASTNAME = "Slobozhanskiy";
    private final String DRIVER_PHONE = "+380680000009";
    private final String DRIVER_EMAIL = "vodila_bombit@gmail.com";
    private final int DRIVER_DISCOUNT = 0;
    private final String FIND_ALL_AVAILABLE_CARS = CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.ID + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.DRIVER_LOGIN + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.NAME + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.CATEGORY + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.STATUS + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.CAPACITY + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.LICENSE_PLATE + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.CURRENT_ADDRESS + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.CURRENT_POINT_X + ", " +
            CAR_TABLE_NAME + "." + CarSqlConstants.CarFields.CURRENT_POINT_Y + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.LOGIN + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.FIRSTNAME + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.LASTNAME + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.PHONE + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.EMAIL + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.DISCOUNT + ", " +
            UserSqlConstants.USER_TABLE_NAME + "." + UserSqlConstants.UserFields.ROLES +
            " from car inner join app_user on app_user.login=car.driver_login " +
            "where status='AVAILABLE'";

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

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void beforeEach() {
        queryBuilder = new QueryBuilder();
        carFieldMapper = new CarFieldMapper();
        Answer<Optional<Car>> answer = invocationOnMock -> {
            PrepareStatementProvider.Execution<Optional<Car>> execution = invocationOnMock
                    .getArgument(1, PrepareStatementProvider.Execution.class);
            return execution.execute(preparedStatement);
        };
        when(prepareStatementProvider.withPrepareStatement(any(), any())).thenAnswer(answer);
    }

    @Test
    public void shouldReturnCarIfCarExistInDB() throws SQLException {
        // given
        Car car = createCar();
        User user = createUser();

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(userJdbcHelper.fillUser(resultSet)).thenReturn(user);
        when(carJdbcHelper.fillCar(resultSet, user)).thenReturn(car);

        // when
        Optional<Car> carOptional = carDao.findCarById(CAR_ID);

        // then
        assertThat(carOptional.isPresent()).isTrue();
        Car resultCar = carOptional.get();
        assertThat(resultCar.getId()).isEqualTo(CAR_ID);
        assertThat(resultCar.getCarName()).isEqualTo(car.getCarName());

        verify(prepareStatementProvider).withPrepareStatement(eq(CarSqlConstants.FIND_CAR_BY_ID), any());
        verify(preparedStatement).setInt(1, CAR_ID);
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
        Optional<Car> carOptional = carDao.findCarById(CAR_ID);

        // then
        assertThat(carOptional.isPresent()).isFalse();

        verify(prepareStatementProvider).withPrepareStatement(eq(CarSqlConstants.FIND_CAR_BY_ID), any());
        verify(preparedStatement).setInt(1, CAR_ID);
        verify(preparedStatement).executeQuery();
        verify(resultSet, times(1)).next();
        verify(userJdbcHelper, times(0)).fillUser(resultSet);
        verify(carJdbcHelper, times(0)).fillCar(any(), any());
    }

    @Test
    public void shouldReturnListOfCarsIfCarsAvailableInDB() throws SQLException {
        Car car = createCar();
        User driver = createUser();
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
        when(carFieldMapper.mapToSqlField(CarField.STATUS)).thenReturn("status");
        when(queryBuilder.getFilterPart(filters, carFieldMapper)).thenReturn(FIND_ALL_AVAILABLE_CARS);

        // when
        List<Car> carList = carDao.findAvailableCars(filters);

        // then
        assertThat(carList == null || carList.isEmpty()).isFalse();
        Car resultCar = carList.get(1);
        assertThat(resultCar.getId()).isEqualTo(CAR_ID);
        assertThat(resultCar.getCarName()).isEqualTo(car.getCarName());
        //TODO тут надо остальные поля проверять?

        verify(prepareStatementProvider).withPrepareStatement(eq(FIND_ALL_AVAILABLE_CARS), any());
        verify(preparedStatement).setInt(1, CAR_ID);
        verify(preparedStatement).executeQuery();
        verify(resultSet, times(2)).next();
        verify(userJdbcHelper).fillUser(resultSet);
        verify(carJdbcHelper).fillCar(resultSet, driver);
    }

    //TODO: Пирог: остальные методы

    private Car createCar() {
        Car car = new Car();
        car.setId(CAR_ID);
        car.setCarName(CAR_NAME);
        car.setCarCategory(CAR_CATEGORY);
        car.setCarCapacity(CAR_CAPACITY);
        car.setStatus(CAR_STATUS);
        car.setLicensePlate(CAR_LICENSE_PLATE);
        car.setCurrentAddress(CAR_ADDRESS_POINT);
        car.setDriver(createUser());
        return car;
    }

    private User createUser() {
        User driver = new User();
        driver.setLogin(DRIVER_LOGIN);
        driver.setFirstname(DRIVER_NAME);
        driver.setLastname(DRIVER_LASTNAME);
        driver.setPhone(DRIVER_PHONE);
        driver.setEmail(DRIVER_EMAIL);
        driver.setDiscount(DRIVER_DISCOUNT);
        driver.addRole(Role.USER);
        driver.addRole(Role.DRIVER);
        return driver;
    }
}

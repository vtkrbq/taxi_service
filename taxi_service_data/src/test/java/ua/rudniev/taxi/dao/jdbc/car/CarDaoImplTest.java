package ua.rudniev.taxi.dao.jdbc.car;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import ua.rudniev.taxi.dao.jdbc.user.UserJdbcHelper;
import ua.rudniev.taxi.dao.jdbc.utils.PrepareStatementProvider;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarDaoImplTest {

    private final int CAR_ID = 1;

    private final String CAR_NAME = "Lada Priora";

    //TODO: Пирог: остальные поля

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
        when(userJdbcHelper.fillUser(resultSet, false)).thenReturn(user);
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
        verify(userJdbcHelper).fillUser(resultSet, false);
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
        verify(userJdbcHelper, times(0)).fillUser(resultSet, false);
        verify(carJdbcHelper, times(0)).fillCar(any(), any());
    }

    //TODO: Пирог: остальные методы

    private Car createCar() {
        Car car = new Car();
        car.setId(CAR_ID);
        return car;
        //TODO: Пирог: все филды
    }

    private User createUser() {
        return new User();
        //TODO: Пирог: все филды
    }
}

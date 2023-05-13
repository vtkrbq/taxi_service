package ua.rudniev.taxi.dao.jdbc.trip;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.dao.common.order.OrderBy;
import ua.rudniev.taxi.dao.jdbc.car.CarJdbcHelper;
import ua.rudniev.taxi.dao.jdbc.user.UserJdbcHelper;
import ua.rudniev.taxi.dao.jdbc.utils.PrepareStatementProvider;
import ua.rudniev.taxi.dao.jdbc.utils.QueryBuilder;
import ua.rudniev.taxi.dao.testutils.CarDataProvider;
import ua.rudniev.taxi.dao.testutils.TripOrderDataProvider;
import ua.rudniev.taxi.dao.trip.TripOrderField;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.trip.TripOrder;
import ua.rudniev.taxi.model.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TripOrderDaoImplTests {
    TripOrderDataProvider todp = new TripOrderDataProvider();
    CarDataProvider cdp = new CarDataProvider();

    @Mock
    private QueryBuilder queryBuilder;

    @Mock
    private TripOrderFieldMapper tripOrderFieldMapper;

    @Mock
    private UserJdbcHelper userJdbcHelper;

    @Mock
    private CarJdbcHelper carJdbcHelper;

    @Mock
    private PrepareStatementProvider prepareStatementProvider;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private TripOrderDaoImpl tripOrderDao;

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
    public void shouldReturnListOfTripOrdersIfTripOrdersExistInDB() throws SQLException {
        // given
        TripOrder expectedTripOrder = todp.createTripOrder();
        List<OrderBy<TripOrderField>> orderByList = new ArrayList<>();
        List<Filter<TripOrderField>> filterList = new ArrayList<>();
        User user = todp.createUser();
        Car car = cdp.createCar();
        User driver = cdp.createUser();
        int pageSize = 1;
        int pageIndex = 1;

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt(TripOrderSqlConstants.TripOrderFields.ID)).thenReturn(todp.TRIP_ORDER_ID);
        when(resultSet.getDouble(TripOrderSqlConstants.TripOrderFields.DEPARTURE_X)).thenReturn(todp.TRIP_ORDER_DEPARTURE.getX());
        when(resultSet.getDouble(TripOrderSqlConstants.TripOrderFields.DEPARTURE_Y)).thenReturn(todp.TRIP_ORDER_DEPARTURE.getY());
        when(resultSet.getString(TripOrderSqlConstants.TripOrderFields.DEPARTURE_ADDRESS)).thenReturn(todp.TRIP_ORDER_DEPARTURE.getAddress());
        when(resultSet.getDouble(TripOrderSqlConstants.TripOrderFields.DESTINATION_X)).thenReturn(todp.TRIP_ORDER_DESTINATION.getX());
        when(resultSet.getDouble(TripOrderSqlConstants.TripOrderFields.DESTINATION_Y)).thenReturn(todp.TRIP_ORDER_DESTINATION.getY());
        when(resultSet.getString(TripOrderSqlConstants.TripOrderFields.DESTINATION_ADDRESS)).thenReturn(todp.TRIP_ORDER_DESTINATION.getAddress());
        when(resultSet.getString(TripOrderSqlConstants.TripOrderFields.CATEGORY)).thenReturn(todp.TRIP_ORDER_CATEGORY.toString());
        when(resultSet.getInt(TripOrderSqlConstants.TripOrderFields.CAPACITY)).thenReturn(todp.TRIP_ORDER_CAPACITY);
        when(resultSet.getBigDecimal(TripOrderSqlConstants.TripOrderFields.PRICE)).thenReturn(todp.TRIP_ORDER_PRICE);
        when(resultSet.getTimestamp(TripOrderSqlConstants.TripOrderFields.CREATED)).thenReturn(Timestamp.from(todp.TRIP_ORDER_TIMESTAMP_CREATED));
        when(resultSet.getTimestamp(TripOrderSqlConstants.TripOrderFields.END_OF_TRIP)).thenReturn(Timestamp.from(todp.TRIP_ORDER_TIMESTAMP_END));
        when(userJdbcHelper.fillUser(resultSet, TripOrderSqlConstants.CLIENT_PREFIX)).thenReturn(user);
        when(userJdbcHelper.fillUser(resultSet, TripOrderSqlConstants.DRIVER_PREFIX)).thenReturn(driver);
        when(carJdbcHelper.fillCar(resultSet, driver, TripOrderSqlConstants.CAR_PREFIX)).thenReturn(car);
        when(queryBuilder.getFilterOrderAndLimitPart(filterList, orderByList, tripOrderFieldMapper)).thenReturn(" limit 1 offset 1");


        // when
        List<TripOrder> resultList = tripOrderDao.findAllTripOrders(pageIndex, pageSize, orderByList, filterList);

        // then
        assertThat(resultList).isNotNull();
        assertThat(resultList).isNotEmpty();
        TripOrder resultTripOrder = resultList.get(0);
        assertThat(resultTripOrder.getId()).isEqualTo(expectedTripOrder.getId());
        assertThat(resultTripOrder.getDeparture()).isEqualTo(expectedTripOrder.getDeparture());
        assertThat(resultTripOrder.getDestination()).isEqualTo(expectedTripOrder.getDestination());
        assertThat(resultTripOrder.getCategory()).isEqualTo(expectedTripOrder.getCategory());
        assertThat(resultTripOrder.getCapacity()).isEqualTo(expectedTripOrder.getCapacity());
        assertThat(resultTripOrder.getUser()).isEqualTo(expectedTripOrder.getUser());
        assertThat(resultTripOrder.getTimestampCreated()).isEqualTo(expectedTripOrder.getTimestampCreated());
        assertThat(resultTripOrder.getTimestampEnd()).isEqualTo(expectedTripOrder.getTimestampEnd());
        assertThat(resultTripOrder.getCar()).isEqualTo(expectedTripOrder.getCar());
        assertThat(resultTripOrder.getPrice()).isEqualTo(expectedTripOrder.getPrice());

        verify(prepareStatementProvider).withPrepareStatement(eq(todp.FIND_ALL_TRIP_ORDERS), any());
        verify(preparedStatement).executeQuery();
        verify(resultSet, times(2)).next();
        verify(userJdbcHelper).fillUser(resultSet, TripOrderSqlConstants.CLIENT_PREFIX);
        verify(userJdbcHelper).fillUser(resultSet, TripOrderSqlConstants.DRIVER_PREFIX);
        verify(carJdbcHelper).fillCar(resultSet, driver, TripOrderSqlConstants.CAR_PREFIX);
    }

}

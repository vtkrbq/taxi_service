package ua.rudniev.taxi.servlet;

import ua.rudniev.taxi.ComponentsContainer;
import ua.rudniev.taxi.StringUtils;
import ua.rudniev.taxi.dao.common.field.Field;
import ua.rudniev.taxi.dao.common.field.FieldType;
import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.dao.common.filter.FilterType;
import ua.rudniev.taxi.dao.common.filter.Value;
import ua.rudniev.taxi.dao.common.order.OrderBy;
import ua.rudniev.taxi.dao.common.order.OrderByType;
import ua.rudniev.taxi.dao.trip.TripOrderField;
import ua.rudniev.taxi.model.trip.TripOrder;
import ua.rudniev.taxi.service.OrderingService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static ua.rudniev.taxi.dao.common.field.FieldType.*;

@WebServlet("/orderStatistics")
public class OrderStatisticsServlet extends HttpServlet {
    private final OrderingService orderingService = ComponentsContainer.getInstance().getOrderingService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentPage = 1;
        int recordsPerPage = 5; //TODO: вынести в константы
        TripOrderField orderByField = TripOrderField.CREATED;
        OrderByType orderByType = OrderByType.DESC;

        String filterBy = req.getParameter("filterBy");
        String filterKey = req.getParameter("filterKey");
        String sortTypeStr = req.getParameter("sortType");
        String sortByStr = req.getParameter("sortBy");
        String filterDate = req.getParameter("created");

        if (!StringUtils.isEmptyOrNull(req.getParameter("quantity"))) recordsPerPage = Integer.parseInt(req.getParameter("quantity"));
        if (!StringUtils.isEmptyOrNull(req.getParameter("currentPage"))) currentPage = Integer.parseInt(req.getParameter("currentPage"));

        if(!StringUtils.isEmptyOrNull(sortByStr)) orderByField = TripOrderField.valueOf(sortByStr);
        if(!StringUtils.isEmptyOrNull(sortTypeStr)) orderByType = OrderByType.valueOf(sortTypeStr);

        OrderBy<TripOrderField> orderBy = new OrderBy<>(orderByField, orderByType);

        List<Filter<TripOrderField>> filters = new ArrayList<>();

        if (!StringUtils.isEmptyOrNull(filterKey) && !StringUtils.isEmptyOrNull(filterBy)) {
            TripOrderField field = TripOrderField.valueOf(filterBy);
            Filter<TripOrderField> filter = new Filter<>(
                    field,
                    getValueByField(filterKey, field)
            );
            filters.add(filter);
        }

        if (!StringUtils.isEmptyOrNull(filterDate)) {
            TripOrderField dateCreated = TripOrderField.CREATED;

            Value fromDate = getValueByField(filterDate, dateCreated);
            fromDate.setInstant(fromDate.getInstant().minus(1, ChronoUnit.DAYS));

            Filter<TripOrderField> dateFromFilter = new Filter<>(
                    dateCreated,
                    fromDate,
                    FilterType.MORE
            );

            filters.add(dateFromFilter);

            Value toDate = getValueByField(filterDate, dateCreated);
            toDate.setInstant(toDate.getInstant().plus(1, ChronoUnit.DAYS));

            Filter<TripOrderField> dateToFilter = new Filter<>(
                    dateCreated,
                    toDate,
                    FilterType.LESS
            );
            filters.add(dateToFilter);
        }

        List<TripOrder> tripOrders = orderingService.findAllTripOrders(
                (currentPage - 1) * recordsPerPage,
                recordsPerPage,
                List.of(orderBy),
                filters);
        int totalRecords = orderingService.getCountOfRecords(filters);
        int pagesQuantity = (int) Math.ceil(totalRecords * 1.0 / recordsPerPage);
        req.setAttribute("toCompare", Instant.MIN);
        req.setAttribute("tripOrders", tripOrders);
        req.setAttribute("pagesQuantity", pagesQuantity);
        req.setAttribute("recordsPerPage", recordsPerPage);
        req.setAttribute("currentPage", currentPage);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/orderStatistics.jsp");
        dispatcher.forward(req, resp);
    }

    private Value getValueByField(String value, Field field) { //TODO: это можно в отельный класс вынести
        FieldType fieldType = field.getFieldType();
        if(fieldType == INTEGER) {
            return new Value(Integer.parseInt(value));
        }
        if(fieldType == STRING) {
            return new Value(value);
        }
        if(fieldType == BIG_DECIMAL) {
            return new Value(new BigDecimal(value));
        }
        if(fieldType == INSTANT) {
            return new Value(Instant.parse(value + "T00:00:00Z"));
        }
        throw new IllegalArgumentException("Unknown fieldType: " + fieldType);
    }
}

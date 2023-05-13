package ua.rudniev.taxi.servlet;

import ua.rudniev.taxi.ComponentsContainer;
import ua.rudniev.taxi.StringUtils;
import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.dao.common.filter.FilterType;
import ua.rudniev.taxi.dao.common.filter.Value;
import ua.rudniev.taxi.dao.common.order.OrderBy;
import ua.rudniev.taxi.dao.common.order.OrderByType;
import ua.rudniev.taxi.dao.trip.TripOrderField;
import ua.rudniev.taxi.servlet.utils.TripOrderServletUtils;
import ua.rudniev.taxi.servlet.utils.ValueProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * This class has fields and methods that responds to the http request from /orderStatistics url
 */
@WebServlet(
        name = "OrderStatisticsServlet",
        urlPatterns = "/orderStatistics"
)
public class OrderStatisticsServlet extends HttpServlet {
    private final TripOrderServletUtils tripOrderServletUtils = ComponentsContainer.getInstance().getTripOrderServletUtils();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentPage = tripOrderServletUtils.getCurrentPageOrDefault(req, "currentPage");
        int recordsPerPage = tripOrderServletUtils.getRecordsPerPageOrDefault(req, "quantity");
        TripOrderField orderByField = tripOrderServletUtils.getOrderByFieldOrDefault(req, "sortBy");
        OrderByType orderByType = tripOrderServletUtils.getOrderByTypeOrDefault(req, "sortType");

        String filterBy = req.getParameter("filterBy");
        String filterKey = req.getParameter("filterKey");
        String filterDate = req.getParameter("created");

        OrderBy<TripOrderField> orderBy = new OrderBy<>(orderByField, orderByType);

        List<Filter<TripOrderField>> filters = new ArrayList<>();
        addFilter(filterKey, filterBy, filters);
        addFilterDate(filterDate, filters);

        req.getSession().setAttribute("filterBy", filterBy); //TODO: Пирог: нам нежелательно тут сессию использовать, почему нельзя req.setAttribute?
        req.getSession().setAttribute("filterKey", filterKey);
        req.getSession().setAttribute("sortType", orderByField.name());
        req.getSession().setAttribute("sortBy", orderByType.name());
        req.getSession().setAttribute("created", filterDate);
        req.getSession().setAttribute("quantity", recordsPerPage);
        tripOrderServletUtils.getTripOrdersAndFillRequestAttributes(currentPage, recordsPerPage, orderBy, filters, req);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/orderStatistics.jsp");
        dispatcher.forward(req, resp);
    }

    private void addFilter(String filterKey, String filterBy, List<Filter<TripOrderField>> filters) {
        if (!StringUtils.isEmptyOrNull(filterKey) && !StringUtils.isEmptyOrNull(filterBy)) {
            TripOrderField field = TripOrderField.valueOf(filterBy);
            Filter<TripOrderField> filter = new Filter<>(
                    field,
                    ValueProvider.getValueByField(filterKey, field)
            );
            filters.add(filter);
        }
    }

    private void addFilterDate(String filterDate, List<Filter<TripOrderField>> filters) {
        if (!StringUtils.isEmptyOrNull(filterDate)) {
            TripOrderField dateCreated = TripOrderField.CREATED;

            Value fromDate = ValueProvider.getValueByField(filterDate, dateCreated);
            fromDate.setInstant(fromDate.getInstant().minus(1, ChronoUnit.DAYS));

            Filter<TripOrderField> dateFromFilter = new Filter<>(
                    dateCreated,
                    fromDate,
                    FilterType.MORE
            );

            filters.add(dateFromFilter);

            Value toDate = ValueProvider.getValueByField(filterDate, dateCreated);
            toDate.setInstant(toDate.getInstant().plus(1, ChronoUnit.DAYS));

            Filter<TripOrderField> dateToFilter = new Filter<>(
                    dateCreated,
                    toDate,
                    FilterType.LESS
            );
            filters.add(dateToFilter);
        }
    }
}

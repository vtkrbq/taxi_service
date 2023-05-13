package ua.rudniev.taxi.servlet.utils;

import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.dao.common.order.OrderBy;
import ua.rudniev.taxi.dao.common.order.OrderByType;
import ua.rudniev.taxi.dao.trip.TripOrderField;
import ua.rudniev.taxi.model.trip.TripOrder;
import ua.rudniev.taxi.service.OrderingService;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;

import static ua.rudniev.taxi.StringUtils.isEmptyOrNull;

public class TripOrderServletUtils {

    private final OrderingService orderingService;

    private final int DEFAULT_CURRENT_PAGE = 1;

    private final int DEFAULT_RECORDS_PER_PAGE = 5;

    public TripOrderServletUtils(OrderingService orderingService) {
        this.orderingService = orderingService;
    }

    public void getTripOrdersAndFillRequestAttributes(
            int currentPage,
            int recordsPerPage,
            OrderBy<TripOrderField> orderBy,
            List<Filter<TripOrderField>> filters,
            HttpServletRequest req
    ) {
        List<TripOrder> tripOrders = orderingService.findAllTripOrders(
                (currentPage - 1) * recordsPerPage,
                recordsPerPage,
                List.of(orderBy),
                filters);
        int totalRecords = orderingService.getCountOfRecords(filters);
        int pagesQuantity = (int) Math.ceil(totalRecords * 1.0 / recordsPerPage);
        req.setAttribute("toCompare", Instant.MIN); //TODO: Пирог: Переделать на null
        req.setAttribute("tripOrders", tripOrders);
        req.setAttribute("pagesQuantity", pagesQuantity);
        req.setAttribute("recordsPerPage", recordsPerPage);
        req.setAttribute("currentPage", currentPage);
    }

    public int getCurrentPageOrDefault(HttpServletRequest req, String paramName) {
        String currentPageStr = req.getParameter(paramName);
        return !isEmptyOrNull(currentPageStr) ? Integer.parseInt(currentPageStr) : DEFAULT_CURRENT_PAGE;
    }

    public int getRecordsPerPageOrDefault(HttpServletRequest req, String paramName) {
        String RecordsPerPage = req.getParameter(paramName);
        return !isEmptyOrNull(RecordsPerPage) ? Integer.parseInt(RecordsPerPage) : getDefaultRecordsPerPage();
    }

    public int getDefaultRecordsPerPage() {
        return DEFAULT_RECORDS_PER_PAGE;
    }

    public TripOrderField getOrderByFieldOrDefault(HttpServletRequest req, String paramName) {
        String sortByStr = req.getParameter(paramName);
        return !isEmptyOrNull(sortByStr) ? TripOrderField.valueOf(sortByStr) : getDefaultOrderByField();
    }

    public TripOrderField getDefaultOrderByField() {
       return TripOrderField.CREATED;
    }

    public OrderByType getOrderByTypeOrDefault(HttpServletRequest req, String paramName) {
        String OrderByTypeStr = req.getParameter(paramName);
        return !isEmptyOrNull(OrderByTypeStr) ? OrderByType.valueOf(OrderByTypeStr) : getDefaultOrderByType();
    }

    public OrderByType getDefaultOrderByType() {
        return OrderByType.DESC;
    }
}

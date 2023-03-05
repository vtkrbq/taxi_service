package ua.rudniev.taxi.servlet;

import ua.rudniev.taxi.ComponentsContainer;
import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.dao.common.filter.Value;
import ua.rudniev.taxi.dao.common.order.OrderBy;
import ua.rudniev.taxi.dao.common.order.OrderByType;
import ua.rudniev.taxi.dao.trip.TripOrderField;
import ua.rudniev.taxi.model.trip.TripOrder;
import ua.rudniev.taxi.model.user.User;
import ua.rudniev.taxi.service.OrderingService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ua.rudniev.taxi.web.SessionAttributes.CURRENT_USER;

@WebServlet("/driverStatistics")
public class DriverStatisticsServlet  extends HttpServlet {
    private final OrderingService orderingService = ComponentsContainer.getInstance().getOrderingService();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentPage = 1;
        int recordsPerPage = 5;
        User user = (User) req.getSession().getAttribute(CURRENT_USER);
        if (req.getParameter("currentPage") != null) currentPage = Integer.parseInt(req.getParameter("currentPage"));
        OrderBy<TripOrderField> orderBy = new OrderBy<>(TripOrderField.CREATED, OrderByType.DESC);
        Filter<TripOrderField> filter =  new Filter<>(
                TripOrderField.DRIVER_LOGIN,
                new Value(user.getLogin())
        );
        List<TripOrder> tripOrders = orderingService.findAllTripOrders(
                (currentPage - 1) * recordsPerPage,
                recordsPerPage,
                List.of(orderBy),
                List.of(filter));
        int totalRecords = orderingService.getCountOfRecords(List.of(filter));
        int pagesQuantity = (int) Math.ceil(totalRecords * 1.0 / recordsPerPage);
        req.setAttribute("tripOrders", tripOrders);
        req.setAttribute("pagesQuantity", pagesQuantity);
        req.setAttribute("recordsPerPage", recordsPerPage);
        req.setAttribute("currentPage", currentPage);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/driverStatistics.jsp");
        dispatcher.forward(req, resp);
    }
}

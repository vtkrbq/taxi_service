package ua.rudniev.taxi.servlet;

import ua.rudniev.taxi.ComponentsContainer;
import ua.rudniev.taxi.dao.trip.TripOrderDao;
import ua.rudniev.taxi.dao.trip.TripOrderDaoImpl;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.trip.TripOrder;
import ua.rudniev.taxi.model.user.User;
import ua.rudniev.taxi.service.OrderingService;
import ua.rudniev.taxi.servlet.validation.ValidationUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/orderStatistics")
public class OrderStatisticsServlet extends HttpServlet {
    private final OrderingService orderingService = ComponentsContainer.getInstance().getOrderingService();
    private String filterBy, filterKey, sortType, sortBy;
    private int recordsPerPage = 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentPage = 1;
        if (Boolean.getBoolean(req.getParameter("sortedAndFiltered"))) { //TODO check with pirog
            filterBy = req.getParameter("filterBy");
            filterKey = req.getParameter("filterKey");
            sortType = req.getParameter("sortType");
            sortBy = req.getParameter("sortBy");
            recordsPerPage = Integer.parseInt(req.getParameter("sortBy"));
        }
        if (req.getParameter("currentPage") != null) currentPage = Integer.parseInt(req.getParameter("currentPage"));
        List<TripOrder> tripOrders = orderingService.findAllTripOrders(
                (currentPage - 1) * recordsPerPage,
                recordsPerPage,
                sortType,
                sortBy,
                filterBy,
                filterKey);
        int totalRecords = orderingService.getCountOfRecords(filterBy, filterKey);
        int pagesQuantity = (int) Math.ceil(totalRecords * 1.0 / recordsPerPage);
        req.setAttribute("tripOrders", tripOrders);
        req.setAttribute("pagesQuantity", pagesQuantity);
        req.setAttribute("recordsPerPage", recordsPerPage);
        req.setAttribute("currentPage", currentPage);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/orderStatistics.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sortType = req.getParameter("sortType");
        sortBy = req.getParameter("sortBy");
        filterBy = req.getParameter("filterBy");
        filterKey = req.getParameter("filterKey");
        recordsPerPage = Integer.parseInt(req.getParameter("quantity"));
        req.setAttribute("sortType", sortType);
        req.setAttribute("sortBy", sortBy);
        req.setAttribute("filterBy", filterBy);
        req.setAttribute("filterKey", filterKey);
        req.setAttribute("quantity", recordsPerPage);
        req.setAttribute("sortedAndFiltered", true); //TODO check with pirog AND RESET BUTTON
        resp.sendRedirect(req.getContextPath() + "/orderStatistics");
    }
}

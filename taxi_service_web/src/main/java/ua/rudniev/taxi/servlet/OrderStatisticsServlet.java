package ua.rudniev.taxi.servlet;

import ua.rudniev.taxi.ComponentsContainer;
import ua.rudniev.taxi.dao.trip.TripOrderDao;
import ua.rudniev.taxi.model.trip.TripOrder;
import ua.rudniev.taxi.service.OrderingService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class OrderStatisticsServlet extends HttpServlet {
    private final OrderingService orderingService = ComponentsContainer.getInstance().getOrderingService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentPage = 1;
        int recordsPerPage = 5;
        if (req.getParameter("currentPage") != null) currentPage = Integer.parseInt(req.getParameter("currentPage"));


        List<TripOrder> tripOrders = orderingService.findAllTripOrders((currentPage - 1) * recordsPerPage, recordsPerPage);


        int totalRecords = orderingService.getCountOfRecords();
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
    }
}

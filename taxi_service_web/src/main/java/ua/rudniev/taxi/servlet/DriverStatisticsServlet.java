package ua.rudniev.taxi.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.rudniev.taxi.ComponentsContainer;
import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.dao.common.filter.Value;
import ua.rudniev.taxi.dao.common.order.OrderBy;
import ua.rudniev.taxi.dao.trip.TripOrderField;
import ua.rudniev.taxi.model.user.User;
import ua.rudniev.taxi.service.OrderingService;
import ua.rudniev.taxi.servlet.utils.TripOrderServletUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ua.rudniev.taxi.web.SessionAttributes.CURRENT_USER;


/**
 * This class has fields and methods that responds to the http request from /driverStatistics url
 */
@WebServlet(
        name = "DriverStatisticsServlet",
        urlPatterns = "/driverStatistics"
)
public class DriverStatisticsServlet extends HttpServlet {
    private final TripOrderServletUtils tripOrderServletUtils = ComponentsContainer.getInstance().getTripOrderServletUtils();

    private final OrderingService orderingService = ComponentsContainer.getInstance().getOrderingService();

    private static final Logger LOGGER = LogManager.getLogger(DriverStatisticsServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentPage = tripOrderServletUtils.getCurrentPageOrDefault(req, "currentPage");
        int recordsPerPage = tripOrderServletUtils.getDefaultRecordsPerPage();

        OrderBy<TripOrderField> orderBy = new OrderBy<>(
                tripOrderServletUtils.getDefaultOrderByField(),
                tripOrderServletUtils.getDefaultOrderByType()
        );

        User user = (User) req.getSession().getAttribute(CURRENT_USER);
        Filter<TripOrderField> filter = new Filter<>(
                TripOrderField.DRIVER_LOGIN,
                new Value(user.getLogin())
        );
        tripOrderServletUtils.getTripOrdersAndFillRequestAttributes(
                currentPage,
                recordsPerPage,
                orderBy,
                List.of(filter),
                req
        );
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/driverStatistics.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        int carId = Integer.parseInt(req.getParameter("car_id"));
        orderingService.completeTripOrder(id, carId);
        LOGGER.info("Trip with id " + id + " has been completed");
        resp.sendRedirect(req.getContextPath() + "/driverStatistics");
    }
}

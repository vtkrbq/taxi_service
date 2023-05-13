package ua.rudniev.taxi.servlet;

import ua.rudniev.taxi.ComponentsContainer;
import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.dao.common.filter.Value;
import ua.rudniev.taxi.dao.common.order.OrderBy;
import ua.rudniev.taxi.dao.trip.TripOrderField;
import ua.rudniev.taxi.model.user.User;
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
 * This class has fields and methods that responds to the http request from /userStatistics url
 */
@WebServlet(
        name = "UserStatisticsServlet",
        urlPatterns = "/userStatistics"
)
public class UserStatisticsServlet extends HttpServlet {

    private final TripOrderServletUtils tripOrderServletUtils = ComponentsContainer
            .getInstance()
            .getTripOrderServletUtils();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentPage = tripOrderServletUtils.getCurrentPageOrDefault(req, "currentPage");
        int recordsPerPage = tripOrderServletUtils.getDefaultRecordsPerPage();
        User user = (User) req.getSession().getAttribute(CURRENT_USER);

        OrderBy<TripOrderField> orderBy = new OrderBy<>(
                tripOrderServletUtils.getDefaultOrderByField(),
                tripOrderServletUtils.getDefaultOrderByType()
        );

        Filter<TripOrderField> filter = new Filter<>(
                TripOrderField.CLIENT_LOGIN,
                new Value(user.getLogin())
        );

        tripOrderServletUtils.getTripOrdersAndFillRequestAttributes(
                currentPage,
                recordsPerPage,
                orderBy,
                List.of(filter),
                req
        );
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/userStatistics.jsp");
        dispatcher.forward(req, resp);
    }
}

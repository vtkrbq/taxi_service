package ua.rudniev.taxi.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.rudniev.taxi.ComponentsContainer;
import ua.rudniev.taxi.dao.car.CarField;
import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.dao.common.filter.FilterType;
import ua.rudniev.taxi.dao.common.filter.Value;
import ua.rudniev.taxi.model.NewTripInfo;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.car.Status;
import ua.rudniev.taxi.model.trip.AddressPoint;
import ua.rudniev.taxi.model.trip.TripOrder;
import ua.rudniev.taxi.model.user.User;
import ua.rudniev.taxi.service.OrderingService;
import ua.rudniev.taxi.web.SessionAttributes;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class has fields and methods that responds to the http request from /ordering url
 */
@WebServlet(
        name = "OrderingServlet",
        urlPatterns = "/ordering"
)
public class OrderingServlet extends HttpServlet {

    private final OrderingService orderingService = ComponentsContainer.getInstance().getOrderingService();
    private static final Logger LOGGER = LogManager.getLogger(OrderingServlet.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/ordering.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        double distance = Double.parseDouble(req.getParameter(FormFields.DISTANCE));
        double departureX = Double.parseDouble(req.getParameter(FormFields.DEPARTURE_X));
        double departureY = Double.parseDouble(req.getParameter(FormFields.DEPARTURE_Y));
        String departureAddress = req.getParameter(FormFields.DEPARTURE_ADDRESS);
        AddressPoint departure = new AddressPoint(departureX, departureY, departureAddress);
        double destinationX = Double.parseDouble(req.getParameter(FormFields.DESTINATION_X));
        double destinationY = Double.parseDouble(req.getParameter(FormFields.DESTINATION_Y));
        String destinationAddress = req.getParameter(FormFields.DESTINATION_ADDRESS);
        Category category = Category.valueOf(req.getParameter(FormFields.CATEGORY));
        int capacity = Integer.parseInt(req.getParameter(FormFields.CAPACITY));
        User user = (User) req.getSession().getAttribute(SessionAttributes.CURRENT_USER);
        TripOrder tripOrder = new TripOrder(
                new AddressPoint(departureX, departureY, departureAddress),
                new AddressPoint(destinationX, destinationY, destinationAddress),
                category,
                capacity,
                user,
                Instant.now()
        );
        List<Filter<CarField>> filters = new ArrayList<>();
        Filter<CarField> filterCategory = new Filter<>(
                CarField.CATEGORY,
                new Value(tripOrder.getCategory().toString())
        );
        filters.add(filterCategory);
        Filter<CarField> filterCapacity = new Filter<>(
                CarField.CAPACITY,
                new Value(tripOrder.getCapacity()),
                FilterType.MORE_OR_EQUALS
        );
        filters.add(filterCapacity);

        Filter<CarField> filterStatus = new Filter<>(
                CarField.STATUS,
                new Value(Status.AVAILABLE.toString())
        );
        filters.add(filterStatus);
        RequestDispatcher dispatcher;
        Optional<NewTripInfo> newTripInfoOptional = orderingService.findAndOrder(tripOrder, distance, departure, filters);
        req.getSession().setAttribute("tripOrder", tripOrder);
        if (newTripInfoOptional.isPresent()) {
            req.getSession().setAttribute("newTripInfo", newTripInfoOptional.get());
            LOGGER.info("Order " + tripOrder + " has been created and taken by driver: " + newTripInfoOptional.get().getCar().getDriver().getLogin());
            resp.sendRedirect("newTripInfo.do");
            //PRG pattern
        } else {
            List<String> errors = new ArrayList<>();
            errors.add("There is no available cars: \nYou can try different category or several cars");//TODO check \n
            dispatcher = req.getRequestDispatcher("/jsp/ordering.jsp");
            req.setAttribute("errors", errors);
            dispatcher.forward(req, resp);
        }
    }

    private static class FormFields {

        private static final String DISTANCE = "distance";

        private static final String DEPARTURE_X = "departureX";

        private static final String DEPARTURE_Y = "departureY";

        private static final String DEPARTURE_ADDRESS = "departureAddress";

        private static final String DESTINATION_X = "destinationX";

        private static final String DESTINATION_Y = "destinationY";

        private static final String DESTINATION_ADDRESS = "destinationAddress";

        private static final String CATEGORY = "category";

        private static final String CAPACITY = "capacity";
    }
}

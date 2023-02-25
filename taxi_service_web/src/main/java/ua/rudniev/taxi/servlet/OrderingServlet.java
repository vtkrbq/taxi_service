package ua.rudniev.taxi.servlet;

import ua.rudniev.taxi.ComponentsContainer;
import ua.rudniev.taxi.model.NewTripInfo;
import ua.rudniev.taxi.model.car.Category;
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

@WebServlet("/ordering")
public class OrderingServlet extends HttpServlet {

    private final OrderingService orderingService = ComponentsContainer.getInstance().getOrderingService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/ordering.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        double departureX = Double.parseDouble(req.getParameter(FormFields.DEPARTURE_X));
        double departureY = Double.parseDouble(req.getParameter(FormFields.DEPARTURE_Y));
        String departureAddress = req.getParameter(FormFields.DEPARTURE_ADDRESS);
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
        Optional<NewTripInfo> newTripInfoOptional = orderingService.findAndOrder(tripOrder);
        RequestDispatcher dispatcher;
        req.setAttribute("tripOrder", tripOrder);
        if (newTripInfoOptional.isPresent()) {
            dispatcher = req.getRequestDispatcher("/jsp/newTripInfo.jsp");
            req.setAttribute("newTripInfo", newTripInfoOptional.get());
        } else {
            List<String> errors = new ArrayList<>();
            errors.add("There is no available cars");
            dispatcher = req.getRequestDispatcher("/jsp/ordering.jsp");
            req.setAttribute("errors", errors);
        }
        dispatcher.forward(req, resp);
    }

    private static class FormFields {

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

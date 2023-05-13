package ua.rudniev.taxi.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.rudniev.taxi.ComponentsContainer;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.car.Status;
import ua.rudniev.taxi.model.user.User;
import ua.rudniev.taxi.service.CarService;
import ua.rudniev.taxi.servlet.validation.ValidationUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ua.rudniev.taxi.web.SessionAttributes.CURRENT_USER;

/**
 * This class has fields and methods that responds to the http request from /carRegistration url
 */
@WebServlet(
        name = "CarRegistrationServlet",
        urlPatterns = "/carRegistration"
)
public class CarRegistrationServlet extends HttpServlet {
    private final CarService carService = ComponentsContainer.getInstance().getCarService();
    private static final Logger LOGGER = LogManager.getLogger(CarRegistrationServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/carRegistration.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> errors = new ArrayList<>();
        String carName = req.getParameter(FormFields.CAR_NAME);
        String carCategory = req.getParameter(FormFields.CAR_CATEGORY).toUpperCase();
        String carCapacity = req.getParameter(FormFields.CAR_CAPACITY);
        String licensePlate = req.getParameter(FormFields.LICENSE_PLATE);
        ValidationUtils.validateCarName(errors, carName);
        ValidationUtils.validateCarCategory(errors, carCategory);
        ValidationUtils.validateCarCapacity(errors, carCapacity);
        ValidationUtils.validateLicensePlate(errors, licensePlate);
        User user = (User) req.getSession().getAttribute(CURRENT_USER);
        Car car = new Car(
                user,
                carName,
                Category.valueOf(carCategory),
                Status.AVAILABLE,
                Integer.parseInt(carCapacity),
                licensePlate);
        if (errors.isEmpty()) {
            try {
                carService.createCar(car);
                LOGGER.info("New car " + car + " has been registered");
            } catch (Exception e) {
                LOGGER.error("An error has occurred while creating a car", e);
                errors.add("Technical error has occurred");
            }
        }
        if (errors.isEmpty()) {
            req.getSession().setAttribute(CURRENT_USER, user); //TODO: Пирог: нам не нужно это каждый раз делать. В сессию мы пользователя кладем единожды - после логина
            resp.sendRedirect(req.getContextPath() + "/ordering");
        } else {
            req.getSession().setAttribute("user", user); //????
            RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/carRegistration.jsp");
            req.setAttribute("errors", errors);
            dispatcher.forward(req, resp);
        }
    }

    private static class FormFields {
        private static final String CAR_NAME = "carName";
        private static final String CAR_CATEGORY = "carCategory";
        private static final String CAR_CAPACITY = "carCapacity";
        private static final String LICENSE_PLATE = "licensePlate";
    }
}

package ua.rudniev.taxi.servlet;

import ua.rudniev.taxi.ComponentsContainer;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.service.CarService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class has fields and methods that responds to the http request from /car url
 */
@WebServlet(
        name = "CarServlet",
        urlPatterns = "/car"
)
public class CarServlet extends HttpServlet {
    private final CarService carService = ComponentsContainer.getInstance().getCarService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Car car = carService.findCarById(id).orElseThrow();
        req.setAttribute("car", car);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/car.jsp");
        dispatcher.forward(req, resp);
    }
}

package ua.rudniev.taxi.servlet;

import ua.rudniev.taxi.ComponentsContainer;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.user.User;
import ua.rudniev.taxi.service.CarService;
import ua.rudniev.taxi.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/carView")
public class CarViewServlet extends HttpServlet {
    private final CarService carService = ComponentsContainer.getInstance().getCarService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Car carView = carService.findCarById(id).orElse(new Car());
        req.setAttribute("carView", carView);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/carView.jsp");
        dispatcher.forward(req, resp);
    }
}

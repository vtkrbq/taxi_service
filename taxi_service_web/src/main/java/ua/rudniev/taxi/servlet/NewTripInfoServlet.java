package ua.rudniev.taxi.servlet;

import ua.rudniev.taxi.model.NewTripInfo;
import ua.rudniev.taxi.model.trip.TripOrder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/newTripInfo.do")
public class NewTripInfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TripOrder tripOrder = (TripOrder) req.getSession().getAttribute("tripOrder");
        NewTripInfo newTripInfoOptional = (NewTripInfo) req.getSession().getAttribute("newTripInfo");
        req.setAttribute("tripOrder", tripOrder);
        req.setAttribute("newTripInfo", newTripInfoOptional);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/newTripInfo.jsp");
        dispatcher.forward(req, resp);
    }
}

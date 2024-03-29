package ua.rudniev.taxi.servlet;

import ua.rudniev.taxi.ComponentsContainer;
import ua.rudniev.taxi.model.user.User;
import ua.rudniev.taxi.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class has fields and methods that responds to the http request from /profileView url
 */
@WebServlet(
        name = "ProfileViewServlet",
        urlPatterns = "/profileView"
)
public class ProfileViewServlet extends HttpServlet {
    private final UserService userService = ComponentsContainer.getInstance().getUserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        User userView = userService.findUser(login).orElseThrow();
        req.setAttribute("userView", userView);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/profileView.jsp");
        dispatcher.forward(req, resp);
    }
}

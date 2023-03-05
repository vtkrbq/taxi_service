package ua.rudniev.taxi.servlet;

import ua.rudniev.taxi.ComponentsContainer;
import ua.rudniev.taxi.model.user.User;
import ua.rudniev.taxi.service.UserService;
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
import java.util.Optional;

import static ua.rudniev.taxi.web.SessionAttributes.CURRENT_USER;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserService userService = ComponentsContainer.getInstance().getUserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> errors = new ArrayList<>();
        String login = req.getParameter(FormFields.LOGIN);
        String password = req.getParameter(FormFields.PASSWORD);
        ValidationUtils.validateMandatory(errors, login, "Login");
        ValidationUtils.validateMandatory(errors, password, "Password");
        Optional<User> userOptional = Optional.empty();
        if (errors.isEmpty()) {
            userOptional = userService.findUserByLoginAndPassword(login, password);
            if (userOptional.isEmpty()) {
                errors.add("Login or password is incorrect");
            }
        }
        if (errors.isEmpty()) {
            req.getSession().setAttribute(CURRENT_USER, userOptional.get());
            resp.sendRedirect(req.getContextPath() + "/ordering");
        } else {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/index.jsp");
            req.setAttribute("errors", errors);
            dispatcher.forward(req, resp);
        }
    }

    private static class FormFields {
        private static final String LOGIN = "login";
        private static final String PASSWORD = "password";
    }
}

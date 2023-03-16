package ua.rudniev.taxi.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.rudniev.taxi.ComponentsContainer;
import ua.rudniev.taxi.exception.PasswordDoesNotMatchException;
import ua.rudniev.taxi.exception.UserAlreadyExistsException;
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

import static ua.rudniev.taxi.web.SessionAttributes.CURRENT_USER;

@WebServlet("/changePassword")
public class ChangePasswordServlet extends HttpServlet {
    private final UserService userService = ComponentsContainer.getInstance().getUserService();
    private static final Logger LOGGER = LogManager.getLogger(ChangePasswordServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/changePassword.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");//TODO: filter add to all servlets
        List<String> errors = new ArrayList<>();
        String oldPassword = req.getParameter(FormFields.OLD_PASSWORD);
        String newPassword = req.getParameter(FormFields.OLD_CONFIRM_PASSWORD);
        String newConfirmPassword = req.getParameter(FormFields.NEW_PASSWORD);
        ValidationUtils.validateMandatory(errors, oldPassword, "Old password");
        ValidationUtils.validatePassword(errors, newPassword, newConfirmPassword);
        User user = (User) req.getSession().getAttribute(CURRENT_USER);
        if (errors.isEmpty()) {
            try {
                userService.updatePassword(user, oldPassword, newPassword);
                LOGGER.info("Password for user " + user.getLogin() + " has been changed");
            } catch (PasswordDoesNotMatchException e) {
                errors.add("Old password is incorrect");
            } catch (Exception e) {
                LOGGER.error("An error has occurred with changing the password", e);
                errors.add("Technical error has occurred");
            }
        }
        if (errors.isEmpty()) {
            req.getSession().setAttribute(CURRENT_USER, user);
            resp.sendRedirect(req.getContextPath() + "/profile");
        } else {
            req.getSession().setAttribute("user", user);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/changePassword.jsp");
            req.setAttribute("errors", errors);
            dispatcher.forward(req, resp);
        }
    }

    private static class FormFields {
        private static final String OLD_PASSWORD = "oldPassword";
        private static final String NEW_PASSWORD = "newPassword";
        private static final String OLD_CONFIRM_PASSWORD = "newConfirmPassword";

    }

}

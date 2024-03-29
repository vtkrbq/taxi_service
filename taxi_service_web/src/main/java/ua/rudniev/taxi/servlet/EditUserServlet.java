package ua.rudniev.taxi.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.rudniev.taxi.ComponentsContainer;
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

/**
 * This class has fields and methods that responds to the http request from /editUser url
 */
@WebServlet(
        name = "EditUserServlet",
        urlPatterns = "/editUser"
)
public class EditUserServlet extends HttpServlet {
    private final UserService userService = ComponentsContainer.getInstance().getUserService();
    private static final Logger LOGGER = LogManager.getLogger(EditUserServlet.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/editUser.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User oldUser = userService.findUser(((User) req.getSession().getAttribute(CURRENT_USER)).getLogin()).orElseThrow();
        List<String> errors = new ArrayList<>();
        String newLogin = req.getParameter(FormFields.LOGIN);
        String name = req.getParameter(FormFields.FIRSTNAME);
        String lastname = req.getParameter(FormFields.LASTNAME);
        String phone = req.getParameter(FormFields.PHONE);
        String email = req.getParameter(FormFields.EMAIL);
        ValidationUtils.validateLogin(errors, newLogin);
        ValidationUtils.validateName(errors, name);
        ValidationUtils.validateLastName(errors, lastname);
        ValidationUtils.validatePhone(errors, phone);
        ValidationUtils.validateEmail(errors, email);
        User user = new User();
        user.setLogin(newLogin);
        user.setFirstname(name);
        user.setLastname(lastname);
        user.setPhone(phone);
        user.setEmail(email);
        oldUser.getRoles().forEach(user::addRole);
        user.setDiscount(oldUser.getDiscount());
        if (errors.isEmpty()) {
            try {
                userService.updateUser(user, oldUser.getLogin());
                LOGGER.info("User " + user.getLogin() + " has been updated");
            } catch (UserAlreadyExistsException e) {
                errors.add("User with login " + newLogin + " is already exist");
            } catch (Exception e) {
                LOGGER.error("An error has occurred while updating a user", e);
                errors.add("Technical error has occurred");
            }
        }
        if (errors.isEmpty()) {
            req.getSession().setAttribute(CURRENT_USER, user);
            resp.sendRedirect(req.getContextPath() + "/profile");
        } else {
            req.getSession().setAttribute("user", user);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/editUser.jsp");
            req.setAttribute("errors", errors);
            dispatcher.forward(req, resp);
        }
    }

    private static class FormFields {
        private static final String LOGIN = "login";
        private static final String FIRSTNAME = "firstname";
        private static final String LASTNAME = "lastname";
        private static final String PHONE = "phone";
        private static final String EMAIL = "email";
    }
}

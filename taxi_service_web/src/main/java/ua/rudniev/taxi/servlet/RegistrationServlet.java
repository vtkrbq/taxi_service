package ua.rudniev.taxi.servlet;

import ua.rudniev.taxi.ComponentsContainer;
import ua.rudniev.taxi.exception.UserAlreadyExistsException;
import ua.rudniev.taxi.model.user.Role;
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

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    private final UserService userService = ComponentsContainer.getInstance().getUserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/registration.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        List<String> errors = new ArrayList<>();
        String login = req.getParameter(FormFields.LOGIN);
        String password = req.getParameter(FormFields.PASSWORD);
        String confirmedPassword = req.getParameter(FormFields.CONFIRM_PASSWORD);
        String name = req.getParameter(FormFields.FIRSTNAME);
        String lastname = req.getParameter(FormFields.LASTNAME);
        String phone = req.getParameter(FormFields.PHONE);
        String email = req.getParameter(FormFields.EMAIL);
        String[] registerAsDriver = req.getParameterValues(FormFields.REGISTER_AS_DRIVER);
        ValidationUtils.validateLogin(errors, login);
        ValidationUtils.validatePassword(errors, password, confirmedPassword);
        ValidationUtils.validateName(errors, name);
        ValidationUtils.validateLastName(errors, lastname);
        ValidationUtils.validatePhone(errors, phone);
        ValidationUtils.validateEmail(errors, email);
        User user = new User();
        user.setLogin(login);
        user.setFirstname(name);
        user.setLastname(lastname);
        user.setPhone(phone);
        user.setEmail(email);
        if (errors.isEmpty()) {
            try {
                userService.createUser(user, password);
            } catch (UserAlreadyExistsException e) {
                errors.add("User with login " + login + " is already exist");
            } catch (Exception e) {
                log("An error has occurred while creating a user", e);
                errors.add("Technical error has occurred");
            }
        }
        if (errors.isEmpty()) {
            if (registerAsDriver != null) {
                user.addRole(Role.DRIVER);
                userService.updateUser(user, login);
                req.getSession().setAttribute(CURRENT_USER, user);
                resp.sendRedirect(req.getContextPath() + "/carRegistration");
            } else {
                req.getSession().setAttribute(CURRENT_USER, user);
                resp.sendRedirect(req.getContextPath() + "/ordering");
            }
        } else {
            req.getSession().setAttribute("user", user);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/registration.jsp");
            req.setAttribute("errors", errors);
            dispatcher.forward(req, resp);
        }
    }

    private static class FormFields {
        private static final String LOGIN = "login";
        private static final String PASSWORD = "password";
        private static final String CONFIRM_PASSWORD = "confirmPassword";
        private static final String FIRSTNAME = "firstname";
        private static final String LASTNAME = "lastname";
        private static final String PHONE = "phone";
        private static final String EMAIL = "email";
        private static final String REGISTER_AS_DRIVER = "registerAsDriver";
    }
}

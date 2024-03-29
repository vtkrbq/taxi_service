package ua.rudniev.taxi.servlet;

import ua.rudniev.taxi.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class has fields and methods that responds to the http request from /language url
 */
@WebServlet(
        name = "LanguageServlet",
        urlPatterns = "/language"
)
public class LanguageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String lang = req.getParameter("lang");
        if (!StringUtils.isEmptyOrNull(lang)) {
            req.getSession().setAttribute("lang", lang);
        }
        resp.sendRedirect(req.getContextPath());
    }
}

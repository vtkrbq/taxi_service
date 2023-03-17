package ua.rudniev.taxi.filter;

import ua.rudniev.taxi.web.SessionAttributes;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(
        filterName = "AuthenticationFilter",
        servletNames = {"OrderingServlet", "CarRegistrationServlet", "CarServlet",
                "ChangePasswordServlet", "DriverStatisticsServlet", "EditUserServlet",
                "LogoutServlet", "NewTripInfoServlet", "OrderStatisticsServlet",
                "ProfileServlet", "ProfileViewServlet", "UserStatisticsServlet"}
)
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (httpRequest.getSession().getAttribute(SessionAttributes.CURRENT_USER) != null) {
            chain.doFilter(request, response);
        } else {
            httpResponse.setStatus(401);
            RequestDispatcher dispatcher = request.getRequestDispatcher(
                    "/jsp/unathorized.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}

package ua.rudniev.taxi.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This filter is used for restraining manual access from jsp pages in "jsp" folder
 */
@WebFilter(
        urlPatterns = "/jsp/*",
        filterName = "JspFilter"
)
public class JspFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.setStatus(403);
        RequestDispatcher dispatcher = servletRequest.getRequestDispatcher(
                "/jsp/forbidden.jsp");
        dispatcher.forward(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}

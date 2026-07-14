package filter;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.bean.User;

import java.io.IOException;

@WebFilter("/dealer/*")
public class DealerAuthorizationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession(false);

        User user = null;

        if (session != null){
            user = (User) session.getAttribute("user");
        }

        if (user == null){
            response.sendRedirect( request.getContextPath() + "/login.jsp");
            return;
        }

        String role = user.getRole();


        boolean authorization = "DEALER".equals(role)
                || "ADMIN".equals(role);

        if (authorization){
            filterChain.doFilter(request, response);
        }
        else{
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Non sei autorizzato ad accedere all'area concessionario"
            );
        }


    }
}

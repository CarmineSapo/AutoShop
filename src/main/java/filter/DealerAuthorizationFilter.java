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

    /*
    * nome dell'attributo request nel quale il filtro
    * inserisce il dealer autenticato
    */
    public static final String DEALER_ATTRIBUTE = "authenticatedDealer";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession(false);

        //User user = null;

        /*
        * l'utente non possiede una sessione
        */

        if (session == null){
            response.sendRedirect( request.getContextPath() + "/login.jsp");
            return;
        }

        Object userAttribute = session.getAttribute("user");

        /*
        * la sessione esiste, ma non contiene
        * un utente autenticato valido
        */

        if (!(userAttribute instanceof User)){
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) userAttribute;

        /*
        * l'utente è autenticato ma non è un dealer
        */
        if(!"DEALER".equals(user.getRole())){
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso consentito soltanto ai dealer");
            return;
        }

        /*
        * il filtro mette il dealer nella request.
        * le Servler protette potranno recuperarlo
        * senza ripetere i controlli
        */

        request.setAttribute(DEALER_ATTRIBUTE, user);

        filterChain.doFilter(request, response);



    }
}

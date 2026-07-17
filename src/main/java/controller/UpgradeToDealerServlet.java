package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.bean.Dealer;
import model.bean.User;
import model.dao.DealerDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/upgrade-to-dealer")
public class UpgradeToDealerServlet extends HttpServlet {

    private final DealerDAO dealerDAO =
            new DealerDAO();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session =
                request.getSession(false);

        /*
         * L'AuthenticationFilter dovrebbe già proteggere
         * questo URL, ma recuperiamo comunque l'utente
         * necessario per l'operazione.
         */
        if (session == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );
            return;
        }

        User user =
                (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );
            return;
        }

        /*
         * Solo un CUSTOMER può effettuare l'upgrade.
         */
        if (!"CUSTOMER".equals(user.getRole())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "L'utente non può effettuare l'upgrade"
            );
            return;
        }

        String companyName =
                getParameter(request, "companyName");

        String vatNumber =
                getParameter(request, "vatNumber");

        String description =
                getParameter(request, "description");

        String phone =
                getParameter(request, "phone");

        String address =
                getParameter(request, "address");

        List<String> errors =
                new ArrayList<>();

        if (companyName.isEmpty()) {
            errors.add(
                    "Il nome della concessionaria è obbligatorio."
            );
        }

        if (!vatNumber.matches("\\d{11}")) {
            errors.add(
                    "La partita IVA deve contenere 11 cifre."
            );
        }

        if (phone.isEmpty()) {
            errors.add(
                    "Il numero di telefono è obbligatorio."
            );
        }

        if (address.isEmpty()) {
            errors.add(
                    "L'indirizzo è obbligatorio."
            );
        }

        if (!errors.isEmpty()) {
            request.setAttribute(
                    "upgradeErrors",
                    errors
            );

            request.getRequestDispatcher(
                    "/WEB-INF/profile.jsp"
            ).forward(request, response);

            return;
        }

        Dealer dealer =
                new Dealer();

        dealer.setUserId(user.getId());
        dealer.setCompanyName(companyName);
        dealer.setVatNumber(vatNumber);
        dealer.setDescription(description);
        dealer.setPhone(phone);
        dealer.setAddress(address);

        try {
            dealerDAO.upgradeToDealer(
                    user,
                    dealer
            );

            /*
             * Aggiorniamo anche l'oggetto User presente
             * nella sessione.
             */
            user.setRole("DEALER");

            session.setAttribute(
                    "user",
                    user
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/dealer/dashboard"
            );

        } catch (SQLException e) {
            throw new ServletException(
                    "Errore durante l'upgrade a dealer",
                    e
            );
        }
    }

    private String getParameter(
            HttpServletRequest request,
            String name
    ) {
        String value =
                request.getParameter(name);

        if (value == null) {
            return "";
        }

        return value.trim();
    }
}
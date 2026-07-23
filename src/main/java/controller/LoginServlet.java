package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.bean.User;
import model.dao.UserDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email =
                request.getParameter("email");

        String password =
                request.getParameter("password");

        if (email != null) {
            email = email.trim();
        }

        /*
         * Controllo dei campi obbligatori.
         */
        if (email == null || email.isBlank()
                || password == null || password.isBlank()) {

            showError(
                    request,
                    response,
                    "Email e password sono obbligatorie."
            );

            return;
        }

        if (email.length() > 100) {

            showError(
                    request,
                    response,
                    "L'email non può superare 100 caratteri."
            );

            return;
        }

        if (!isEmailValid(email)) {

            showError(
                    request,
                    response,
                    "Il formato dell'email non è valido."
            );

            return;
        }

        if (password.length() > 72) {

            showError(
                    request,
                    response,
                    "La password non può superare 72 caratteri."
            );

            return;
        }

        try {

            User user =
                    userDAO.findByEmail(email);

            /*
             * Il messaggio rimane generico:
             * non comunichiamo quale dei due
             * dati è sbagliato.
             */
            if (user == null
                    || !BCrypt.checkpw(
                    password,
                    user.getPasswordHash()
            )) {

                showError(
                        request,
                        response,
                        "Email o password non corretti."
                );

                return;
            }

            HttpSession session =
                    request.getSession();

            session.setAttribute(
                    "user",
                    user
            );

            /*
             * Un dealer non deve conservare il carrello
             * creato prima dell'autenticazione.
             */
            if ("DEALER".equals(user.getRole())) {
                session.removeAttribute("cart");
            }

            response.sendRedirect(
                    request.getContextPath()
                            + "/catalog"
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Errore durante il login",
                    e
            );
        }
    }

    private boolean isEmailValid(String email) {

        int atPosition =
                email.indexOf('@');

        int dotPosition =
                email.lastIndexOf('.');

        return atPosition > 0
                && dotPosition > atPosition + 1
                && dotPosition < email.length() - 1;
    }

    private void showError(
            HttpServletRequest request,
            HttpServletResponse response,
            String message
    ) throws ServletException, IOException {

        request.setAttribute(
                "error",
                message
        );

        request.getRequestDispatcher(
                "/login.jsp"
        ).forward(request, response);
    }
}

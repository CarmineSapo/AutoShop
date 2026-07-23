package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.bean.User;
import model.dao.UserDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username =
                request.getParameter("username");

        String email =
                request.getParameter("email");

        String password =
                request.getParameter("password");

        /*
         * Username ed email vengono ripuliti
         * dagli spazi iniziali e finali.
         */
        if (username != null) {
            username = username.trim();
        }

        if (email != null) {
            email = email.trim();
        }

        /*
         * Controllo dei campi obbligatori.
         */
        if (username == null || username.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()) {

            showError(
                    request,
                    response,
                    "Tutti i campi sono obbligatori."
            );

            return;
        }

        /*
         * Gli stessi limiti saranno presenti
         * anche nel form HTML.
         */
        if (username.length() < 3
                || username.length() > 30) {

            showError(
                    request,
                    response,
                    "Lo username deve contenere tra 3 e 30 caratteri."
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

        /*
         * Controllo semplice dell'email:
         * deve contenere una @ e un punto
         * dopo la @.
         */
        if (!isEmailValid(email)) {

            showError(
                    request,
                    response,
                    "Il formato dell'email non è valido."
            );

            return;
        }

        if (password.length() < 8
                || password.length() > 72) {

            showError(
                    request,
                    response,
                    "La password deve contenere tra 8 e 72 caratteri."
            );

            return;
        }

        try {

            User existingEmail =
                    userDAO.findByEmail(email);

            if (existingEmail != null) {

                showError(
                        request,
                        response,
                        "Email già registrata."
                );

                return;
            }

            User existingUsername =
                    userDAO.findByUsername(username);

            if (existingUsername != null) {

                showError(
                        request,
                        response,
                        "Username già registrato."
                );

                return;
            }

            User user = new User();

            user.setUsername(username);
            user.setEmail(email);

            String hashedPassword =
                    BCrypt.hashpw(
                            password,
                            BCrypt.gensalt()
                    );

            user.setPasswordHash(hashedPassword);
            user.setRole("CUSTOMER");

            userDAO.save(user);

            /*
             * Dopo la registrazione andiamo
             * alla pagina di login.
             */
            response.sendRedirect(
                    request.getContextPath()
                            + "/login.jsp"
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Errore durante la registrazione",
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

    /*
     * Mostra nuovamente la JSP mantenendo
     * la stessa request e il messaggio di errore.
     */
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
                "/register.jsp"
        ).forward(request, response);
    }
}

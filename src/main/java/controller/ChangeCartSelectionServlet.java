package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.cart.Cart;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

@WebServlet("/cart/change-selection")
public class ChangeCartSelectionServlet
        extends HttpServlet {

    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        response.setContentType("application/json");

        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        /*
         * Il carrello può essere utilizzato anche senza login,
         * ma deve esistere una sessione contenente un Cart.
         */
        if (session == null) {
            sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Carrello non disponibile");
            return;
        }

        Object cartAttribute = session.getAttribute("cart");

        if (!(cartAttribute instanceof Cart)) {
            sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Carrello non disponibile");
            return;
        }

        Cart cart = (Cart) cartAttribute;

        Integer vehicleId = parsePositiveInteger(request.getParameter("vehicleId"));

        if (vehicleId == null) {
            sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "ID del veicolo non valido");
            return;
        }

        String selectedParameter = request.getParameter("selected");

        /*
         * Non usiamo direttamente Boolean.parseBoolean(),
         * perché qualsiasi valore diverso da "true"
         * verrebbe interpretato come false.
         *
         * Vogliamo invece rifiutare valori inventati.
         */
        if (!"true".equals(selectedParameter)
                && !"false".equals(selectedParameter)) {

            sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Valore di selezione non valido");
            return;
        }

        boolean selected = Boolean.parseBoolean(selectedParameter);

        /*
         * Controllo lato server:
         * il vehicleId deve appartenere realmente
         * al carrello presente nella sessione.
         */
        boolean updated = cart.setItemSelected(vehicleId, selected);

        if (!updated) {
            sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Veicolo non presente nel carrello");
            return;
        }

        /*
         * La risposta è JSON.
         * Locale.US garantisce che il numero decimale
         * venga scritto con il punto, come richiesto da JSON.
         */
        PrintWriter writer = response.getWriter();

        writer.printf(
                Locale.US,
                """
                {
                    "success": true,
                    "selectedCount": %d,
                    "selectedTotal": %.2f
                }
                """,
                cart.getSelectedCount(),
                cart.getSelectedTotal()
        );
    }

    private Integer parsePositiveInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            int number = Integer.parseInt(value);

            if (number <= 0) {
                return null;
            }

            return number;

        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void sendJsonError(HttpServletResponse response, int status, String message) throws IOException {

        response.setStatus(status);

        response.getWriter().printf(
                """
                {
                    "success": false,
                    "message": "%s"
                }
                """,
                message
        );
    }
}
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.cart.Cart;

import java.io.IOException;

@WebServlet("/cart/change-selection")
public class ChangeCartSelectionServlet
        extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        response.setContentType(
                "application/json"
        );

        response.setCharacterEncoding(
                "UTF-8"
        );

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST
            );
            return;
        }

        Cart cart =
                (Cart) session.getAttribute("cart");

        if (cart == null) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST
            );
            return;
        }

        String vehicleIdParameter =
                request.getParameter("vehicleId");

        String selectedParameter =
                request.getParameter("selected");

        int vehicleId;

        try {
            vehicleId =
                    Integer.parseInt(
                            vehicleIdParameter
                    );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            return;
        }

        boolean selected =
                Boolean.parseBoolean(
                        selectedParameter
                );

        boolean updated =
                cart.setItemSelected(
                        vehicleId,
                        selected
                );

        if (!updated) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND
            );
            return;
        }

        String json =
                "{"
                        + "\"selectedCount\":"
                        + cart.getSelectedCount()
                        + ","
                        + "\"selectedTotal\":"
                        + cart.getSelectedTotal()
                        + "}";

        response.getWriter().write(json);
    }
}
package controller;

import filter.DealerAuthorizationFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.bean.User;
import model.bean.Vehicle;
import model.dao.VehicleDAO;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/dealer/change-vehicle-visibility")

public class ChangeVehicleVisibilityServlet extends HttpServlet {

    private final VehicleDAO vehicleDAO = new VehicleDAO();


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        User dealer =
                (User) request.getAttribute(DealerAuthorizationFilter.DEALER_ATTRIBUTE);

        Integer vehicleId =
                parsePositiveInteger(
                        request.getParameter("id")
                );

        if (vehicleId == null) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "ID del veicolo non valido"
            );
            return;
        }

        String action =
                request.getParameter("action");

        boolean newActiveValue;

        if ("deactivate".equals(action)) {
            newActiveValue = false;

        } else if ("activate".equals(action)) {
            newActiveValue = true;

        } else {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Azione non valida"
            );
            return;
        }

        try {
            /*
             * il veicolo deve esistere e appartenere
             * al dealer autenticato.
             */
            Vehicle vehicle =
                    vehicleDAO.getVehicleByIdAndDealer(
                            vehicleId,
                            dealer.getId()
                    );

            if (vehicle == null) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Veicolo non trovato"
                );
                return;
            }

            boolean updated =
                    vehicleDAO.updateVehicleVisibility(
                            vehicleId,
                            dealer.getId(),
                            newActiveValue
                    );

            if (!updated) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Veicolo non trovato o non modificabile"
                );
                return;
            }

            response.sendRedirect(
                    request.getContextPath()
                            + "/dealer/vehicles"
            );

        } catch (SQLException e) {
            throw new ServletException(
                    "Errore durante la modifica della visibilità del veicolo",
                    e
            );
        }


    }


    private Integer parsePositiveInteger(
            String value
    ) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            int number =
                    Integer.parseInt(value);

            if (number <= 0) {
                return null;
            }

            return number;

        } catch (NumberFormatException e) {
            return null;
        }
    }

}

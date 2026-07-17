package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.bean.Dealer;
import model.bean.Vehicle;
import model.dao.DealerDAO;
import model.dao.VehicleDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/dealer-profile")
public class DealerProfileServlet
        extends HttpServlet {

    private final DealerDAO dealerDAO =
            new DealerDAO();

    private final VehicleDAO vehicleDAO =
            new VehicleDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String idParameter =
                request.getParameter("id");

        int dealerId;

        /*
         * Validazione lato server dell'ID ricevuto.
         */
        try {
            dealerId =
                    Integer.parseInt(idParameter);

            if (dealerId <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "ID del dealer non valido"
            );

            return;
        }

        try {
            Dealer dealer =
                    dealerDAO.getDealerByUserId(
                            dealerId
                    );

            if (dealer == null) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Dealer non trovato"
                );

                return;
            }

            List<Vehicle> vehicles =
                    vehicleDAO.getPublicVehiclesByDealer(
                            dealerId
                    );

            request.setAttribute(
                    "dealer",
                    dealer
            );

            request.setAttribute(
                    "vehicles",
                    vehicles
            );

            request.getRequestDispatcher(
                    "/WEB-INF/dealer-profile.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Errore durante il caricamento del profilo dealer",
                    e
            );
        }
    }
}
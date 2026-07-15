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
import java.util.List;

@WebServlet("/dealer/vehicles")
public class DealerVehiclesServlet extends HttpServlet{

    private final VehicleDAO vehicleDAO = new VehicleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        User dealer = (User) request.getAttribute(DealerAuthorizationFilter.DEALER_ATTRIBUTE);


        try {
            List<Vehicle> vehicles = vehicleDAO.getVehiclesByDealer(dealer.getId());

            request.setAttribute("vehicles", vehicles);

            request.getRequestDispatcher("/WEB-INF/dealer/my-vehicles.jsp")
                    .forward(request, response);


        } catch (SQLException e) {
            throw new ServletException("Errore durante il caricamento dei veicoli del dealer",
                    e);
        }

    }
}

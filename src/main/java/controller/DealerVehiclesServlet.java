package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

        HttpSession session = request.getSession(false);

        //Controllo autenticazione
        if (session == null){
            response.sendRedirect(request.getContextPath() +"/login.jsp");
            return;
        }




        User user =
                (User) session.getAttribute("user");

        if (user == null){
            response.sendRedirect(request.getContextPath() +"/login.jsp");
            return;
        }
        //----------------------------



        //controllo autorizzazione
        if (!"DEALER".equals(user.getRole())){
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Accesso consentito soltanto ai dealer");
            return;
        }
        //------------------------ Errore 403 forbidden



            try {
                List<Vehicle> vehicles = vehicleDAO.getVehiclesByDealer(user.getId());

                request.setAttribute("vehicles", vehicles);

                request.getRequestDispatcher("/WEB-INF/dealer/my-vehicles.jsp")
                        .forward(request, response);


            } catch (SQLException e) {
                throw new ServletException("Errore durante il caricamento dei veicoli del dealer",
                        e);
            }

    }
}

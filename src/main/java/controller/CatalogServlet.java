package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.bean.Vehicle;
import model.dao.VehicleDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


@WebServlet("/catalog")  //Significa che questa Servlet risponde all’URL:

//                          http://localhost:8080/AutoShop/catalog

public class CatalogServlet  extends HttpServlet {

    private final VehicleDAO vehicleDao = new VehicleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Vehicle> vehicles = vehicleDao.getAllVehicles();

            request.setAttribute(
                    "vehicles",
                    vehicles);  //Qui salviamo la lista dentro la richiesta HTTP. La JSP potrà usare il nome: vehicles

            request.getRequestDispatcher("/catalog.jsp")
                    .forward(request, response);  //Qui passiamo il controllo alla pagina JSP. Servlet → catalog.jsp La servlet non stampa HTML direttamente.


        } catch (SQLException e) {
            throw new ServletException("Errore durante il caricamento del catalogo: ", e );
        }
    }
}

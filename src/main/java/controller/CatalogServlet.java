package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.bean.Vehicle;
import model.dao.VehicleDAO;
import model.dao.VehicleImageDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


@WebServlet("/catalog")
public class CatalogServlet extends HttpServlet{

    private final VehicleDAO vehicleDAO =
            new VehicleDAO();

    private final VehicleImageDAO vehicleImageDAO =
            new VehicleImageDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{

        /*
         * Quando l'utente apre la pagina Filtri
         * mostriamo soltanto il form senza interrogare il database
         */
        if ("true".equals(request.getParameter("showFilters"))) {

            request.getRequestDispatcher(
                    "/catalog.jsp"
            ).forward(request, response);

            return;
        }

        String brand = getParameter(request, "brand");

        String maxPriceParameter = getParameter(request, "maxPrice");

        String fuelType = getParameter(request, "fuelType");

        String transmission = getParameter(request, "transmission");

        double maxPrice = 0; // Il valore 0 di maxPrice significa che il filtro non è satto applicato

        if (brand.length() > 50) {
            request.setAttribute(
                    "filterError",
                    "La marca non può superare 50 caratteri."
            );

            brand = "";
        }

        if(!maxPriceParameter.isEmpty()) {
            try {
                maxPrice = Double.parseDouble(maxPriceParameter);

                if (maxPrice <= 0) {
                    request.setAttribute("filterError",
                            "Il prezzo massimo deve essere maggiore di zero."
                    );

                    maxPrice = 0;
                }

            } catch (NumberFormatException e){
                request.setAttribute("filterError",
                        "Il prezzo massimo non è valido"
                );

                maxPrice = 0;
            }
        }

        // controllo lato server

        if (!isValidFuelType(fuelType)){
            request.setAttribute("filterError",
                    "Il carburante non è valido"
            );
            fuelType = "";
        }

        if (!isValidTransmission(transmission)) {
            request.setAttribute("filterError",
                    "Tipo di cambio non valido"
            );

            transmission = "";
        }


        try {
            List<Vehicle> vehicles =
                    vehicleDAO.getFilteredVehicles(
                            brand,
                            maxPrice,
                            fuelType,
                            transmission
                    );

            for (Vehicle vehicle : vehicles) {

                vehicle.setImagePaths(
                        vehicleImageDAO.getImagesByVehicleId(
                                vehicle.getId()
                        )
                );
            }

            request.setAttribute("vehicles", vehicles);

            boolean filtersApplied =
                    !brand.isEmpty()
                            || maxPrice > 0
                            || !fuelType.isEmpty()
                            || !transmission.isEmpty();

            request.setAttribute(
                    "filtersApplied",
                    filtersApplied
            );

            request.getRequestDispatcher("/catalog.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il caricamento del catalogo",
                    e);
        }
    }



/// ///   UTILS ///////////////////////////////////////////////////////////////////////////
    private String getParameter(HttpServletRequest request, String name){
        String value = request.getParameter(name);

        if (value == null){
            return "";
        }
        return value.trim();
    }


    private boolean isValidFuelType(String fuelType){
        return fuelType.isEmpty()
                || "Benzina".equals(fuelType)
                || "Diesel".equals(fuelType)
                || "Elettrico".equals(fuelType)
                || "Ibrido".equals(fuelType)
                || "GPL".equals(fuelType);
    }


    private boolean isValidTransmission(String transmission){
        return transmission.isEmpty()
                ||transmission.equals("MANUALE")
                ||transmission.equals("AUTOMATICA");
    }
}
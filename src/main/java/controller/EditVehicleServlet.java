package controller;

import filter.DealerAuthorizationFilter;
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
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@WebServlet("/dealer/edit-vehicle")
public class EditVehicleServlet extends HttpServlet {

    private final VehicleDAO vehicleDAO = new VehicleDAO();

    private static final Set<String> ALLOWED_FUEL_TYPES =
            Set.of(
                    "Benzina",
                    "Diesel",
                    "Elettrico",
                    "Ibrido",
                    "GPL"
            );

    private static final Set<String> ALLOWED_TRANSMISSIONS =
            Set.of(
                    "Manuale",
                    "Automatico"
            );


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    User dealer = (User) request.getAttribute(DealerAuthorizationFilter.DEALER_ATTRIBUTE);

        Integer vehicleId = parsePositiveInteger(request.getParameter("id"));

        if (vehicleId == null) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "ID del veicolo non valido"
            );
            return;
        }


        try {
            Vehicle vehicle = vehicleDAO.getVehicleByIdAndDealer(
                    vehicleId, dealer.getId()
            );

            if (vehicle == null) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Veicolo non trovato"
                );
                return;
            }

            request.setAttribute("vehicle", vehicle);

            request.getRequestDispatcher("/WEB-INF/dealer/edit-vehicle.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Errore durante il caricamento del veicolo", e
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        User dealer = (User) request.getAttribute(DealerAuthorizationFilter.DEALER_ATTRIBUTE);

        Integer vehicleId = parsePositiveInteger(request.getParameter("id"));

        if (vehicleId == null) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "ID del veicolo non valido"
            );
            return;
        }


        //Controlliamo che il veicolo appqartenga al dealer autenticato
        //non ci fidiamo dell'id dato dal form

        Vehicle existingVehicle;

        try {
            existingVehicle =
                    vehicleDAO.getVehicleByIdAndDealer(
                            vehicleId,
                            dealer.getId()
                    );

        } catch (SQLException e) {
            throw new ServletException(
                    "Errore durante il controllo del veicolo",
                    e
            );
        }

        if (existingVehicle == null) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Veicolo non trovato"
            );
            return;
        }


        List<String> errors = new ArrayList<>();

        String brand =
                getTrimmedParameter(request, "brand");

        String model =
                getTrimmedParameter(request, "model");

        String productionYearParameter =
                getTrimmedParameter(
                        request,
                        "productionYear"
                );

        String kmParameter =
                getTrimmedParameter(request, "km");

        String fuelType =
                getTrimmedParameter(request, "fuelType");

        String transmission =
                getTrimmedParameter(
                        request,
                        "transmission"
                );

        String priceParameter =
                getTrimmedParameter(request, "price");

        String description =
                getTrimmedParameter(
                        request,
                        "description"
                );

        if (brand.isEmpty()) {
            errors.add(
                    "La marca è obbligatoria."
            );
        }

        if (model.isEmpty()) {
            errors.add(
                    "Il modello è obbligatorio."
            );
        }

        int productionYear = 0;

        try {
            productionYear =
                    Integer.parseInt(
                            productionYearParameter
                    );

            int currentYear =
                    Year.now().getValue();

            if (productionYear < 1886
                    || productionYear > currentYear) {

                errors.add(
                        "L'anno di produzione non è valido."
                );
            }

        } catch (NumberFormatException e) {
            errors.add(
                    "L'anno di produzione deve essere un numero."
            );
        }

        int km = 0;

        try {
            km = Integer.parseInt(kmParameter);

            if (km < 0) {
                errors.add(
                        "I chilometri non possono essere negativi."
                );
            }

        } catch (NumberFormatException e) {
            errors.add(
                    "I chilometri devono essere un numero intero."
            );
        }

        if (!ALLOWED_FUEL_TYPES.contains(fuelType)) {
            errors.add(
                    "Il tipo di carburante non è valido."
            );
        }

        if (!ALLOWED_TRANSMISSIONS.contains(
                transmission
        )) {
            errors.add(
                    "Il tipo di cambio non è valido."
            );
        }

        double price = 0;

        try {
            price = Double.parseDouble(
                    priceParameter.replace(',', '.')
            );

            if (price <= 0) {
                errors.add(
                        "Il prezzo deve essere maggiore di zero."
                );
            }

        } catch (NumberFormatException e) {
            errors.add(
                    "Il prezzo non è valido."
            );
        }



        /*
         * creiamo un oggetto con i dati ricevuti dal form
         */

        Vehicle updatedVehicle = new Vehicle();

        updatedVehicle.setId(vehicleId);


        /*
         * il dealerID non viene preso dal browser, viene preso dalla sessione autenticata
         */

        updatedVehicle.setDealerId(dealer.getId());

        updatedVehicle.setBrand(brand);
        updatedVehicle.setModel(model);
        updatedVehicle.setProductionYear(productionYear);
        updatedVehicle.setKm(km);
        updatedVehicle.setFuelType(fuelType);
        updatedVehicle.setTransmission(transmission);
        updatedVehicle.setPrice(price);
        updatedVehicle.setDescription(description);

        /*
         * manteniamo lo stato già presente nel DB
         */
        updatedVehicle.setStatus(
                existingVehicle.getStatus());

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);

            /*
             * la JSP userà questi valori per non svuotare il form dopo un errore di validazione
             */
            request.setAttribute(
                    "vehicle",
                    updatedVehicle
            );

            request.getRequestDispatcher("/WEB-INF/dealer/edit-vehicle.jsp")
                    .forward(request, response);

            return;
        }

        try {
            boolean updated = vehicleDAO.updateVehicle(updatedVehicle);

            if (!updated) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Veicolo non trovato o non modificabile");
                return;
            }

            response.sendRedirect(
                    request.getContextPath() + "/dealer/vehicles"
            );
        } catch (SQLException e) {
            throw new ServletException("Errore durante la modifica del veicolo", e);
        }

    }



    private String getTrimmedParameter(
            HttpServletRequest request,
            String parameterName
    ) {
        String value =
                request.getParameter(parameterName);

        if (value == null) {
            return "";
        }

        return value.trim();
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


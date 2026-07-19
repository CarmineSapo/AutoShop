package controller;


import filter.DealerAuthorizationFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.bean.User;
import model.bean.Vehicle;
import model.dao.VehicleDAO;
import model.dao.VehicleImageDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@WebServlet("/dealer/add-vehicle")
@MultipartConfig(
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 15 * 1024 * 1024
)
public class AddVehicleServlet extends HttpServlet{

    private final VehicleDAO vehicleDAO = new VehicleDAO();

    private final VehicleImageDAO vehicleImageDAO = new VehicleImageDAO();

    private static final Set<String> ALLOWED_FUEL_TYPES =
            Set.of(
                    "Benzina",
                    "Diesel",
                    "Elettrico",
                    "Ibrido",
                    "GPL",
                    "Metano"
            );

    private static final Set<String> ALLOWED_TRANSMISSIONS =
            Set.of(
                   "Manuale",
                   "Automatico"
            );




    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{


        request.getRequestDispatcher("/WEB-INF/dealer/add-vehicle.jsp")
                .forward(request,response);

    }







    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{

        request.setCharacterEncoding("UTF-8");

        User dealer = (User) request.getAttribute(DealerAuthorizationFilter.DEALER_ATTRIBUTE);

        List<String> errors = new ArrayList<>();

        List<Part> imageParts = new ArrayList<>();

        for (Part part : request.getParts()) {

            if ("images".equals(part.getName())
                    && part.getSize() > 0) {

                imageParts.add(part);
            }
        }

        if (imageParts.size() > 3) {
            errors.add(
                    "Puoi caricare un massimo di 3 immagini."
            );
        }

        for (Part imagePart : imageParts) {

            String contentType =
                    imagePart.getContentType();

            boolean validType =
                    "image/jpeg".equals(contentType)
                            || "image/png".equals(contentType)
                            || "image/webp".equals(contentType);

            if (!validType) {
                errors.add(
                        "Sono consentite soltanto immagini JPG, PNG o WEBP."
                );
                break;
            }
        }

        String brand = getTrimmedParameter(request, "brand");
        String model = getTrimmedParameter(request, "model");
        String productionYearParameter =
                getTrimmedParameter(request, "productionYear");
        String kmParameter =
                getTrimmedParameter(request, "km");
        String fuelType =
                getTrimmedParameter(request, "fuelType");
        String transmission =
                getTrimmedParameter(request, "transmission");
        String priceParameter =
                getTrimmedParameter(request, "price");
        String description =
                getTrimmedParameter(request, "description");

        if (brand.isEmpty()) {
            errors.add("La marca è obbligatoria.");
        }

        if (model.isEmpty()) {
            errors.add("Il modello è obbligatorio.");
        }

        int productionYear = 0;

        try {
            productionYear =
                    Integer.parseInt(productionYearParameter);

            int currentYear = Year.now().getValue();

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
            errors.add("Il tipo di carburante non è valido.");
        }

        if (!ALLOWED_TRANSMISSIONS.contains(transmission)) {
            errors.add("Il tipo di cambio non è valido.");
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
            errors.add("Il prezzo non è valido.");
        }

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);

            request.getRequestDispatcher(
                    "/WEB-INF/dealer/add-vehicle.jsp"
            ).forward(request, response);

            return;
        }

        Vehicle vehicle = new Vehicle();

        /*
         * Il dealerId viene preso dalla sessione.
         * Non viene accettato dal form, perché un utente
         * potrebbe modificarlo e pubblicare per conto di altri.
         */
        vehicle.setDealerId(dealer.getId());

        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setProductionYear(productionYear);
        vehicle.setKm(km);
        vehicle.setFuelType(fuelType);
        vehicle.setTransmission(transmission);
        vehicle.setPrice(price);
        vehicle.setDescription(description);
        vehicle.setStatus("AVAILABLE");

        try {
            int generatedVehicleId =
                    vehicleDAO.insertVehicle(vehicle);

            saveVehicleImages(
                    imageParts,
                    generatedVehicleId
            );

            vehicle.setId(generatedVehicleId);

            response.sendRedirect(                     //Non facciamo forward. Evita che aggiornando la pagina dopo
                    request.getContextPath()           //l'inserimento, il browser ripeta la POST e crea due volte lo stesso veicolo
                            + "/dealer/vehicles"
            );

        } catch (SQLException e) {
            throw new ServletException(
                    "Errore durante l'inserimento del veicolo",
                    e
            );
        }
    }

















//Utility



    private String getTrimmedParameter(//Evitiamo nullpointer exception
            HttpServletRequest request,
            String parameterName
    ) {
        String value =
                request.getParameter(parameterName);

        if (value == null) {
            return "";
        }

        return value.trim().replaceAll("\\s+", " ");
    }


    private void saveVehicleImages(
            List<Part> imageParts,
            int vehicleId
    ) throws IOException, SQLException {

        String uploadDirectory =
                getServletContext().getRealPath(
                        "/uploads/vehicles"
                );

        Path uploadPath =
                Path.of(uploadDirectory);

        Files.createDirectories(uploadPath);

        int sortOrder = 1;

        for (Part imagePart : imageParts) {

            String extension =
                    getImageExtension(
                            imagePart.getContentType()
                    );

            String fileName =
                    UUID.randomUUID() + extension;

            Path destination =
                    uploadPath.resolve(fileName);

            try (InputStream inputStream =
                         imagePart.getInputStream()) {

                Files.copy(
                        inputStream,
                        destination
                );
            }

            String databasePath =
                    "uploads/vehicles/" + fileName;

            vehicleImageDAO.insertImage(
                    vehicleId,
                    databasePath,
                    sortOrder
            );

            sortOrder++;
        }
    }

    private String getImageExtension(
            String contentType
    ) {
        if ("image/png".equals(contentType)) {
            return ".png";
        }

        if ("image/webp".equals(contentType)) {
            return ".webp";
        }

        return ".jpg";
    }



}

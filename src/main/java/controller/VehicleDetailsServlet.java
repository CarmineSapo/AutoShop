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

@WebServlet("/vehicle")
public class VehicleDetailsServlet extends HttpServlet {

    private final VehicleDAO vehicleDao = new VehicleDAO();

    private final VehicleImageDAO vehicleImageDAO =
            new VehicleImageDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isBlank()){

            response.sendRedirect(request.getContextPath() + "/catalog");
            return;
        }

        try{
            int id;

            try {
                id = Integer.parseInt(idParam);

                if (id <= 0) {
                    throw new NumberFormatException();
                }

            } catch (NumberFormatException e) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "ID del veicolo non valido"
                );

                return;
            }

            Vehicle vehicle = vehicleDao.getVehicleById(id);

            if (vehicle == null){
                response.sendRedirect(request.getContextPath() + "/catalog");
                return;
            }

            vehicle.setImagePaths(
                    vehicleImageDAO.getImagesByVehicleId(
                            vehicle.getId()
                    )
            );

            request.setAttribute("vehicle", vehicle);

            request.getRequestDispatcher("/vehicle-details.jsp")
                    .forward(request, response);


        } catch (SQLException e) {
            throw new ServletException("errore durante il caricamento del veicolo ",e);
        }

    }


}

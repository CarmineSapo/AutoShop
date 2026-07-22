package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.bean.User;
import model.bean.Vehicle;
import model.cart.Cart;
import model.dao.VehicleDAO;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/add-to-cart")
public class AddToCartServlet extends HttpServlet {

    private final VehicleDAO vehicleDAO = new VehicleDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/catalog");
            return;
        }

        try {
            int vehicleId = Integer.parseInt(idParam);

            Vehicle vehicle = vehicleDAO.getVehicleById(vehicleId);

            if (vehicle == null) {
                response.sendRedirect(request.getContextPath() + "/catalog");
                return;
            }

            User user = (User) request.getSession()
                    .getAttribute("user");

            if (isVehicleOwner(user, vehicle)) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Non puoi aggiungere al carrello un tuo veicolo."
                );

                return;
            }

            HttpSession session = request.getSession();

            Cart cart = (Cart) session.getAttribute("cart");

            if (cart == null) {
                cart = new Cart();
                session.setAttribute("cart", cart);
            }

            cart.addVehicle(vehicle);

            response.sendRedirect(request.getContextPath() + "/cart");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/catalog");

        } catch (SQLException e) {
            throw new ServletException("Errore durante l'aggiunta al carrello", e);
        }
    }













    /*
     *controlla se l'utente autenticato è il dealer
     *proprietario del veicolo
     */
    private boolean isVehicleOwner(
            User user,
            Vehicle vehicle
    ) {
        return user != null && user.getId() == vehicle.getDealerId();
    }

}
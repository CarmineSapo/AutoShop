package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.cart.Cart;

import java.io.IOException;

@WebServlet("/remove-from-cart")
public class RemoveFromCartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{

        String idParam = request.getParameter("id");

        HttpSession session = request.getSession(false);

        if (session != null && idParam != null && !idParam.isBlank()){

            try{
                int vehicleId = Integer.parseInt(idParam);

                Cart cart = (Cart) session.getAttribute("cart");

                if (cart != null){
                    cart.removeVehicle(vehicleId);
                }
            } catch (NumberFormatException ignored){
            }
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}

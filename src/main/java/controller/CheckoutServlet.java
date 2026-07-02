package controller;


import com.example.autoshop.HelloServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.bean.User;
import model.cart.Cart;
import model.dao.OrderDAO;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{


        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null){
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.getItems().isEmpty()){
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        User user = (User) session.getAttribute("user");

        try{
            int orderId = orderDAO.createOrder(user, cart);

            session.removeAttribute("cart");

            request.setAttribute("orderId", orderId);

            request.getRequestDispatcher("/WEB-INF/order-confirmation.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Errore durante la conferma dell'ordine", e);
        }
    }
}

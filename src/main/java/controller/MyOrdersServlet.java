package controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.bean.Order;
import model.bean.User;
import model.dao.OrderDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/my-orders")
public class MyOrdersServlet extends HttpServlet {


    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected  void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null){

            response.sendRedirect( request.getContextPath() + "/login-jsp");

            return;
        }

        User user = (User) session.getAttribute("user");

        try{
            List<Order> orders = orderDAO.getOrderByUser(user.getId());

            request.setAttribute("orders", orders);

            request.getRequestDispatcher("/WEB-INF/my-orders.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException( "Errore durante il caricamento degli ordini", e);
        }
    }
}

package controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.bean.User;
import model.dao.UserDAO;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.isBlank()
        || password == null || password.isBlank()){

            response.sendRedirect(request.getContextPath() +"/login.jsp");
            return;
        }

        try{

            User user = userDAO.findByEmail(email);

            if (user == null || !user.getPasswordHash().equals(password)){

                request.setAttribute("error", "Email o password non corretti");
                request.getRequestDispatcher("/login.jsp")
                        .forward(request, response);
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            response.sendRedirect(request.getContextPath() + "/catalog");
        } catch (SQLException e) {
            throw new ServletException("errore durante il login", e);
        }

    }
}

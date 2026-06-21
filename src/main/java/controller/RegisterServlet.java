package controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.bean.User;
import model.dao.UserDAO;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (username == null || username.isBlank()
        || email == null || email.isBlank()
        || password == null || password.isBlank()){
            response.sendRedirect(request.getContextPath() + "/register.jsp");
            return;
        }

        try{
            User existingUser = userDAO.findByEmail(email);

                    if (existingUser != null){
                        request.setAttribute("error", "Email già registrata");
                        request.getRequestDispatcher("/register.jsp")
                                .forward(request, response);
                        return;
                    }


                    User user = new User();
                    user.setUsername(username);
                    user.setEmail(email);

                    //da cambiare con BCrypt
                    user.setPasswordHash(password);

                    user.setRole("CUSTOMER");

                    userDAO.save(user);

                    response.sendRedirect(request.getContextPath() + "/login.jsp");


        } catch (SQLException e) {
            throw new ServletException("errore durante la registrazione", e);
        }
    }
}

package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.utils.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


@WebServlet("/test-db")

public class TestConnectionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


        response.setContentType("text/plain");

        try(Connection connection = DBConnection.getConnection()) {
            response.getWriter().println("connessione al db riuscita");

        } catch (SQLException e) {
            //throw new RuntimeException(e);
            response.getWriter().print("errore connessione al db:");
            response.getWriter().println(e.getMessage());
        }
    }
}

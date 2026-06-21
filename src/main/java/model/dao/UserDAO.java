package model.dao;

import model.bean.User;
import model.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User findByEmail(String email) throws SQLException {

        String sql = "SELECT * FROM users WHERE email = ?";

        try(
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
                ){

            statement.setString(1, email);

            try(ResultSet resultSet = statement.executeQuery()){

                if (resultSet.next()){

                    User user = new User();

                    user.setId(resultSet.getInt("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPasswordHash(resultSet.getString("password_hash"));
                    user.setRole(resultSet.getString("role"));

                    return user;
                }
            }
        }

        return null;

    }


    public User findByUsername(String username) throws SQLException {

        String sql = "SELECT * FROM users WHERE username = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    User user = new User();

                    user.setId(resultSet.getInt("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPasswordHash(resultSet.getString("password_hash"));
                    user.setRole(resultSet.getString("role"));

                    return user;
                }
            }
        }

        return null;
    }


    public void save (User user) throws SQLException{

        String sql = """
                INSERT INTO users
                (username, email, password_hash, role)
                VALUES (?, ?, ?, ?)
                """;

        try(
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);

                ){

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.setString(4, user.getRole());

            statement.executeUpdate();
        }
    }


}

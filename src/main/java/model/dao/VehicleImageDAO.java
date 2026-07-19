package model.dao;

import model.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleImageDAO {

    public void insertImage(
            int vehicleId,
            String imagePath,
            int displayOrder
    ) throws SQLException {

        String sql = """
                INSERT INTO vehicle_images (
                    vehicle_id,
                    image_path,
                    display_order
                )
                VALUES (?, ?, ?)
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setInt(1, vehicleId);
            statement.setString(2, imagePath);
            statement.setInt(3, displayOrder);

            statement.executeUpdate();
        }
    }

    public List<String> getImagesByVehicleId(
            int vehicleId
    ) throws SQLException {

        List<String> images = new ArrayList<>();

        String sql = """
                SELECT image_path
                FROM vehicle_images
                WHERE vehicle_id = ?
                ORDER BY display_order
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setInt(1, vehicleId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    images.add(
                            resultSet.getString("image_path")
                    );
                }
            }
        }

        return images;
    }
}
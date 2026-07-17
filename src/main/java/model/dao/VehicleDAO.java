package model.dao;

import model.bean.Vehicle;
import model.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    public List<Vehicle> getAllVehicles() throws SQLException {

        List<Vehicle> vehicles = new ArrayList<>();

        String sql = "SELECT * FROM vehicles WHERE status = 'AVAILABLE' AND is_active = TRUE";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {

                Vehicle vehicle = new Vehicle();

                vehicle.setId(resultSet.getInt("id"));
                vehicle.setDealerId(resultSet.getInt("dealer_id"));
                vehicle.setBrand(resultSet.getString("brand"));
                vehicle.setModel(resultSet.getString("model"));
                vehicle.setProductionYear(resultSet.getInt("production_year"));
                vehicle.setKm(resultSet.getInt("km"));
                vehicle.setFuelType(resultSet.getString("fuel_type"));
                vehicle.setTransmission(resultSet.getString("transmission"));
                vehicle.setPrice(resultSet.getDouble("price"));
                vehicle.setDescription(resultSet.getString("description"));
                vehicle.setStatus(resultSet.getString("status"));
                vehicle.setActive(resultSet.getBoolean("is_active"));

                vehicles.add(vehicle);
            }
        }

        return vehicles;

    }

    public Vehicle getVehicleById(int id) throws SQLException {

        String sql = "SELECT * FROM vehicles WHERE id = ? AND is_active = TRUE";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Vehicle vehicle = new Vehicle();

                    vehicle.setId(resultSet.getInt("id"));
                    vehicle.setDealerId(resultSet.getInt("dealer_id"));
                    vehicle.setBrand(resultSet.getString("brand"));
                    vehicle.setModel(resultSet.getString("model"));
                    vehicle.setProductionYear(resultSet.getInt("production_year"));
                    vehicle.setKm(resultSet.getInt("km"));
                    vehicle.setFuelType(resultSet.getString("fuel_type"));
                    vehicle.setTransmission(resultSet.getString("transmission"));
                    vehicle.setPrice(resultSet.getDouble("price"));
                    vehicle.setDescription(resultSet.getString("description"));
                    vehicle.setStatus(resultSet.getString("status"));

                    return vehicle;
                }
            }
        }

        return null;


    }


    public List<Vehicle> getVehiclesByDealer(int dealerId)
            throws SQLException {

        List<Vehicle> vehicles = new ArrayList<>();

        String sql = """
                SELECT *
                FROM vehicles
                WHERE dealer_id = ?
                ORDER BY created_at DESC
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setInt(1, dealerId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Vehicle vehicle = new Vehicle();

                    vehicle.setId(resultSet.getInt("id"));
                    vehicle.setDealerId(resultSet.getInt("dealer_id"));
                    vehicle.setBrand(resultSet.getString("brand"));
                    vehicle.setModel(resultSet.getString("model"));

                    vehicle.setProductionYear(
                            resultSet.getInt("production_year")
                    );

                    vehicle.setKm(resultSet.getInt("km"));

                    vehicle.setFuelType(
                            resultSet.getString("fuel_type")
                    );

                    vehicle.setTransmission(
                            resultSet.getString("transmission")
                    );

                    vehicle.setPrice(resultSet.getDouble("price"));

                    vehicle.setDescription(
                            resultSet.getString("description")
                    );

                    vehicle.setStatus(resultSet.getString("status"));

                    vehicle.setActive(resultSet.getBoolean("is_active"));

                    vehicles.add(vehicle);
                }
            }
        }

        return vehicles;
    }


    public int insertVehicle(Vehicle vehicle) throws SQLException {
        //Il valore restituito è l'id generato dal database

        String sql = """
                INSERT INTO vehicles (
                    dealer_id,
                    brand,
                    model,
                    production_year,
                    km,
                    fuel_type,
                    transmission,
                    price,
                    description,
                    status
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'AVAILABLE')
                """;

        try (
                Connection connection = DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {
            statement.setInt(1, vehicle.getDealerId());
            statement.setString(2, vehicle.getBrand());
            statement.setString(3, vehicle.getModel());
            statement.setInt(4, vehicle.getProductionYear());
            statement.setInt(5, vehicle.getKm());
            statement.setString(6, vehicle.getFuelType());
            statement.setString(7, vehicle.getTransmission());
            statement.setDouble(8, vehicle.getPrice());
            statement.setString(9, vehicle.getDescription());

            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException(
                        "Inserimento del veicolo non riuscito"
                );
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }

                throw new SQLException(
                        "Inserimento riuscito, ma ID non restituito"
                );
            }
        }
    }


    public Vehicle getVehicleByIdAndDealer(int vehicleId, int dealerId)  //prendiamo un veicolo del dealer
            throws SQLException {
        String sql = """
                SELECT
                    id,
                    dealer_id,
                    brand,
                    model,
                    production_year,
                    km,
                    fuel_type,
                    transmission,
                    price,
                    description,
                    status,
                    is_active
                FROM vehicles
                WHERE id = ?
                  AND dealer_id = ?
                """; //prendiamo il veicolo con id richiesto e con proprietario il dealer autenticato

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setInt(1, vehicleId);
            statement.setInt(2, dealerId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) {
                    return null;
                }

                Vehicle vehicle = new Vehicle();

                vehicle.setId(resultSet.getInt("id"));
                vehicle.setDealerId(
                        resultSet.getInt("dealer_id")
                );
                vehicle.setBrand(
                        resultSet.getString("brand")
                );
                vehicle.setModel(
                        resultSet.getString("model")
                );
                vehicle.setProductionYear(
                        resultSet.getInt("production_year")
                );
                vehicle.setKm(
                        resultSet.getInt("km")
                );
                vehicle.setFuelType(
                        resultSet.getString("fuel_type")
                );
                vehicle.setTransmission(
                        resultSet.getString("transmission")
                );
                vehicle.setPrice(
                        resultSet.getDouble("price")
                );
                vehicle.setDescription(
                        resultSet.getString("description")
                );
                vehicle.setStatus(
                        resultSet.getString("status")
                );
                vehicle.setActive(
                        resultSet.getBoolean("is_active")
                );

                return vehicle;
            }
        }
    }


    public boolean updateVehicle(Vehicle vehicle) //Aggiorna veicolo
            throws SQLException {

        String sql = """
                UPDATE vehicles
                SET
                    brand = ?,
                    model = ?,
                    production_year = ?,
                    km = ?,
                    fuel_type = ?,
                    transmission = ?,
                    price = ?,
                    description = ?
                WHERE id = ?
                  AND dealer_id = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setString(
                    1,
                    vehicle.getBrand()
            );

            statement.setString(
                    2,
                    vehicle.getModel()
            );

            statement.setInt(
                    3,
                    vehicle.getProductionYear()
            );

            statement.setInt(
                    4,
                    vehicle.getKm()
            );

            statement.setString(
                    5,
                    vehicle.getFuelType()
            );

            statement.setString(
                    6,
                    vehicle.getTransmission()
            );

            statement.setDouble(
                    7,
                    vehicle.getPrice()
            );

            statement.setString(
                    8,
                    vehicle.getDescription()
            );

            statement.setInt(
                    9,
                    vehicle.getId()
            );

            statement.setInt(
                    10,
                    vehicle.getDealerId()
            );

            int affectedRows = statement.executeUpdate();

            return affectedRows == 1;
        }
    }


    public boolean updateVehicleVisibility(
            int vehicleId,
            int dealerId,
            boolean active
    ) throws SQLException {

        String sql = """
                UPDATE vehicles
                SET is_active = ?
                WHERE id = ?
                  AND dealer_id = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setBoolean(1, active);
            statement.setInt(2, vehicleId);
            statement.setInt(3, dealerId);

            int affectedRows = statement.executeUpdate();

            return affectedRows == 1;
        }
    }

    public List<Vehicle> getPublicVehiclesByDealer(
            int dealerId
    ) throws SQLException {

        List<Vehicle> vehicles = new ArrayList<>();

        String sql = """
            SELECT
                id,
                dealer_id,
                brand,
                model,
                production_year,
                km,
                fuel_type,
                transmission,
                price,
                description,
                status,
                is_active
            FROM vehicles
            WHERE dealer_id = ?
              AND status = 'AVAILABLE'
              AND is_active = TRUE
            ORDER BY created_at DESC
            """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setInt(1, dealerId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    Vehicle vehicle = new Vehicle();

                    vehicle.setId(
                            resultSet.getInt("id")
                    );

                    vehicle.setDealerId(
                            resultSet.getInt("dealer_id")
                    );

                    vehicle.setBrand(
                            resultSet.getString("brand")
                    );

                    vehicle.setModel(
                            resultSet.getString("model")
                    );

                    vehicle.setProductionYear(
                            resultSet.getInt("production_year")
                    );

                    vehicle.setKm(
                            resultSet.getInt("km")
                    );

                    vehicle.setFuelType(
                            resultSet.getString("fuel_type")
                    );

                    vehicle.setTransmission(
                            resultSet.getString("transmission")
                    );

                    vehicle.setPrice(
                            resultSet.getDouble("price")
                    );

                    vehicle.setDescription(
                            resultSet.getString("description")
                    );

                    vehicle.setStatus(
                            resultSet.getString("status")
                    );

                    vehicle.setActive(
                            resultSet.getBoolean("is_active")
                    );

                    vehicles.add(vehicle);
                }
            }
        }

        return vehicles;
    }

}

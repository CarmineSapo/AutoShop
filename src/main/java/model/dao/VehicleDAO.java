package model.dao;

import model.bean.Vehicle;
import model.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    public List<Vehicle> getAllVehicles() throws SQLException{

        List<Vehicle> vehicles = new ArrayList<>();

        String sql = "SELECT * FROM vehicles WHERE status = 'AVAILABLE'";

        try(
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                ){
            while ( resultSet.next()){

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

                vehicles.add(vehicle);
            }
        }

        return vehicles;

    }

    public Vehicle getVehicleById(int id) throws  SQLException{

        String sql = "SELECT * FROM vehicles WHERE id = ?";

        try(
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ){
            statement.setInt(1, id);

            try( ResultSet resultSet = statement.executeQuery()){

                if (resultSet.next()){

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
}

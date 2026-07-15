package model.dao;

import model.bean.OrderItem;
import model.bean.Vehicle;
import model.bean.Order;
import model.bean.User;
import model.cart.Cart;
import model.cart.CartItem;
import model.utils.DBConnection;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Or;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderDAO {

    public int createOrder(User user, Cart cart)
            throws SQLException {

        List<CartItem> selectedItems =
                cart.getSelectedItems();

        if (selectedItems.isEmpty()) {
            throw new SQLException(
                    "Nessun veicolo selezionato"
            );
        }

        String insertOrderSql = """
            INSERT INTO orders (
                user_id,
                total_price,
                status
            )
            VALUES (?, ?, 'PAID')
            """;

        String insertOrderItemSql = """
            INSERT INTO order_items (
                order_id,
                vehicle_id,
                purchase_price
            )
            VALUES (?, ?, ?)
            """;

        String updateVehicleSql = """
            UPDATE vehicles
            SET status = 'SOLD'
            WHERE id = ?
              AND status = 'AVAILABLE'
            """;

        try (Connection connection =
                     DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                int orderId;

                /*
                 * Inserimento dell'ordine.
                 */
                try (PreparedStatement orderStatement =
                             connection.prepareStatement(
                                     insertOrderSql,
                                     Statement.RETURN_GENERATED_KEYS
                             )) {

                    orderStatement.setInt(
                            1,
                            user.getId()
                    );

                    orderStatement.setDouble(
                            2,
                            cart.getSelectedTotal()
                    );

                    int affectedRows =
                            orderStatement.executeUpdate();

                    if (affectedRows != 1) {
                        throw new SQLException(
                                "Errore durante la creazione dell'ordine"
                        );
                    }

                    try (ResultSet generatedKeys =
                                 orderStatement.getGeneratedKeys()) {

                        if (!generatedKeys.next()) {
                            throw new SQLException(
                                    "ID ordine non generato"
                            );
                        }

                        orderId =
                                generatedKeys.getInt(1);
                    }
                }

                /*
                 * Inserimento dei veicoli selezionati
                 * nella tabella order_items.
                 */
                try (PreparedStatement itemStatement =
                             connection.prepareStatement(
                                     insertOrderItemSql
                             )) {

                    for (CartItem item : selectedItems) {

                        itemStatement.setInt(
                                1,
                                orderId
                        );

                        itemStatement.setInt(
                                2,
                                item.getVehicle().getId()
                        );

                        itemStatement.setDouble(
                                3,
                                item.getPrice()
                        );

                        itemStatement.addBatch();
                    }

                    itemStatement.executeBatch();
                }

                /*
                 * Aggiornamento dello stato dei veicoli.
                 */
                try (PreparedStatement updateStatement =
                             connection.prepareStatement(
                                     updateVehicleSql
                             )) {

                    for (CartItem item : selectedItems) {

                        updateStatement.setInt(
                                1,
                                item.getVehicle().getId()
                        );

                        int affectedRows =
                                updateStatement.executeUpdate();

                        if (affectedRows != 1) {
                            throw new SQLException(
                                    "Uno dei veicoli non è più disponibile"
                            );
                        }
                    }
                }

                connection.commit();

                return orderId;

            } catch (SQLException e) {

                connection.rollback();

                throw e;

            } finally {

                connection.setAutoCommit(true);
            }
        }
    }


    public List<Order> getOrderByUser( int userId) throws  SQLException{

        List<Order> orders = new ArrayList<>();

        String sql = """
                SELECT id, user_id, order_date, total_price, status
                FROM orders
                WHERE user_id = ?
                ORDER BY order_date DESC
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
                ){

            statement.setInt(1, userId);

            try( ResultSet resultSet = statement.executeQuery()){

                while (resultSet.next()){

                    Order order =  new Order();

                    order.setId(resultSet.getInt("id"));
                    order.setUserId(resultSet.getInt("user_id"));

                    Timestamp timestamp = resultSet.getTimestamp("order_date");

                    if (timestamp != null){

                        order.setOrderDate(timestamp.toLocalDateTime());
                    }

                    order.setTotalPrice(resultSet.getDouble("total_price"));

                    order.setStatus(resultSet.getString("status"));

                    order.setItems(
                            getItemsByOrder(connection, order.getId())
                    );

                    orders.add(order);
                }
            }
        }

        return orders;
    }

    private List<OrderItem> getItemsByOrder(
            Connection connection,
            int orderId) throws SQLException {

        List<OrderItem> items = new ArrayList<>();

        String sql = """
            SELECT
                oi.order_id,
                oi.vehicle_id,
                oi.purchase_price,
                v.dealer_id,
                v.brand,
                v.model,
                v.production_year,
                v.km,
                v.fuel_type,
                v.transmission,
                v.price,
                v.description,
                v.status
            FROM order_items oi
            JOIN vehicles v ON oi.vehicle_id = v.id
            WHERE oi.order_id = ?
            """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Vehicle vehicle = new Vehicle();

                    vehicle.setId(
                            resultSet.getInt("vehicle_id"));

                    vehicle.setDealerId(
                            resultSet.getInt("dealer_id"));

                    vehicle.setBrand(
                            resultSet.getString("brand"));

                    vehicle.setModel(
                            resultSet.getString("model"));

                    vehicle.setProductionYear(
                            resultSet.getInt("production_year"));

                    vehicle.setKm(
                            resultSet.getInt("km"));

                    vehicle.setFuelType(
                            resultSet.getString("fuel_type"));

                    vehicle.setTransmission(
                            resultSet.getString("transmission"));

                    vehicle.setPrice(
                            resultSet.getDouble("price"));

                    vehicle.setDescription(
                            resultSet.getString("description"));

                    vehicle.setStatus(
                            resultSet.getString("status"));

                    OrderItem item = new OrderItem();

                    item.setOrderId(
                            resultSet.getInt("order_id"));

                    item.setVehicleId(
                            resultSet.getInt("vehicle_id"));

                    item.setPurchasePrice(
                            resultSet.getDouble("purchase_price"));

                    item.setVehicle(vehicle);

                    items.add(item);
                }
            }
        }

        return items;
    }
}

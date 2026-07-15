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

    public int createOrder(User user, Cart cart) throws SQLException{

        if (user == null) {
            throw new IllegalArgumentException(
                    "L'utente non può essere null"
            );
        }

        if (cart == null) {
            throw new IllegalArgumentException(
                    "Il carrello non può essere null"
            );
        }

        /*
         * Creiamo una copia della lista degli elementi selezionati.
         * In questo modo lavoriamo su una lista stabile durante
         * l'intera transazione.
         */
        List<CartItem> selectedItems =
                List.copyOf(cart.getSelectedItems());

        if (selectedItems.isEmpty()) {
            throw new SQLException(
                    "Nessun veicolo selezionato per l'acquisto"
            );
        }

        /*
         * FOR UPDATE blocca temporaneamente la riga del veicolo
         * fino al commit o rollback della transazione.
         *
         * In questo modo due utenti non possono acquistare
         * contemporaneamente lo stesso veicolo.
         */
        String checkVehicleSql = """
            SELECT price, status, is_active
            FROM vehicles
            WHERE id = ?
            FOR UPDATE
            """;

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
              AND is_active = TRUE
            """;

        try (Connection connection =
                     DBConnection.getConnection()) {

            /*
             * Memorizziamo il precedente valore per ripristinarlo
             * alla fine del metodo.
             */
            boolean previousAutoCommit =
                    connection.getAutoCommit();

            connection.setAutoCommit(false);

            try {
                /*
                 * I prezzi vengono riletti dal database.
                 * Non ci fidiamo del prezzo presente nel carrello,
                 * perché potrebbe essere stato modificato dopo
                 * l'aggiunta del veicolo.
                 */
                Map<Integer, Double> currentPrices =
                        new LinkedHashMap<>();

                double totalPrice = 0.0;

                try (PreparedStatement checkStatement =
                             connection.prepareStatement(
                                     checkVehicleSql
                             )) {

                    for (CartItem item : selectedItems) {

                        int vehicleId =
                                item.getVehicle().getId();

                        checkStatement.setInt(
                                1,
                                vehicleId
                        );

                        try (ResultSet resultSet =
                                     checkStatement.executeQuery()) {

                            /*
                             * Il veicolo potrebbe essere stato
                             * eliminato o potrebbe non esistere.
                             */
                            if (!resultSet.next()) {
                                throw new SQLException(
                                        "Il veicolo con ID "
                                                + vehicleId
                                                + " non esiste"
                                );
                            }

                            String status =
                                    resultSet.getString(
                                            "status"
                                    );

                            boolean active =
                                    resultSet.getBoolean(
                                            "is_active"
                                    );

                            /*
                             * Il veicolo deve essere ancora disponibile
                             * e l'inserzione deve essere attiva.
                             */
                            if (!"AVAILABLE".equals(status)
                                    || !active) {

                                throw new SQLException(
                                        "Il veicolo con ID "
                                                + vehicleId
                                                + " non è più disponibile"
                                );
                            }

                            double currentPrice =
                                    resultSet.getDouble(
                                            "price"
                                    );

                            currentPrices.put(
                                    vehicleId,
                                    currentPrice
                            );

                            totalPrice += currentPrice;
                        }
                    }
                }

                int generatedOrderId;

                /*
                 * Inserimento dell'ordine principale.
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
                            totalPrice
                    );

                    int affectedRows =
                            orderStatement.executeUpdate();

                    if (affectedRows != 1) {
                        throw new SQLException(
                                "Creazione dell'ordine non riuscita"
                        );
                    }

                    try (ResultSet generatedKeys =
                                 orderStatement.getGeneratedKeys()) {

                        if (!generatedKeys.next()) {
                            throw new SQLException(
                                    "ID dell'ordine non restituito"
                            );
                        }

                        generatedOrderId =
                                generatedKeys.getInt(1);
                    }
                }

                /*
                 * Inseriamo tutte le righe in order_items.
                 */
                try (PreparedStatement itemStatement =
                             connection.prepareStatement(
                                     insertOrderItemSql
                             )) {

                    for (CartItem item : selectedItems) {

                        int vehicleId =
                                item.getVehicle().getId();

                        double purchasePrice =
                                currentPrices.get(vehicleId);

                        itemStatement.setInt(
                                1,
                                generatedOrderId
                        );

                        itemStatement.setInt(
                                2,
                                vehicleId
                        );

                        itemStatement.setDouble(
                                3,
                                purchasePrice
                        );

                        itemStatement.addBatch();
                    }

                    int[] results =
                            itemStatement.executeBatch();

                    /*
                     * Controlliamo che nessuna operazione del batch
                     * sia fallita.
                     */
                    for (int result : results) {

                        if (result
                                == Statement.EXECUTE_FAILED) {

                            throw new SQLException(
                                    "Inserimento di un elemento "
                                            + "dell'ordine non riuscito"
                            );
                        }
                    }
                }

                /*
                 * Dopo aver creato l'ordine, contrassegniamo
                 * come venduti soltanto i veicoli selezionati.
                 */
                try (PreparedStatement updateStatement =
                             connection.prepareStatement(
                                     updateVehicleSql
                             )) {

                    for (CartItem item : selectedItems) {

                        int vehicleId =
                                item.getVehicle().getId();

                        updateStatement.setInt(
                                1,
                                vehicleId
                        );

                        int affectedRows =
                                updateStatement.executeUpdate();

                        /*
                         * Grazie al controllo nella WHERE, la modifica
                         * riesce soltanto se il veicolo è ancora
                         * AVAILABLE e attivo.
                         */
                        if (affectedRows != 1) {
                            throw new SQLException(
                                    "Impossibile acquistare il veicolo "
                                            + "con ID "
                                            + vehicleId
                            );
                        }
                    }
                }

                /*
                 * Tutte le operazioni sono riuscite:
                 * rendiamo definitive le modifiche.
                 */
                connection.commit();

                return generatedOrderId;

            } catch (SQLException | RuntimeException e) {

                /*
                 * Se fallisce anche una sola operazione,
                 * annulliamo l'intera transazione.
                 */
                try {
                    connection.rollback();

                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }

                throw e;

            } finally {
                /*
                 * Ripristiniamo la configurazione originale
                 * della connessione.
                 */
                connection.setAutoCommit(
                        previousAutoCommit
                );
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

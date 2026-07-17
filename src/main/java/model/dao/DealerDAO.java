package model.dao;

import model.bean.Dealer;
import model.bean.User;
import model.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class DealerDAO {

    public void upgradeToDealer(
            User user,
            Dealer dealer
    ) throws SQLException {

        String insertDealerSql = """
                INSERT INTO dealers (
                    user_id,
                    company_name,
                    vat_number,
                    description,
                    phone,
                    address
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        String updateUserSql = """
                UPDATE users
                SET role = 'DEALER'
                WHERE id = ?
                  AND role = 'CUSTOMER'
                """;

        try (Connection connection =
                     DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                /*
                 * Creazione del profilo dealer.
                 */
                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     insertDealerSql
                             )) {

                    statement.setInt(
                            1,
                            user.getId()
                    );

                    statement.setString(
                            2,
                            dealer.getCompanyName()
                    );

                    statement.setString(
                            3,
                            dealer.getVatNumber()
                    );

                    statement.setString(
                            4,
                            dealer.getDescription()
                    );

                    statement.setString(
                            5,
                            dealer.getPhone()
                    );

                    statement.setString(
                            6,
                            dealer.getAddress()
                    );

                    statement.executeUpdate();
                }

                /*
                 * Aggiornamento del ruolo dell'utente.
                 */
                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     updateUserSql
                             )) {

                    statement.setInt(
                            1,
                            user.getId()
                    );

                    int affectedRows =
                            statement.executeUpdate();

                    if (affectedRows != 1) {
                        throw new SQLException(
                                "Impossibile aggiornare il ruolo dell'utente"
                        );
                    }
                }

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;

            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public Dealer getDealerByUserId(int userId)
            throws SQLException {

        String sql = """
            SELECT
                user_id,
                company_name,
                vat_number,
                description,
                phone,
                address
            FROM dealers
            WHERE user_id = ?
            """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setInt(1, userId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (!resultSet.next()) {
                    return null;
                }

                Dealer dealer = new Dealer();

                dealer.setUserId(
                        resultSet.getInt("user_id")
                );

                dealer.setCompanyName(
                        resultSet.getString("company_name")
                );

                dealer.setVatNumber(
                        resultSet.getString("vat_number")
                );

                dealer.setDescription(
                        resultSet.getString("description")
                );

                dealer.setPhone(
                        resultSet.getString("phone")
                );

                dealer.setAddress(
                        resultSet.getString("address")
                );

                return dealer;
            }
        }
    }
}
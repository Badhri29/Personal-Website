import java.sql.*;
import java.util.Scanner;

public class BaseClass {
    static Scanner input = new Scanner(System.in);

    /**
     * Establishes and returns a connection to the MySQL database using the specified
     * connection parameters such as database URL, username, and password.
     *
     * @return A {@code Connection} object representing the connection to the database.
     * @throws SQLException if a database access error occurs or the connection cannot be established.
     */
    public static Connection getConnection() throws SQLException {
        Connection con = null;
        if (con == null) {
            final String url = "jdbc:mysql://localhost:3306/personalportfolio";
            final String userName = "root";
            final String password = "root";
            con = DriverManager.getConnection(url, userName, password);
        }
        return con;
    }

    /**
     * Closes the provided database connection if it is not null.
     * This method helps to ensure that database resources are released
     * properly after usage, preventing potential resource leaks.
     *
     * @param connection the {@code Connection} object to be closed; if the connection is null,
     *                   the method does nothing. If an error occurs while closing the connection,
     *                   a {@code RuntimeException} is thrown wrapping the {@code SQLException}.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static int getLastBalance(Connection getConnection) {
        int lastBalance = 0;
        try {
            Connection connection = getConnection;
            String getQuary = "SELECT balance FROM account ORDER BY id DESC LIMIT 1";
            PreparedStatement getPS = connection.prepareStatement(getQuary);
            ResultSet s = getPS.executeQuery();
            if (s.next()) {
                lastBalance = s.getInt("balance");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastBalance;
    }

    public static int setNewBalance() {
        int currentBalance = 0;
        do {
            System.out.print("Enter amount: ");
            currentBalance = input.nextInt();
            if (currentBalance <= 0) {
                System.out.println("Amount cannot be negative or zero");
            }
        } while (currentBalance <= 0);
        return currentBalance;
    }

    public static void credit() {
        Connection connection = null;
        try {
            connection = getConnection();
            int creditAmount = setNewBalance();
            int updatedBalance = getLastBalance(connection) + creditAmount;

            String query = "INSERT INTO ACCOUNT(credit,balance) VALUES (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, creditAmount);
            preparedStatement.setInt(2, updatedBalance);
            preparedStatement.executeUpdate();
            System.out.println("Amount credited successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    public static void debit() {
        Connection connection = null;
        try {
            connection = getConnection();
            int debitAmount = setNewBalance();
            int updatedBalance = getLastBalance(connection) - debitAmount;

            String query = "INSERT INTO ACCOUNT(debit,balance) VALUES (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, debitAmount);
            preparedStatement.setInt(2, updatedBalance);
            preparedStatement.executeUpdate();
            System.out.println("Amount debited successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }
}
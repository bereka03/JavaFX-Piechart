package com.example.quiz5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.ObservableList;

public class ProductDatabaseManager {

    // Define the JDBC URL, username, and password
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/Products";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    // Method to get a database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection connection = getConnection()) {
            // Create the Product table if it does not exist
            String createTableQuery = "CREATE TABLE IF NOT EXISTS products (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "quantity INT NOT NULL)";
            connection.createStatement().executeUpdate(createTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addProductToDatabase(Product product) {
        try (Connection connection = getConnection()) {
            String insertQuery = "INSERT INTO products (name, quantity) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, product.getName());
                preparedStatement.setInt(2, product.getQuantity());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadProductsFromDatabase(ObservableList<Product> products) {
        try (Connection connection = getConnection()) {
            String selectQuery = "SELECT * FROM products";
            try (ResultSet resultSet = connection.createStatement().executeQuery(selectQuery)) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int quantity = resultSet.getInt("quantity");
                    products.add(new Product(name, quantity));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

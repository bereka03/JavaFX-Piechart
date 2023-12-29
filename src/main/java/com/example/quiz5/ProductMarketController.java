package com.example.quiz5;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMarketController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField quantityTextField;

    @FXML
    private PieChart pieChart;

    @FXML
    private Button addButton;

    @FXML
    private void initialize() {
        // Initialize the PieChart when the controller is loaded
        updatePieChart();
    }

    @FXML
    private void addProduct() {
        String name = nameTextField.getText();
        int quantity = Integer.parseInt(quantityTextField.getText());

        // Add product to the database
        addProductToDatabase(name, quantity);

        // Update the PieChart with the new data
        updatePieChart();

        // Clear the input fields
        nameTextField.clear();
        quantityTextField.clear();
    }

    private void addProductToDatabase(String name, int quantity) {
        try (Connection connection = ProductDatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO products (product, quantity) VALUES (?, ?)")) {

            statement.setString(1, name);
            statement.setInt(2, quantity);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updatePieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Fetch data from the database and populate pieChartData
        try (Connection connection = ProductDatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT product, SUM(quantity) AS total FROM products GROUP BY product");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String productName = resultSet.getString("product");
                int totalQuantity = resultSet.getInt("total");

                pieChartData.add(new PieChart.Data(productName, totalQuantity));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Update the PieChart with the new data
        pieChart.setData(pieChartData);
    }
}



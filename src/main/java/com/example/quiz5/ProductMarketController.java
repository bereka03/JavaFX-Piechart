package com.example.quiz5;

import javafx.beans.binding.Bindings;
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
import java.util.Map;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
             PreparedStatement statement = connection.prepareStatement("INSERT INTO products (name, quantity) VALUES (?, ?)")) {

            statement.setString(1, name);
            statement.setInt(2, quantity);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updatePieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Fetch data from the database and populate pieChartData using Java Stream API
        try (Connection connection = ProductDatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT name, SUM(quantity) AS total FROM products GROUP BY name");
             ResultSet resultSet = statement.executeQuery()) {

            // Use Java Stream API to collect the data and group by product name
            Map<String, Integer> productQuantities = StreamSupport.stream(
                            Spliterators.spliteratorUnknownSize((PrimitiveIterator.OfInt) resultSet, Spliterator.ORDERED),
                            false)
                    .collect(Collectors.toMap(
                            result -> {
                                try {
                                    return resultSet.getString("name");
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            },
                            result -> {
                                try {
                                    return resultSet.getInt("quantity");
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            },
                            Integer::sum
                    ));

            // Populate pieChartData with the grouped data
            productQuantities.forEach((productName, totalQuantity) -> {
                PieChart.Data data = new PieChart.Data(productName, totalQuantity);
                pieChartData.add(data);

                // Set the label to include the quantity
                data.nameProperty().bind(Bindings.concat(productName, " - ", totalQuantity));
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Update the PieChart with the new data
        pieChart.setData(pieChartData);
    }

}



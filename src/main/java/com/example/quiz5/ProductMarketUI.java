package com.example.quiz5;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProductMarketUI {

    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private final PieChart pieChart = new PieChart();

    public void initialize(Stage primaryStage) {
        primaryStage.setTitle("Product Market");

        // Create UI components
        Label nameLabel = new Label("Product Name:");
        Label quantityLabel = new Label("Quantity:");
        TextField nameTextField = new TextField();
        TextField quantityTextField = new TextField();
        Button addButton = new Button("Add Product");

        HBox inputBox = new HBox(10, nameLabel, nameTextField, quantityLabel, quantityTextField, addButton);
        inputBox.setPadding(new Insets(10));

        VBox vbox = new VBox(10, inputBox, pieChart);
        vbox.setPadding(new Insets(10));

        // Event handling for the "Add Product" button
        addButton.setOnAction(event -> {
            String name = nameTextField.getText();
            int quantity = Integer.parseInt(quantityTextField.getText());
            Product product = new Product(name, quantity);
            ProductDatabaseManager.addProductToDatabase(product);
            products.add(product);
            updatePieChart();
        });

        // Set up the scene
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);

        // Initialize database and load existing products
        ProductDatabaseManager.initializeDatabase();
        ProductDatabaseManager.loadProductsFromDatabase(products);

        // Update PieChart with initial data
        updatePieChart();

        // Show the stage
        primaryStage.show();
    }

    private void updatePieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Fetch data from the products list and populate pieChartData
        for (Product product : products) {
            String label = product.getName() + " - " + product.getQuantity();
            pieChartData.add(new PieChart.Data(label, product.getQuantity()));
        }

        // Update the PieChart with the new data
        pieChart.setData(pieChartData);
    }


}

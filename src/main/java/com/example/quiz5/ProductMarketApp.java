package com.example.quiz5;

import javafx.application.Application;
import javafx.stage.Stage;

public class ProductMarketApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ProductMarketUI ui = new ProductMarketUI();
        ui.initialize(primaryStage);
    }
}

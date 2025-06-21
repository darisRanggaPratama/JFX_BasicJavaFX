package com.tama.basicjfx;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main application class that launches the Stock Market Dashboard.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Launch the Stock Market Dashboard
        StockMarketDashboard dashboard = new StockMarketDashboard();
        dashboard.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

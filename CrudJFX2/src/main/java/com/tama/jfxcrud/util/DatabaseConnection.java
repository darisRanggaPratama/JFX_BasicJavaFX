package com.tama.jfxcrud.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static String URL = "jdbc:mysql://localhost:3306/test";
    private static String USER = "rangga";
    private static String PASSWORD = "rangga";
    
    private static Connection connection;
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed() || !connection.isValid(5)) {
            System.out.println("DEBUG: Creating new connection to " + URL);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void setConnectionParams(String host, String port, String database, String user, String password) {
        URL = String.format("jdbc:mysql://%s:%s/%s", host, port, database);
        USER = user;
        PASSWORD = password;
        // Close existing connection if any
        closeConnection();
    }

    public static boolean establishConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("DEBUG: Connection established successfully to " + URL);
            return true;
        } catch (SQLException e) {
            System.err.println("DEBUG: Failed to establish connection: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean testConnection(String host, String port, String database, String user, String password) {
        String testUrl = String.format("jdbc:mysql://%s:%s/%s", host, port, database);
        try (Connection testConn = DriverManager.getConnection(testUrl, user, password)) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    
    public static boolean isConnected() {
        if (connection == null) {
            System.out.println("DEBUG: isConnected() - connection is null");
            return false;
        }

        try {
            boolean closed = connection.isClosed();
            boolean valid = connection.isValid(5); // 5 second timeout

            System.out.println("DEBUG: isConnected() - closed: " + closed + ", valid: " + valid);

            return !closed && valid;
        } catch (SQLException e) {
            System.err.println("DEBUG: isConnected() - SQLException: " + e.getMessage());
            return false;
        }
    }
}

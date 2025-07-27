package com.tama.customer.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {
    private static DBConnectionManager instance;
    private Connection connection;
    private String server;
    private String database;
    private String port;
    private String user;
    private String password;

    private DBConnectionManager() {
        // Private constructor for Singleton pattern
    }

    public static DBConnectionManager getInstance() {
        if (instance == null) {
            instance = new DBConnectionManager();
        }
        return instance;
    }

    public void setupConnection(String server, String database, String port, String user, String password) {
        this.server = server;
        this.database = database;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = String.format("jdbc:mysql://%s:%s/%s", server, port, database);
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean testConnection() {
        try {
            Connection testConn = getConnection();
            return testConn != null && !testConn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

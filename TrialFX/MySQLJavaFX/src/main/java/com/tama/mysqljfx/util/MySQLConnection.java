package com.tama.mysqljfx.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/testing";
    private static final String USER = "rangga";
    private static final String PASSWORD = "rangga"; // TODO: Move to configuration file

    // Private constructor to prevent instantiation
    private MySQLConnection() {
        throw new IllegalStateException("Utility class");
    }

    public static Connection createConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false); // Enable manual transaction management
            return connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            throw new SQLException("Gagal koneksi ke database: " + e.getMessage());
        }
    }

    public static boolean isConnected(Connection connection) {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}

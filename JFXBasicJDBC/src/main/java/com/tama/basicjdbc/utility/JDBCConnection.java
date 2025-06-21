package com.tama.basicjdbc.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCConnection {
    private static final Logger LOGGER = Logger.getLogger(JDBCConnection.class.getName());
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test";
    private static final String DB_USER = "rangga";
    private static final String DB_PASSWORD = "rangga";


    public static Connection getConnection() throws SQLException {
        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            return connection;
        }catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Driver MySQL tidak ditemukan", e);
            throw new SQLException("Driver MySQL tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal terhubung ke database", e);
            throw e;
        }
    }
}

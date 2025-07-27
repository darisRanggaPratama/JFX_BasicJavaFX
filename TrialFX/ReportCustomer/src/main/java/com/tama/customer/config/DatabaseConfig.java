package com.tama.customer.config;

import java.sql.Connection;
import java.sql.SQLException;
import com.mysql.cj.jdbc.MysqlDataSource;

public class DatabaseConfig {
    private static DatabaseConfig instance;
    private final MysqlDataSource dataSource;

    private DatabaseConfig() {
        dataSource = new MysqlDataSource();
    }

    public static DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    public void configure(String server, String database, int port, String user, String password) throws SQLException {
        dataSource.setServerName(server);
        dataSource.setDatabaseName(database);
        dataSource.setPort(port);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        // Set additional properties
        dataSource.setUseSSL(false);
        dataSource.setAllowPublicKeyRetrieval(true);
        dataSource.setServerTimezone("UTC");
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}

package com.tama.basicjdbc.dao;

import com.tama.basicjdbc.model.Users;
import com.tama.basicjdbc.utility.JDBCConnection;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javafx.collections.FXCollections.observableArrayList;

public class UserDAO implements DAOInterface<Users> {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    @Override
    public int addData(Users data) {
        String query = "INSERT INTO users (name, address, phone) VALUES (?, ?, ?)";

        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement preStatement = connection.prepareStatement(query)) {
            preStatement.setString(1, data.getName());
            preStatement.setString(2, data.getAddress());
            preStatement.setString(3, data.getPhone());

            return preStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal menambahkan data", e);
            return -1;
        }
    }

    @Override
    public int updateData(Users data) {
        String query = "UPDATE users SET name = ?, address = ?, phone = ? WHERE id = ?";

        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement preStatement = connection.prepareStatement(query)) {
            preStatement.setString(1, data.getName());
            preStatement.setString(2, data.getAddress());
            preStatement.setString(3, data.getPhone());
            preStatement.setInt(4, data.getId());

            return preStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal memperbarui data", e);
            return -1;
        }
    }

    @Override
    public int deleteData(Users data) {
        String query = "DELETE FROM users WHERE id = ?";

        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement preStatement = connection.prepareStatement(query)) {
            preStatement.setInt(1, data.getId());
            return preStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal menghapus data", e);
            return -1;
        }
    }

    @Override
    public ObservableList<Users> showData() {
        ObservableList<Users> userList = observableArrayList();
        String query = "SELECT * FROM users;";

        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement preStatement = connection.prepareStatement(query);
                ResultSet resultSet = preStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");

                Users users = new Users(id, name, address, phone);
                userList.add(users);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal menampilkan data", e);
        }
        return userList;
    }
}

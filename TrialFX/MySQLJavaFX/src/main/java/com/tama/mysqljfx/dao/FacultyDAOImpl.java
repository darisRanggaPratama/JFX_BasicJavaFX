package com.tama.mysqljfx.dao;

import com.tama.mysqljfx.entity.Faculty;
import com.tama.mysqljfx.util.DAOService;
import com.tama.mysqljfx.util.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FacultyDAOImpl implements DAOService<Faculty> {

    @Override
    public List<Faculty> fetchAll() throws SQLException, ClassNotFoundException {
        List<Faculty> faculties = new ArrayList<>();
        try (Connection connection = MySQLConnection.createConnection()) {
            String query = "SELECT id, name FROM faculty";
            try (PreparedStatement preState = connection.prepareStatement(query)) {
                try (ResultSet result = preState.executeQuery()) {
                    while (result.next()) {
                        Faculty faculty = new Faculty();
                        faculty.setId(result.getInt("id"));
                        faculty.setName(result.getString("name"));
                        faculties.add(faculty);
                    }
                }
            }
        }
        return faculties;
    }

    @Override
    public int addData(Faculty object) throws SQLException, ClassNotFoundException {
        int result = 0;
        try (Connection connection = MySQLConnection.createConnection()) {
            String query = "INSERT INTO faculty (name) VALUES (?)";
            try (PreparedStatement preState = connection.prepareStatement(query)) {
                preState.setString(1, object.getName());
                if (preState.executeUpdate() != 0) {
                    connection.commit();
                    result = 1;
                } else {
                    connection.rollback();
                }
            }
        }
        return result;
    }

    public int updateData(Faculty faculty) throws SQLException, ClassNotFoundException {
        int result = 0;
        try (Connection connection = MySQLConnection.createConnection()) {
            String query = "UPDATE faculty SET name = ? WHERE id = ?";
            try (PreparedStatement preState = connection.prepareStatement(query)) {
                preState.setString(1, faculty.getName());
                preState.setInt(2, faculty.getId());
                if (preState.executeUpdate() != 0) {
                    connection.commit();
                    result = 1;
                } else {
                    connection.rollback();
                }
            }
        }
        return result;
    }

    public int deleteData(Faculty faculty) throws SQLException, ClassNotFoundException {
        int result = 0;
        try (Connection connection = MySQLConnection.createConnection()) {
            String query = "DELETE FROM faculty WHERE id = ?";
            try (PreparedStatement preState = connection.prepareStatement(query)) {
                preState.setInt(1, faculty.getId());
                if (preState.executeUpdate() != 0) {
                    connection.commit();
                    result = 1;
                } else {
                    connection.rollback();
                }
            }
        }
        return result;
    }
}

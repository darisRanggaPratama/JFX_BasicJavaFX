package com.tama.mysqljfx.dao;

import com.tama.mysqljfx.entity.Department;
import com.tama.mysqljfx.entity.Faculty;
import com.tama.mysqljfx.util.DAOService;
import com.tama.mysqljfx.util.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAOImpl implements DAOService<Department> {
    @Override
    public List<Department> fetchAll() throws SQLException, ClassNotFoundException {
        List<Department> departments = new ArrayList<>();
        try (Connection connection = MySQLConnection.createConnection()) {
            String query = "SELECT dep.id, dep.name, dep.faculty_id, fac.name AS faculty_name FROM department dep JOIN faculty fac ON dep.faculty_id = fac.id";
            try (PreparedStatement preState = connection.prepareStatement(query)) {
                try (ResultSet result = preState.executeQuery()) {
                    while (result.next()) {
                        Faculty faculty = new Faculty();
                        faculty.setId(result.getInt("faculty_id"));
                        faculty.setName(result.getString("faculty_name"));

                        Department department = new Department();
                        department.setId(result.getInt("id"));
                        department.setName(result.getString("name"));
                        department.setFaculty(faculty);
                        departments.add(department);
                    }
                }
            }
        }
        return departments;
    }

    @Override
    public int addData(Department object) throws SQLException, ClassNotFoundException {
        int result = 0;
        try (Connection connection = MySQLConnection.createConnection()) {
            String query = "INSERT INTO department (name, faculty_id) VALUES (?, ?)";
            try (PreparedStatement preState = connection.prepareStatement(query)) {
                preState.setString(1, object.getName());
                preState.setInt(2, object.getFaculty().getId());
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

    public int updateData(Department department) throws SQLException, ClassNotFoundException {
        int result = 0;
        try (Connection connection = MySQLConnection.createConnection()) {
            String query = "UPDATE department SET name = ?, faculty_id = ? WHERE id = ?";
            try (PreparedStatement preState = connection.prepareStatement(query)) {
                preState.setString(1, department.getName());
                preState.setInt(2, department.getFaculty().getId());
                preState.setInt(3, department.getId());
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

    public int deleteData(Department department) throws SQLException, ClassNotFoundException {
        int result = 0;
        try (Connection connection = MySQLConnection.createConnection()) {
            String query = "DELETE FROM department WHERE id = ?";
            try (PreparedStatement preState = connection.prepareStatement(query)) {
                preState.setInt(1, department.getId());
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

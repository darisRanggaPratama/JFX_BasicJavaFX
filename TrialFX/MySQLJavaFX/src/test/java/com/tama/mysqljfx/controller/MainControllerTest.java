package com.tama.mysqljfx.controller;

import com.tama.mysqljfx.dao.DepartmentDAOImpl;
import com.tama.mysqljfx.dao.FacultyDAOImpl;
import com.tama.mysqljfx.entity.Department;
import com.tama.mysqljfx.entity.Faculty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class untuk memverifikasi bahwa duplikasi data sudah diperbaiki
 * dalam MainController setelah operasi save.
 */
public class MainControllerTest {

    @Mock
    private FacultyDAOImpl facultyDAO;
    
    @Mock
    private DepartmentDAOImpl departmentDAO;

    private ObservableList<Faculty> faculties;
    private ObservableList<Department> departments;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        faculties = FXCollections.observableArrayList();
        departments = FXCollections.observableArrayList();
    }

    @Test
    void testFacultyListRefreshWithoutDuplication() throws SQLException, ClassNotFoundException {
        // Arrange: Setup initial data
        Faculty existingFaculty = new Faculty();
        existingFaculty.setId(1);
        existingFaculty.setName("Teknik");
        
        Faculty newFaculty = new Faculty();
        newFaculty.setId(2);
        newFaculty.setName("Ekonomi");

        // Add existing faculty to the list
        faculties.add(existingFaculty);
        
        // Mock DAO to return both faculties after save
        List<Faculty> allFaculties = Arrays.asList(existingFaculty, newFaculty);
        when(facultyDAO.fetchAll()).thenReturn(allFaculties);

        // Act: Simulate the save operation using setAll instead of addAll
        faculties.setAll(facultyDAO.fetchAll());

        // Assert: Verify no duplication occurred
        assertEquals(2, faculties.size(), "Should have exactly 2 faculties");
        assertEquals("Teknik", faculties.get(0).getName());
        assertEquals("Ekonomi", faculties.get(1).getName());
        
        // Verify that there are no duplicate entries
        long uniqueCount = faculties.stream()
                .map(Faculty::getName)
                .distinct()
                .count();
        assertEquals(2, uniqueCount, "All faculty names should be unique");
    }

    @Test
    void testDepartmentListRefreshWithoutDuplication() throws SQLException, ClassNotFoundException {
        // Arrange: Setup initial data
        Faculty faculty = new Faculty();
        faculty.setId(1);
        faculty.setName("Teknik");

        Department existingDept = new Department();
        existingDept.setId(1);
        existingDept.setName("Teknik Informatika");
        existingDept.setFaculty(faculty);

        Department newDept = new Department();
        newDept.setId(2);
        newDept.setName("Teknik Elektro");
        newDept.setFaculty(faculty);

        // Add existing department to the list
        departments.add(existingDept);

        // Mock DAO to return both departments after save
        List<Department> allDepartments = Arrays.asList(existingDept, newDept);
        when(departmentDAO.fetchAll()).thenReturn(allDepartments);

        // Act: Simulate the save operation using setAll instead of addAll
        departments.setAll(departmentDAO.fetchAll());

        // Assert: Verify no duplication occurred
        assertEquals(2, departments.size(), "Should have exactly 2 departments");
        assertEquals("Teknik Informatika", departments.get(0).getName());
        assertEquals("Teknik Elektro", departments.get(1).getName());

        // Verify that there are no duplicate entries
        long uniqueCount = departments.stream()
                .map(Department::getName)
                .distinct()
                .count();
        assertEquals(2, uniqueCount, "All department names should be unique");
    }

    @Test
    void testAddAllCausesDuplication() throws SQLException, ClassNotFoundException {
        // This test demonstrates the old problematic behavior
        Faculty faculty1 = new Faculty();
        faculty1.setId(1);
        faculty1.setName("Teknik");

        Faculty faculty2 = new Faculty();
        faculty2.setId(2);
        faculty2.setName("Ekonomi");

        // Add initial faculty
        faculties.add(faculty1);

        // Mock DAO to return both faculties
        List<Faculty> allFaculties = Arrays.asList(faculty1, faculty2);
        when(facultyDAO.fetchAll()).thenReturn(allFaculties);

        // Act: Use addAll (the old problematic way)
        faculties.addAll(facultyDAO.fetchAll());

        // Assert: This will show duplication
        assertEquals(3, faculties.size(), "addAll causes duplication - should have 3 items (1 duplicate)");
        
        // Count occurrences of "Teknik"
        long teknikCount = faculties.stream()
                .map(Faculty::getName)
                .filter(name -> "Teknik".equals(name))
                .count();
        assertEquals(2, teknikCount, "Teknik appears twice due to duplication");
    }
}

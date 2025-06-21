package com.tama.basicjfx.ComboList;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import static javafx.collections.FXCollections.observableArrayList;

public class StudentController {

    private ObservableList<Student> studentList;
    private ObservableMap<String, Student> studentMap;

    @FXML
    private ComboBox<Student> cBox;

    @FXML
    private ListView<Student> listView;

    @FXML
    private TableView<Student> table;

    @FXML
    private TableColumn<Student, String> nameColumn;

    @FXML
    private Button tester;

    public void buttonAction(ActionEvent event) {
        studentList.add(new Student("Rudeus Greyrat"));
        studentMap.put("444", new Student("George"));
        cBox.setItems(observableArrayList(studentMap.values()));
    }


    public void initialize() {
        // Initialize ComboBox with sample data
        // List: array, Map: Key-Value, Set: unique values

        studentMap = FXCollections.observableHashMap();
        studentMap.put("111", new Student("Oyen"));
        studentMap.put("222", new Student("Tama"));
        studentMap.put("333", new Student("Mujair"));

        studentList = observableArrayList(
                new Student("John"),
                new Student("Jane"),
                new Student("Doe")
        );

        studentList.addListener(new ListChangeListener<Student>() {

            @Override
            public void onChanged(Change<? extends Student> c) {
                System.out.println("Data changed in the list:");
            }
        });



        studentList.add(new Student("Jet lee"));
        studentList.remove(new Student("John"));

        cBox.setItems(observableArrayList(studentMap.values()));
        cBox.getSelectionModel().select(0);
        listView.setItems(studentList);
        table.setItems(studentList);
        nameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));

        // Select first item in ComboBox
        if (!studentList.isEmpty()) {
            cBox.getSelectionModel().selectFirst();
        }
    }
}

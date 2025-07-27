package com.tama.customer.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Customer {
    private final StringProperty nik;
    private final StringProperty name;
    private final ObjectProperty<LocalDate> born;
    private final BooleanProperty active;
    private final DoubleProperty salary;

    public Customer() {
        this(null, null, null, false, 0.0);
    }

    public Customer(String nik, String name, LocalDate born, boolean active, double salary) {
        this.nik = new SimpleStringProperty(nik);
        this.name = new SimpleStringProperty(name);
        this.born = new SimpleObjectProperty<>(born);
        this.active = new SimpleBooleanProperty(active);
        this.salary = new SimpleDoubleProperty(salary);
    }

    // NIK
    public String getNik() { return nik.get(); }
    public void setNik(String value) { nik.set(value); }
    public StringProperty nikProperty() { return nik; }

    // Name
    public String getName() { return name.get(); }
    public void setName(String value) { name.set(value); }
    public StringProperty nameProperty() { return name; }

    // Born
    public LocalDate getBorn() { return born.get(); }
    public void setBorn(LocalDate value) { born.set(value); }
    public ObjectProperty<LocalDate> bornProperty() { return born; }

    // Active
    public boolean isActive() { return active.get(); }
    public void setActive(boolean value) { active.set(value); }
    public BooleanProperty activeProperty() { return active; }

    // Salary
    public double getSalary() { return salary.get(); }
    public void setSalary(double value) { salary.set(value); }
    public DoubleProperty salaryProperty() { return salary; }
}

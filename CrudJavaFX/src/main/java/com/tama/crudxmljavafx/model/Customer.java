package com.tama.crudxmljavafx.model;

import java.time.LocalDate;

/**
 * Customer model class representing the customer table in database
 */
public class Customer {
    private Integer idx;
    private String nik;
    private String name;
    private LocalDate born;
    private Boolean active;
    private Integer salary;

    // Default constructor
    public Customer() {}

    // Constructor with all fields
    public Customer(Integer idx, String nik, String name, LocalDate born, Boolean active, Integer salary) {
        this.idx = idx;
        this.nik = nik;
        this.name = name;
        this.born = born;
        this.active = active;
        this.salary = salary;
    }

    // Constructor without idx (for new records)
    public Customer(String nik, String name, LocalDate born, Boolean active, Integer salary) {
        this.nik = nik;
        this.name = name;
        this.born = born;
        this.active = active;
        this.salary = salary;
    }

    // Getters and Setters
    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBorn() {
        return born;
    }

    public void setBorn(LocalDate born) {
        this.born = born;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    // Helper method for active status display
    public String getActiveStatus() {
        return active != null && active ? "Active" : "Inactive";
    }

    // Helper method for salary formatting
    public String getFormattedSalary() {
        return salary != null ? String.format("Rp %,d", salary) : "Rp 0";
    }

    @Override
    public String toString() {
        return "Customer{" +
                "idx=" + idx +
                ", nik='" + nik + '\'' +
                ", name='" + name + '\'' +
                ", born=" + born +
                ", active=" + active +
                ", salary=" + salary +
                '}';
    }
}

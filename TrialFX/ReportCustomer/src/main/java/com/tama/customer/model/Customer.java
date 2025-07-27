package com.tama.customer.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Customer {
    private int idx;
    private String nik;
    private String name;
    private LocalDate born;
    private boolean active;
    private BigDecimal salary;

    public Customer() {
    }

    public Customer(String nik, String name, LocalDate born, boolean active, BigDecimal salary) {
        this.nik = nik;
        this.name = name;
        this.born = born;
        this.active = active;
        this.salary = salary;
    }

    // Getters and Setters
    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}

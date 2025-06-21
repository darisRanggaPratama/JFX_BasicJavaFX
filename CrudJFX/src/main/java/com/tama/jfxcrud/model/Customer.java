package com.tama.jfxcrud.model;

import java.time.LocalDate;

public class Customer {
    private int idx;
    private String nik;
    private String name;
    private LocalDate born;
    private boolean active;
    private int salary;
    
    public Customer() {
    }
    
    public Customer(int idx, String nik, String name, LocalDate born, boolean active, int salary) {
        this.idx = idx;
        this.nik = nik;
        this.name = name;
        this.born = born;
        this.active = active;
        this.salary = salary;
    }
    
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
    
    public int getSalary() {
        return salary;
    }
    
    public void setSalary(int salary) {
        this.salary = salary;
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

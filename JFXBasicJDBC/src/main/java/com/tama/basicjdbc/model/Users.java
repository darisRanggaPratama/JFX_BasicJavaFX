package com.tama.basicjdbc.model;

public class Users {
    private int id;
    private String name;
    private String address;
    private String phone;

    public Users() {
        // Default constructor
    }

    public Users(int id, String name, String address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "id= " + id +
                ", name= '" + name + '\'' +
                "}";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

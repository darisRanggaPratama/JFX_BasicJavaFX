package com.tama.otodidakjfx;

import javafx.beans.property.*;

/**
 * Model class untuk data Person
 * Menggunakan JavaFX Properties untuk data binding
 */
public class Person {
    private final IntegerProperty id;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty email;
    private final IntegerProperty age;
    private final StringProperty city;

    public Person() {
        this(0, "", "", "", 0, "");
    }

    public Person(int id, String firstName, String lastName, String email, int age, String city) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.age = new SimpleIntegerProperty(age);
        this.city = new SimpleStringProperty(city);
    }

    // ID Property
    public IntegerProperty idProperty() {
        return id;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    // First Name Property
    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    // Last Name Property
    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    // Email Property
    public StringProperty emailProperty() {
        return email;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    // Age Property
    public IntegerProperty ageProperty() {
        return age;
    }

    public int getAge() {
        return age.get();
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    // City Property
    public StringProperty cityProperty() {
        return city;
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    // Full name property (computed property)
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    @Override
    public String toString() {
        return String.format("Person{id=%d, firstName='%s', lastName='%s', email='%s', age=%d, city='%s'}",
                getId(), getFirstName(), getLastName(), getEmail(), getAge(), getCity());
    }
}

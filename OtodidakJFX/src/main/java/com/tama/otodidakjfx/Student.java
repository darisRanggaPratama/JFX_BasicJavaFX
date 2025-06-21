package com.tama.otodidakjfx;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Model class untuk data Student
 * Digunakan untuk Jasper Reports dan form handling
 */
public class Student {
    private String nim;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String gender;
    private String faculty;
    private String major;
    private Integer entryYear;
    private Double gpa;
    private String address;
    private List<String> hobbies;
    private boolean agreementAccepted;
    private LocalDate registrationDate;

    // Default constructor
    public Student() {
        this.registrationDate = LocalDate.now();
    }

    // Constructor with all fields
    public Student(String nim, String firstName, String lastName, String email, String phone,
                   LocalDate birthDate, String gender, String faculty, String major,
                   Integer entryYear, Double gpa, String address, List<String> hobbies,
                   boolean agreementAccepted) {
        this.nim = nim;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.gender = gender;
        this.faculty = faculty;
        this.major = major;
        this.entryYear = entryYear;
        this.gpa = gpa;
        this.address = address;
        this.hobbies = hobbies;
        this.agreementAccepted = agreementAccepted;
        this.registrationDate = LocalDate.now();
    }

    // Getters and Setters
    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Integer getEntryYear() {
        return entryYear;
    }

    public void setEntryYear(Integer entryYear) {
        this.entryYear = entryYear;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public boolean isAgreementAccepted() {
        return agreementAccepted;
    }

    public void setAgreementAccepted(boolean agreementAccepted) {
        this.agreementAccepted = agreementAccepted;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    // Computed properties for Jasper Reports
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFormattedBirthDate() {
        return birthDate != null ? birthDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) : "";
    }

    public String getFormattedRegistrationDate() {
        return registrationDate != null ? registrationDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) : "";
    }

    public String getHobbiesString() {
        return hobbies != null ? String.join(", ", hobbies) : "";
    }

    public String getGpaString() {
        return gpa != null ? String.format("%.2f", gpa) : "0.00";
    }

    public String getAgreementStatus() {
        return agreementAccepted ? "Ya" : "Tidak";
    }

    public int getAge() {
        if (birthDate != null) {
            return LocalDate.now().getYear() - birthDate.getYear();
        }
        return 0;
    }

    @Override
    public String toString() {
        return String.format("Student{nim='%s', fullName='%s', faculty='%s', major='%s'}",
                nim, getFullName(), faculty, major);
    }
}

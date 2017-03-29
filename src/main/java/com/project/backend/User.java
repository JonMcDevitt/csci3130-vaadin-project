package com.project.backend;

// Backend DTO class. This is just a typical Java backend implementation
// class and nothing Vaadin specific.

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String department;
    @OneToMany
    private List<Course> courses; /** A list of course IDs to use for fetching from the database  */

    public User(String email, String password, String firstName,
                String lastName, String department) {
    	this.email = email;
    	this.password = password;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.department = department;
    	this.courses = new ArrayList<>();
    }

    public User() {
    }

    public void initCourses() {
        courses = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void addCourse(String courseCode, String courseSection, String course) {

    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Course> getCourses() {
        return courses;
    }

    @Override
    public String toString() {
        return "User{firstName=" + firstName
                + ", lastName=" + lastName + ", department= " + department + ", email="
                + email + '}';
    }
}
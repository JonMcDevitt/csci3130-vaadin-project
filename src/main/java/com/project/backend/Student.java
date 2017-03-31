package com.project.backend;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Jili on 2017-03-01
 *
 * Temporary Student class for creating Student objects for "Go to class" function test
 */

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Student {
    @Id
    private String studentId;
    @NotNull
    @Size(min=2, max=64)
    private String barcode, firstName, lastName;
    @ManyToMany(mappedBy="studentRoster")
    private List<Course> courseList;

    public Student(String id, String barcode, String firstName, String lastName) {
        this.studentId = id;
        this.barcode = barcode;
        this.firstName = firstName;
        this.lastName = lastName;
        courseList = new ArrayList<>();
    }

    public String getBarcode() {
        return barcode;
    }
    public void courseListInit(){
        courseList = new ArrayList<>();
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Student() {
    }

    public String getId() {
        return studentId;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }
    public List<Course> getCourseList(){
        return courseList;
    }

    public void setId(String id) {
        this.studentId =  id;
    }
    public void addCourse(Course newCourse){
        courseList.add(newCourse);
    }

    public void setFirstName(String firstname) {
        this.firstName = firstname;
    }

    public void setLastName (String lastname) {
        this.lastName = lastname;
    }
    @Override
    public String toString() {
        return "User{firstName=" + firstName
                + ", lastName=" + lastName + ", id=" + studentId +'}';
    }
}


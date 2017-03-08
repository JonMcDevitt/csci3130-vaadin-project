package com.project.backend;

/*
 * Created by Jili on 2017-03-01
 * 
 * Temporary Student class for creating Student objects for "Go to class" function test
 */

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Student {
    @Id
    private String id;
    private String firstName;
    private String lastName;

    Student(String id, String firstName, String lastName) {
        this.id = id;
    	this.firstName = firstName;
    	this.lastName = lastName;
    }

    public Student() {
    }

    String getId() {
        return id;
    }
    
    public String getFirstName(){
    	return firstName;
    }
    
    public String getLastName(){
    	return lastName;
    }
    
    @Override
    public String toString() {
        return "RegisteredUser{firstName=" + firstName
                + ", lastName=" + lastName + '}';
    }
}


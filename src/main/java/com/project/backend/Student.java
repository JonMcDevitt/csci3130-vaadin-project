package com.project.backend;

/*
 * Created by Jili on 2017-03-01
 * 
 * Temporary Student class for creating Student objects for "Go to class" function test
 */

public class Student {
    private final String id;
    private final String firstName;
    private final String lastName;

    Student(String id, String firstName, String lastName) {
        this.id = id;
    	this.firstName = firstName;
    	this.lastName = lastName;
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


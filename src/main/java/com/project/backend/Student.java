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
    private String barcode;
    private String firstName;
    private String lastName;

    Student(String id, String barcode, String firstName, String lastName) {
        this.id = id;
        this.barcode = barcode;
    	this.firstName = firstName;
    	this.lastName = lastName;
    }

    public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Student() {
    }

    public String getId() {
        return id;
    }
    
    public String getFirstName(){
    	return firstName;
    }
    
    public String getLastName(){
    	return lastName;
    }
    
    public void setId(String id) {
    	this.id =  id;
    }
    
    public void setFirstName(String firstname) {
    	this.firstName = firstname;
    }
    
    public void setLastName (String lastname) {
    	this.lastName = lastname;
    }
    @Override
    public String toString() {
        return "RegisteredUser{firstName=" + firstName
                + ", lastName=" + lastName + ", id=" + id +'}';
    }
}


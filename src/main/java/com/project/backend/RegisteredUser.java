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
public class RegisteredUser{

    @Id
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String department;
    @OneToMany
    private List<Course> courses; /** A list of course IDs to use for fetching from the database  */

    public RegisteredUser(String email, String password, String firstName,
    						String lastName, String department) {
    	this.email = email;
    	this.password = password;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.department = department;
    	this.courses = new ArrayList<>();
    }

    public RegisteredUser() {
    }

    //check if this RegisteredUser object has the same user name or email address as the other RegisteredUser object in the database
    //This is used to validate if the user name or the email has been used before when sign up for a new user
    public boolean isValid(RegisteredUserDatabase userDatabase) {
    	for (RegisteredUser r : userDatabase.getUserList()) {
    		if (r.email.equals(this.email)){
    			return false;
    		}
    	}
    	return true;
    }

    //check if required textfields are filled
    public boolean isValidUser() {
        for (Field f : this.getClass().getDeclaredFields()) {
            if (f.getType() == String.class) {
                try {
                    String value = (String) f.get(this);
                    if (value.isEmpty()) {
                        return false;
                    }
                } catch (IllegalAccessException e) {
                    System.err.println(e);
                    return false;
                }
            }
        }        
        return true;
    }

    String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void addCourse(String courseCode, String courseSection, String course) {

    }

    @Override
    public String toString() {
        return "RegisteredUser{firstName=" + firstName
                + ", lastName=" + lastName + ", department= " + department + ", email="
                + email + '}';
    }
}
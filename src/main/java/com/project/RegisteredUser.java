package com.project;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.apache.commons.beanutils.BeanUtils;

// Backend DTO class. This is just a typical Java backend implementation
// class and nothing Vaadin specific.

public class RegisteredUser implements Serializable, Cloneable {

    private final String userName;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String department;
    
    public RegisteredUser(String userName, String email, String password, String firstName,
    						String lastName, String department) {
    	this.userName = userName;
    	this.email = email;
    	this.password = password;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.department = department;
    }
    
    //check if this RegisteredUser object has the same user name or email address as the other RegisteredUser object in the database
    //This is used to validate if the user name or the email has been used before when sign up for a new user
    public boolean isValid(RegisteredUserDatabase userDatabase) {
    	for (RegisteredUser r : userDatabase.getUserList()) {
    		if (r.userName.equals(this.userName) || r.email.equals(this.email)){
    			return false;
    		}
    	}
    	return true;
    }

    //check if required textfields are filled
    public boolean isValidUser() {
        for (Field f : getClass().getFields()) {
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
    
    @Override
    public RegisteredUser clone() throws CloneNotSupportedException {
        try {
            return (RegisteredUser) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }

    @Override
    public String toString() {
        return "RegisteredUser{firstName=" + firstName
                + ", lastName=" + lastName + ", department= " + department + ", userName=" + userName + ", email="
                + email + '}';
    }
}


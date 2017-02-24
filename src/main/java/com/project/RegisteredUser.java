package com.project;

import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;

/**
 * A simple DTO for the address book example.
 *
 * Serializable and cloneable Java Object that are typically persisted
 * in the database and can also be easily converted to different formats like JSON.
 */
// Backend DTO class. This is just a typical Java backend implementation
// class and nothing Vaadin specific.
public class RegisteredUser implements Serializable, Cloneable {

    private String userName = "";
    private String email = "";
    private String password = "";
    private String validationPassword = "";
    private String firstName = "";
    private String lastName = "";
    private String department = "";
    
    public RegisteredUser(String userName, String email, String password, String validationPassword, String firstName,
    						String lastName, String department){
    	this.userName = userName;
    	this.email = email;
    	this.password = password;
    	this.validationPassword = validationPassword;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.department = department;
    }    
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getValidationPassword() {
        return validationPassword;
    }

    public void setValidationPassword(String validationPassword) {
        this.validationPassword = validationPassword;
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
    
    //check if this RegisteredUser object has the same user name or email address as the other RegisteredUser object in the database
    //This is used to validate if the user name or the email has been used before when sign up for a new user
    public boolean isValid(RegisteredUserDatabase userDatabase){
    	for (RegisteredUser r : userDatabase.getUserList()){
    		if(r.getUserName().equals(this.userName) || r.getEmail().equals(this.email)){
    			return false;
    		}
    	}
    	return true;
    }
    
    //check if the password is the same as the validationPassword
    public boolean passwordIsValid() {
    	return this.password.equals(this.validationPassword);
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


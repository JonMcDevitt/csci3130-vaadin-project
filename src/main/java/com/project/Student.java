package com.project;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.apache.commons.beanutils.BeanUtils;

/*
 * Created by Jili on 2017-03-01
 * 
 * Temporary Student class for creating Student objects for "Go to class" function test
 */

public class Student implements Serializable, Cloneable {

    private final String firstName;
    private final String lastName;

    public Student(String firstName, String lastName) {

    	this.firstName = firstName;
    	this.lastName = lastName;
    }
    
    public String getFirstName(){
    	return firstName;
    }
    
    public String getLastName(){
    	return lastName;
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
                + ", lastName=" + lastName + '}';
    }
}


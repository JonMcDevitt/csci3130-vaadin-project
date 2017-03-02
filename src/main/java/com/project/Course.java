package com.project;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.beanutils.BeanUtils;

/*
 * Created by Jili on 2017-03-01
 * Temporary Course class for creating Course objects for "Go to class" function test
 */

public class Course implements Serializable, Cloneable {

    private final String courseName;
    private final ArrayList<Student> studentRoster;

    public Course(String courseName) {

    	this.courseName = courseName;
    	
    	//create a test studentRoster
    	studentRoster = new ArrayList<>();   	
    	studentRoster.add(new Student("TestFN1", "TestLN1"));
    	studentRoster.add(new Student("TestFN2", "TestLN2"));
    }
    
    public String getCourseName(){
    	return courseName;
    }
    
    public ArrayList<Student> getStudentRoster(){
    	return studentRoster;
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
        return "RegisteredUser{courseName=" + courseName + '}';
    }
}


package com.project;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Jili on 2017-03-01
 * Temporary Course class for creating Course objects for "Go to class" function test
 */

public class Course{

    private final String courseName;
    private final List<Student> studentRoster;

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
    
    public List<Student> getStudentRoster(){
    	return studentRoster;
    }

    @Override
    public String toString() {
        return "RegisteredUser{courseName=" + courseName + '}';
    }
}


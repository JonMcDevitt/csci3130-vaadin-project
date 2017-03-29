package com.project.backend;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
 * Created by Jili on 2017-03-01
 * Temporary Course class for creating Course objects for "Go to class" function test
 */

@Entity
public class Course implements Comparable<Course>{

    @Id
    private String courseCode;
    private String courseName;
    @ManyToMany
    @JoinTable(name="enroll", joinColumns={
            @JoinColumn(name="Course_ID")
            }
        , inverseJoinColumns={
            @JoinColumn(name="Student_ID")
            })
    private List<Student> studentRoster;
    @OneToMany
    private List<ClassDay> classDays;
    
    public Course(String courseName, String courseCode, List<String> classInfo) {
        this();
        setCourseName(courseName);
        setCourseCode(courseCode);

        //create a test studentRoster
        studentRoster = new ArrayList<>();
        studentRoster.add(new Student("B00123456", "21264084453726", "TestFN1", "TestLN1"));
        studentRoster.add(new Student("B00987654", "234567898765432", "TestFN2", "TestLN2"));
        LocalDate today = LocalDate.of(2017, 3, 15);
        
        
        classDays = new ArrayList<>();

        /** TODO:   parse classInfo to construct the ClassDay object (create Dates)
         * */
    }

    public List<ClassDay> getClassDays() {
		return classDays;
	}

	public Course() {
    }

    public void initLists() {
        studentRoster = new ArrayList<>();
        classDays = new ArrayList<>();
    }

    public String getCourseName(){
    	return courseName;
    }
    
    public List<Student> getStudentRoster(){
    	return studentRoster;
    }

    public String getCourseCode() {
        return courseCode;
    }

    
    public void addStudent(Student student){
    	studentRoster.add(student);
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode.replaceAll(" ", "_");
    }


    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return "Course{courseName=" + courseName + '}';
    }

    @Override
    public int compareTo(Course c) {
        if(courseCode.equals(c.getCourseCode())) {
            return 0;
        }
        return courseCode.compareTo(c.getCourseCode());
    }
}


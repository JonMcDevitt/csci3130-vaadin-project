package com.project.backend;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by Jili on 2017-03-01
 * Temporary Course class for creating Course objects for "Go to class" function test
 */

@Entity
public class Course implements Comparable<Course> {

    @Id
    private String courseCode;
    private String courseName;
    @ManyToMany
    @JoinTable(
            name = "enroll", joinColumns = {
            @JoinColumn(name = "Course_ID")
    }, inverseJoinColumns = {
            @JoinColumn(name = "Student_ID")
    })
    private List<Student> studentRoster;
    @OneToMany
    private List<AttendanceTable> attendance;

    public Course(String courseName, String courseCode) {
        this();
        setCourseName(courseName);
        setCourseCode(courseCode);

        //create a test studentRoster
        studentRoster = new ArrayList<>();
        attendance = new ArrayList<>();
    }

    public List<AttendanceTable> getAttendance() {
        return attendance;
    }

    public Course() {
    }

    public void initLists() {
        studentRoster = new ArrayList<>();
        attendance = new ArrayList<>();
    }

    public String getCourseName() {
        return courseName;
    }

    public List<Student> getStudentRoster() {
        return studentRoster;
    }

    public void addAttendanceTable(AttendanceTable at) {
        attendance.add(at);
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void addStudent(Student student) {
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
        return courseCode.compareTo(c.getCourseCode());
    }
}


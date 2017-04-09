package com.project.backend;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

/**
 * Created by Owner on 2017-03-10.
 */
public class CourseTest {
    private Course course;

    @Before
    public void setUp() {
        course = DatabaseHandler.getInstance().addCourse("Software Engineering","CSCI3130");
        assertNotNull(course);
    }

    @Test
    public void addStudent() {
        int currSize = course.getStudentRoster().size();
        Student s = new Student("B00123456", "12345678987654321", "Test", "McGee");
        course.addStudent(s);
        assertTrue(currSize < course.getStudentRoster().size());
    }

    @Test
    public void addAttendanceTable() {
        int currSize = course.getAttendance().size();
        course.addAttendanceTable(new AttendanceTable());
        assertTrue(currSize < course.getAttendance().size());
    }

    @Test
    public void initLists() {
        Course c = new Course();
        c.initLists();
        assertNotNull(c.getStudentRoster());
        assertNotNull(c.getAttendance());
    }

    @Test
    public void compareTo() throws Exception {
        /** Test isLess */
        Course isLess = DatabaseHandler.getInstance().addCourse("Software Engineering", "CSCI3130");
        Course isMore = DatabaseHandler.getInstance().addCourse("Software Engineering", "CSCI 3130");
        assertTrue(isLess.compareTo(isMore) != 0);
    }
}
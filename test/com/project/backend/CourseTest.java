package com.project.backend;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Owner on 2017-03-10.
 */
public class CourseTest {
    private Course course;

    @Before
    public void setUp() {
        course = DatabaseHandler.addCourse("Software Engineering","CSCI 3130", "02");
        assertNotNull(course);
    }

    @Test
    public void compareTo() throws Exception {
        /** Test isLess */
        Course isLess = DatabaseHandler.addCourse("Software Engineering","CSCI 3130", "01");

        /** Test isMore */
        Course isMore = DatabaseHandler.addCourse("Software Engineering","CSCI 3130", "03");

        /** Test notEqual   */
        Course oOpSysOne = DatabaseHandler.addCourse("Operating Systems","CSCI 3120", "01");
        Course oOpSysTwo = DatabaseHandler.addCourse("Operating Systems", "CSCI 3120", "02");

        assertEquals(course.compareTo(isLess), 1);
        assertEquals(course.compareTo(isMore), -1);
        assertNotEquals(course.compareTo(oOpSysOne), 0);
        assertEquals(oOpSysOne.compareTo(oOpSysTwo), -1);
    }
}
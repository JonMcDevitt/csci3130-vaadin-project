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
        course = new Course("Software Engineering","CSCI 3130", "02");
        assertNotNull(course);
    }

    @Test
    public void compareTo() throws Exception {
        Course isEqual = new Course("Software Engineering","CSCI 3130", "02");
        Course isLess = new Course("Software Engineering","CSCI 3130", "01");
        Course isMore = new Course("Software Engineering","CSCI 3130", "03");
        Course oOpSysOne = new Course("Operating Systems","CSCI 3120", "01");
        Course oOpSysTwo = new Course("Operating Systems", "CSCI 3120", "02");

        assertEquals(course.compareTo(isEqual), 0);
        assertEquals(course.compareTo(isLess), 1);
        assertEquals(course.compareTo(isMore), -1);
        assertNotEquals(course.compareTo(oOpSysOne), 0);
        assertEquals(oOpSysOne.compareTo(oOpSysTwo), -1);
    }

}
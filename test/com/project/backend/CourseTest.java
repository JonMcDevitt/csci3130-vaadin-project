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
        course = DatabaseHandler.addCourse("Software Engineering","CSCI3130");
        assertNotNull(course);
    }

    @Test
    public void compareTo() throws Exception {
        /** Test isLess */
        Course isLess = DatabaseHandler.addCourse("Software Engineering","CSCI3130");
        DatabaseHandler.addStudent("CSCI3130", "B00630312", "Taylor", "Lundy", "3456789876543");
        DatabaseHandler.addStudent("CSCI3130", "B00630315", "Tyler", "Bundy", "34345789876543");
        List<Student> printList = DatabaseHandler.getCourseStudents("CSCI3130");
        System.out.println(printList);
        /** Test isMore */

        /** Test notEqual   */
        Course oOpSysOne = DatabaseHandler.addCourse("Operating Systems","CSCI 3120");

      //  assertEquals(course.compareTo(isLess), 1);
     //   assertEquals(course.compareTo(isMore), -1);
    //    assertNotEquals(course.compareTo(oOpSysOne), 0);
        //assertEquals(oOpSysOne.compareTo(oOpSysTwo), -1);
    }
}
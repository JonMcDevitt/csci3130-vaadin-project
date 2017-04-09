package com.project.backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Owner on 4/7/2017.
 */
public class StudentTest {
    private Student test;

    @Before
    public void setUp() throws Exception {
        DatabaseHandler.getInstance().addCourse("TestCourse 1", "TEST 0001");
        test = DatabaseHandler.getInstance().addStudent("TEST 0001", "B00123456", "Test", "McGee", "12345678987654321");
        assertNotNull(test);
    }

    @After
    public void tearDown() throws Exception {
        DatabaseHandler.getInstance().removeStudent("TEST 0001", "B00123456");
        DatabaseHandler.getInstance().removeCourse(DatabaseHandler.getInstance().getCourseById("TEST 0001"));
    }

    @Test
    public void getBarcode() throws Exception {
        assertNotNull(test);
        assertNotNull(test.getBarcode());
    }

    @Test
    public void courseListInit() throws Exception {
        test.courseListInit();
        assertNotNull(test.getCourseList());
    }

    @Test
    public void setBarcode() throws Exception {
        String barcode = test.getBarcode();
        assertNotNull(barcode);
        test.setBarcode("98765432123456789");
        assertNotEquals(test.getBarcode(), barcode);
    }

    @Test
    public void getId() throws Exception {
        assertNotNull(test.getId());
    }

    @Test
    public void getFirstName() throws Exception {
        assertNotNull(test.getFirstName());
    }

    @Test
    public void getLastName() throws Exception {
        assertNotNull(test.getLastName());
    }

    @Test
    public void getCourseList() throws Exception {
        assertNotNull(test.getCourseList());
        assertTrue(test.getCourseList().size() == 1);
    }
}
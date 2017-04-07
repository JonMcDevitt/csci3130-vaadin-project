package com.project.backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Created by Owner on 4/7/2017.
 */
public class AttendanceTableTest {
    private AttendanceTable table;
    private Course course;
    private Student student;
    private AttendanceRecord record;

    @Before
    public void setUp() {
        course = DatabaseHandler.addCourse("TestCourse", "TEST_0001");
        assertNotNull(course);

        student = new Student("B00123456", "12345678987654321", "Test", "McGee");
        assertNotNull(student);
        course.addStudent(student);

        table = new AttendanceTable();
        table.setDate(LocalDateTime.now());
        assertNotNull(table);

        record = new AttendanceRecord();
        record.setStudent(student);
        record.setCourse(course);
        record.setAttendanceID();
        assertEquals(record.getStudent(), student);
        assertEquals(record.getCourse(), course);
        assertEquals(record.getStatus(), AttendanceRecord.Status.ABSENT);

        table.addAttendanceRecord(record);
        assertNotNull(table.getRecordById(course, student));
        course.addAttendanceTable(table);
    }

    @Test
    public void getRecords() throws Exception {
        assertNotNull(table.getRecords());
        assertFalse(table.getRecords().isEmpty());
    }

    @Test
    public void getDate() throws Exception {
        assertNotEquals(table.getDate(), LocalDateTime.now());
    }
}
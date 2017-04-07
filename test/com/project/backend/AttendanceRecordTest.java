package com.project.backend;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Owner on 4/7/2017.
 */
public class AttendanceRecordTest {
    private AttendanceRecord record;

    @Before
    public void setUp() {
        record = new AttendanceRecord();
    }

    @Test
    public void getStudent() throws Exception {
        Student s = new Student("B00123456", "12345678987654321", "Test", "McGee");
        record.setStudent(s);
        assertNotNull(record.getStudent());
    }

    @Test
    public void getStatus() throws Exception {
        assertEquals(record.getStatus(), AttendanceRecord.Status.ABSENT);
        record.setStatus(AttendanceRecord.Status.PRESENT);
        assertNotEquals(record.getStatus(), AttendanceRecord.Status.EXCUSED);
        record.setStatus(AttendanceRecord.Status.EXCUSED);
        assertEquals(record.getStatus(), AttendanceRecord.Status.EXCUSED);
    }

    @Test
    public void getStudentId() throws Exception {
        Student s = new Student("B00123456", "12345678987654321", "Test", "McGee");
        record.setStudent(s);
        assertEquals("B00123456", record.getStudentId());
    }

}
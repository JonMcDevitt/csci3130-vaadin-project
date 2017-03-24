package com.project.backend;

import org.junit.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
/**
 * Created by Owner on 2017-03-07.
 */
public class ClassDayTest {
    private ClassDay classDay;

    @Before
    public void setUp() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(new Student("B00123123", "5667887678", "Bob", "Barker"));
        students.add(new Student("B00987987","87565789", "Alica", "Blah"));
        classDay = new ClassDay(
                new Date(2017, 03, 07, 12, 30),
                new Date(2017, 03, 07, 13, 30),
                students
        );

        assertNotNull(classDay);
        assertNotNull(classDay.getAbsentStudents());
        assertTrue(classDay.getAttendingStudents().isEmpty());
        assertNotNull(classDay.getStartTime());
        assertNotNull(classDay.getEndTime());
        assertTrue(classDay.getStartTime().before(classDay.getEndTime()));
        assertFalse(classDay.isCancelled());
    }

    @After
    public void tearDown() throws Exception {
        classDay = null;
    }

    @Test
    public void studentScanned() throws Exception {
        int absentSize = classDay.getAbsentStudents().size();
        int attendingSize = classDay.getAttendingStudents().size();

        classDay.studentScanned("B00987987");
        assertEquals(absentSize-1, classDay.getAbsentStudents().size());
        assertEquals(attendingSize+1, classDay.getAttendingStudents().size());
    }

    @Test
    public void modifyCancellationStatus() throws Exception {
        assertFalse(classDay.isCancelled());
        classDay.modifyCancellationStatus(true);
        assertTrue(classDay.isCancelled());
    }
}
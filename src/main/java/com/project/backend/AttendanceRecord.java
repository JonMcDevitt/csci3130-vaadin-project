package com.project.backend;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToOne;

@Entity
public class AttendanceRecord {

    @Id
    private String attendanceID;
    @OneToOne
    private Course course;
    @OneToOne
    private Student student;
    private Status status;

    public enum Status {
        PRESENT, ABSENT, EXCUSED;

        public String toString() {
            return name();
        }
    }

    public AttendanceRecord() {
        status = Status.ABSENT;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public void setAttendanceID() {
        attendanceID = course.getCourseCode() + student.getId();
    }

    public String getAttendanceID() {
        return attendanceID;
    }

    public String getStudentId() {
        return student.getId();
    }
}


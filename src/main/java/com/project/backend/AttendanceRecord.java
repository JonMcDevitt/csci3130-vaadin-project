package com.project.backend;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(AttendanceKey.class)
public class AttendanceRecord {
    
    @Id
    private String courseCode, studentId;
    
    private Date timestamp;
    
    public void stampDate() {
        timestamp = new Date();
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Date getTimestamp() {
        return timestamp;
    }
    
}

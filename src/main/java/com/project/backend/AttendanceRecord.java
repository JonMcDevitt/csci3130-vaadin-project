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
    private LocalDateTime timestamp;
   private Status status;
    public enum Status {
        PRESENT, ABSENT;

        public String toString() {
            return name();
        }
    }
    public AttendanceRecord(){
    	status = Status.ABSENT;
    }
    
    
    public void stampDate() {
        timestamp = LocalDateTime.now();
        timestamp = timestamp.truncatedTo(ChronoUnit.DAYS);
    }
    public Status getStatus(){
    		return status;
    }
    public void setStatus(Status status){
    	this.status=status;
    }
    
    public Course getCourseCode() {
        return course;
    }
    

    public void setCourseCode(Course course) {
        this.course = course;
    }
    public void setAttendanceID(){
    	attendanceID=course.getCourseCode()+student.getId();
    }
    public String getAttendanceID(){
    	return attendanceID;
    }
    public Student getStudentId() {
        return student;
    }

    public void setStudentId(Student student) {
        this.student = student;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
}


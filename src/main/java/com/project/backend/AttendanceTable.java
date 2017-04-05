package com.project.backend;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AttendanceTable {
    private List<AttendanceRecord> records;
    @Id
    private LocalDateTime date;

    public AttendanceTable() {
        records = new ArrayList<>();
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public AttendanceRecord getRecordById(Course course, Student student) {
        for (AttendanceRecord record : records) {
            if (record.getAttendanceID().equals(course.getCourseCode() + student.getId())) {
                return record;
            }
        }
        return null;
    }

    public List<AttendanceRecord> getRecords() {
        return records;
    }

    public void setRecords(List<AttendanceRecord> records) {
        this.records = records;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void addAttendanceRecord(AttendanceRecord ar) {
        records.add(ar);
    }


}

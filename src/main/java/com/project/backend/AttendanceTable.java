package com.project.backend;

import java.time.LocalDateTime;
import java.util.List;

public class AttendanceTable {
    private List<AttendanceRecord> records;
    private LocalDateTime date;
    public AttendanceTable(){}
    public void setDate(LocalDateTime date){
    	this.date=date;
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

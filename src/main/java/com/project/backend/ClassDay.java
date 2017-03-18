package com.project.backend;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Owner on 2017-03-06.
 */
@Entity
public class ClassDay {
    @Id
    private Date startTime, endTime;
    @OneToMany
    private List<Student> attendingStudents, absentStudents;
    private boolean cancellation;

    public ClassDay(Date startTime, Date endTime, List<Student> studentsInCourse) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.absentStudents = new ArrayList<>(studentsInCourse);
        this.attendingStudents = new ArrayList<>();
        this.cancellation = false;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public List<Student> getAttendingStudents() {
        return attendingStudents;
    }

    public List<Student> getAbsentStudents() {
        return absentStudents;
    }

    public void studentScanned(String studentId) {
        for(int i = 0; i < absentStudents.size(); i++) {
            if(studentId.equals(absentStudents.get(i).getId())) {
                attendingStudents.add(absentStudents.remove(i));
                break;
            }
        }
    }

    public boolean isCancelled() {
        return cancellation;
    }

    public void modifyCancellationStatus(boolean status) {
        cancellation = status;
    }
    
}

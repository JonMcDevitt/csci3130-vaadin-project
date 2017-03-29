package com.project.backend;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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

    public ClassDay() {
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
        absentStudents.stream().filter(s -> s.getId().equals(studentId)).findAny().ifPresent(s -> {
            attendingStudents.add(s);
            absentStudents.remove(s);
        });
    }

    public boolean isCancelled() {
        return cancellation;
    }

    public void modifyCancellationStatus(boolean status) {
        cancellation = status;
    }
}

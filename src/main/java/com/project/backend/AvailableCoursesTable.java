package com.project.backend;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Owner on 2017-03-06.
 */
public class AvailableCoursesTable {
    private final static AvailableCoursesTable instance;
    private final static Course COURSE_NOT_FOUND = null;
    private final List<Course> courses;

    static {
        instance = new AvailableCoursesTable();
    }

    private AvailableCoursesTable() {
        courses = new ArrayList<>();
        /** Adding mock data    */
        courses.add(new Course("Computer Science 1", "CSCI 1100", "01"));
        courses.add(new Course("Computer Science 1", "CSCI 1100", "02"));
        courses.add(new Course("Computer Science 2", "CSCI 1101", "01"));
        courses.add(new Course("Computer Science 2", "CSCI 1101", "02"));
        courses.sort(Course::compareTo);
    }

    public Course select(String id, String section) {
        for(Course c : courses) {
            if(c.getCourseCode().equals(id) && c.getCourseSection() == Byte.parseByte(section)) {
                return c;
            }
        }
        return COURSE_NOT_FOUND;
    }

    public void insert(Course c) {
        if(courses.isEmpty()) {
            courses.add(c);
        }
        else {
            int index = binarySearch(c);
            if(index >= 0) {
                System.err.println("Cannot add to table, course already present.");
            } else {
                courses.add(-index-1, c);
            }
        }
    }

    private int binarySearch(Course c) {
        int lo = 0, hi = courses.size()-1, mid = 0, comp;
        while(lo <= hi) {
            mid = (lo+hi)/2;
            comp = c.compareTo(courses.get(mid));
            if(comp == 0) {
                return mid;
            } else if (comp < 0) {
                hi = mid-1;
            } else {
                lo = mid+1;
            }
        }
        if(c.compareTo(courses.get(mid)) < 0) {
            return (-mid-1);
        } else {
            return -mid-2;
        }
    }

    static AvailableCoursesTable getInstance() {
        return instance;
    }

    private static void populate() {
        instance.courses.add(new Course("Computer Science 1", "CSCI 1100", "01"));
        instance.courses.add(new Course("Computer Science 1", "CSCI 1100", "02"));
        instance.courses.add(new Course("Computer Science 2", "CSCI 1101", "01"));
        instance.courses.add(new Course("Computer Science 2", "CSCI 1101", "02"));
    }

    static void reset() {
        instance.courses.clear();
        /** Temporary: add in */
        populate();
    }

    int size() {
        return courses.size();
    }
}

package com.project.backend;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Owner on 2017-03-07.
 */

/** TODO: Get rid of this once JPA container is done
 * */
public class AvailableCoursesTableTest {
    private static AvailableCoursesTable db;

    @BeforeClass
    public static void setUp() {
        db = AvailableCoursesTable.getInstance();
        assertNotNull(db);
    }

    @After
    public void tearDown() {
        AvailableCoursesTable.reset();
    }

    @Test
    public void select() throws Exception {
        assertNotNull(db.select("CSCI 1101", "01"));
    }

    @Test
    public void insert() throws Exception {
        int size = db.size();
        db.insert(new Course("Computer Science 3", "CSCI 2110", "01"));
        assertNotNull(db.select("CSCI 2110", "01"));
        assertEquals(size, db.size()-1);
    }

}
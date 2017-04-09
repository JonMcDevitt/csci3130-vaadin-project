package com.project.backend;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by Jonathan McDevitt on 2017-03-29.
 */

public class DatabaseHandler {
    
    private static DatabaseHandler instance;
    public final EntityManager em;

    public DatabaseHandler() {
        em = Persistence.createEntityManagerFactory("alpha_scanner_db").createEntityManager();
    }

    
    
    
    public static DatabaseHandler getInstance() {
        if (instance == null) {
            synchronized (DatabaseHandler.class) {
                if (instance == null) {
                    instance = new DatabaseHandler();
                }
            }
        }
        return instance;
    }
    
    /**
     * Course database functions
     */

    public List<Course> getAllCourses() {
        return em.createQuery("SELECT c FROM Course c", Course.class).getResultList();
    }

    public Course getCourseById(String code) {
        try {
            return em.createQuery("SELECT c FROM Course c WHERE c.courseCode LIKE :courseCode", Course.class)
                    .setParameter("courseCode", code.replaceAll(" ", "_")).getSingleResult();
        } catch (NoResultException e) {    
            return null;
        }
    }

    public Course addCourse(String inputCourseName, String inputCourseCode) {
        Course c = new Course();
        c.setCourseName(inputCourseName);
        c.setCourseCode(inputCourseCode);
        c.initLists();

        if (getCourseById(inputCourseCode) == null) {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        }
        return c;
    }

    public void removeCourse(Course course) {
        Course c = getCourseById(course.getCourseCode());
        em.getTransaction().begin();
        em.remove(c);
        em.getTransaction().commit();
    }

    /**
     * User database functions
     */

    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public User addUser(String email, String password, String firstName, String lastName, String department) {
        User u = new User();
        u.setEmail(email);
        u.setPassword(password);
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setDepartment(department);
        u.initCourses();

        if (getUserById(email) == null) {
            em.getTransaction().begin();
            em.persist(u);
            em.getTransaction().commit();
        }

        return u;
    }

    public List<Student> getStudentbyBarcode(String barcode) {
        try {
            return em.createQuery("SELECT s FROM Student s WHERE s.barcode = :bcode", Student.class)
                    .setParameter("bcode", barcode).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void updateStudentAttendanceStatus(String barcode, Course course, AttendanceRecord.Status status) {
        getStudentbyBarcode(barcode).stream().forEach(s -> {
            AttendanceRecord rec = getRecordById(course, s);
            em.getTransaction().begin();
            rec.setStatus(status);
            em.getTransaction().commit();
        });
    }

    public User getUserById(String email) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email LIKE :email", User.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Student database functions
     */

    public Student getStudentByID(String studId) {
        try {
            return em.createQuery("SELECT s FROM Student s WHERE s.studentId = :studID", Student.class)
                    .setParameter("studID", studId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Student addStudent(String courseid, String studID, String fName, String lName, String barCode) {
        Student stud = new Student();
        stud.setId(studID);
        stud.setBarcode(barCode);
        stud.setFirstName(fName);
        stud.setLastName(lName);
        stud.courseListInit();
        Course tempCourse = getCourseById(courseid);
        Student tempStud = getStudentByID(studID);
        if (tempCourse == null) {
            return null;
        }
        if (tempStud == null) {
            tempCourse.addStudent(stud);
            stud.addCourse(tempCourse);
            em.getTransaction().begin();
            em.persist(stud);
            em.getTransaction().commit();
            return stud;
        } else {
            tempStud.addCourse(tempCourse);
            tempCourse.addStudent(tempStud);
            return tempStud;
        }

    }

    public List<Student> getCourseStudents(String courseID) {
        Course testCourse = getCourseById(courseID);
        return testCourse.getStudentRoster();
    }

    /*
     * AttendanceRecord database functions
     */
    public void addTabletoCourse(Course currCourse, AttendanceTable at) {
        em.getTransaction().begin();
        Course toAdd = getCourseById(currCourse.getCourseCode());
        em.persist(at);
        at.getRecords().forEach(em::persist);
        at.getRecords().forEach(r -> em.persist(r.getStudent()));
        toAdd.addAttendanceTable(at);
        em.getTransaction().commit();
    }

    public AttendanceRecord getRecordById(Course course, Student student) {
        try {
            String courseCode = course.getCourseCode();
            String studentId = student.getId();
            String searchKey = courseCode + studentId;
            return em.createQuery("select r " + "from AttendanceRecord r " + "where r.attendanceID = :attendanceID",
                    AttendanceRecord.class).setParameter("attendanceID", searchKey).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public List<AttendanceRecord> getAllRecords() {
        return em.createQuery("select r " + "from AttendanceRecord r", AttendanceRecord.class).getResultList();
    }

    public AttendanceRecord addRecord(Course course, Student student) {
        AttendanceRecord record = new AttendanceRecord();
        record.setCourse(course);
        record.setStudent(student);
        record.setStatus(AttendanceRecord.Status.PRESENT);
        record.setAttendanceID();

        if (getRecordById(course, student) == null) {
            em.getTransaction().begin();
            em.persist(record);
            em.getTransaction().commit();
        }

        return record;
    }

    public void removeStudent(String courseID, String studentID) {
        Student toBeRemove = getStudentByID(studentID);
        Course toRemoveFrom = getCourseById(courseID);
        if (toBeRemove != null && toRemoveFrom != null) {
            em.getTransaction().begin();
            toBeRemove.getCourseList().remove(toRemoveFrom);
            toRemoveFrom.getStudentRoster().remove(toBeRemove);
            em.getTransaction().commit();
        }
    }

    public void changeStudent(String currid, String barcode, String fname, String lname, List<Course> courseList) {
        Student toChange = getStudentByID(currid);
        assert toChange != null;
        em.getTransaction().begin();
        toChange.setFirstName(fname);
        toChange.setLastName(lname);
        toChange.setBarcode(barcode);
        if (courseList != null) {
            toChange.setCourseList(courseList);
        }
        em.getTransaction().commit();
    }

    public void updateStudentList(String studentid, List<Course> courseList) {
        Student s = getStudentByID(studentid);
        assert s != null;
        em.getTransaction().begin();
        s.setCourseList(courseList);
        em.getTransaction().commit();
    }
    
    public void close() {
        em.close();
        instance = null;
    }
}

package com.project.backend;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

/**
 * Created by Jonathan McDevitt on 2017-03-29.
 */
public class DatabaseHandler {
    private static final EntityManager em = Persistence.createEntityManagerFactory("alpha_scanner_db")
            .createEntityManager();

    /**
     * Course database functions
     */

    public static List<Course> getAllCourses() {
        return em.createQuery(
                "SELECT c FROM Course c", Course.class
        ).getResultList();
    }

    public static Course getCourseById(String code) {
        try {
            return em.createQuery(
                    "SELECT c FROM Course c WHERE c.courseCode LIKE :courseCode",
                    Course.class
            ).setParameter("courseCode", code.replaceAll(" ", "_"))
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static Course addCourse(String inputCourseName, String inputCourseCode) {
        Course c = new Course();
        c.setCourseName(inputCourseName);
        c.setCourseCode(inputCourseCode);
        c.initLists();

        if(getCourseById(inputCourseCode) == null) {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        }
        return c;
    }

    /** User database functions */

    public static List<User> getAllUsers() {
        return em.createQuery(
                "SELECT u FROM User u",
                User.class
        ).getResultList();
    }

    public static User addUser(String email, String password, String firstName,
                        String lastName, String department) {
        User u = new User();
        u.setEmail(email);
        u.setPassword(password);
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setDepartment(department);
        u.initCourses();

        if(getUserById(email) == null) {
            em.getTransaction().begin();
            em.persist(u);
            em.getTransaction().commit();
        }

        return u;
    }

    public static User getUserById(String email) {
        try {
            return em.createQuery(
                    "SELECT u FROM User u WHERE u.email LIKE :email",
                    User.class
            ).setParameter("email", email).getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    /** Student database functions  */

    public static Student getStudentByID(String studId){
        try {
            return em.createQuery(
                    "SELECT s FROM Student s WHERE s.studentId = :studID",
                    Student.class
            ).setParameter("studID", studId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static Student addStudent(String courseid, String studID, String fName, String lName, String barCode){
        Student stud = new Student();
        stud.setId(studID);
        stud.setBarcode(barCode);
        stud.setFirstName(fName);
        stud.setLastName(lName);
        stud.courseListInit();
        Course tempCourse =getCourseById(courseid);
        Student tempStud = getStudentByID(studID);
        if(tempCourse==null){
            return null;
        }
        if(tempStud==null){
            tempCourse.addStudent(stud);
            stud.addCourse(tempCourse);
            em.getTransaction().begin();
            em.persist(stud);
            em.getTransaction().commit();
            return stud;
        }
        else{
            tempStud.addCourse(tempCourse);
            tempCourse.addStudent(tempStud);
            return tempStud;
        }

    }

    public static List<Student> getCourseStudents(String courseID){
        Course testCourse = getCourseById(courseID);
        return testCourse.getStudentRoster();
    }
    
    /*
     * AttendanceRecord database functions
     */
    
    public static AttendanceRecord getRecordById(String courseCode, String studentId) {
        try {
            return em.createQuery(
                      "select r "
                    + "from AttendanceRecord r "
                    + "where r.courseCode like :courseCode and r.studentId like :studentId",
                    AttendanceRecord.class)
            .setParameter("courseCode", courseCode)
            .setParameter("studentId", studentId)
            .getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
        
    }
    
    public static List<AttendanceRecord> getAllRecords() {
            return em.createQuery(
                      "select r "
                    + "from AttendanceRecord r",
                    AttendanceRecord.class)
            .getResultList();
    }
    
    public static AttendanceRecord addRecord(String courseCode, String studentId) {
        AttendanceRecord record = new AttendanceRecord();
        record.setCourseCode(courseCode);
        record.setStudentId(studentId);
        
        if (getRecordById(courseCode, studentId) == null) {
            em.getTransaction().begin();
            em.persist(record);
            em.getTransaction().commit();
        }
        
        return record;
    }
    
}

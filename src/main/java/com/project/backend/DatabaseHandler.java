package com.project.backend;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import org.eclipse.persistence.internal.jpa.JPAQuery;
import org.eclipse.persistence.queries.JPAQueryBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    public static  Student addStudent(String courseid, String studID, String fName, String lName, String barCode){
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
}

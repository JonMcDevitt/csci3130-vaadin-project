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

  /*  public static Course getCourseById(String id) {
        String[] courseId = id.split("-");
        if (courseId.length != 2) {
            return null;
        }
        return getCourseById(courseId[0], courseId[1]);
    }*/

    public static Course getCourseById(String code) {
        try {
            return em.createQuery(
                    "SELECT c FROM Course c WHERE c.courseCode = :courseCode",
                    Course.class
            ).setParameter("courseCode", code)
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
        Course test = getCourseById(inputCourseCode);
        if( test == null) {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        }
        return c;
    }
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

    /** Student database functions  */

    /** User database functions */
}

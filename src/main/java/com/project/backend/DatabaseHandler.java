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

    public static Course getCourseById(String id) {
        String[] courseId = id.split("-");
        if (courseId.length != 2) {
            return null;
        }
        return getCourseById(courseId[0], courseId[2]);
    }

    public static Course getCourseById(String code, String section) {

        return em.createQuery(
                "SELECT *" +
                        " FROM Course" +
                        " WHERE courseCode=" + code.replaceAll(" ", "_") +
                        " AND courseSection=" + section,
                Course.class
        ).getSingleResult();
    }

    public static Course addCourse(String inputCourseName, String inputCourseCode, String inputCourseSection) {
        Course c = new Course();
        c.setCourseName(inputCourseName);
        c.setCourseCode(inputCourseCode);
        c.setCourseSection(inputCourseSection);
        c.initLists();
        if(getCourseById(inputCourseCode, inputCourseSection) == null) {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        }
        return c;
    }

    /** Student database functions  */

    /** User database functions */
}

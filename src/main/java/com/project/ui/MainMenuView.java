package com.project.ui;

import java.util.ArrayList;
import java.util.List;

import com.project.backend.Course;
import com.project.backend.DatabaseHandler;
import com.project.backend.Student;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;

import static javax.swing.JOptionPane.*;
//import static javax.lang.System.*;

/**
 * Created by Owner on 2017-02-17. Modified by Jili on 2017-03-01. Added a grid
 * object to display courses and a "Go To Course" button to go to the course
 * selected from the grid
 * <p>
 * modified by Adeline on 2017-03-29. added the delete course functionality
 */
public class MainMenuView extends CustomComponent implements View {
    static final String NAME = "";

    public static final String COURSE_GRID_ID = "courseGrid";
    private static final String GO_TO_COURSE_BUTTON_ID = "goToCourseButton";

    private Label welcome = new Label();
    private Grid courseGrid = new Grid();
    private Button addCourse;
    private Button goToCourse;
    private Button logout;
    private Button delete;

    // selectedCourse is to store selected Course object from the grid
    private Course selectedCourse = null;

    // A testing courseList
    private List<Course> courseList;

    public MainMenuView() {
        // Create a courseList for testing
        courseList = new ArrayList<>();
        DatabaseHandler.addCourse("Computer Science 1", "CSCI 1100");
        DatabaseHandler.addCourse("Computer Science 2", "CSCI 1101");

        logout = new Button("Log Out", (Button.ClickListener) clickEvent -> logOut());

        // goToCourse button is for resetting the UI. selectedCourse is passed
        // in as a parameter for the
        // use of it's attributes in CourseView
        goToCourse = new Button("Go To Course", (Button.ClickListener) clickEvent -> goToCourse());

        addCourse = new Button("Add new course", (Button.ClickListener) clickEvent -> addCourse());
        delete = new Button("Delete", (ClickListener) clickEvent -> delete());
        initStyles();
        setCompositionRoot(new CssLayout(welcome, goToCourse, addCourse, delete, logout, courseGrid));

    }

    private void initStyles(){
        logout.addStyleName("alphabutton");
        goToCourse.addStyleName("alphabutton");
        addCourse.addStyleName("alphabutton");
        delete .addStyleName("alphabutton");
    }

    private void addCourse() {
        getUI().addWindow(new AddCourseInputsView(courseList, courseGrid));
    }

    private void goToCourse() {
        if (selectedCourse == null) {
            Notification.show("Please select a course from the course table");
        } else {
            getUI().getNavigator().navigateTo(CourseView.NAME);
        }
    }

    private void logOut() {
        getUI().setSession(null);
        getUI().getNavigator().navigateTo(NAME);
    }

    private void selectCourse() {
        selectedCourse = (Course) courseGrid.getSelectedRow();
        if (selectedCourse != null) {
            getUI().getNavigator().addView(CourseView.NAME, new CourseView(selectedCourse.getCourseCode()));
        }
    }

    private void delete() {
        if (selectedCourse == null) {
            Notification.show("Please select a course from the course table");
        } else {
            List<Student> courseStudents = DatabaseHandler.getCourseStudents(selectedCourse.getCourseCode());
            for (Student s : courseStudents) {
                DatabaseHandler.removeStudent(selectedCourse.getCourseCode(), s.getId());
            }
            DatabaseHandler.removeCourse(selectedCourse);
            BeanItemContainer<Course> bic = (BeanItemContainer<Course>) courseGrid.getContainerDataSource();
            bic.removeAllItems();
            bic.addAll(DatabaseHandler.getAllCourses());
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        String email = String.valueOf(getSession().getAttribute("user"));
        String firstName = DatabaseHandler.getUserById(email).getFirstName();
        welcome.setValue("Welcome, " + firstName + ", to Alpha Scanner!");

        courseGrid.setContainerDataSource(new BeanItemContainer<>(Course.class, DatabaseHandler.getAllCourses()));
        courseGrid.removeColumn("studentRoster");
        courseGrid.removeColumn("attendance");
        // Add a selectionListener to select a course and pass it to
        // selectedCourse as a Course object
        courseGrid.addSelectionListener(e -> selectCourse());
    }
}

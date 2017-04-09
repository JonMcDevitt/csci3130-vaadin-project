package com.project.ui;

import java.util.ArrayList;
import java.util.List;

import com.project.backend.Course;
import com.project.backend.DatabaseHandler;
import com.project.backend.Student;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

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

    private Label welcome;
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

        welcome = new Label();
        // goToCourse button is for resetting the UI. selectedCourse is passed
        // in as a parameter for the
        // use of it's attributes in CourseView
        goToCourse = new Button("Go To Course", (Button.ClickListener) clickEvent -> goToCourse());

        addCourse = new Button("", (Button.ClickListener) clickEvent -> addCourse());
        addCourse.setIcon(FontAwesome.PLUS_SQUARE);
        addCourse.addStyleName("tinybutton");
        delete = new Button("Delete", (ClickListener) clickEvent -> delete());
        if(selectedCourse==null){
        	delete.setEnabled(false);
        	goToCourse.setEnabled(false);
        }

        HorizontalLayout bottomButtons = new HorizontalLayout(goToCourse, delete);
        bottomButtons.setWidth("500px");
        bottomButtons.setSpacing(true);
        bottomButtons.setComponentAlignment(goToCourse, Alignment.MIDDLE_RIGHT);
        bottomButtons.setComponentAlignment(delete, Alignment.MIDDLE_LEFT);

        HorizontalLayout topLayout = new HorizontalLayout(welcome, logout);
        HorizontalLayout middleLayout = new HorizontalLayout(courseGrid, addCourse);
        topLayout.setWidth("100%");
        topLayout.setComponentAlignment(welcome, Alignment.TOP_LEFT);
        topLayout.setComponentAlignment(logout, Alignment.TOP_RIGHT);
        VerticalLayout mainLayout = new VerticalLayout(topLayout, middleLayout,bottomButtons);
        mainLayout.setSizeFull();
        mainLayout.setSpacing(true);
        mainLayout.setComponentAlignment(topLayout, Alignment.TOP_CENTER);
        mainLayout.setComponentAlignment(bottomButtons, Alignment.BOTTOM_CENTER);
        mainLayout.setComponentAlignment(middleLayout, Alignment.MIDDLE_CENTER);
        logout.addStyleName("plainbutton");
        goToCourse.addStyleName("alphabutton");
        delete .addStyleName("alphabutton");
        topLayout.addStyleName("topbar");
        setCompositionRoot(mainLayout);
    }

    private void addCourse() {
    	delete.setEnabled(false);
    	goToCourse.setEnabled(false);
        getUI().addWindow(new AddCourseInputsView(courseList, courseGrid));
    }

    private void goToCourse() {
        if (selectedCourse == null) {
            Notification.show("Please select a course from the course table");
        } else {
            getUI().getNavigator().navigateTo(CourseView.NAME);
            delete.setEnabled(false);
            goToCourse.setEnabled(true);
        }
    }

    private void logOut() {
        getUI().getSession().close();
        getUI().getNavigator().navigateTo(NAME);
    }

    private void selectCourse() {
        selectedCourse = (Course) courseGrid.getSelectedRow();
        if (selectedCourse != null) {
            getUI().getNavigator().addView(CourseView.NAME, new CourseView(selectedCourse.getCourseCode()));
            delete.setEnabled(true);
            goToCourse.setEnabled(true);
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
            delete.setEnabled(false);
            goToCourse.setEnabled(false);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        String email = String.valueOf(getSession().getAttribute("user"));
        String firstName = DatabaseHandler.getUserById(email).getFirstName();
        welcome.setContentMode(ContentMode.HTML);
        welcome.setValue("<h6>Welcome, <span style=\"font-weight:bold\">" + firstName + "</span>, to Alpha Scanner!</h6>");
        courseGrid.setContainerDataSource(new BeanItemContainer<>(Course.class, DatabaseHandler.getAllCourses()));
        courseGrid.removeColumn("studentRoster");
        courseGrid.removeColumn("attendance");
        // Add a selectionListener to select a course and pass it to
        // selectedCourse as a Course object
        courseGrid.addSelectionListener(e -> selectCourse());
    }
}

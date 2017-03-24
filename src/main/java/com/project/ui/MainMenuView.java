package com.project.ui;

import java.util.ArrayList;
import java.util.List;

import com.project.backend.Course;
import com.project.backend.RegisteredUser;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;

/**
 * Created by Owner on 2017-02-17.
 * Modified by Jili on 2017-03-01. Added a grid object to display courses and a "Go To Course" button to go to
 * the course selected from the grid
 */
public class MainMenuView extends CustomComponent implements View {
    public static final String NAME = "";

    public static final String COURSE_GRID_ID = "courseGrid";
    public static final String GO_TO_COURSE_BUTTON_ID = "goToCourseButton";
    
    private Label welcome = new Label();
    private Grid courseGrid = new Grid();
    private Button addCourse;
    private Button goToCourse;
    private Button logout;
    
    //selectedCourse is to store selected Course object from the grid
    private Course selectedCourse = null;
    
    //A testing courseList
    private List<Course> courseList;

	public MainMenuView() {
		//Create a courseList for testing
		courseList = new ArrayList<>();
		courseList.add(new Course("TestCourse1", "CSCI 0001", "01"));
		courseList.add(new Course("TestCourse2", "CSCI 0001", "02"));

		//Display course name only in the grid
		courseGrid.setContainerDataSource(new BeanItemContainer<>(Course.class, courseList));
		courseGrid.removeColumn("studentRoster");

		//Add a selectionListener to select a course and pass it to selectedCourse as a Course object
		courseGrid.addSelectionListener(e -> {
			selectedCourse = (Course) courseGrid.getSelectedRow();
			getUI().getNavigator().addView(CourseView.NAME, new CourseView(selectedCourse));
		});

		logout = new Button("Log Out", (Button.ClickListener) clickEvent -> {
			getUI().setSession(null);
			getUI().getNavigator().navigateTo(NAME);
		});

		//goToCourse button is for resetting the UI. selectedCourse is passed in as a parameter for the
		//use of it's attributes in CourseView
		goToCourse = new Button("Go To Course", (Button.ClickListener) clickEvent ->{
			if(selectedCourse == null){
				Notification.show("Please select a course from the course table");
			}
			else{
				getUI().getNavigator().navigateTo(CourseView.NAME);
			}
		});

		addCourse = new Button("Add new course", (Button.ClickListener) clickEvent -> {
			getUI().addWindow(new AddCourseInputsView(courseList, courseGrid));
		});
		setCompositionRoot(new CssLayout(welcome, goToCourse, addCourse, logout, courseGrid));
	}
    
    public MainMenuView(Course course) {    	
    	//Create a courseList for testing
    	courseList = new ArrayList<>();
    	courseList.add(new Course("TestCourse1", "CSCI 0001", "01"));
    	courseList.add(new Course("TestCourse2", "CSCI 0001", "02"));
    	
    	for(int i = 0; i < courseList.size(); i++){
    		if(courseList.get(i).getCourseName().equals(course.getCourseName())){
    			courseList.remove(i);
    			courseList.add(i, course);
    		}
    	}
    	
    	//Display course name only in the grid 
    	courseGrid.setContainerDataSource(new BeanItemContainer<>(Course.class, courseList));
    	courseGrid.removeColumn("studentRoster");
    	
    	//Add a selectionListener to select a course and pass it to selectedCourse as a Course object
    	courseGrid.addSelectionListener(e -> {
    		selectedCourse = (Course) courseGrid.getSelectedRow();
    		getUI().getNavigator().addView(CourseView.NAME, new CourseView(selectedCourse));
    	});

		logout = new Button("Log Out", (Button.ClickListener) clickEvent -> {
			getUI().getNavigator().navigateTo(NAME);
		});

    	goToCourse = new Button("Go to Course", (Button.ClickListener) clickEvent ->{
			if(selectedCourse == null){
				Notification.show("Please select a course from the course table");
			}
			else{
				getUI().getNavigator().navigateTo(CourseView.NAME);
			}
		});

        goToCourse.setId(GO_TO_COURSE_BUTTON_ID);

        addCourse = new Button("Add new course", (Button.ClickListener) clickEvent -> {
            getUI().addWindow(new AddCourseInputsView(courseList, courseGrid));
        });
        setCompositionRoot(new CssLayout(welcome, goToCourse, addCourse, logout, courseGrid));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        String username = String.valueOf(getSession().getAttribute("user"));
        welcome.setValue("Welcome, " + username + ", to our application!");
    }
}

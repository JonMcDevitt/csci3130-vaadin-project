package com.project;

import java.util.ArrayList;

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

    Label welcome = new Label();
    Grid courseGrid = new Grid();
    Button goToCourse = new Button("Go To Course");
    
    //selectedCourse is to store selected Course object from the grid
    private Course selectedCourse = null;
    
    //A testing courseList
    private ArrayList<Course> courseList; 

    Button logout = new Button("Log Out", (Button.ClickListener) clickEvent -> {
        getSession().setAttribute("user", null);
        getUI().getNavigator().navigateTo(NAME);
    });

    public MainMenuView() {
    	//Create a courseList for testing
    	courseList = new ArrayList<>();
    	courseList.add(new Course("TestCourse1"));
    	courseList.add(new Course("TestCourse2"));
    	
    	//Display course name only in the grid 
    	courseGrid.setContainerDataSource(new BeanItemContainer<>(Course.class, courseList));
    	courseGrid.removeColumn("studentRoster");
    	
    	//Add a selectionListener to select a course and pass it to selectedCourse as a Course object
    	courseGrid.addSelectionListener(e -> {
    		selectedCourse = (Course) courseGrid.getSelectedRow();
    	});
    	
    	//goToCourse button is for resetting the UI. selectedCourse is passed in as a parameter for the
    	//use of it's attributes in CourseView
    	goToCourse.addClickListener((Button.ClickListener) clickEvent ->{
    		if(selectedCourse == null){
    			Notification.show("Please select a course from the course table");
    		}
    		else{
    			getUI().setContent(new CourseView(selectedCourse));
    		}
    	});
    	
        setCompositionRoot(new CssLayout(welcome, goToCourse, logout, courseGrid));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        String username = String.valueOf(getSession().getAttribute("user"));
        welcome.setValue("Welcome, " + username);
    }
}

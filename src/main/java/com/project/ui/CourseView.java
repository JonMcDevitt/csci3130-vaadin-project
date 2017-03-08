package com.project.ui;

import com.project.backend.Course;
import com.project.backend.Student;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/*
 * Created by Jili on 2017-03-01
 * 
 */

public class CourseView extends CustomComponent implements View {

    public static final String NAME = "CourseInfo";
    private static final String WIDTH_TEXTFIELD_DEFAULT = "300px";

    private Button goToMain;
    private Label courseName;
    private Grid studentGrid = new Grid();

    public CourseView(){}
    
    //a overloading constructor uses a Course type parameter to set up the UI content
    public CourseView(Course course) {
        setSizeFull();

        configureComponents(course);
        createLayout();
    }

    private void configureComponents(Course course) {

    	goToMain = new Button("Back to main page");
    	courseName = new Label(course.getCourseName());
    	
    	//Display the parameter -- course's student roaster
    	studentGrid.setContainerDataSource(new BeanItemContainer<>(Student.class, course.getStudentRoster()));
    	
    	//goToMain goes back to the main page
    	goToMain.addClickListener(e -> {
    		getUI().getNavigator().navigateTo("");
    	});
    }

    private void createLayout() {
        VerticalLayout mainLayout = new VerticalLayout(courseName, goToMain, studentGrid);
        mainLayout.setSpacing(true);
        
        setCompositionRoot(mainLayout);       
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }
}

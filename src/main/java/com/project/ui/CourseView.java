package com.project.ui;

import com.project.backend.Course;
import com.project.backend.Student;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class CourseView extends CustomComponent implements View {
	
    public static final String NAME = "CourseInfo";
    private static final String WIDTH_TEXTFIELD_DEFAULT = "300px";

    private Button goToMain;
    private Button addStudent;
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
    	addStudent = new Button("Add Student");
    	courseName = new Label(course.getCourseName());
    	
    	//Display the parameter -- course's student roaster
    	studentGrid.setContainerDataSource(new BeanItemContainer<>(Student.class, course.getStudentRoster()));
    	
    	//goToMain goes back to the main page
    	goToMain.addClickListener(e -> {
    		getUI().getNavigator().navigateTo(MainMenuView.NAME);
    	});
    	
    	addStudent.addClickListener(e -> {
    		getUI().getNavigator().addView(NewStudentView.NAME, new NewStudentView(course));
    		getUI().getNavigator().navigateTo(NewStudentView.NAME);
    	});
    }

    private void createLayout() {
    	HorizontalLayout buttons = new HorizontalLayout(goToMain, addStudent);
    	buttons.setSpacing(true);
        buttons.setMargin(new MarginInfo(true, true));
        VerticalLayout mainLayout = new VerticalLayout(courseName, buttons, studentGrid);
        mainLayout.setSpacing(true);
        
        setCompositionRoot(mainLayout);       
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }
}

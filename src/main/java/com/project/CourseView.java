package com.project;

import java.util.ArrayList;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

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

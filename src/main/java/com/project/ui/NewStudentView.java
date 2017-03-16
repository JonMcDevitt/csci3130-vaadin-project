package com.project.ui;

import java.util.ArrayList;
import java.util.List;

import com.project.backend.Course;
import com.project.backend.Student;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/*
 * created by Adeline
 * ui for the student view

*/

public class NewStudentView extends CustomComponent implements View  {
     public static final String NAME = "NewStudentView";
        private static final String WIDTH_TEXTFIELD_DEFAULT = "300px";

        private TextField id;
        private TextField firstName;
        private TextField lastName;
        
        private Button addButton;
        private Button clearButton;
        private Button cancelButton;
        private List<Component> components;

     public  NewStudentView() {}
     
     public  NewStudentView(Course course) {
         setSizeFull();

         id = new TextField("ID: ");
        
         firstName = new TextField("First Name: ");
         lastName = new TextField("Last Name: ");
         addButton = new Button("Add Student");
         clearButton = new Button("Clear");
         cancelButton = new Button("Cancel");
         components = new ArrayList<>();

         configureComponents();
         configureActions(course);
         setCompositionRoot(createLayout());
     }

        private void configureComponents() {
        	setSizeFull();

            id = new TextField("ID: ");
           
            firstName = new TextField("First Name: ");
            lastName = new TextField("Last Name: ");
            addButton = new Button("Add Student");
            clearButton = new Button("Clear");
            cancelButton = new Button("Cancel");
            components = new ArrayList<>();
            
            components.add(id);
            components.add(firstName);
            components.add(lastName);
            components.add(clearButton);
            components.add(cancelButton);
            
           id.setInputPrompt("Enter ID here.");
            firstName.setInputPrompt("Enter first name.");
            lastName.setInputPrompt("Enter last name.");
       
            for (Component component : components) {
                if (component instanceof AbstractTextField) {
                    component.setWidth(WIDTH_TEXTFIELD_DEFAULT);
                    AbstractTextField textField = (AbstractTextField) component;
                    textField.setRequired(true);
                    textField.setInvalidAllowed(false);
                }
            }
        }

        private void configureActions(Course course) {
        

            clearButton.addClickListener((Button.ClickListener) clickEvent -> {
                id.clear();
                firstName.clear();
                lastName.clear();
               
            });

            cancelButton.addClickListener(e -> {
                getUI().getNavigator().navigateTo(CourseView.NAME);
            });
            
            addButton.addClickListener(e -> {
            	course.addStudent(new Student(id.getValue(), firstName.getValue(), lastName.getValue()));
            	getUI().getNavigator().addView(CourseView.NAME, new CourseView(course));
                getUI().getNavigator().navigateTo(CourseView.NAME);
            });
        }

        private Layout createLayout() {
            HorizontalLayout buttons = new HorizontalLayout(
                    addButton, clearButton, cancelButton);
            buttons.setSpacing(true);
            buttons.setMargin(new MarginInfo(true, true));
            VerticalLayout fields = new VerticalLayout(
                    id, firstName, lastName, buttons);
            fields.setSpacing(true);
            fields.setMargin(new MarginInfo(true, true, true, true));
            fields.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            VerticalLayout viewLayout = new VerticalLayout(fields);
            viewLayout.setSizeFull();
            viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
            viewLayout.setStyleName(Reindeer.LAYOUT_BLACK);
            return viewLayout;
        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
            id.focus();
        }
}
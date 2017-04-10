package com.project.ui;

import java.util.ArrayList;
import java.util.List;

import com.project.backend.Course;
import com.project.backend.DatabaseHandler;
import com.project.backend.Student;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;

/*
 * created by Adeline
 * ui for the student view

*/

public class NewStudentView extends CustomComponent implements View {
    public static final String NAME = "NewStudentView";
    private static final String WIDTH_TEXTFIELD_DEFAULT = "300px";
    private final String courseID;
    private final String fname;
    private final String lname;
    private final String department;

    private TextField id;
    private TextField firstName;
    private TextField lastName;
    private TextField barcode;

    private Button addButton;
    private Button clearButton;
    private Button cancelButton;
    private List<Component> components;

    private Label header;
    private Label subheader;

    public NewStudentView(String courseID,String firstName, String lastName, String department) {
        this.fname= firstName;
        this.lname = lastName;
                this.department = department;
        this.courseID = courseID;
        setSizeFull();
        components = new ArrayList<>();

        configureComponents();
        configureActions(courseID);
        VerticalLayout layout = new VerticalLayout(createLayout());
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setCompositionRoot(layout);
    }

    private void configureComponents() {
        setSizeFull();

        id = new TextField("ID: ");
        firstName = new TextField("First Name: ");
        lastName = new TextField("Last Name: ");
        barcode = new TextField("Barcode: ");
        addButton = new Button("Add Student");
        clearButton = new Button("Clear");
        cancelButton = new Button("Cancel");

        components = new ArrayList<>();

        components.add(id);
        components.add(firstName);
        components.add(lastName);
        components.add(barcode);
        components.add(clearButton);
        components.add(cancelButton);

        id.setInputPrompt("Enter ID here.");
        firstName.setInputPrompt("Enter first name.");
        lastName.setInputPrompt("Enter last name.");

        UserInterfaceHelperFunctions.setTextFieldsWidth(components, WIDTH_TEXTFIELD_DEFAULT);
        UserInterfaceHelperFunctions.setTextFieldsRequired(components, true);
        UserInterfaceHelperFunctions.setTextFieldsInvalidAllowed(components, false);
    }

    private void configureActions(String courseID) {

        clearButton.addClickListener((Button.ClickListener) clickEvent -> clearFields());

        cancelButton.addClickListener(e -> cancel());

        addButton.addClickListener(e -> addStudent(courseID));
    }

    private void addStudent(String courseID) {
        DatabaseHandler.getInstance().addStudent(courseID, id.getValue(), firstName.getValue(), lastName.getValue(), barcode.getValue());
        getUI().getNavigator().addView(CourseView.NAME, new CourseView(courseID,fname,lname,department));
        getUI().getNavigator().navigateTo(CourseView.NAME);
    }

    private void cancel() {
        getUI().getNavigator().navigateTo(CourseView.NAME);
    }

    private void clearFields() {
        id.clear();
        firstName.clear();
        lastName.clear();
        barcode.clear();
    }

    private Layout createLayout() {
        header = new Label("<h1><center>New Student</center></h1>");
        header.setContentMode(ContentMode.HTML);
        subheader = new Label("<h2><center>Please enter the student's information</center></h2>");
        subheader.setContentMode(ContentMode.HTML);
        HorizontalLayout buttons = new HorizontalLayout(
                addButton, clearButton, cancelButton);
        buttons.setSpacing(true);
        buttons.setMargin(new MarginInfo(true, true));
        VerticalLayout fields = new VerticalLayout(
                header, subheader, id, firstName, lastName, barcode, buttons);
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, true));
        fields.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setSizeFull();
        fields.setComponentAlignment(id, Alignment.MIDDLE_CENTER);
        fields.setComponentAlignment(firstName, Alignment.MIDDLE_CENTER);
        fields.setComponentAlignment(id, Alignment.MIDDLE_CENTER);
        fields.setComponentAlignment(lastName, Alignment.MIDDLE_CENTER);
        fields.setComponentAlignment(barcode, Alignment.MIDDLE_CENTER);
        fields.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLACK);
        return viewLayout;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        id.focus();
    }
}
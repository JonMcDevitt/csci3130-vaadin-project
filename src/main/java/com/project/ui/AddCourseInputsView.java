package com.project.ui;

import com.project.backend.Course;
import com.project.backend.DatabaseHandler;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Owner on 2017-03-16.
 */
public class AddCourseInputsView extends Window {
    private Button save;
    private TextField inputCourseCode, inputCourseName;
    private Label header;
    private Label subheader;

    public AddCourseInputsView(List<Course> courseList, Grid courseGrid) {
        super();
        center();
        this.setWidth("500px");
        this.setHeight("300px");
        setClosable(true);
        setModal(true);
        initButtons(courseGrid);
        initInputs();
        header = new Label("<h1>New Course</h1>");
        header.setContentMode(ContentMode.HTML);
        subheader = new Label("<h2>Please enter your course name and code</h2>");
        subheader.setContentMode(ContentMode.HTML);
        HorizontalLayout buttons = new HorizontalLayout(save);
        VerticalLayout layout = new VerticalLayout(header, subheader, inputCourseCode, inputCourseName, buttons);
        layout.setSpacing(true);
        layout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(header, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(subheader, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(inputCourseCode, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(inputCourseName, Alignment.MIDDLE_CENTER);
        setContent(layout);

    }

    private void initButtons(Grid courseGrid) {
        save = new Button("Save", (Button.ClickListener) clickEvent -> {
            /**
             *  TODO:   Fix validator functions
             * if(validateCourseCode(inputCourseCode.getValue()) &&
                    validateCourseName(inputCourseName.getValue()) &&
                    validateCourseSection(inputCourseSection.getValue())) {
                Course newCourse = new Course(inputCourseCode.getValue(), inputCourseName.getValue(), inputCourseSection.getValue());
                courseList.add(newCourse);
            } else {
                Notification.show("Invalid fields.", Notification.Type.WARNING_MESSAGE);
            }*/

            DatabaseHandler.addCourse(inputCourseName.getValue(), inputCourseCode.getValue());
            BeanItemContainer<Course> bic = (BeanItemContainer<Course>) courseGrid.getContainerDataSource();
            bic.removeAllItems();
            bic.addAll(DatabaseHandler.getAllCourses());
            courseGrid.setHeightByRows(courseGrid.getContainerDataSource().size());
            this.close();
        });
    }

    private boolean validateCourseSection(String value) {
        return Pattern.compile("0[0-9]").matcher(value).matches();
    }

    private boolean validateCourseName(String value) {
        return Pattern.compile("([a-zA-Z0-9_-]\\s*)+").matcher(value).matches();
    }

    private boolean validateCourseCode(String value) {
        return Pattern.compile("[a-zA-Z]{4}[0-9]{4}").matcher(value).matches();
    }

    private void initInputs() {
        inputCourseCode = new TextField();
        inputCourseCode.setInputPrompt("Course Code (e.g. CSCI 1101)");
        inputCourseCode.setWidth("400px");
        inputCourseName = new TextField();
        inputCourseName.setWidth("400px");
        inputCourseName.setInputPrompt("Course Name (e.g. Software Engineering)");
    }
}

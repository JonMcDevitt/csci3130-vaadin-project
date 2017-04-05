package com.project.ui;

import com.project.backend.Course;
import com.project.backend.DatabaseHandler;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Owner on 2017-03-16.
 */
public class AddCourseInputsView extends Window {
    private Button save;
    private TextField inputCourseCode, inputCourseName;

    public AddCourseInputsView(List<Course> courseList, Grid courseGrid) {
        super();
        center();

        setClosable(true);
        setModal(true);

        initButtons(courseGrid);
        initInputs();

        /** TODO:   Make all this pretty
         * */
        HorizontalLayout buttons = new HorizontalLayout(save);
        VerticalLayout layout = new VerticalLayout(inputCourseCode, inputCourseName, buttons);
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
        inputCourseName = new TextField();
        inputCourseName.setInputPrompt("Course Name (e.g. Software Engineering)");
    }
}

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
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class CourseView extends CustomComponent implements View {

    public static final String NAME = "CourseInfo";

    private static final String WIDTH_TEXTFIELD_DEFAULT = "300px";
    public static final String TAKE_ATTENDANCE_FOR_TODAY_BUTTON_ID = "takeAttendanceForTodayButton";

    private Course course;
    private Button goToMain;
    private Button addStudent;
    private Button editStudent;
    private Button goToTakeAttendance;
    private Student currStudent;
    private Label courseName;
    private VerticalLayout popupContent;
    private Grid studentGrid = new Grid();

    public CourseView() {
    }

    //a overloading constructor uses a Course type parameter to set up the UI content
    CourseView(Course course) {
        this.course = course;
        setSizeFull();

        configureComponents(course);
        createLayout();
    }

    private void configureComponents(Course course) {

        goToMain = new Button("Back to main page",
                (Button.ClickListener) clickEvent -> goToMain());
        addStudent = new Button("Add Student",
                (Button.ClickListener) clickEvent -> goToStudent());
        courseName = new Label(course.getCourseName());
        editStudent = new Button("Edit selected Student",
                (Button.ClickListener) clickEvent -> editStudent());
        goToTakeAttendance = new Button("Take Attendance For Today",
                (Button.ClickListener) clickEvent -> takeAttendance());

        configurePopup();
        //Display the parameter -- course's student roaster
        studentGrid.setContainerDataSource(new BeanItemContainer<>(Student.class, course.getStudentRoster()));
        studentGrid.setColumnOrder("id");
        goToTakeAttendance.setId(TAKE_ATTENDANCE_FOR_TODAY_BUTTON_ID);
    }

    private void goToMain() {
        getUI().getNavigator().addView(MainMenuView.NAME, new MainMenuView(course));
        getUI().getNavigator().navigateTo(MainMenuView.NAME);
    }

    private void goToStudent() {
        getUI().getNavigator().addView(NewStudentView.NAME, new NewStudentView(course));
        getUI().getNavigator().navigateTo(NewStudentView.NAME);
    }

    private void editStudent() {
        if (studentGrid.getSelectedRow() != null) {
            currStudent = (Student) studentGrid.getSelectedRow();
            editStudentButton(currStudent);
            popupContent.setVisible(true);
        }
    }

    private void takeAttendance() {
        getUI().setContent(new AttendanceView(course));
    }

    private void createLayout() {
        HorizontalLayout buttons = new HorizontalLayout(goToMain, addStudent, editStudent, goToTakeAttendance);
        buttons.setSpacing(true);
        buttons.setMargin(new MarginInfo(true, true));
        VerticalLayout mainLayout = new VerticalLayout(courseName, buttons, studentGrid);
        mainLayout.setSpacing(true);
        HorizontalLayout horizontalLayout = new HorizontalLayout(mainLayout, popupContent);
        horizontalLayout.setSpacing(true);
        setCompositionRoot(horizontalLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }

    private void configurePopup() {
        popupContent = new VerticalLayout();
        HorizontalLayout popButtons = new HorizontalLayout();

        TextField id = new TextField("ID");
        TextField barcode = new TextField("Barcode");
        TextField fname = new TextField("First Name");
        TextField lname = new TextField("Last Name");

        popupContent.addComponent(id);
        popupContent.addComponent(barcode);
        popupContent.addComponent(fname);
        popupContent.addComponent(lname);

        Button saveButton = new Button("Save",
                (Button.ClickListener) clickEvent -> changeStudent(id, barcode, fname, lname));
        Button cancelButton = new Button("Cancel",
                (Button.ClickListener) clickEvent -> popupContent.setVisible(false));

        popButtons.addComponent(saveButton);
        popButtons.addComponent(cancelButton);
        popupContent.addComponent(popButtons);
        popupContent.setVisible(false);
    }

    private void editStudentButton(Student student) {
        TextField idEdit = (TextField) popupContent.getComponent(0);
        TextField barcodeEdit = (TextField) popupContent.getComponent(1);
        TextField firstEdit = (TextField) popupContent.getComponent(2);
        TextField lastEdit = (TextField) popupContent.getComponent(3);

        idEdit.setValue(student.getId());
        barcodeEdit.setValue(student.getBarcode());
        firstEdit.setValue(student.getFirstName());
        lastEdit.setValue(student.getLastName());

    }

    private void changeStudent(TextField idnum, TextField barcode, TextField firstname, TextField lastname) {
        currStudent.setId(idnum.getValue());
        currStudent.setBarcode(barcode.getValue());
        currStudent.setFirstName(firstname.getValue());
        currStudent.setLastName(lastname.getValue());
        popupContent.setVisible(false);
        Notification saved = new Notification("Saved edit");
        saved.setDelayMsec(5000);
        studentGrid.refreshRows(currStudent);
    }
}

package com.project.ui;

import com.project.backend.AttendanceRecord;
import com.project.backend.AttendanceTable;
import com.project.backend.Course;
import com.project.backend.DatabaseHandler;
import com.project.backend.Student;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

import javax.swing.*;

public class CourseView extends CustomComponent implements View {

    public static final String NAME = "CourseInfo";

    private static final String WIDTH_TEXTFIELD_DEFAULT = "300px";
    public static final String TAKE_ATTENDANCE_FOR_TODAY_BUTTON_ID = "takeAttendanceForTodayButton";

    private String courseID;
    private Button goToMain;
    private Button addStudent;
    private Button editStudent;
    private Button goToTakeAttendance;
    private Button deleteStudent;
    private Student currStudent;
    private Label courseName;
    private VerticalLayout popupContent;
    private Grid studentGrid = new Grid();
    private HorizontalLayout topLayout;
    private Label welcome;
    private Button logout;

    public CourseView() {
    }

    //a overloading constructor uses a Course type parameter to set up the UI content
    public CourseView(String courseID) {
        this.courseID=courseID;
        setSizeFull();
        configureComponents(courseID);
        createLayout();
    }

    private void configureComponents(String courseID) {
    	Course course = DatabaseHandler.getInstance().getCourseById(courseID);

        goToMain = new Button("Back to main page",
                (Button.ClickListener) clickEvent -> goToMain());
        addStudent = new Button("",
                (Button.ClickListener) clickEvent -> goToStudent());
        editStudent = new Button("Edit Student",
                (Button.ClickListener) clickEvent -> editStudent());
        goToTakeAttendance = new Button("Take Attendance For Today",
                (Button.ClickListener) clickEvent -> takeAttendance());
        deleteStudent = new Button("Delete Student",
                (Button.ClickListener) clickEvent -> deleteStudent(courseID));

        configurePopup();
        //Display the parameter -- course's student roaster
        studentGrid.setContainerDataSource(new BeanItemContainer<>(Student.class, DatabaseHandler.getInstance().getCourseStudents(courseID)));
        studentGrid.setColumnOrder("id");
        studentGrid.removeColumn("courseList");
        studentGrid.setWidth("700px");
        editStudentGrid(studentGrid);
        goToTakeAttendance.setId(TAKE_ATTENDANCE_FOR_TODAY_BUTTON_ID);
        logout = new Button("Log Out", (Button.ClickListener) clickEvent -> logOut());
        welcome = new Label("<h6>"+DatabaseHandler.getInstance().getCourseById(courseID).getCourseName()+"</h6>");
    }
    
    private void editStudentGrid(Grid studentGrid){
    	studentGrid.setEditorEnabled(true);
    	studentGrid.setEditorSaveCaption("Save");
    	studentGrid.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler(){

			private static final long serialVersionUID = 1L;

			@Override
			public void preCommit(CommitEvent commitEvent) throws CommitException {
			}

			@Override
			public void postCommit(CommitEvent commitEvent) throws CommitException {
			}
    	});
    }

    private void goToMain() {
        getUI().getNavigator().addView(MainMenuView.NAME, new MainMenuView());
        getUI().getNavigator().navigateTo(MainMenuView.NAME);
    }

    private void goToStudent() {
        getUI().getNavigator().addView(NewStudentView.NAME, new NewStudentView(courseID));
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
        getUI().setContent(new AttendanceView(DatabaseHandler.getInstance().getCourseById(courseID)));
    }

    private void createLayout() {

        welcome.setContentMode(ContentMode.HTML);
        logout.setStyleName("plainbutton");
        goToMain.addStyleName("plainbutton");
        topLayout = new HorizontalLayout(welcome, goToMain, logout);

        topLayout.setWidth("100%");
        topLayout.setComponentAlignment(welcome, Alignment.TOP_LEFT);
        topLayout.setComponentAlignment(logout, Alignment.TOP_RIGHT);
        topLayout.setComponentAlignment(goToMain, Alignment.TOP_RIGHT);
        topLayout.addStyleName("topbar");
        topLayout.setExpandRatio(welcome, 4f);
        topLayout.setExpandRatio(logout, 1f);
        topLayout.setExpandRatio(goToMain, 2f);
        addStudent.setIcon(FontAwesome.PLUS_SQUARE);
        addStudent.addStyleName("tinybutton");

        HorizontalLayout buttons = new HorizontalLayout(editStudent, deleteStudent, goToTakeAttendance);
        buttons.setSpacing(true);
        //buttons.setMargin(new MarginInfo(true, true));
        VerticalLayout mainLayout = new VerticalLayout(studentGrid,buttons);
        //mainLayout.setSpacing(true);
        HorizontalLayout horizontalLayout = new HorizontalLayout(topLayout, mainLayout, addStudent,popupContent);
        mainLayout.setSpacing(true);
        //horizontalLayout.setSizeFull();
        //horizontalLayout.setSpacing(true);
        buttons.setComponentAlignment(goToTakeAttendance, Alignment.MIDDLE_CENTER);
        buttons.setComponentAlignment(editStudent, Alignment.MIDDLE_CENTER);
        buttons.setComponentAlignment(deleteStudent, Alignment.MIDDLE_CENTER);
        mainLayout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
        mainLayout.setComponentAlignment(studentGrid, Alignment.MIDDLE_CENTER);
        horizontalLayout.setComponentAlignment(mainLayout, Alignment.MIDDLE_CENTER);
        horizontalLayout.setComponentAlignment(popupContent, Alignment.MIDDLE_CENTER);
        VerticalLayout layout = new VerticalLayout(topLayout, horizontalLayout);
        layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
        setCompositionRoot(layout);
    }
    private void logOut() {
        getUI().getSession().close();
        getUI().getNavigator().navigateTo(NAME);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }

    private void configurePopup() {
        popupContent = new VerticalLayout();
        HorizontalLayout popButtons = new HorizontalLayout();

        //TextField id = new TextField("ID");
        TextField barcode = new TextField("Barcode");
        barcode.addStyleName("spacer-bot");
        TextField fname = new TextField("First Name");
        fname.addStyleName("spacer-bot");
        TextField lname = new TextField("Last Name");
        lname.addStyleName("spacer-bot");
        //popupContent.addComponent(id);
        popupContent.addComponent(barcode);
        popupContent.addComponent(fname);
        popupContent.addComponent(lname);

        Button saveButton = new Button("Save",
                (Button.ClickListener) clickEvent -> changeStudent( barcode, fname, lname));
        Button cancelButton = new Button("Cancel",
                (Button.ClickListener) clickEvent -> popupContent.setVisible(false));

        popButtons.addComponent(saveButton);
        popButtons.addComponent(cancelButton);
        popButtons.setSpacing(true);

        popupContent.addComponent(popButtons);
        popupContent.setVisible(false);
    }

    private void editStudentButton(Student student) {
        TextField barcodeEdit = (TextField) popupContent.getComponent(0);
        TextField firstEdit = (TextField) popupContent.getComponent(1);
        TextField lastEdit = (TextField) popupContent.getComponent(2);

        barcodeEdit.setValue(student.getBarcode());
        firstEdit.setValue(student.getFirstName());
        lastEdit.setValue(student.getLastName());

    }

    private void deleteStudent(String courseID) {
        currStudent = (Student) studentGrid.getSelectedRow();
        if(currStudent!=null){
        	DatabaseHandler.getInstance().removeStudent(courseID, currStudent.getId());
        	getUI().getNavigator().addView(CourseView.NAME, new CourseView(courseID));
        	getUI().getNavigator().navigateTo(CourseView.NAME);
        }
        DatabaseHandler.getInstance().removeStudent(courseID, currStudent.getId());
        getUI().getNavigator().addView(CourseView.NAME, new CourseView(courseID));
        getUI().getNavigator().navigateTo(CourseView.NAME);
    }

    private void changeStudent( TextField barcode, TextField firstname, TextField lastname) {
    	String currID = currStudent.getId();
    	System.out.println("Current Student ID: "+currID+"\n");
    	String barcodes = barcode.getValue();
    	String fname = firstname.getValue();
    	String lname = lastname.getValue();
    	DatabaseHandler.getInstance().changeStudent(currID,barcodes,fname,lname, null);
        popupContent.setVisible(false);
        Notification saved = new Notification("Saved edit");
        saved.setDelayMsec(5000);
        studentGrid.refreshRows(currStudent);
    }
}

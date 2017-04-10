package com.project.ui;

import com.project.backend.AttendanceRecord;
import com.project.backend.AttendanceTable;
import com.project.backend.Course;
import com.project.backend.DatabaseHandler;
import com.project.backend.Student;
import com.project.backend.User;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
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
    private Button goToTakeAttendance;
    private Button deleteStudent;
    private Student currStudent;
    private Label courseName;
    private Grid studentGrid = new Grid();
    private HorizontalLayout topLayout;
    private Label welcome;
    private Label title;
    private Button logout;
    private String firstNameGlobal;
    private String lastNameGlobal;
    private String departmentGlobal;
    private VaadinSession session;

    public CourseView() {
    }

    //a overloading constructor uses a Course type parameter to set up the UI content
    public CourseView(String courseID, VaadinSession session) {
        this.courseID=courseID;
        setSizeFull();
        configureComponents(courseID);
        createLayout();
        this.session = session;
    }
    
    public CourseView(String courseID) {
        this.courseID=courseID;
        setSizeFull();
        configureComponents(courseID);
        createLayout();
    }
    
    
    public CourseView(String courseID,String firstName, String lastName, String department) {
        this.courseID=courseID;
        this.firstNameGlobal=firstName;
        this.lastNameGlobal=lastName;
        this.departmentGlobal=department;
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
        goToTakeAttendance = new Button("Take Attendance For Today",
                (Button.ClickListener) clickEvent -> takeAttendance());
        deleteStudent = new Button("Delete Student",
                (Button.ClickListener) clickEvent -> deleteStudent(courseID));
        //Display the parameter -- course's student roaster
        studentGrid.setContainerDataSource(new BeanItemContainer<>(Student.class, DatabaseHandler.getInstance().getCourseStudents(courseID)));
        studentGrid.setColumnOrder("id");
        studentGrid.removeColumn("courseList");
        studentGrid.setWidth("700px");
        editStudentGrid(studentGrid);
        goToTakeAttendance.setId(TAKE_ATTENDANCE_FOR_TODAY_BUTTON_ID);
        logout = new Button("Log Out", (Button.ClickListener) clickEvent -> logOut());
        welcome = new Label("<center><h2>"+DatabaseHandler.getInstance().getCourseById(courseID).getCourseName()+"</h2></center>");
        title = new Label("<h6>"+firstNameGlobal+" "+lastNameGlobal+", "+departmentGlobal+"</h6>");
        title.setContentMode(ContentMode.HTML);
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
        getUI().getNavigator().addView(NewStudentView.NAME, new NewStudentView(courseID, firstNameGlobal,lastNameGlobal,departmentGlobal));
        getUI().getNavigator().navigateTo(NewStudentView.NAME);
    }

    private void takeAttendance() {
        getUI().setContent(new AttendanceView(DatabaseHandler.getInstance().getCourseById(courseID),firstNameGlobal,lastNameGlobal,departmentGlobal));
    }

    private void createLayout() {

        welcome.setContentMode(ContentMode.HTML);
        logout.setStyleName("plainbutton");
        goToMain.addStyleName("plainbutton");
        topLayout = new HorizontalLayout( title, goToMain, logout);

        topLayout.setWidth("100%");
        topLayout.setComponentAlignment(title, Alignment.TOP_LEFT);
        topLayout.setComponentAlignment(logout, Alignment.TOP_RIGHT);
        topLayout.setComponentAlignment(goToMain, Alignment.TOP_RIGHT);
        topLayout.addStyleName("topbar");
        topLayout.setExpandRatio(title, 4f);
        topLayout.setExpandRatio(logout, 1f);
        topLayout.setExpandRatio(goToMain, 2f);
        addStudent.setIcon(FontAwesome.PLUS_SQUARE);
        addStudent.addStyleName("tinybutton");

        HorizontalLayout buttons = new HorizontalLayout(deleteStudent, goToTakeAttendance);
        buttons.setSpacing(true);
        //buttons.setMargin(new MarginInfo(true, true));
        HorizontalLayout horizontalLayout = new HorizontalLayout(studentGrid, addStudent);
        VerticalLayout mainLayout = new VerticalLayout(welcome, horizontalLayout ,buttons);
        

        //mainLayout.setSpacing(true);
      
        mainLayout.setSpacing(true);
        //horizontalLayout.setSizeFull();
        //horizontalLayout.setSpacing(true);
        buttons.setComponentAlignment(goToTakeAttendance, Alignment.MIDDLE_CENTER);
        buttons.setComponentAlignment(deleteStudent, Alignment.MIDDLE_CENTER);
        mainLayout.setComponentAlignment(welcome, Alignment.MIDDLE_CENTER);
        mainLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
        mainLayout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
        //mainLayout.setComponentAlignment(studentGrid, Alignment.MIDDLE_CENTER);
       //horizontalLayout.setComponentAlignment(mainLayout, Alignment.MIDDLE_CENTER);
        VerticalLayout layout = new VerticalLayout(topLayout,mainLayout);
        //layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
        setCompositionRoot(layout);
    }
    private void logOut() {
        getUI().getSession().close();
        getUI().getNavigator().navigateTo(NAME);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        configureComponents(null);
    }

    private void deleteStudent(String courseID) {
        currStudent = (Student) studentGrid.getSelectedRow();
        if(currStudent!=null){
        	DatabaseHandler.getInstance().removeStudent(courseID, currStudent.getId());
        	getUI().getNavigator().addView(CourseView.NAME, new CourseView(courseID, firstNameGlobal, lastNameGlobal,departmentGlobal));
        	getUI().getNavigator().navigateTo(CourseView.NAME);
        }
        DatabaseHandler.getInstance().removeStudent(courseID, currStudent.getId());
        getUI().getNavigator().addView(CourseView.NAME, new CourseView(courseID, firstNameGlobal, lastNameGlobal,departmentGlobal));
        getUI().getNavigator().navigateTo(CourseView.NAME);
    }
}

 /*CSCI 3130
 * 
 * March 19, 2017
 * 
 *Project - User Story 2
 *By Team Alpha
 *
 *This user story was created by Nicholas Broderick and Liam Gowan.
 *
 *This class utilizes the BarcodeScannerComponent, Course, ClassDay and Student classes to 
 *show the attendance status of students in a class, and to update attendance status by scanning
 *in barcodes.
 */

package com.project.ui;

import static java.util.stream.Collectors.groupingBy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.project.backend.AttendanceRecord;
import com.project.backend.AttendanceTable;
import com.project.backend.Course;
import com.project.backend.DatabaseHandler;
import com.project.backend.Student;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

public class AttendanceView extends CustomComponent implements View {
    private static final long serialVersionUID = 8909424502341897571L;

    private Course course;

    private Button toCourseViewButton;

    private IndexedContainer attendanceRecords;
    private Map<String, List<Item>> barcodeToItemMap;

    //private static final String BACK_BUTTON_ID = "backButton";

    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String BANNER_NUMBER = "Banner Number";
    private static final String BARCODE = "Barcode";
    private static final String STATUS = "Attendance Status";
    private HorizontalLayout topLayout;
    private Button logout;
    private String firstNameGlobal;
    private String lastNameGlobal;
    private String departmentGlobal;
    private Label title;
    Label header;

    AttendanceView() {}
    
    AttendanceView(Course course, String firstName, String lastName, String department) {
        this(course, LocalDateTime.now().truncatedTo(ChronoUnit.DAYS), firstName, lastName, department);
    }

    // Creates the view
    
    public AttendanceView(Course course, LocalDateTime classDate, String firstName, String lastName, String department) {
        this.course = course;
        	
        firstNameGlobal = firstName;
        lastNameGlobal = lastName;
        departmentGlobal = department;
        
        // creates all components used in view
        Grid attendanceGrid = new Grid();

        BarcodeScannerComponent barcodeScannerComponent = new BarcodeScannerComponent();

        attendanceRecords = makeAttendanceRecordContainer(classDate, course);
        attendanceGrid.setContainerDataSource(attendanceRecords);
        barcodeToItemMap = makeBarcodeToItemMap(attendanceRecords);

        barcodeScannerComponent.onBarcodeScanned(barcode -> {
            DatabaseHandler.getInstance().updateStudentAttendanceStatus(barcode, course, AttendanceRecord.Status.PRESENT);
            updateAttendanceGrid(barcode, AttendanceRecord.Status.PRESENT);
        });

        // configures components
        logout = new Button("Log Out", (Button.ClickListener) clickEvent -> logOut());
        logout.setStyleName("plainbutton");
        configureAttendanceGrid(attendanceGrid);
        configureLabel(classDate);
        configureBackButton();
        title = new Label("<h6>"+firstNameGlobal+" "+lastNameGlobal+", "+departmentGlobal+"</h6");
        title.setContentMode(ContentMode.HTML);
        
        topLayout = new HorizontalLayout(title, toCourseViewButton, logout);

        topLayout.setWidth("100%");
        topLayout.setComponentAlignment(title, Alignment.TOP_LEFT);
        topLayout.setComponentAlignment(logout, Alignment.TOP_RIGHT);
        topLayout.setComponentAlignment(toCourseViewButton, Alignment.TOP_RIGHT);
        topLayout.setExpandRatio(title, 4f);
        topLayout.setExpandRatio(logout, 1f);
        topLayout.setExpandRatio(toCourseViewButton, 2f);
        topLayout.addStyleName("topbar");

        VerticalLayout layout = new VerticalLayout(topLayout, header, attendanceGrid, barcodeScannerComponent);

        // adds components to layout and alligns them.
        layout.setSizeFull();
        layout.setSpacing(true);
        layout.setComponentAlignment(attendanceGrid, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(barcodeScannerComponent, Alignment.MIDDLE_CENTER);
        setCompositionRoot(layout);
    }

    // configures grid, sets to width to 100%
    private void configureAttendanceGrid(Grid attendanceGrid) {
        attendanceGrid.setContainerDataSource(attendanceRecords);
        attendanceGrid.setWidth("100%");
        attendanceGrid.setEditorEnabled(true);
        attendanceGrid.getColumn(BANNER_NUMBER).setEditable(false);
        attendanceGrid.getColumn(LAST_NAME).setEditable(false);
        attendanceGrid.getColumn(FIRST_NAME).setEditable(false);
        attendanceGrid.getColumn(BARCODE).setEditable(false);
        attendanceGrid.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {


            private static final long serialVersionUID = 1L;

            @Override
            public void preCommit(CommitEvent commitEvent) throws CommitException {

            }

            @Override
            public void postCommit(CommitEvent commitEvent) throws CommitException {
                Item editedItem = attendanceRecords.getItem(attendanceGrid.getEditedItemId());
                String barcodeValue = editedItem.getItemProperty(BARCODE).getValue().toString();
                AttendanceRecord.Status status = (AttendanceRecord.Status) editedItem.getItemProperty(STATUS).getValue();
                DatabaseHandler.getInstance().updateStudentAttendanceStatus(barcodeValue, course, status);
            }
        });
    }

    // configures back button, if clicked it will go to course view
    private void configureBackButton() {
       // toCourseViewButton.setId(BACK_BUTTON_ID);
        toCourseViewButton = new Button("Back to Course View");
        toCourseViewButton.addClickListener(e -> getUI().setContent(new CourseView(course.getCourseCode(),firstNameGlobal,lastNameGlobal, departmentGlobal)));
        toCourseViewButton.setStyleName("plainbutton");
    }

    // Configures label, it displays "courseCode - courseName" at the top of the
    // view
    private void configureLabel(LocalDateTime classDate) {
        String courseCode = course.getCourseCode();
        String courseName = course.getCourseName();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        String caption = String.format("<center><h2>Attendance for: <span style=\"font-weight:bold\">%s - %s</span> on %s</h2></center>",
                courseCode, courseName, classDate.format(formatter).toString());
        header= new Label(caption);
        header.setContentMode(ContentMode.HTML);
    }

    // sets the content of each grid. If the students are coming from the absent
    // list, they will
    // be marked as absent, and if they are coming from the attending list, they
    // will be
    // marked as present
    private IndexedContainer makeAttendanceRecordContainer(LocalDateTime classDay, Course currCourse) {
        IndexedContainer attendanceRecords = new IndexedContainer();

        attendanceRecords.addContainerProperty(FIRST_NAME, String.class, "");
        attendanceRecords.addContainerProperty(LAST_NAME, String.class, "");
        attendanceRecords.addContainerProperty(BANNER_NUMBER, String.class, "");
        attendanceRecords.addContainerProperty(BARCODE, String.class, "");
        attendanceRecords.addContainerProperty(STATUS, AttendanceRecord.Status.class, AttendanceRecord.Status.ABSENT);

        AttendanceTable table = getAttendanceTable(currCourse, classDay);

        for (AttendanceRecord record : table.getRecords()) {
            Item item = attendanceRecords.addItem(record.getStudent().getId());
            setRecordItemProperties(item, record.getStudent(), record);
        }

        return attendanceRecords;
    }

    private Map<String, List<Item>> makeBarcodeToItemMap(IndexedContainer attendanceRecords) {
        return attendanceRecords.getItemIds().stream().map(attendanceRecords::getItem)
                .collect(groupingBy(item -> (String) item.getItemProperty(BARCODE).getValue()));
    }

    private AttendanceTable getAttendanceTable(Course currCourse, LocalDateTime classDate) {
        Optional<AttendanceTable> attendanceTableOp = currCourse.getAttendance().stream()
                .filter(ar -> ar.getDate().truncatedTo(ChronoUnit.DAYS).equals(classDate)).findAny();

        if (!attendanceTableOp.isPresent()) {
            AttendanceTable at = new AttendanceTable();
            List<Student> roster = currCourse.getStudentRoster();
            at.setDate(LocalDateTime.now());

            roster.forEach(s -> {
                AttendanceRecord ar = new AttendanceRecord();
                ar.setCourse(currCourse);
                ar.setStudent(s);
                ar.setAttendanceID();

                at.addAttendanceRecord(ar);
            });

            DatabaseHandler.getInstance().addTabletoCourse(currCourse, at);
            return at;
        } else {
            return attendanceTableOp.get();
        }
    }

    // sets item properties for the grid
    @SuppressWarnings("unchecked")
    private static void setRecordItemProperties(Item item, Student student, AttendanceRecord record) {
        item.getItemProperty(FIRST_NAME).setValue(student.getFirstName());
        item.getItemProperty(LAST_NAME).setValue(student.getLastName());
        item.getItemProperty(BANNER_NUMBER).setValue(student.getId());
        item.getItemProperty(BARCODE).setValue(student.getBarcode());
        item.getItemProperty(STATUS).setValue(record.getStatus());
    }

    @SuppressWarnings("unchecked")
    private void updateAttendanceGrid(String barcode, AttendanceRecord.Status status) {
        Optional.ofNullable(barcodeToItemMap.get(barcode))
                .ifPresent(items -> items.forEach(item -> item.getItemProperty(STATUS).setValue(status)));
    }

    @Override
    public void enter(ViewChangeEvent event) {
        if (course == null) {
            getUI().getNavigator().navigateTo(MainMenuView.NAME);
        }
    }


    private void logOut() {
        getUI().getSession().close();
        getUI().getNavigator().navigateTo(MainMenuView.NAME);
    }

}
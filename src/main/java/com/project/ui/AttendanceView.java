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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.project.backend.DatabaseHandler;
import com.project.backend.AttendanceRecord;
import com.project.backend.AttendanceTable;
import com.project.backend.ClassDay;
import com.project.backend.Course;
import com.project.backend.Student;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class AttendanceView extends CustomComponent implements View {
    private static final long serialVersionUID = 8909424502341897571L;

    private Course course;
    private Grid attendanceGrid;
    private LocalDateTime todayClassDay;
    private IndexedContainer attendanceRecords;
    private Button toCourseViewButton;

    private static final String BACK_BUTTON_ID = "backButton";
    
    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String BANNER_NUMBER = "Banner Number";
    private static final String BARCODE = "Barcode";
    private static final String PRESENT = "Attendance Status";

    //Creates the view
    AttendanceView(Course course) {
        this.course = course;
        //creates all compontents used in view
        attendanceGrid = new Grid();
        todayClassDay = LocalDateTime.now();
        todayClassDay = todayClassDay.truncatedTo(ChronoUnit.DAYS);
        BarcodeScannerComponent barcodeScannerComponent = new BarcodeScannerComponent();
        attendanceRecords = getAttendanceRecords(todayClassDay,course);
        Label label = new Label();
        toCourseViewButton = new Button();

        //configures components
        configureGrid();
        configureLabel(label);
        configureBackButton(toCourseViewButton);

        attendanceGrid.setContainerDataSource(attendanceRecords);

        barcodeScannerComponent.onBarcodeScanned(s -> {
            DatabaseHandler.studentScanned(s,course);
            updateAttendanceGrid(s, AttendanceStatus.PRESENT);
        });

        VerticalLayout layout = new VerticalLayout(label, attendanceGrid, barcodeScannerComponent, toCourseViewButton);
        layout.setMargin(true);
        
        //adds components to layout and alligns them.
        layout.setSizeFull();
        layout.setSpacing(true);
        layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(attendanceGrid, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(barcodeScannerComponent, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(toCourseViewButton, Alignment.MIDDLE_CENTER);

        setCompositionRoot(layout);
    }

    //configures grid, sets to width to 100%
    private void configureGrid() {
        attendanceGrid.setContainerDataSource(attendanceRecords);
        attendanceGrid.setWidth("100%");
    }

    //configures back button, if clicked it will go to course view
    private void configureBackButton(Button backButton) {
        toCourseViewButton.setId(BACK_BUTTON_ID);
        toCourseViewButton.setCaption("Back to Course View");
        toCourseViewButton.addClickListener(e -> {
            getUI().setContent(new CourseView(course.getCourseCode()));
        });
    }

    //Configures label, it displays "courseCode - courseName" at the top of the view
    private void configureLabel(Label label) {
        String courseCode = course.getCourseCode();
        String courseName = course.getCourseName();
        String caption = String.format("Attendance for: %s - %s", courseCode, courseName);
        label.setCaption(caption);
    }

    //accepts a barcode, if the barcode is found it will mark the status for that student as present
    @SuppressWarnings("unchecked")
    private void updateAttendanceGrid(String barcode, AttendanceStatus status) {
        Item record = attendanceRecords.getItem(barcode);
        if (record != null) {
            record.getItemProperty(PRESENT).setValue(status);
        }
    }

    
    //sets the content of each grid. If the students are coming from the absent list, they will
    //be marked as absent, and if they are coming from the attending list, they will be
    //marked as present
    private static IndexedContainer getAttendanceRecords(LocalDateTime classDay, Course currCourse) {

        IndexedContainer attendanceRecords = new IndexedContainer();


        attendanceRecords.addContainerProperty(FIRST_NAME, String.class, "");
        attendanceRecords.addContainerProperty(LAST_NAME, String.class, "");
        attendanceRecords.addContainerProperty(BANNER_NUMBER, String.class, "");
        attendanceRecords.addContainerProperty(BARCODE, String.class, "");
        attendanceRecords.addContainerProperty(PRESENT, AttendanceRecord.Status.class, AttendanceRecord.Status.ABSENT);
        
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        Optional<AttendanceTable> attendanceTableOp = currCourse.getAttedance().stream()
        		.filter(ar -> ar.getDate().truncatedTo(ChronoUnit.DAYS).equals(now))
        		.findAny();
        
        if (!attendanceTableOp.isPresent()) {
        	AttendanceTable at = new AttendanceTable();
        	List<Student> roster = currCourse.getStudentRoster();
        	
        	roster.forEach(s -> {
        		AttendanceRecord ar = new AttendanceRecord();
        		ar.setCourseCode(currCourse);
        		ar.setStudentId(s);
        		ar.setAttendanceID();
        		at.addAttendanceRecord(ar);
        	});
        	
        	at.setRecords(roster);
        }
        
        for (Student student : currCourse.getStudentRoster()) {
            Item item = attendanceRecords.addItem(student.getBarcode());
            setRecordItemProperties(item, student, DatabaseHandler.getRecordById( currCourse.getCourseCode(),student.getId()));
        }

        return attendanceRecords;
    }

    //sets item properties for the grid
    @SuppressWarnings("unchecked")
    private static void setRecordItemProperties(Item item, Student student, AttendanceRecord record) {
        item.getItemProperty(FIRST_NAME).setValue(student.getFirstName());
        item.getItemProperty(LAST_NAME).setValue(student.getLastName());
        item.getItemProperty(BANNER_NUMBER).setValue(student.getId());
        item.getItemProperty(BARCODE).setValue(student.getBarcode());
        item.getItemProperty(PRESENT).setValue(record.getStatus());
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }

    //Checks if a given date is equal to todays day of month, month and year.


    //Returns ClassDay object for today. If one isn't already existing, it gets created and uploaded
    //to database.
 

   
}
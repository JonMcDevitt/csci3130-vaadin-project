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
    private Button toCourseViewButton;
    private Label header;
    private BarcodeScannerComponent barcodeScannerComponent;

    private IndexedContainer attendanceRecords;
    private Map<String, List<Item>> barcodeToItemMap;

    private static final String BACK_BUTTON_ID = "backButton";

    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String BANNER_NUMBER = "Banner Number";
    private static final String BARCODE = "Barcode";
    private static final String STATUS = "Attendance Status";

    public AttendanceView(Course course) {
        this(course, LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
    }

    // Creates the view
    public AttendanceView(Course course, LocalDateTime classDate) {
        this.course = course;

        // creates all components used in view
        attendanceGrid = new Grid();
        toCourseViewButton = new Button();
        header = new Label();
        barcodeScannerComponent = new BarcodeScannerComponent();

        attendanceRecords = makeAttendanceRecordContainer(classDate, course);
        attendanceGrid.setContainerDataSource(attendanceRecords);
        barcodeToItemMap = makeBarcodeToItemMap(attendanceRecords);

        barcodeScannerComponent.onBarcodeScanned(s -> {
            DatabaseHandler.studentScanned(s, course);
            updateAttendanceStatus(s, AttendanceRecord.Status.PRESENT);
        });

        // configures components
        configureAttendanceGrid(attendanceGrid);
        configureLabel(header, classDate);
        configureBackButton(toCourseViewButton);

        VerticalLayout layout = new VerticalLayout(header, attendanceGrid, barcodeScannerComponent, toCourseViewButton);
        layout.setMargin(true);

        // adds components to layout and alligns them.
        layout.setSizeFull();
        layout.setSpacing(true);
        layout.setComponentAlignment(header, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(attendanceGrid, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(barcodeScannerComponent, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(toCourseViewButton, Alignment.MIDDLE_CENTER);

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
        	
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void preCommit(CommitEvent commitEvent) throws CommitException {
				
			}

			@Override
			public void postCommit(CommitEvent commitEvent) throws CommitException {
				Item editedItem = attendanceRecords.getItem(attendanceGrid.getEditedItemId());
				DatabaseHandler.studentChangedManually(editedItem.getItemProperty(BARCODE).getValue().toString(), course, editedItem.getItemProperty(PRESENT).getValue().toString());
			}
        	
        });
    }

    // configures back button, if clicked it will go to course view
    private void configureBackButton(Button backButton) {
        toCourseViewButton.setId(BACK_BUTTON_ID);
        toCourseViewButton.setCaption("Back to Course View");
        toCourseViewButton.addClickListener(e -> {
            getUI().setContent(new CourseView(course.getCourseCode()));
        });
    }

    // Configures label, it displays "courseCode - courseName" at the top of the
    // view
    private void configureLabel(Label label, LocalDateTime classDate) {
        String courseCode = course.getCourseCode();
        String courseName = course.getCourseName();
        String caption = String.format("Attendance for: %s - %s on %s", courseCode, courseName, classDate.toString());
        label.setCaption(caption);
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
        return attendanceRecords.getItemIds().stream()
                .map(attendanceRecords::getItem)
                .collect(groupingBy(item -> (String) item.getItemProperty(BARCODE).getValue()));
    }

    private AttendanceTable getAttendanceTable(Course currCourse, LocalDateTime classDate) {
        Optional<AttendanceTable> attendanceTableOp = currCourse.getAttedance().stream()
                .filter(ar -> ar.getDate().truncatedTo(ChronoUnit.DAYS).equals(classDate)).findAny();

        if (!attendanceTableOp.isPresent()) {
            AttendanceTable at = new AttendanceTable();
            List<Student> roster = currCourse.getStudentRoster();
            at.setDate(LocalDateTime.now());

            roster.forEach(s -> {
                AttendanceRecord ar = new AttendanceRecord();
                ar.setCourseCode(currCourse);
                ar.setStudentId(s);
                ar.setStudent(s);
                ar.setAttendanceID();

                at.addAttendanceRecord(ar);
            });

            DatabaseHandler.addTabletoCourse(currCourse, at);
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
    private void updateAttendanceStatus(String barcode, AttendanceRecord.Status status) {
        Optional.ofNullable(barcodeToItemMap.get(barcode))
                .ifPresent(items -> items.forEach(item -> item.getItemProperty(STATUS).setValue(status)));
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }

}
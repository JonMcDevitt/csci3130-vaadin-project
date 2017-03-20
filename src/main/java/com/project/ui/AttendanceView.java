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

import java.util.Date;
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
    private ClassDay todayClassDay;
    private BarcodeScannerComponent barcodeScannerComponent;
    private IndexedContainer attendanceRecords;
    private Button toCourseViewButton;
    private Label label;

    public static final String BACK_BUTTON_ID = "backButton";
    
    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String BANNER_NUMBER = "Banner Number";
    private static final String BARCODE = "Barcode";
    private static final String PRESENT = "Present";

    public AttendanceView(Course course) {
        this.course = course;

        attendanceGrid = new Grid();
        todayClassDay = getTodayClassDay(course);
        barcodeScannerComponent = new BarcodeScannerComponent();
        attendanceRecords = getAttendanceRecords(todayClassDay);
        label = new Label();
        toCourseViewButton = new Button();


        configureGrid(todayClassDay);
        configureLabel(label);
        configureBackButton(toCourseViewButton);

        attendanceGrid.setContainerDataSource(attendanceRecords);

        barcodeScannerComponent.onBarcodeScanned(s -> {
            todayClassDay.studentScanned(s);
            updateAttendanceGrid(s, AttendanceStatus.PRESENT);
        });

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        layout.addComponents(label, attendanceGrid, barcodeScannerComponent, toCourseViewButton);
        layout.setSizeFull();
        layout.setSpacing(true);
        layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(attendanceGrid, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(barcodeScannerComponent, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(toCourseViewButton, Alignment.MIDDLE_CENTER);

        setCompositionRoot(layout);
    }

    private void configureGrid(ClassDay classDay) {
        attendanceGrid.setContainerDataSource(attendanceRecords);
        attendanceGrid.setWidth("100%");
    }

    private void configureBackButton(Button backButton) {
        toCourseViewButton.setId(BACK_BUTTON_ID);
        toCourseViewButton.setCaption("Back to Course View");
        toCourseViewButton.addClickListener(e -> {
            getUI().setContent(new CourseView(course));
        });
    }

    private void configureLabel(Label label) {
        String courseCode = course.getCourseCode();
        String courseName = course.getCourseName();
        String caption = String.format("Attendance for: %s %s", courseCode, courseName);
        label.setCaption(caption);
    }

    @SuppressWarnings("unchecked")
    private void updateAttendanceGrid(String barcode, AttendanceStatus status) {
        Item record = attendanceRecords.getItem(barcode);
        if (record != null) {
            record.getItemProperty(PRESENT).setValue(status);
        }
    }

    private static IndexedContainer getAttendanceRecords(ClassDay classDay) {

        IndexedContainer attendanceRecords = new IndexedContainer();


        attendanceRecords.addContainerProperty(FIRST_NAME, String.class, "");
        attendanceRecords.addContainerProperty(LAST_NAME, String.class, "");
        attendanceRecords.addContainerProperty(BANNER_NUMBER, String.class, "");
        attendanceRecords.addContainerProperty(BARCODE, String.class, "");
        attendanceRecords.addContainerProperty(PRESENT, AttendanceStatus.class, AttendanceStatus.ABSENT);

        if (classDay != null) {
            for (Student student : classDay.getAbsentStudents()) {
                Item item = attendanceRecords.addItem(student.getBarcode());
                setRecordItemProperties(item, student, AttendanceStatus.ABSENT);
            }
            for (Student student : classDay.getAttendingStudents()) {
                Item item = attendanceRecords.addItem(student.getBarcode());
                setRecordItemProperties(item, student, AttendanceStatus.PRESENT);
            }
        }

        return attendanceRecords;
    }

    @SuppressWarnings("unchecked")
    private static void setRecordItemProperties(Item item, Student student, AttendanceStatus status) {
        item.getItemProperty(FIRST_NAME).setValue(student.getFirstName());
        item.getItemProperty(LAST_NAME).setValue(student.getLastName());
        item.getItemProperty(BANNER_NUMBER).setValue(student.getId());
        item.getItemProperty(BARCODE).setValue(student.getBarcode());
        item.getItemProperty(PRESENT).setValue(status);
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }

    private static boolean isToday(Date compareDate) {
        Date date = new Date();
        if (compareDate.getYear() == date.getYear() && compareDate.getDate() == date.getDate()
                && date.getMonth() == compareDate.getMonth()) {
            return true;
        }
        return false;

    }

    private ClassDay getTodayClassDay(Course course) {
        for (ClassDay c : course.getClassDays()) {
            if (isToday(c.getStartTime())) {
                return c;
            }
        }
        
        ClassDay classDay = new ClassDay(new Date(), new Date(), course.getStudentRoster());
        course.getClassDays().add(classDay);

        return classDay;
    }

    private enum AttendanceStatus {
        PRESENT, ABSENT;

        public String toString() {
            return name();
        }
    }
}
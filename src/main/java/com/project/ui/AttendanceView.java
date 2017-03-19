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
    
    private Grid attendanceGrid;
	private ClassDay todayClassDay;
	private BarcodeScannerComponent barcodeScannerComponent;
	private IndexedContainer attendanceRecords;
	private Button toCourseView;
	private Label label;
	
    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String BANNER_NUMBER = "Banner Number";
    private static final String BARCODE = "Barcode";
    private static final String PRESENT = "Present";
	
    
    //Sets up the view of AttendanceView
	public AttendanceView(Course course) {
		//make instances of all needed components
	    barcodeScannerComponent = new BarcodeScannerComponent();
		attendanceGrid = new Grid();
        todayClassDay = getTodayClassDay(course);
		attendanceRecords = getAttendanceRecords(todayClassDay);
		
		//configure, add and align all components
		configureGrid(todayClassDay);
		configureLabel(course);
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		configureBackButton(course);
		layout.addComponents(label, attendanceGrid,barcodeScannerComponent, toCourseView);
		layout.setSizeFull();
		layout.setSpacing(true);
		layout.setComponentAlignment(label,Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(attendanceGrid,Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(barcodeScannerComponent,Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(toCourseView,Alignment.MIDDLE_CENTER);
		
		attendanceGrid.setContainerDataSource(attendanceRecords);

		//When the barcode scanner scans something, it will update the status for the appropriate
		//student to be "Present"
		barcodeScannerComponent.onBarcodeScanned(s -> {
		    todayClassDay.studentScanned(s);
		    updateAttendanceGrid(s,PRESENT);
		});
		setCompositionRoot(layout);
	}
	
	//Configures the grid to show information from the attendanceRecords class for each student
	private void configureGrid(ClassDay classDay){
		attendanceGrid.setContainerDataSource(attendanceRecords);
		attendanceGrid.setWidth("100%");
	}
	
	//Configures a back button to go back to the course view
	private void configureBackButton(Course course){
		toCourseView = new Button("Back to Course View");
		toCourseView.addClickListener(e ->{
    		getUI().setContent(new CourseView(course));
		});
	}
	
	//Configures a label to say the course code and name
	private void configureLabel(Course course){
		label = new Label("Attendance for: " +course.getCourseCode()+" - "+course.getCourseName());
	}

	//updates the attendance grid whenever a barcode belonging to a student in the roster is scanned
	@SuppressWarnings("unchecked")
    private void updateAttendanceGrid(String barcode, String present) {
	    Item record = attendanceRecords.getItem(barcode);
	    if (record != null) {
	        record.getItemProperty(PRESENT).setValue(present);
	    }
    }
	
	//This sets the appropriate value for everything in the grid
	@SuppressWarnings("unchecked")
    private static IndexedContainer getAttendanceRecords(ClassDay classDay) {
		IndexedContainer attendanceRecords = new IndexedContainer();
		
		attendanceRecords.addContainerProperty(FIRST_NAME, String.class, "");
		attendanceRecords.addContainerProperty(LAST_NAME, String.class, "");
		attendanceRecords.addContainerProperty(BANNER_NUMBER, String.class, "");
		attendanceRecords.addContainerProperty(BARCODE, String.class, "");
		attendanceRecords.addContainerProperty(PRESENT, String.class, "Absent");
		
		//Marks students as absent if the students come from the absent list, and present
		//if from the attending list
		if (classDay != null) {
			for (Student s : classDay.getAbsentStudents()) {
				Item item = attendanceRecords.addItem(s.getBarcode());
				item.getItemProperty(FIRST_NAME).setValue(s.getFirstName());
				item.getItemProperty(LAST_NAME).setValue(s.getLastName());
				item.getItemProperty(BANNER_NUMBER).setValue(s.getId());
				item.getItemProperty(BARCODE).setValue(s.getBarcode());
				item.getItemProperty(PRESENT).setValue("Absent");
			}
			for (Student s : classDay.getAttendingStudents()) {
				Item item = attendanceRecords.addItem(s.getBarcode());
				item.getItemProperty(FIRST_NAME).setValue(s.getFirstName());
				item.getItemProperty(LAST_NAME).setValue(s.getLastName());
				item.getItemProperty(BANNER_NUMBER).setValue(s.getId());
				item.getItemProperty(BARCODE).setValue(s.getBarcode());
				item.getItemProperty(PRESENT).setValue("Present");
			}
		}
		
		return attendanceRecords;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
	//Compares two dates to see if they are the same, (will return true if they have the same 
	//day of the month, year, and month.
	private static boolean isToday(Date compareDate){
		Date date = new Date();
		if(compareDate.getYear()==date.getYear()&&compareDate.getDate()==date.getDate()
				&&date.getMonth()==compareDate.getMonth()){
			return true;
		}
		return false;
			
	}
	
	//Returns a ClassDay object for today
	private ClassDay getTodayClassDay(Course course) {
	    for (ClassDay c : course.getClassDays()) {
            if (isToday(c.getStartTime())){
                return c;
            }
        }
	    return new ClassDay(new Date(), new Date(), course.getStudentRoster());
	}	
}
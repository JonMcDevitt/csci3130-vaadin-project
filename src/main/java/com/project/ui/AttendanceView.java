package com.project.ui;

import java.util.Date;

import com.project.backend.ClassDay;
import com.project.backend.Course;
import com.project.backend.Student;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

public class AttendanceView extends CustomComponent implements View {
    
    private static final long serialVersionUID = 8909424502341897571L;
    
    private Grid attendanceGrid;
	private ClassDay todayClassDay;
	private BarcodeScanner barcodeScanner;
	private IndexedContainer attendanceRecords;
	
    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String BANNER_NUMBER = "Banner Number";
    private static final String BARCODE = "Barcode";
    private static final String PRESENT = "Present";
	
	public AttendanceView(Course course) {
	    barcodeScanner = new BarcodeScanner();
		attendanceGrid = new Grid();
        todayClassDay = getTodayClassDay(course);
		attendanceRecords = getAttendanceRecords(todayClassDay);
		
		attendanceGrid.setContainerDataSource(attendanceRecords);

		barcodeScanner.onBarcodeScanned(s -> {
		    todayClassDay.studentScanned(s);
		    updateAttendanceGrid(s, true);
		});
		
		VerticalLayout layout = new VerticalLayout(barcodeScanner, attendanceGrid);
		setCompositionRoot(layout);
	}

	@SuppressWarnings("unchecked")
    private void updateAttendanceGrid(String barcode, boolean present) {
	    Item record = attendanceRecords.getItem(barcode);
	    if (record != null) {
	        record.getItemProperty(PRESENT).setValue(present);
	    }
    }
	
	@SuppressWarnings("unchecked")
    private static IndexedContainer getAttendanceRecords(ClassDay classDay) {
		IndexedContainer attendanceRecords = new IndexedContainer();
		
		attendanceRecords.addContainerProperty(FIRST_NAME, String.class, "");
		attendanceRecords.addContainerProperty(LAST_NAME, String.class, "");
		attendanceRecords.addContainerProperty(BANNER_NUMBER, String.class, "");
		attendanceRecords.addContainerProperty(BARCODE, String.class, "");
		attendanceRecords.addContainerProperty(PRESENT, Boolean.class, false);
		
		if (classDay != null) {
			for (Student s : classDay.getAbsentStudents()) {
				Item item = attendanceRecords.addItem(s.getBarcode());
				item.getItemProperty(FIRST_NAME).setValue(s.getFirstName());
				item.getItemProperty(LAST_NAME).setValue(s.getLastName());
				item.getItemProperty(BANNER_NUMBER).setValue(s.getId());
				item.getItemProperty(BARCODE).setValue(s.getBarcode());
				item.getItemProperty(PRESENT).setValue(false);
			}
			for (Student s : classDay.getAttendingStudents()) {
				Item item = attendanceRecords.addItem(s.getBarcode());
				item.getItemProperty(FIRST_NAME).setValue(s.getFirstName());
				item.getItemProperty(LAST_NAME).setValue(s.getLastName());
				item.getItemProperty(BANNER_NUMBER).setValue(s.getId());
				item.getItemProperty(BARCODE).setValue(s.getBarcode());
				item.getItemProperty(PRESENT).setValue(true);
			}
		}
		
		return attendanceRecords;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
	private static boolean isToday(Date compareDate){
		Date date = new Date();
		if(compareDate.getYear()==date.getYear()&&compareDate.getDate()==date.getDate()
				&&date.getMonth()==compareDate.getMonth()){
			return true;
		}
		return false;
			
	}
	
	private ClassDay getTodayClassDay(Course course) {
	    for (ClassDay c : course.getClassDays()) {
            if (isToday(c.getStartTime())){
                return c;
            }
        }
	    return new ClassDay(new Date(), new Date(), course.getStudentRoster());
	}	
}
package com.project.ui;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import com.project.backend.ClassDay;
import com.project.backend.Course;
import com.project.backend.Student;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

public class AttendanceView extends CustomComponent implements View {
	private Course course;
	private Grid attendanceGrid;
	private Date date;
	
	public AttendanceView(Course course){
		date = new Date();
		this.course=course;
		BarcodeScanner scanner = new BarcodeScanner();
		attendanceGrid = new Grid();
		
		List<ClassDay> classDays = course.getClassDays();
		ClassDay todayClassDay = null;
		for (ClassDay c : classDays) {
			if (isToday(c.getStartTime())){
				todayClassDay=c;
				break;
			}
		}
		if(todayClassDay==null){
			todayClassDay = new ClassDay(new Date(), new Date(), course.getStudentRoster());
			course.getClassDays().add(todayClassDay);
		}
		configureGrid(todayClassDay);
		VerticalLayout layout = new VerticalLayout(scanner, attendanceGrid);
		setCompositionRoot(layout);
	}
	private void configureGrid(ClassDay classDay){
		List<AttendanceRecordEntry> list = getAttendanceRecordList(classDay);
		
		attendanceGrid.setContainerDataSource(getAttendanceRecords(classDay));
	}
	
	private static IndexedContainer getAttendanceRecords(ClassDay classDay) {
		IndexedContainer attendanceRecords = new IndexedContainer();
		
		final String FIRST_NAME = "First Name";
		final String LAST_NAME = "Last Name";
		final String BANNER_NUMBER = "Banner Number";
		final String BARCODE = "Barcode";
		final String PRESENT = "Present";

		attendanceRecords.addContainerProperty(FIRST_NAME, String.class, "");
		attendanceRecords.addContainerProperty(LAST_NAME, String.class, "");
		attendanceRecords.addContainerProperty(BANNER_NUMBER, String.class, "");
		attendanceRecords.addContainerProperty(BARCODE, String.class, "");
		attendanceRecords.addContainerProperty(PRESENT, Boolean.class, false);
		
		if (classDay != null) {
			for (Student s : classDay.getAbsentStudents()) {
				Item item = attendanceRecords.addItem(s.getId());
				item.getItemProperty(FIRST_NAME).setValue(s.getFirstName());
				item.getItemProperty(LAST_NAME).setValue(s.getLastName());
				item.getItemProperty(BANNER_NUMBER).setValue(s.getId());
				item.getItemProperty(BARCODE).setValue(s.getBarcode());
				item.getItemProperty(PRESENT).setValue(false);
			}
			for (Student s : classDay.getAttendingStudents()) {
				Item item = attendanceRecords.addItem(s.getId());
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
	
	public List<AttendanceRecordEntry> getAttendanceRecordList(ClassDay classDay){
		List<AttendanceRecordEntry> list = new ArrayList<AttendanceRecordEntry>();
		for (Student s : classDay.getAbsentStudents()) {
			list.add(new AttendanceRecordEntry(s.getFirstName(), s.getLastName(), s.getId(), s.getBarcode(), false));
		}
		for (Student s : classDay.getAttendingStudents()) {
			list.add(new AttendanceRecordEntry(s.getFirstName(), s.getLastName(), s.getId(), s.getBarcode(), true));
		}
		return list;
	}
	
	private static boolean isToday(Date compareDate){
		Date date = new Date();
		if(compareDate.getYear()==date.getYear()&&compareDate.getDate()==date.getDate()
				&&date.getMonth()==compareDate.getMonth()){
			return true;
		}
		return false;
			
	}
	
}

class AttendanceRecordEntry{
	private String firstName;
	private String lastName;
	private String bannerNumber;
	private String barcode;
	private boolean present;
	
	public AttendanceRecordEntry(String firstName, String lastName, String bannerNumber, String barcode,
			boolean present) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.bannerNumber = bannerNumber;
		this.barcode = barcode;
		this.present = present;
	}
}




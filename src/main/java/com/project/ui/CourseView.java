package com.project.ui;

import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonActionListener;

import com.project.backend.ClassDay;
import com.project.backend.Course;
import com.project.backend.Student;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/*
 * Created by Jili on 2017-03-01
 * 
 */

public class CourseView extends CustomComponent implements View {

    public static final String NAME = "CourseInfo";
    private static final String WIDTH_TEXTFIELD_DEFAULT = "300px";
    private Button goToMain;
    private Button editStudent;
    private Student currStudent;
    private ClassDay currDay;
    private Label courseName;
    private VerticalLayout popupContent;
    private Grid studentGrid = new Grid();
    VerticalLayout mainLayout;

    public CourseView(){}
    
    //a overloading constructor uses a Course type parameter to set up the UI content
    public CourseView(Course course) {
        setSizeFull();

        configureComponents(course);
        createLayout();
    }

    private void configureComponents(Course course) {

    	goToMain = new Button("Back to main page");
    	courseName = new Label(course.getCourseName());
    	editStudent = new Button("Edit selected Student");
    	configurePopup();
    	//Display the parameter -- course's student roaster
    	studentGrid.setContainerDataSource(new BeanItemContainer<>(Student.class, course.getStudentRoster()));
    	studentGrid.setColumnOrder("id");    	
    	studentGrid.addColumn(Integer.class);
    	//goToMain goes back to the main page
    	goToMain.addClickListener(e -> {
    		getUI().getNavigator().navigateTo("");
    	});
    	editStudent.addClickListener(e -> {
    		if((Student) studentGrid.getSelectedRow()!=null){
    			currStudent =(Student) studentGrid.getSelectedRow();
    			editStudentButton(currStudent);
    			popupContent.setVisible(true);
    		}
    	});
    }
    public void createLayout() {
        VerticalLayout mainLayout = new VerticalLayout(courseName, goToMain, editStudent, studentGrid);
        HorizontalLayout realmainLayout = new HorizontalLayout(mainLayout, popupContent);
        mainLayout.setSpacing(true);
        setCompositionRoot(realmainLayout);       
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }
    public void configurePopup(){
    	popupContent = new VerticalLayout();
    	HorizontalLayout popButtons = new HorizontalLayout();
    	TextField id = new TextField("ID");
    	TextField fname = new TextField("First Name");
    	TextField lname = new TextField("Last Name");
    	popupContent.addComponent(id);
    	popupContent.addComponent(fname);
    	popupContent.addComponent(lname);
    	Button saveButton = new Button("Save");
    	Button cancelButton = new Button("Cancel");
    	popButtons.addComponent(saveButton);
    	popButtons.addComponent(cancelButton);
    	popupContent.addComponent(popButtons);
    	popupContent.setVisible(false);
    	saveButton.addClickListener(e -> {
    	    changeStudent(id,fname,lname);
    	});
    	cancelButton.addClickListener(e -> {
    		popupContent.setVisible(false);
    	});
    }
    public void editStudentButton(Student stud){
    	TextField idEdit = (TextField)popupContent.getComponent(0);
    	TextField firstEdit = (TextField)popupContent.getComponent(1);
    	TextField lastEdit = (TextField)popupContent.getComponent(2);
    	idEdit.setValue(stud.getId());
    	firstEdit.setValue(stud.getFirstName());
    	lastEdit.setValue(stud.getLastName());

    }
    public void changeStudent(TextField idnum, TextField firstname, TextField lastname){
    	currStudent.setId(idnum.getValue());
		currStudent.setFirstName(firstname.getValue());
		currStudent.setLastName(lastname.getValue());
		popupContent.setVisible(false);
		Notification.show("Saved edit");
		studentGrid.refreshRows(currStudent);
    }
}

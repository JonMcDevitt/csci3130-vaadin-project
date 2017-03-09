package com.project.ui;

import com.project.backend.RegisteredUserDatabase;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class NewStudentView extends CustomComponent implements View {

    public static final String NAME = "NewStudentView";
    private static final String WIDTH_TEXTFIELD_DEFAULT = "300px";

    private final TextField id;
    private final TextField firstName;
    private final TextField lastName;

    private final Button addButton;
    private final Button cancelButton;

    public NewStudentView() {
        setSizeFull();

        id = new TextField("Student ID");
        firstName = new TextField("First Name: ");
        lastName = new TextField("Last Name: ");

        addButton = new Button("Add");
        cancelButton = new Button("Cancel");

        configureComponents();
        configureActions();
        setCompositionRoot(createLayout());
    }

    private void configureComponents() {

        id.setInputPrompt("e.g. B00101111 ");
        firstName.setInputPrompt("Enter first name.");
        lastName.setInputPrompt("Enter last name.");
    }

    private void configureActions() {
       //need to implement addButton actionListener to create a student object and add it to the student database

    	cancelButton.addClickListener(e -> {
    		getUI().getNavigator().navigateTo(CourseView.NAME);
    	});
    }

    private Layout createLayout() {
        HorizontalLayout buttons = new HorizontalLayout(
                addButton, cancelButton);
        buttons.setSpacing(true);
        buttons.setMargin(new MarginInfo(true, true));
        VerticalLayout fields = new VerticalLayout(
                id, firstName, lastName, buttons);
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, true));
        fields.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLACK);
        return viewLayout;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        id.focus();
    }
}

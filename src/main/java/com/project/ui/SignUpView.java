package com.project.ui;

import java.util.ArrayList;
import java.util.List;

import com.project.backend.RegisteredUser;
import com.project.backend.RegisteredUserDatabase;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Created by jackieflash on 2017-02-23.
 */
public class SignUpView extends CustomComponent implements View {

    public static final String NAME = "SignUp";
    private static final String WIDTH_TEXTFIELD_DEFAULT = "300px";

    private final TextField userEmail;
    private final TextField firstName;
    private final TextField lastName;
    private final TextField department;
    private final PasswordField password;
    private final PasswordField confirmPassword;
    private final Button signUpButton;
    private final Button clearButton;
    private final Button cancelButton;
    private final List<Component> components;

    SignUpView() {
        setSizeFull();

        userEmail = new TextField("User Email: ");
        password = new PasswordField("Password: ");
        confirmPassword = new PasswordField("Confirm Password: ");
        firstName = new TextField("First Name: ");
        lastName = new TextField("Last Name: ");
        department = new TextField("Department: ");
        signUpButton = new Button("Sign Up");
        clearButton = new Button("Clear");
        cancelButton = new Button("Cancel");
        components = new ArrayList<>();

        configureComponents();
        configureActions();
        setCompositionRoot(createLayout());
    }

    private void configureComponents() {

        components.add(userEmail);
        components.add(password);
        components.add(confirmPassword);
        components.add(firstName);
        components.add(lastName);
        components.add(department);
        components.add(signUpButton);
        components.add(clearButton);
        components.add(cancelButton);
        //Input Prompts
        userEmail.setInputPrompt("Enter email here.");
        firstName.setInputPrompt("Enter first name.");
        lastName.setInputPrompt("Enter last name.");
        department.setInputPrompt("Enter department.");

        for (Component component : components) {
            if (component instanceof AbstractTextField) {
                component.setWidth(WIDTH_TEXTFIELD_DEFAULT);
                AbstractTextField textField = (AbstractTextField) component;
                textField.setRequired(true);
                textField.setInvalidAllowed(false);
            }
        }
    }

    private void configureActions() {
        RegisteredUserDatabase userDatabase = RegisteredUserDatabase.getInstance();

        signUpButton.addClickListener((Button.ClickListener) clickEvent -> {
            RegisteredUser newUser = new RegisteredUser(userEmail.getValue(), password.getValue(),
                    firstName.getValue(), lastName.getValue(), department.getValue());

            boolean isValidUser = newUser.isValidUser(); 
            boolean isValid = newUser.isValid(userDatabase);
            boolean passwordConfirm = password.getValue().equals(confirmPassword.getValue());

            if (isValidUser && isValid && passwordConfirm) {
                this.getUI().setContent(new LoginView());
                Notification.show("Signed up with email: " + userEmail.getValue());
                userDatabase.save(newUser);
            } else if (isValidUser) {
                Notification.show("Missing fields.");
            } else {
                Notification.show(userEmail.getValue() + " already in use.");
            }
        });

        clearButton.addClickListener((Button.ClickListener) clickEvent -> {
            userEmail.clear();
            firstName.clear();
            lastName.clear();
            department.clear();
            password.clear();
            confirmPassword.clear();
        });
        
        cancelButton.addClickListener((Button.ClickListener) clickEvent -> this.getUI().setContent(new LoginView()));
    }

    private Layout createLayout() {
        HorizontalLayout buttons = new HorizontalLayout(
                signUpButton, clearButton, cancelButton);
        buttons.setSpacing(true);
        buttons.setMargin(new MarginInfo(true, true));
        VerticalLayout fields = new VerticalLayout(
                userEmail, firstName, lastName, department, password,
                confirmPassword, buttons);
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
        userEmail.focus();
    }
}

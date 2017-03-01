package com.project;

import java.util.ArrayList;
import java.util.List;

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
    private static final String USER_INPUT_PROMPT_MESSAGE = "\"Your email here (e.g. joe_blow@mail.com)\"";

    private final TextField userName;
    private final TextField userEmail;
    private final TextField firstName;
    private final TextField lastName;
    private final TextField department;
    private final PasswordField password;
    private final PasswordField confirmPassword;
    private final Button signUpButton;
    private final Button clearButton;
    private final List<Component> components;

    public SignUpView() {
        setSizeFull();

        userName = new TextField("Username: ");
        userEmail = new TextField("User Email: ");
        password = new PasswordField("Password: ");
        confirmPassword = new PasswordField("Confirm Password: ");
        firstName = new TextField("First Name: ");
        lastName = new TextField("Last Name: ");
        department = new TextField("Department: ");
        signUpButton = new Button("Sign Up");
        clearButton = new Button("Clear");
        components = new ArrayList<Component>();

        configureComponents();
        configureActions();
        setCompositionRoot(createLayout());
    }

    private void configureComponents() {
        components.add(userName);
        components.add(userEmail);
        components.add(password);
        components.add(confirmPassword);
        components.add(firstName);
        components.add(lastName);
        components.add(department);
        components.add(signUpButton);
        components.add(clearButton);

        for (Component component : components) {
            if (component instanceof AbstractTextField) {
                component.setWidth(WIDTH_TEXTFIELD_DEFAULT);
                AbstractTextField textField = (AbstractTextField) component;
                textField.setRequired(true);
                textField.setInputPrompt(USER_INPUT_PROMPT_MESSAGE);
                textField.setInvalidAllowed(false);
            }
        }
    }

    private void configureActions() {
        RegisteredUserDatabase userDatabase = RegisteredUserDatabase.getInstance();

        signUpButton.addClickListener((Button.ClickListener) clickEvent -> {
            RegisteredUser newUser = new RegisteredUser(userName.getValue(), userEmail.getValue(), password.getValue(),
                    firstName.getValue(), lastName.getValue(), department.getValue());

            boolean isValidUser = newUser.isValidUser();
            boolean isValid = newUser.isValid(userDatabase);

            if (isValidUser && isValid) {
                this.getUI().setContent(new LoginView());
                Notification.show("Signed up with username: " + userName.getValue());
                userDatabase.save(newUser);
            } else if (isValidUser) {
                Notification.show("Missing fields.");
            } else {
                Notification.show(userName.getValue() + " already used");
            }
        });

        clearButton.addClickListener((Button.ClickListener) clickEvent -> {
            userName.clear();
            userEmail.clear();
            firstName.clear();
            lastName.clear();
            department.clear();
            password.clear();
            confirmPassword.clear();
        });
    }

    private Layout createLayout() {
        HorizontalLayout buttons = new HorizontalLayout(signUpButton, clearButton);
        buttons.setSpacing(true);
        buttons.setMargin(new MarginInfo(true, true));
        VerticalLayout fields = new VerticalLayout(userName, userEmail, firstName, lastName, department, password,
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
        userName.focus();
    }
}

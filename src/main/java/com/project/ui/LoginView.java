package com.project.ui;

import com.project.backend.RegisteredUser;
import com.project.backend.RegisteredUserDatabase;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;

import java.util.Optional;

/**
 * Created by Owner on 2017-02-17.
 */
public class LoginView extends CustomComponent implements View {

    private final RegisteredUserDatabase database = RegisteredUserDatabase.getInstance();

    public static final String NAME = "Login";
    
    private final TextField emailTextField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Button loginButton = new Button();
    private final Button signUpButton = new Button();
    
    private static final int MINIMUM_PASSWORD_LENGTH = 8;

    public LoginView() {
        initUsernameTextField(emailTextField);
        initPasswordField(passwordField);
        initLoginButton(loginButton);
        initSignUpButton(signUpButton);
        
        setSizeFull();
        setupLayout();
    }

    private final void initUsernameTextField(TextField usernameTextField) {
        usernameTextField.addValidator(new EmailValidator("Username must be an email address."));
        usernameTextField.setWidth("300px");
        usernameTextField.setRequired(true);
        usernameTextField.setInputPrompt("Email address or username");
        usernameTextField.addShortcutListener(new ShortcutListener("Login", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object o, Object o1) {
                LoginView.this.loginButton.click();
            }
        });
    }
    
    private final void initPasswordField(PasswordField passwordField) {
        passwordField.addValidator(new PasswordValidator());
        passwordField.setWidth("300px");
        passwordField.setRequired(true);
        passwordField.setValue("");
        passwordField.setNullRepresentation("");
        passwordField.addFocusListener((FieldEvents.FocusListener) focusEvent -> LoginView.this.focus());
        passwordField.addShortcutListener(new ShortcutListener("Login", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object o, Object o1) {
                LoginView.this.loginButton.click();
            }
        });
    }

    private final void initLoginButton(Button loginButton) {
        Button.ClickListener onLoginClicked = event -> 
        {
            
            emailTextField.setValue("test@test.com");
            passwordField.setValue("p4ssw0rd");

            String uName = emailTextField.getValue();
            String pWord = passwordField.getValue();
            Optional<RegisteredUser> r = database.fetchUser(uName);
            if(!r.isPresent()) {
                Notification.show("Invalid username", Notification.Type.ERROR_MESSAGE);
            } else if(pWord.equals(r.get().getPassword())) {
                getSession().setAttribute("user", uName);
                getUI().getNavigator().navigateTo(MainMenuView.NAME);
            } else {
                Notification.show("Error: Invalid password", Notification.Type.ERROR_MESSAGE);
                passwordField.setValue("");
                passwordField.focus();
            }
        };
        
        loginButton.setCaption("Login");
        loginButton.addClickListener(onLoginClicked);
    }
    
    private final void initSignUpButton(Button signUpButton) {
        signUpButton.setCaption("Sign up");
        signUpButton.addClickListener((Button.ClickListener) clickEvent -> getUI().setContent(new SignUpView()));
    }
    
    private final void setupLayout() {
        HorizontalLayout buttons = new HorizontalLayout(loginButton, signUpButton);
        buttons.setSpacing(true);
        buttons.setMargin(new MarginInfo(true, true));

        FormLayout fields = new FormLayout(emailTextField, passwordField, buttons);
        fields.setSpacing(true);
        fields.setWidth("400px");
        fields.setMargin(new MarginInfo(true, true));

        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLACK);
        viewLayout.setStyleName("centre-panel", true);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        setCompositionRoot(viewLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        emailTextField.focus();
    }

    @Override
    protected void focus() {
        if(passwordField.getValue().equals("")) {
            passwordField.setInputPrompt(null);
        }
    }

    private static class PasswordValidator extends AbstractValidator<String> {
        
        private static final String errorMessage = "Invalid password.";
        
        public PasswordValidator() { 
            super(errorMessage);
        }

        @Override
        protected boolean isValidValue(String value) {
            if (value == null) {
                return false;
            } else {
                boolean atLeastOneDigit = value.matches(".*\\d.*");
                boolean hasMinimumLength = value.length() == MINIMUM_PASSWORD_LENGTH;
                return atLeastOneDigit && hasMinimumLength;
            }
        }

        @Override
        public Class<String> getType() {
            return String.class;
        }
    }
}

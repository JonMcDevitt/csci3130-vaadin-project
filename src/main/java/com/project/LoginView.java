package com.project;

import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Created by Owner on 2017-02-17.
 */
public class LoginView extends CustomComponent implements View {
    
    public static final String NAME = "Login";
    
    private final String testUsername = "test@test.com";
    private final String testUserPassword = "p4ssw0rd"; 
    
    private final TextField usernameTextField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Button loginButton = new Button();
    private final Button signUpButton = new Button();
    
    private static final int MINIMUM_PASSWORD_LENGTH = 8;

    public LoginView() {
        initUsernameTextField(usernameTextField);
        initPasswordField(passwordField);
        initLoginButton(loginButton);
        initSignUpButton(signUpButton);
        
        setSizeFull();
        setupLayout();
    }

    private final void initUsernameTextField(TextField usernameTextField) {
        usernameTextField.addValidator(new EmailValidator("Username must be an email address."));
        usernameTextField.setCaption("User email: ");
        usernameTextField.setWidth("300px");
        usernameTextField.setRequired(true);
        usernameTextField.setInputPrompt("Your email here (e.g. joe_blow@mail.com)");
        usernameTextField.setInvalidAllowed(false);
    }
    
    private final void initPasswordField(PasswordField passwordField) {
        passwordField.addValidator(new PasswordValidator());
        passwordField.setCaption("Login: ");
        passwordField.setWidth("300px");
        passwordField.setRequired(true);
        passwordField.setValue("");
        passwordField.setNullRepresentation("");
    }

    private final void initLoginButton(Button loginButton) {
        Button.ClickListener onLoginClicked = event -> 
        {
            boolean validUsername = usernameTextField.getValue().equals(testUsername);
            boolean validPassword = passwordField.getValue().equals(testUserPassword);
            
            if (!usernameTextField.isValid()) {
                Notification.show("Invalid username.");
            } else if (validUsername && validPassword) {
                getSession().setAttribute("user", testUsername);
                getUI().getNavigator().navigateTo(MainMenuView.NAME);
            } else {
                passwordField.setValue(null);
                passwordField.focus();
            }
        };
        
        loginButton.setCaption("Login");
        loginButton.addClickListener(onLoginClicked);
    }
    
    private final void initSignUpButton(Button signUpButton) {
        signUpButton.setCaption("Sign up for service");
    }
    
    private final void setupLayout() {
        HorizontalLayout buttons = new HorizontalLayout(loginButton, signUpButton);
        buttons.setSpacing(true);
        buttons.setMargin(new MarginInfo(true, true));

        VerticalLayout fields = new VerticalLayout(usernameTextField, passwordField, buttons);
        String caption = String.format("Please login to access the application (%s/%s)", testUsername, testUserPassword);
        fields.setCaption(caption);
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, true));
        fields.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLACK);
        viewLayout.setStyleName("centre-panel", true);
        setCompositionRoot(viewLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        usernameTextField.focus();
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

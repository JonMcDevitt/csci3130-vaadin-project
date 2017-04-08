package com.project.ui;

import com.project.backend.DatabaseHandler;
import com.project.backend.User;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
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
    
    public static final String EMAIL_TEXT_FIELD_ID = "emailTextField";
    public static final String PASSWORD_TEXT_FIELD_ID = "passwordTextField";
    public static final String LOGIN_BUTTON_ID = "passwordTextField";
    
    
    private final TextField emailTextField = new TextField();
    private final TextField tmpPassword = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Button loginButton = new Button();
    private final Button signUpButton = new Button();
    
    private static final int MINIMUM_PASSWORD_LENGTH = 8;

    public LoginView() {
        initUsernameTextField(emailTextField);
        initPasswordField(passwordField,tmpPassword);
        initLoginButton(loginButton);
        initSignUpButton(signUpButton);
        setSizeFull();
        setupLayout();
        initStyles();
    }

    private final void initUsernameTextField(TextField usernameTextField) {
    	usernameTextField.setIcon(FontAwesome.USER);
    	usernameTextField.setId(EMAIL_TEXT_FIELD_ID);
        usernameTextField.addValidator(new EmailValidator("Username must be an email address."));
        usernameTextField.setWidth("300px");
        usernameTextField.addStyleName("requiredField");
        //usernameTextField.setRequired(true);
        usernameTextField.setInputPrompt("Email address or username");
        usernameTextField.setInvalidAllowed(false);
        usernameTextField.addShortcutListener(new ShortcutListener("Login", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object o, Object o1) {
                LoginView.this.loginButton.click();
            }
        });
    }
    
    private final void initPasswordField(PasswordField passwordField, TextField tmpPassword) {
    	tmpPassword.setInputPrompt("Password");
    	tmpPassword.setIcon(FontAwesome.LOCK);
    	tmpPassword.setWidth("300px");
    	passwordField.setIcon(FontAwesome.LOCK);
    	passwordField.setId(PASSWORD_TEXT_FIELD_ID);
        //passwordField.addValidator(new PasswordValidator());
        passwordField.setWidth("300px");
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
            String email = emailTextField.getValue();
            String pWord = passwordField.getValue();
            User r = DatabaseHandler.getUserById(email);
            if(r == null) {
                Notification.show("Invalid username", Notification.Type.ERROR_MESSAGE);
            } else if(r.getPassword() != null && r.getPassword().equals(pWord)) {
                getSession().setAttribute("user", email);
                getUI().getNavigator().navigateTo(MainMenuView.NAME);
            } else {
                Notification.show("Error: Invalid password", Notification.Type.ERROR_MESSAGE);
                passwordField.setValue("");
                passwordField.focus();
            }
        };
        loginButton.setWidth("300px");
        loginButton.setId(LOGIN_BUTTON_ID);
        loginButton.setCaption("Login");
        loginButton.addClickListener(onLoginClicked);
    }
    
    private final void initSignUpButton(Button signUpButton) {
    	signUpButton.setWidth("300px");
        signUpButton.setCaption("Sign up");
        signUpButton.addClickListener((Button.ClickListener) clickEvent -> getUI().setContent(new SignUpView()));
    }
    
    /*private void configurePasswordButtons(FormLayout fields){
    	fields.getComponent(2).addFocusListener(new FocusListen(){
    		
    	});
    }*/
    
    private final void setupLayout() {
    	Embedded image = new Embedded(null, new ThemeResource("../barcodewithstring1.png"));
        VerticalLayout buttons = new VerticalLayout(loginButton, signUpButton);
        buttons.setSpacing(true);
        //buttons.setMargin(new MarginInfo(true, true));

        FormLayout fields = new FormLayout(image, emailTextField, tmpPassword, buttons);
        tmpPassword.addFocusListener(new FocusListener(){
        	public void focus(FieldEvents.FocusEvent event){
        		fields.replaceComponent(tmpPassword, passwordField);
        		passwordField.focus();
        		if(emailTextField.getValue().equals(""))
        			emailTextField.setInputPrompt("Email address or username");
        	}
        });
        passwordField.addBlurListener(new BlurListener(){
			public void blur(BlurEvent event) {
				if(passwordField.getValue().equals(""))
					fields.replaceComponent(passwordField, tmpPassword);
			}
        });
        //passwordField.addBlurListener(listener);
        fields.setSpacing(true);
        fields.setWidth("400px");
        fields.setMargin(new MarginInfo(true, true));
        //configurePasswordButtons(fields);
        VerticalLayout viewLayout = new VerticalLayout(fields);
        //viewLayout.setStyleName(Reindeer.LAYOUT_BLACK);
        //viewLayout.setStyleName("centre-panel", true);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        setCompositionRoot(viewLayout);
    }

    private void initStyles(){
        signUpButton.addStyleName("red");
        loginButton.addStyleName("red");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        while (DatabaseHandler.getUserById("test@test.com") == null) {
            DatabaseHandler.addUser(
                    "test@test.com", "p4ssw0rd", "La",
                    "Tester", "Computer Science"
            );
        }
       // emailTextField.focus();
    }

    @Override
    protected void focus() {
        if(passwordField.getValue().equals("")) {
            passwordField.setInputPrompt(null);
        }
    }

    /*private static class PasswordValidator extends AbstractValidator<String> {
        
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
    }*/
}

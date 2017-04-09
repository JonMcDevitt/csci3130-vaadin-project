package com.project.ui;

import java.util.ArrayList;
import java.util.List;

import com.project.backend.DatabaseHandler;
import com.project.backend.Validator;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
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

    static final String NAME = "SignUp";
    private static final String WIDTH_TEXTFIELD_DEFAULT = "338px";

    private final TextField userEmail;
    private final TextField firstName;
    private final TextField lastName;
    private final TextField department;
    private final TextField tmpPassword;
    private final TextField tmpConfirmPassword;
    private final PasswordField password;
    private final PasswordField confirmPassword;
    private final Button signUpButton;
    private final Button clearButton;
    private final Button cancelButton;
    private final List<Component> components;

    SignUpView() {
        //setSizeFull();

        userEmail = new TextField();
        password = new PasswordField();
        confirmPassword = new PasswordField();
        tmpPassword = new TextField();
        tmpConfirmPassword = new TextField();
        firstName = new TextField();
        lastName = new TextField();
        department = new TextField();
        signUpButton = new Button("Sign Up");
        clearButton = new Button("Clear All");
        cancelButton = new Button("Back");
        components = new ArrayList<>();

        configureComponents();
        configureActions();
        setCompositionRoot(createLayout());
    }

    private void configureComponents() {
    	userEmail.setIcon(FontAwesome.ENVELOPE);
    	password.setIcon(FontAwesome.LOCK);
    	confirmPassword.setIcon(FontAwesome.LOCK);
    	tmpPassword.setIcon(FontAwesome.LOCK);
    	tmpConfirmPassword.setIcon(FontAwesome.LOCK);
    	firstName.setIcon(FontAwesome.USER);
    	lastName.setIcon(FontAwesome.USER);
    	department.setIcon(FontAwesome.SUITCASE);
    	signUpButton.setIcon(FontAwesome.PLUS);
    	clearButton.setIcon(FontAwesome.REMOVE);
    	cancelButton.setIcon(FontAwesome.ARROW_LEFT);
        components.add(userEmail);
        components.add(password);
        components.add(confirmPassword);
        components.add(tmpPassword);
        components.add(tmpConfirmPassword);
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
        tmpPassword.setInputPrompt("Enter Password");
        tmpConfirmPassword.setInputPrompt("Confirm Password");
        signUpButton.setWidth("105px");
        clearButton.setWidth("105px");
        cancelButton.setWidth("105px");
        UserInterfaceHelperFunctions.setTextFieldsWidth(components, WIDTH_TEXTFIELD_DEFAULT);
        UserInterfaceHelperFunctions.setTextFieldsInvalidAllowed(components, false);
        //UserInterfaceHelperFunctions.setTextFieldsRequired(components, true);
    }

    private void configureActions() {
        signUpButton.addClickListener((Button.ClickListener) clickEvent -> signUp());

        clearButton.addClickListener((Button.ClickListener) clickEvent -> clear());

        cancelButton.addClickListener((Button.ClickListener) clickEvent -> cancel());
    }


    private void cancel() {
        this.getUI().setContent(new LoginView());
    }

    private void clear() {
        userEmail.clear();
        firstName.clear();
        lastName.clear();
        department.clear();
        tmpPassword.clear();
        tmpConfirmPassword.clear();
        password.clear();
        confirmPassword.clear();
    }
    
    private boolean areAnyEmpty(){
    	if(userEmail.isEmpty()||firstName.isEmpty()||lastName.isEmpty()||department.isEmpty()||
    			password.isEmpty()||confirmPassword.isEmpty())
    		return true;
    	return false;
    }
    private void signUp() {
    	if(areAnyEmpty())
    		Notification.show("Please enter all fields");
        boolean isValidUser = Validator.validateUser(userEmail.getValue());
        boolean passwordConfirm = password.getValue().equals(confirmPassword.getValue());

        if (isValidUser && passwordConfirm&&!areAnyEmpty()) {
            this.getUI().setContent(new LoginView());
            Notification.show("Signed up with email: " + userEmail.getValue());
            DatabaseHandler.addUser(userEmail.getValue(), password.getValue(),
                    firstName.getValue(), lastName.getValue(), department.getValue());
        } else if (!isValidUser&&!areAnyEmpty()) {
            Notification.show("Invalid email.");
        } else if(!passwordConfirm&&!areAnyEmpty()) {
            Notification.show("Passwords do not match.");
        }
       // UserInterfaceHelperFunctions.setTextFieldsRequired(components, false);
    }

    private Layout createLayout() {
        HorizontalLayout buttons = new HorizontalLayout(
                signUpButton, clearButton, cancelButton);
        buttons.setSpacing(true);
        //buttons.setMargin(new MarginInfo(true, true));
        FormLayout fields = new FormLayout(
                userEmail, firstName, lastName, department, tmpPassword,
                tmpConfirmPassword, buttons);
        tmpPassword.addFocusListener(new FocusListener(){
        	public void focus(FieldEvents.FocusEvent event){
        		fields.replaceComponent(tmpPassword, password);
        		password.focus();
        		//if(emailTextField.getValue().equals(""))
        			//emailTextField.setInputPrompt("Email address or username");
        	}
        });
        password.addBlurListener(new BlurListener(){
			public void blur(BlurEvent event) {
				if(password.getValue().equals(""))
					fields.replaceComponent(password, tmpPassword);
			}
        });
        
        tmpConfirmPassword.addFocusListener(new FocusListener(){
        	public void focus(FieldEvents.FocusEvent event){
        		fields.replaceComponent(tmpConfirmPassword, confirmPassword);
        		confirmPassword.focus();
        		//if(emailTextField.getValue().equals(""))
        			//emailTextField.setInputPrompt("Email address or username");
        	}
        });
        confirmPassword.addBlurListener(new BlurListener(){
			public void blur(BlurEvent event) {
				if(confirmPassword.getValue().equals(""))
					fields.replaceComponent(confirmPassword, tmpConfirmPassword);
			}
        });
        
        //p
        fields.setSpacing(true);
        fields.setWidth("400px");
        fields.setMargin(new MarginInfo(true, true, true, true));
        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLACK);
        viewLayout.setStyleName("centre-panel", true);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        return viewLayout;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        userEmail.focus();
    }
}

package com.project;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;

/**
 * Created by jackieflash on 2017-02-23.
 */
public class SignUpView extends CustomComponent implements View {

    public static final String NAME = "Signup";
    private final TextField username;
    private final PasswordField password, confirmPassword;
    private final Button signUpButton, clearButton;
    private static final String WIDTH_DEFAULT = "300px";
    private static final String USER_INPUT_PROMPT_MESSAGE
            = "\"Your email here (e.g. joe_blow@mail.com)\"";
    private final TextField useremail;
    private final TextField firstname;
    private final TextField lastname;
    private final TextField department;

    public SignUpView() {
        setSizeFull();
        username = new TextField("Username: ");
        useremail = new TextField("User Email: ");
        firstname = new TextField("First Name: ");
        lastname = new TextField("Last Name: ");
        department = new TextField("Department: ");
        password = new PasswordField("Password: ");
        confirmPassword = new PasswordField("Confirm Password: ");
        signUpButton = new Button("Sign Up");
        clearButton = new Button("Clear");
        configureComponents();
        configureActions();
        setCompositionRoot(createLayout());
    }

    private void configureComponents() {
        username.setWidth(WIDTH_DEFAULT);
        username.setRequired(true);
        username.setInputPrompt(USER_INPUT_PROMPT_MESSAGE);
        username.setInvalidAllowed(false);
//Emailuseremail.setWidth(WIDTH_DEFAULT);
        useremail.setRequired(true);
        useremail.setInputPrompt(USER_INPUT_PROMPT_MESSAGE);
        useremail.setInvalidAllowed(false);
//First Namefirstname.setWidth(WIDTH_DEFAULT);
        firstname.setRequired(true);
        firstname.setInputPrompt(USER_INPUT_PROMPT_MESSAGE);
        firstname.setInvalidAllowed(false);//Last Namelastname.setWidth(WIDTH_DEFAULT);
        lastname.setRequired(true);
        lastname.setInputPrompt(USER_INPUT_PROMPT_MESSAGE);
        lastname.setInvalidAllowed(false);//Departmentdepartment.setWidth(WIDTH_DEFAULT);department.setRequired(true);department.setInputPrompt(USER_INPUT_PROMPT_MESSAGE);department.setInvalidAllowed(false);//Passwordpassword.setWidth(WIDTH_DEFAULT);password.setRequired(true);password.setValue("");password.setNullRepresentation("");//Password ValidationconfirmPassword.setWidth(WIDTH_DEFAULT);confirmPassword.setRequired(true);confirmPassword.setValue("");confirmPassword.setNullRepresentation("");
    }

    private void configureActions() {
    	RegisteredUserDatabase userDatabase = new RegisteredUserDatabase();
    	
    	
        signUpButton.addClickListener((Button.ClickListener) clickEvent -> {
            RegisteredUser newUser = new RegisteredUser(username.getValue(),
                    useremail.getValue(),
                    firstname.getValue(),
                    lastname.getValue(),
                    department.getValue(),
                    password.getValue(),
                    confirmPassword.getValue());
            if (newUser.isValid(userDatabase)) {
                this.getUI().setContent(new LoginView());
                Notification.show("Signed up with username: "
                        + username.getValue());
                userDatabase.save(newUser);
            }
            else{
            	Notification.show(username.getValue() + " already used");
            }
            
            for(RegisteredUser r : userDatabase.getDatabase()){
            	System.out.println("Database: " + r.toString());
            }
            
        });

        clearButton.addClickListener((Button.ClickListener) clickEvent -> {
            username.clear();
            useremail.clear();
            firstname.clear();
            lastname.clear();
            department.clear();
            password.clear();
            confirmPassword.clear();
        });
    }

    private Layout createLayout() {
        HorizontalLayout buttons = new HorizontalLayout(signUpButton,
                clearButton);
        buttons.setSpacing(true);
        buttons.setMargin(new MarginInfo(true, true));
        VerticalLayout fields = new VerticalLayout(username, useremail, 
                firstname, lastname, department, password, confirmPassword, buttons);
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
        username.focus();
    }
}

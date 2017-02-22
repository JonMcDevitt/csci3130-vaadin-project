package com.project;

import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;

import javax.xml.soap.Text;

/**
 * Created by Owner on 2017-02-17.
 */
public class LoginView extends CustomComponent implements View{
    public static final String NAME = "Login";
    private final TextField username;
    private final PasswordField password;

    private final Button loginButton, signUp;

    public LoginView() {
        setSizeFull();

        username = new TextField("User email: ");
        username.setWidth("300px");
        username.setRequired(true);
        username.setInputPrompt("Your email here (e.g. joe_blow@mail.com)");
        username.addValidator(new EmailValidator(
                "Username must be an email address."
        ));

        username.setInvalidAllowed(false);

        password = new PasswordField("Password: ");
        password.setWidth("300px");
        password.addValidator(new PasswordValidator());
        password.setRequired(true);
        password.setValue("");
        password.setNullRepresentation("");

        loginButton = new Button("Login");
        loginButton.setCaption("Login");
        loginButton.addClickListener((Button.ClickListener) clickEvent -> logIn(this.username, this.password));

        signUp = new Button("Sign Up");
        signUp.setCaption("Sign up for service");

        HorizontalLayout buttons = new HorizontalLayout(loginButton, signUp);
        buttons.setSpacing(true);
        buttons.setMargin(new MarginInfo(true, true));

        VerticalLayout fields = new VerticalLayout(username, password, buttons);
        fields.setCaption("Please login to access the application. (test@test.com/p4ssw0rd");
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, true));
        fields.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLACK);
        setCompositionRoot(viewLayout);
    }

    private void logIn(TextField uname, PasswordField pword) {
        if(!uname.isValid()) {
            Notification.show("Invalid username.");
        } else if(uname.getValue().equals("test@test.com") && pword.getValue().equals("p4ssw0rd")) {
            getSession().setAttribute("user", uname.getValue());
            getUI().getNavigator().navigateTo(MainMenuView.NAME);
        } else {
            pword.setValue(null);
            pword.focus();
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        username.focus();
    }

    public static class PasswordValidator extends AbstractValidator<String> {

        public PasswordValidator() {
            super("Invalid password.");
        }

        @Override
        protected boolean isValidValue(String value) {
            return !(
                    value != null && (
                            value.length() < 8 || !value.matches(".*\\d.*")
                    )
            );
        }

        @Override
        public Class<String> getType() {
            return String.class;
        }
    }

    public TextField getUsername() {
        return username;
    }

    public PasswordField getPassword() {
        return password;
    }
}

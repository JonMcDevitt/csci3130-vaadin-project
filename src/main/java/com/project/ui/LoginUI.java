package com.project.ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class LoginUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        new Navigator(this, this);

        getNavigator().addView(LoginView.NAME, LoginView.class);
        getNavigator().addView(MainMenuView.NAME, MainMenuView.class);
        getNavigator().addView(CourseView.NAME, CourseView.class);
        getNavigator().addView(NewStudentView.NAME, NewStudentView.class);

        getNavigator().addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent viewChangeEvent) {
                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = viewChangeEvent.getNewView() instanceof LoginView;
                boolean ret = true;
                if (!isLoggedIn && !isLoginView) {
                    getNavigator().navigateTo(LoginView.NAME);
                    ret = false;
                } else if (isLoggedIn && isLoginView) {
                    ret = false;
                }

                return ret;
            }

            @Override
            public void afterViewChange(ViewChangeEvent viewChangeEvent) {
            }
        });
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = LoginUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}

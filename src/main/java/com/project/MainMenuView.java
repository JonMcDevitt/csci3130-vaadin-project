package com.project;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

/**
 * Created by Owner on 2017-02-17.
 */
public class MainMenuView extends CustomComponent implements View {
    public static final String NAME = "";

    Label welcome = new Label();

    Button logout = new Button("Log Out", (Button.ClickListener) clickEvent -> {
        getSession().setAttribute("user", null);
        getUI().getNavigator().navigateTo(NAME);
    });

    public MainMenuView() {
        setCompositionRoot(new CssLayout(welcome, logout));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        String username = String.valueOf(getSession().getAttribute("user"));
        welcome.setValue("Welcome, " + username + ", to our application!");
    }
}

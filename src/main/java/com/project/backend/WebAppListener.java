package com.project.backend;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class WebAppListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        DatabaseHandler.getInstance().close();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
    }
}

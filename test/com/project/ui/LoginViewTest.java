package com.project.ui;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.*;

/**
 * Created by Owner on 2017-02-22.
 */
public class LoginViewTest extends TestBenchTestCase{
    @Before
    public void setUp() {
        setDriver(new ChromeDriver());
    }

    private void openTestURL(String url) {
        getDriver().get(url);
    }

    @Test
    public void testLogin() {
        openTestURL("http://localhost:8080");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        $(TextFieldElement.class).first().setValue("test@test.com");
        $(PasswordFieldElement.class).first().setValue("p4ssw0rd");
        $(ButtonElement.class).first().click();
        assertEquals(getDriver().getCurrentUrl(), "http://localhost:8080/#!");
    }

    @After
    public void tearDown() {
        getDriver().close();
    }
}
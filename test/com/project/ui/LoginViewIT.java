package com.project.ui;

import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import static org.junit.Assert.*;

/**
 * Created by Owner on 2017-02-22.
 */
public class LoginViewIT extends TestBenchTestCase{
    @Before
    public void setUp() {
        setDriver(TestBench.createDriver(new PhantomJSDriver()));
        getDriver().manage().window().setSize(new Dimension(1920, 1080));
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
        getDriver().quit();
    }
}
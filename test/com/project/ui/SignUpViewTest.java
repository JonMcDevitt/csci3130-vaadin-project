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
 * Created by Owner on 2017-03-17.
 */
public class SignUpViewTest extends TestBenchTestCase{
    @Before
    public void setUp() throws Exception {
        setDriver(TestBench.createDriver(new PhantomJSDriver()));
        /** Need to do this so that the elements are not out of the viewport    */
        getDriver().manage().window().setSize(new Dimension(1024, 768));
        openTestURL("http://localhost:8080");
        waitMilli(1000);
        $(ButtonElement.class).get(1).click();
        waitMilli(1000);
    }

    private void openTestURL(String url) {
        getDriver().get(url);
    }

    @Test
    public void signUpDialogOpens() {
        assertTrue($(TextFieldElement.class).exists());
        assertEquals(4, $(TextFieldElement.class).all().size());
        assertEquals(2, $(PasswordFieldElement.class).all().size());
        assertEquals(3, $(ButtonElement.class).all().size());
        waitMilli(1000);
    }

    @Test
    public void signUpClearTest() {
        $(TextFieldElement.class).all().get(0).setValue("test2@test.com");
        assertEquals("test2@test.com", $(TextFieldElement.class).all().get(0).getValue());
        $(ButtonElement.class).all().get(1).click();
        assertNotEquals("test2@test.com", $(TextFieldElement.class).all().get(0).getValue());
    }

    @Test
    public void signUpCancelTest() {
        $(ButtonElement.class).all().get(2).click();
        assertEquals(1, $(TextFieldElement.class).all().size());
        assertEquals(1, $(PasswordFieldElement.class).all().size());
        assertEquals(2, $(ButtonElement.class).all().size());
    }

    @After
    public void tearDown() throws Exception {
        getDriver().close();
    }

    private void waitMilli(long milli) {
        try {
            Thread.sleep(milli);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
package com.project.ui;

import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.*;

/**
 * Created by Owner on 2017-03-17.
 */
public class AddCourseInputsViewTest extends TestBenchTestCase{
    @Before
    public void setUp() throws Exception {
        setDriver(new ChromeDriver());
    }

    private void openTestURL(String url) {
        getDriver().get(url);
    }

    private void login() {
        openTestURL("http://localhost:8080");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        $(TextFieldElement.class).first().setValue("test@test.com");
        $(PasswordFieldElement.class).first().setValue("p4ssw0rd");
        $(ButtonElement.class).first().click();
    }

    @Test
    public void testDialogOpens() {
        login();
        $(ButtonElement.class).all().get(1).click();
        assertTrue($(WindowElement.class).exists());
    }

    @Test
    public void testCourseAdded() {
        login();
        /** Find the number of rows */
        long currentRows = $(GridElement.class).first().getRowCount();

        /** Open the Add Course dialog  */
        $(ButtonElement.class).all().get(1).click();

        /** Fill out the form   */
        $(TextFieldElement.class).all().get(0).setValue("CSCI 1101");
        $(TextFieldElement.class).all().get(1).setValue("Java 2");
        $(TextFieldElement.class).all().get(2).setValue("01");

        /** Click button; expect that one more element has been added to the grid   */
        $(ButtonElement.class).all().get($(ButtonElement.class).all().size()-1).click();
        assertEquals($(GridElement.class).first().getRowCount(), currentRows + 1);
    }

    @After
    public void tearDown() throws Exception {
        getDriver().close();
    }
}
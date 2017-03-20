package com.project.ui;

import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.project.backend.RegisteredUser;
import com.project.backend.RegisteredUserDatabase;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class AttendanceViewTest extends TestBenchTestCase {

    private final long delayBetweenViewChanges = 100L;
    
    @Rule
    public ScreenshotOnFailureRule screenshotOnFailure = 
            new ScreenshotOnFailureRule(this, true);
    
    @Before
    public void setup() throws InterruptedException {
        setDriver(new PhantomJSDriver());
        getDriver().get("http://localhost:8080/");
        Thread.sleep(2000L);
        navigateToAttandanceView();
    }
    
    private void waitForPageLoad() throws InterruptedException {
        Thread.sleep(delayBetweenViewChanges);
    }
    
    private void navigateToAttandanceView() throws InterruptedException {
        TextFieldElement usernameTextField = $(TextFieldElement.class).id(LoginView.EMAIL_TEXT_FIELD_ID);
        PasswordFieldElement passwordField = $(PasswordFieldElement.class).id(LoginView.PASSWORD_TEXT_FIELD_ID);
        ButtonElement loginButton = $(ButtonElement.class).id(LoginView.LOGIN_BUTTON_ID);
        
        RegisteredUser testUser = RegisteredUserDatabase.getTestUser();
        
        usernameTextField.setValue(testUser.getEmail());
        passwordField.setValue(testUser.getPassword());
        
        loginButton.click();
        Thread.sleep(delayBetweenViewChanges);
        
        GridElement courseTable = $(GridElement.class).id(MainMenuView.COURSE_GRID_ID);
        GridElement.GridCellElement firstRow = courseTable.getCell(0, 0);        
        firstRow.click();
        
        ButtonElement goToCourseButton = $(ButtonElement.class).id(MainMenuView.GO_TO_COURSE_BUTTON_ID);        
        goToCourseButton.click();
        Thread.sleep(delayBetweenViewChanges);

        ButtonElement takeAttendanceforToday = $(ButtonElement.class).id(CourseView.TAKE_ATTENDANCE_FOR_TODAY_BUTTON_ID);

        takeAttendanceforToday.click();
        Thread.sleep(delayBetweenViewChanges);
    }
    
    @Test
    public void testBackButton() throws InterruptedException {
        ButtonElement backButton = $(ButtonElement.class).id(AttendanceView.BACK_BUTTON_ID);
        backButton.click();
        
        waitForPageLoad();
        
        try {
            $(ButtonElement.class).id(CourseView.TAKE_ATTENDANCE_FOR_TODAY_BUTTON_ID);
        }
        catch (NoSuchElementException e) {
            Assert.fail("Did not successfully navigate back to Course page");
        }
        
    }
    
}

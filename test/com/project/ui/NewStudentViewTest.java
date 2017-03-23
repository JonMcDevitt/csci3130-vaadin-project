package com.project.ui;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class NewStudentViewTest extends TestBenchTestCase {
	
	@Before
    public void setUp() throws Exception {
      setDriver(TestBench.createDriver(new PhantomJSDriver()));
      goToPage();
    }
	
	/**
     * Opens the URL where the application is deployed.
     */
    private void openTestUrl() {
    	getDriver().get("http://localhost:8080/#!Login");
    	getDriver().manage().window().setSize(new Dimension(1920, 1080));
    }
	
    private void goToPage() {
    	openTestUrl();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        $(TextFieldElement.class).first().setValue("test");
        $(PasswordFieldElement.class).first().setValue("test@test.com");
        $(ButtonElement.class).first().click();
                
        $(GridElement.class).first().getCell(0, 0).click();
        $(ButtonElement.class).first().click();
              
        $(GridElement.class).first().getCell(0, 0).click();
        $(ButtonElement.class).get(1).click();
    }
    
    @Test
    public void testTextFieldsClearButton() {     
    	assertEquals($(TextFieldElement.class).all().size(), 3);
    	    	
    	/**check TextFiled */
        TextFieldElement textFieldId = $(TextFieldElement.class).get(0);
        textFieldId.setValue("123");
        assertEquals("123",textFieldId.getValue());
        
        TextFieldElement textFieldFN = $(TextFieldElement.class).get(1);
        textFieldFN.setValue("TestFirstName");
        assertEquals("TestFirstName",textFieldFN.getValue());
        
        TextFieldElement textFieldLN = $(TextFieldElement.class).get(2);
        textFieldLN.setValue("TestLastName");
        assertEquals("TestLastName",textFieldLN.getValue());
        
        
        assertTrue($(ButtonElement.class).caption("Clear").exists());
        
        /**Check the clear button  */
        ButtonElement clearButton = $(ButtonElement.class).get(1);
        clearButton.click();   
                     
        assertEquals("Enter ID here.",textFieldId.getValue());
        assertEquals("Enter first name.",textFieldFN.getValue());
        assertEquals("Enter last name.",textFieldLN.getValue());	
    }
    
    @Test
    public void testCancelButton() {     
        assertTrue($(ButtonElement.class).caption("Cancel").exists());
        
        /**Check the cancel button  */
        $(ButtonElement.class).get(2).click();
        assertEquals(getDriver().getCurrentUrl(), "http://localhost:8080/#!CourseInfo");	
    }
    
    @Test
    public void testAddStudentButton() {     
        assertTrue($(ButtonElement.class).caption("Add Student").exists());
        
        /**add a new student */
        TextFieldElement textFieldId = $(TextFieldElement.class).get(0);
        textFieldId.setValue("123");
        assertEquals("123",textFieldId.getValue());
        
        TextFieldElement textFieldFN = $(TextFieldElement.class).get(1);
        textFieldFN.setValue("TestFirstName");
        assertEquals("TestFirstName",textFieldFN.getValue());
        
        TextFieldElement textFieldLN = $(TextFieldElement.class).get(2);
        textFieldLN.setValue("TestLastName");
        assertEquals("TestLastName",textFieldLN.getValue());
        
        $(ButtonElement.class).get(0).click();
        assertEquals(getDriver().getCurrentUrl(), "http://localhost:8080/#!CourseInfo");	
        
        /**find the newly added student row  */
        int newRow = 0;
        
        for(int i = 0; i < ($(GridElement.class).first().getSize().getHeight() - 1); i++){
        	try{
        		$(GridElement.class).first().getCell(i, 0);
        		newRow++;
        	}
        	catch (org.openqa.selenium.NoSuchElementException e){
        		newRow--;
        		break;
        	}
        } 
        
        /**check the first name and last name to see if it is the new student */
        assertEquals("TestFirstName",$(GridElement.class).first().getCell(newRow, 0).getText());
        assertEquals("TestLastName",$(GridElement.class).first().getCell(newRow, 1).getText());
    }
    
    @After
    public void tearDown() throws Exception {
        getDriver().quit();
    }
}	   
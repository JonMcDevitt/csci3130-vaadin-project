package com.project.ui;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class NewStudentViewTest extends TestBenchTestCase {
	
	 	@Rule
	    public ScreenshotOnFailureRule screenshotOnFailureRule =
	            new ScreenshotOnFailureRule(this, true);

	    @Before
	    public void setUp() throws Exception {
	    	//setDriver(new PhantomJSDriver());
	    	setDriver(TestBench.createDriver(new FirefoxDriver()));
	    }
	    

	    /**
	     * Opens the URL where the application is deployed.
	     */
	    private void openTestUrl() {
	    	getDriver().get("http://localhost:8080/#!NewStudentView");
	    }

	    @Test
	    public void testNoLabel() throws Exception {
	        openTestUrl();

	        //Test there is no labels
	        assertFalse($(LabelElement.class).exists());
	       
	        
	        }
	    
	    @Test
	    public void testTextFieldNButton() throws Exception {
	    	
	        //check TextFiled
	        TextFieldElement textFieldId = $(TextFieldElement.class).get(0);
	        textFieldId.setValue("123");
	        assertEquals("123",textFieldId.getValue());
	        
	        TextFieldElement textFieldFN = $(TextFieldElement.class).get(1);
	        textFieldFN.setValue("TetsFirstName");
	        assertEquals("TestFirstName",textFieldFN.getValue());
	        
	        TextFieldElement textFieldLN = $(TextFieldElement.class).get(2);
	        textFieldLN.setValue("TestLastName");
	        assertEquals("TestLastName",textFieldLN.getValue());
	        
	        
	        assertTrue($(ButtonElement.class).exists());
	        
	        // Check the clear button
	        ButtonElement clearButton = $(ButtonElement.class).get(1);
	        clearButton.click();   
	        
	        assertEquals("",textFieldId.getValue());
	        assertEquals("",textFieldFN.getValue());
	        assertEquals("",textFieldLN.getValue());
	    }

        @After
        public void tearDown() throws Exception {
        	getDriver().quit();
	    }
}	   

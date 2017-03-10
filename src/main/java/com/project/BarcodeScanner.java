package com.project;

import java.util.Optional;
import java.util.function.Consumer;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class BarcodeScanner extends CustomComponent {
    
    private static final long serialVersionUID = -2387815294780431466L;
    
    private TextField hiddenTextField;
    
    private Button activateButton;
    private Button deactivateButton;
    
    private VerticalLayout verticalLayout;
    private HorizontalLayout horizontalLayout;
    
    private Optional<Consumer<String>> callbackOptional;

    public BarcodeScanner() {
        hiddenTextField = new TextField();
        activateButton = new Button();
        deactivateButton = new Button();
        
        setupHiddenTextField(hiddenTextField);
        setupActivateButton(activateButton);
        setupDeactivateButton(deactivateButton);
        
        horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponents(activateButton, deactivateButton);
        
        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.addComponents(hiddenTextField, horizontalLayout);
        
        setCompositionRoot(verticalLayout);
    }

    private void setupHiddenTextField(TextField textField) {
        textField.setVisible(true);
        textField.setImmediate(true);
        textField.setWidth(300, UNITS_PIXELS);
        
        textField.addBlurListener(e -> {
            if (!activateButton.isEnabled() && deactivateButton.isEnabled()) {
                deactivateButton.setEnabled(false);
                activateButton.setEnabled(true);
            }
        });
        
        textField.addFocusListener(e ->{
        	if (!deactivateButton.isEnabled() && activateButton.isEnabled()) {
        		activateButton.setEnabled(false);
        		deactivateButton.setEnabled(true);
        	}
        });
    }
    
    private void setupActivateButton(Button activateButton) {
        activateButton.setCaption("Start scanning");
        activateButton.setEnabled(true);
        activateButton.setDisableOnClick(true);
        activateButton.setWidth(150,UNITS_PIXELS);
        
        activateButton.addClickListener(e -> {
            deactivateButton.setEnabled(true);
            hiddenTextField.focus();
        });
    }
    
    private void setupDeactivateButton(Button deactivateButton) {
        deactivateButton.setCaption("Stop scanning");
        deactivateButton.setEnabled(false);
        deactivateButton.setDisableOnClick(true);
        deactivateButton.setWidth(150,UNITS_PIXELS);
        
        deactivateButton.addClickListener(e -> {
            activateButton.setEnabled(true);
        });
    }
    
    private void toggleButtons() {
        Button enabledButton = activateButton.isEnabled() ? activateButton : deactivateButton;
        Button disabledButton = activateButton.isEnabled() ? deactivateButton : activateButton;
        enabledButton.setEnabled(false);
        disabledButton.setEnabled(true);
    }

}
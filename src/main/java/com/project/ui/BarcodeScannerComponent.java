/*CSCI 3130
 * 
 * March 15, 2017
 * 
 *Project - User Story 2
 *By Team Alpha
 *
 *This user story was created by Nicholas Broderick and Liam Gowan.
 *
 *This class allows for a component to be added that is able to continuously scan barcodes in. 
 *It will be activated/deactivated if the focus is gain/lost, respectively. 
 *
 *Attribution-
 *The code used to detect when an enter key is pressed in the text field was found at the following link.
 *http://ramontalaverasuarez.blogspot.ca/2014/06/vaadin-7-detect-enter-key-in-textfield.html
 */


package com.project.ui;

import java.util.Optional;
import java.util.function.Consumer;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class BarcodeScannerComponent extends CustomComponent {

    private static final long serialVersionUID = -2387815294780431466L;

    private TextField textField;

    private Button activateButton;
    private Button deactivateButton;

    private VerticalLayout verticalLayout;
    private HorizontalLayout horizontalLayout;

    private Optional<Consumer<String>> callbackOptional;
    private OnEnterKeyHandler onEnterKeyHandler;

    //Creates new barcode object and calls setup methods.
    public BarcodeScannerComponent() {
        textField = new TextField();
        activateButton = new Button();
        deactivateButton = new Button();

        setupTextField(textField);
        setupActivateButton(activateButton);
        setupDeactivateButton(deactivateButton);

        horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponents(activateButton, deactivateButton);

        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.addComponents(textField, horizontalLayout);

        callbackOptional = Optional.empty();
        initStyles();
        setCompositionRoot(verticalLayout);
        setSizeUndefined();
    }

    private void initStyles() {
        textField.addStyleName("alphabutton");
        activateButton.addStyleName("alphabutton");
        deactivateButton.addStyleName("alphabutton");
    }

    //Sets up the textfield, and toggle the component to be deactivated if focus is lost.
    private void setupTextField(TextField tf) {
        tf.setInputPrompt("Input barcodes here");
        tf.setVisible(true);
        tf.setImmediate(true);
        tf.setWidth(300, UNITS_PIXELS);
        tf.addBlurListener(e -> {
            if (!activateButton.isEnabled() && deactivateButton.isEnabled()) {
                deactivateButton.setEnabled(false);
                activateButton.setEnabled(true);
            }
        });

        //Upon an enter key being pressed, the component will return the string (barcode) it in the textfield.
        onEnterKeyHandler = new OnEnterKeyHandler() {
            @Override
            public void onEnterKeyPressed() {
                callbackOptional.ifPresent(callback -> {
                    String value = textField.getValue();
                    textField.clear();
                    callback.accept(value);
                });
            }
        };

        onEnterKeyHandler.installOn(textField);

        //toggles the component to be 'active' if focus is gained.
        tf.addFocusListener(e -> {
            if (!deactivateButton.isEnabled() && activateButton.isEnabled()) {
                activateButton.setEnabled(false);
                deactivateButton.setEnabled(true);
            }
        });
    }

    //Sets up the activate button (caption, enabled, width, click listener). If this button is pushed, the 
    //textfield will have focus, and this button will be disabled, and the deactivate button will be enabled
    private void setupActivateButton(Button activateButton) {
        activateButton.setCaption("Start scanning");
        activateButton.setEnabled(true);
        activateButton.setDisableOnClick(true);
        activateButton.setWidth(150, UNITS_PIXELS);

        activateButton.addClickListener(e -> {
            deactivateButton.setEnabled(true);
            textField.focus();
        });
    }

    //Sets up the deactivate button (caption, enabled, width, click listener). If this button is pushed, the 
    //textfield will lose focus, and this button will be disabled, and the activate button will be enabled
    private void setupDeactivateButton(Button deactivateButton) {
        deactivateButton.setCaption("Stop scanning");
        deactivateButton.setEnabled(false);
        deactivateButton.setDisableOnClick(true);
        deactivateButton.setWidth(150, UNITS_PIXELS);

        deactivateButton.addClickListener(e -> {
            activateButton.setEnabled(true);
        });
    }

    //Allows for the listening of an enter key being pressed
    private abstract class OnEnterKeyHandler {

        @SuppressWarnings("serial")
        final ShortcutListener enterShortCut
                = new ShortcutListener("EnterOnTextAreaShorcut", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onEnterKeyPressed();
            }
        };

        @SuppressWarnings("serial")
        public void installOn(final TextField component) {

            component.addFocusListener(new FieldEvents.FocusListener() {
                @Override
                public void focus(FieldEvents.FocusEvent event) {
                    component.addShortcutListener(enterShortCut);
                }
            });

            component.addBlurListener(new FieldEvents.BlurListener() {
                @Override
                public void blur(FieldEvents.BlurEvent event) {
                    component.removeShortcutListener(enterShortCut);
                }
            });
        }

        public abstract void onEnterKeyPressed();

    }

    // Register the callback to be executed when a barcode is scanned
    public void onBarcodeScanned(Consumer<String> onBarcodeScanned) {
        callbackOptional = Optional.of(onBarcodeScanned);
    }

    // Simulates a barcode being scanned.
    public void simulateBarcodeScan(String barcode) {
        textField.setValue(barcode);
        onEnterKeyHandler.enterShortCut.handleAction(null, null);
    }

    public Button getActivateButton() {
        return activateButton;
    }

    public Button getDeactivateButton() {
        return deactivateButton;
    }

    public TextField getTextField() {
        return textField;
    }

}
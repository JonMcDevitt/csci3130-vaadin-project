package com.project;

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

public class BarcodeScanner extends CustomComponent {

    private static final long serialVersionUID = -2387815294780431466L;

    private TextField hiddenTextField;

    private Button activateButton;
    private Button deactivateButton;

    private VerticalLayout verticalLayout;
    private HorizontalLayout horizontalLayout;

    private Optional<Consumer<String>> callbackOptional;
    private OnEnterKeyHandler onEnterKeyHandler;

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
        verticalLayout.addComponents(hiddenTextField, horizontalLayout);

        setCompositionRoot(verticalLayout);
    }

    private void setupHiddenTextField(TextField textField) {
        textField.setVisible(true);
        textField.setImmediate(true);        

        textField.addBlurListener(e -> {
            if (!activateButton.isEnabled() && deactivateButton.isEnabled()) {
                deactivateButton.setEnabled(false);
                activateButton.setEnabled(true);
            }
        });

        onEnterKeyHandler = new OnEnterKeyHandler() {
            @Override
            public void onEnterKeyPressed() {
                callbackOptional.ifPresent(c -> {
                    c.accept(hiddenTextField.getValue());
                    hiddenTextField.clear();
                });
            }
        };
        
        onEnterKeyHandler.installOn(hiddenTextField);
    }

    private void setupActivateButton(Button activateButton) {
        activateButton.setEnabled(true);
        activateButton.setDisableOnClick(true);

        activateButton.addClickListener(e -> {
            deactivateButton.setEnabled(true);
            hiddenTextField.focus();
        });
    }

    private void setupDeactivateButton(Button deactivateButton) {
        deactivateButton.setCaption("Stop scanning");
        deactivateButton.setEnabled(false);
        deactivateButton.setDisableOnClick(true);

        deactivateButton.addClickListener(e -> {
            activateButton.setEnabled(true);
        });
    }

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
    
    public void onBarcodeScanned(Consumer<String> onBarcodeScanned) {
        System.out.println("callback registered");
        this.callbackOptional = Optional.of(onBarcodeScanned);
    }

}
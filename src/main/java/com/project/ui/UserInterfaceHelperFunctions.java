package com.project.ui;

import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Component;

import java.util.List;

/**
 * Created by Jonathan McDevitt on 2017-03-27.
 */
public class UserInterfaceHelperFunctions {
    static void setTextFieldsWidth(List<Component> components, String widthInPixels) {
        for (Component component : components) {
            if (component instanceof AbstractTextField) {
                component.setWidth(widthInPixels);
                AbstractTextField textField = (AbstractTextField) component;
                textField.setInvalidAllowed(false);
            }
        }
    }

    static void setTextFieldsRequired(List<Component> components, boolean required) {
        for(Component component : components) {
            if(component instanceof AbstractTextField) {
                AbstractTextField textField = (AbstractTextField) component;
                textField.setRequired(required);
            }
        }
    }

    static void setTextFieldsInvalidAllowed(List<Component> components, boolean allowed) {
        for(Component component : components) {
            if(component instanceof AbstractTextField) {
                AbstractTextField textField = (AbstractTextField) component;
                textField.setInvalidAllowed(allowed);
            }
        }
    }
}

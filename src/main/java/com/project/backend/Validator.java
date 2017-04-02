package com.project.backend;

import java.util.regex.Pattern;

/**
 * Created by Jonathan McDevitt on 2017-03-29.
 */
public class Validator {

    /** Source: https://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
     *  */
    private final static String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-+]+)*" +
            "@[_A-Za-z0-9-+]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean validateUser(String email) {
        return validateEmail(email) && DatabaseHandler.getUserById(email) == null;
    }

    private static boolean validateEmail(String email) {
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }
}

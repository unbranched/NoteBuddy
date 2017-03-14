package nl.yoerinijs.notebuddy.validators;

import android.util.Log;

/**
 * A simple password validator
 */
public class PasswordValidator {

    private static final String LOG_TAG = "Password Validator";

    public boolean isPasswordValid(String password) {
        return checkPassword(password);
    }

    private boolean checkPassword(String password) {
        boolean numerical = false;
        try {
            Integer passwordInt = Integer.valueOf(password);
            if (passwordInt != null) {
                numerical = true;
            }
        } catch (Exception e) {
            numerical = false;
        }
        boolean length = (password.length() < 4) ? false : true;
        boolean space = (password.contains(" ")) ? false : true;
        boolean result = (length && space && numerical) ? true : false;

        // Log result
        Log.d(LOG_TAG, "Password validation is: " + result);

        // Return result
        return result;
    }
}

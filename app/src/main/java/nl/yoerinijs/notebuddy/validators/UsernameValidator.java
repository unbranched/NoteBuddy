package nl.yoerinijs.notebuddy.validators;

import android.util.Log;

/**
 * A simple username validator
 */
public class UsernameValidator {

    private static final String LOG_TAG = "Username Validator";

    public boolean isUsernameValid(String username) {
        return checkUsername(username);
    }

    private boolean checkUsername(String username) {
        boolean length = (username.length() < 4) ? false : true;
        boolean space = (username.contains(" ")) ? false : true;
        boolean result = (length && space) ? true : false;

        // Log result
        Log.d(LOG_TAG, "Username validation is: " + result);

        // Return result
        return result;
    }
}

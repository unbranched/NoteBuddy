package nl.yoerinijs.notebuddy.validators;

import android.util.Log;

/**
 * A simple secret validator
 */
public class SecretValidator {
    private static final String LOG_TAG = "Secret Validator";

    public boolean isSecretValid(String secret) {
        return checkSecret(secret);
    }

    private boolean checkSecret(String secret) {
        boolean result = (secret.length() < 4) ? false : true;

        // Log result
        Log.d(LOG_TAG, "Secret validation is: " + result);

        // Return result
        return result;
    }
}

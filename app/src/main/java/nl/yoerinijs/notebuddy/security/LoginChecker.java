package nl.yoerinijs.notebuddy.security;

import android.content.Context;
import android.util.Log;

import java.security.NoSuchAlgorithmException;

import nl.yoerinijs.notebuddy.storage.KeyValueDB;

/**
 * A class that checks whether the user exists
 */
public class LoginChecker {

    private static final String LOG_TAG = "Login Checker";

    /**
     * A method that returns whether a user is created
     * @param context
     * @return
     * @throws NoSuchAlgorithmException
     */
    public Boolean isCreated(Context context) throws NoSuchAlgorithmException {

        KeyValueDB k = new KeyValueDB();
        String setupStatus = k.getSetup(context);

        if (setupStatus == null) {
            Log.d(LOG_TAG, "User is not created");
            return false;
        } else {
            Log.d(LOG_TAG, "User is created");
            return true;
        }
    }

}

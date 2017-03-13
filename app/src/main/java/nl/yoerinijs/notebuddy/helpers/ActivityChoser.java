package nl.yoerinijs.notebuddy.helpers;

import android.content.Context;
import android.util.Log;

import java.security.NoSuchAlgorithmException;

import nl.yoerinijs.notebuddy.security.LoginChecker;
import nl.yoerinijs.notebuddy.storage.KeyValueDB;

/**
 * This class determines when to start the setup and the login activities
 */
public class ActivityChoser {

    private static final String SETUP_ACTIVITY = "SetupActivity";
    private static final String LOGIN_ACTIVITY = "LoginActivity";
    private static final String LOG_TAG = "Activity Choser";

    /**
     * A method that returns which activity to start
     * @param context
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String determineActivity(Context context) throws NoSuchAlgorithmException {
        Log.d(LOG_TAG, "Check which activity to use");
        String activity;
        LoginChecker l = new LoginChecker();

        if (l.isCreated(context) == false) {
            // Log that user must setup his settings
            Log.d(LOG_TAG, "Login is not created");
            Log.d(LOG_TAG, "Go to: " + SETUP_ACTIVITY);

            // Return activity
            activity = SETUP_ACTIVITY;

            // Store that the setup is set
            KeyValueDB k = new KeyValueDB();
            k.setSetup(context);
        } else {
            // Log that the user must login
            Log.d(LOG_TAG, "Login is created");
            Log.d(LOG_TAG, "Go to: " + LOGIN_ACTIVITY);

            // Return activity
            activity = LOGIN_ACTIVITY;
        }
        return activity;
    }
}

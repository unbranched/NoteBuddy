package nl.yoerinijs.notebuddy.helpers;

import android.content.Context;
import android.support.annotation.NonNull;

import java.security.NoSuchAlgorithmException;

import nl.yoerinijs.notebuddy.storage.KeyValueDB;

/**
 * This class determines when to start the setup and the login activities
 */
public class ActivityChoser {

    private static final String SETUP_ACTIVITY = "SetupActivity";

    private static final String LOGIN_ACTIVITY = "LoginActivity";

    /**
     * A method that returns which activity to start
     * @param context
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String determineActivity(@NonNull Context context) throws NoSuchAlgorithmException {
        if(null == KeyValueDB.getSetup(context)) {
            KeyValueDB.setSetup(context);
            return SETUP_ACTIVITY;
        }
        return LOGIN_ACTIVITY;
    }
}

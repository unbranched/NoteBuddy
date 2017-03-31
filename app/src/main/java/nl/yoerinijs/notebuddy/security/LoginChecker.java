package nl.yoerinijs.notebuddy.security;

import android.content.Context;
import android.support.annotation.NonNull;

import java.security.NoSuchAlgorithmException;

import nl.yoerinijs.notebuddy.storage.KeyValueDB;

/**
 * A class that checks whether the user exists
 */
public class LoginChecker {

    /**
     * A method that returns whether a user is created
     * @param context
     * @return
     * @throws NoSuchAlgorithmException
     */
    public boolean isCreated(@NonNull Context context) throws NoSuchAlgorithmException {

        KeyValueDB k = new KeyValueDB();
        String setupStatus = k.getSetup(context);

        if (setupStatus == null) {
            return false;
        } else {
            return true;
        }
    }

}

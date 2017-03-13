package nl.yoerinijs.notebuddy.security;

import android.content.Context;
import android.util.Log;

import nl.yoerinijs.notebuddy.storage.KeyValueDB;

import static nl.yoerinijs.notebuddy.security.AesCbcWithIntegrity.generateSalt;
import static nl.yoerinijs.notebuddy.security.AesCbcWithIntegrity.saltString;

/**
 * A class that generates a salt for encryption purposes
 */
public class SaltGenerator {

    private static final String LOG_TAG = "Salt Generator";

    String salt;

    public String getSalt(Context context) {
        try {
            KeyValueDB k = new KeyValueDB();
            if (k.getSalt(context) == null) {
                // Generate salt
                salt = saltString(generateSalt());

                // Store salt in key value storage
                k.setSalt(context, salt);

                // Log
                Log.d(LOG_TAG, "Use new salt");
            } else {
                // Get salt from key value storage
                salt = k.getSalt(context);

                // Log
                Log.d(LOG_TAG, "Use set salt");
            }
        } catch (Exception e) {
            // Log failure
            Log.d(LOG_TAG, e.getMessage());
        }
        return salt;
    }
}

package nl.yoerinijs.notebuddy.security;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A simple class that encrypts a string
 */
public class StringEncryptor {

    private static final String LOG_TAG = "String Encryptor";

    /**
     * A method that encrypts a string with SHA-256
     * @param string
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String encrypt(String string) throws NoSuchAlgorithmException {
        // Encrypt string
        String encryptedString;
        try {
            MessageDigest m = MessageDigest.getInstance("SHA-256");
            m.update(string.getBytes());

            // Return encrypted string
            encryptedString = new String(m.digest());

            // Log success
            Log.d(LOG_TAG, "String encrypted");
        } catch (Exception e) {
            // Return empty string
            encryptedString = "";

            // Log failure
            Log.d(LOG_TAG, e.getMessage());
        }

        // Returning string
        return encryptedString;
    }
}

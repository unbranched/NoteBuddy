package nl.yoerinijs.notebuddy.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.security.NoSuchAlgorithmException;

import nl.yoerinijs.notebuddy.security.EncryptionHandler;

/**
 * A class that holds all methods related to Android's Shared Preferences
 */
public class KeyValueDB {

    private static final String PREFS_NAME = "NoteBuddyPrefs";
    private static final String KEY_USERNAME = "username_scrt_key";
    private static final String KEY_DERIVED_SALT = "salt_derived_scrt_key";
    private static final String KEY_MASTER_SALT = "salt_master_scrt_key";
    private static final String KEY_SETUP = "setup_scrt_key";
    private static final String KEY_RANDOM_PASSWORD_STRING = "random_password_string_scrt_key";
    private static final String KEY_VERIFICATION_PASSWORD_HASH = "verification_password_hash_scrt_key";
    private static final String VALUE_SETUP = "set_scrt_value";
    private static final String LOG_TAG = "Key Value DB";

    public KeyValueDB() {
        super();
    }

    /**
     * Method to store random password string
     * @param context
     * @param encryptedSample
     * @throws NoSuchAlgorithmException
     */
    public void setRandomPasswordString(@NonNull Context context, @NonNull String encryptedSample) throws NoSuchAlgorithmException {
        setValue(context, KEY_RANDOM_PASSWORD_STRING, encryptedSample, false);
    }

    /**
     * Method to retrieve random password string
     * @param context
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String getRandomPasswordString(@NonNull Context context) throws NoSuchAlgorithmException {
        return getValue(context, KEY_RANDOM_PASSWORD_STRING);
    }

    /**
     * Method to set hash for password verification
     * @param context
     * @param verificationPasswordHash
     * @throws NoSuchAlgorithmException
     */
    public void setVerificationPasswordHash(@NonNull Context context, @NonNull String verificationPasswordHash) throws NoSuchAlgorithmException {
        setValue(context, KEY_VERIFICATION_PASSWORD_HASH, verificationPasswordHash, false);
    }

    /**
     * Method to get hash for password verification
     * @param context
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String getVerificationPasswordHash(@Nullable Context context) throws NoSuchAlgorithmException {
        return getValue(context, KEY_VERIFICATION_PASSWORD_HASH);
    }

    /**
     * Method to set salt for derived key
     * @param context
     * @param salt
     * @throws NoSuchAlgorithmException
     */
    public void setDerivedKeySalt(@NonNull Context context, @NonNull String salt) throws NoSuchAlgorithmException {
        setValue(context, KEY_DERIVED_SALT, salt, false);
    }

    /**
     * Method to retrieve salt for derived key
     * @param context
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String getDerivedKeySalt(@NonNull Context context) throws NoSuchAlgorithmException {
        return getValue(context, KEY_DERIVED_SALT);
    }

    /**
     * Method to set salt for master key
     * @param context
     * @param salt
     * @throws NoSuchAlgorithmException
     */
    public void setMasterKeySalt(@NonNull Context context, @NonNull String salt) throws NoSuchAlgorithmException {
        setValue(context, KEY_MASTER_SALT, salt, false);
    }

    /**
     * Method to get salt for master key
     * @param context
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String getMasterKeySalt(@NonNull Context context) throws NoSuchAlgorithmException {
        return getValue(context, KEY_MASTER_SALT);
    }

    /**
     * Method to set setup status
     * @param context
     * @throws NoSuchAlgorithmException
     */
    public void setSetup(@NonNull Context context) throws NoSuchAlgorithmException {
        setValue(context, KEY_SETUP, VALUE_SETUP, true);
    }

    /**
     * Method to get setup status
     * @param context
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String getSetup(@NonNull Context context) throws NoSuchAlgorithmException {
        return getValue(context, KEY_SETUP);
    }

    /**
     * Method to set username
     * @param context
     * @param value
     * @throws NoSuchAlgorithmException
     */
    public void setUsername(@NonNull Context context, @NonNull String value) throws NoSuchAlgorithmException {
        setValue(context, KEY_USERNAME, value, false);
    }

    /**
     * Method to retrieve username
     * @param context
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String getUsername(@NonNull Context context) throws NoSuchAlgorithmException {
        return getValue(context, KEY_USERNAME);
    }

    /**
     * Get value method
     * @param context
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     */
    private String getValue(@NonNull Context context, @NonNull String key) throws NoSuchAlgorithmException {
        SharedPreferences settings;
        String text;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        EncryptionHandler eh = new EncryptionHandler();
        text = settings.getString(eh.hashString(key), null);

        // Log success
        Log.d(LOG_TAG, "Value retrieved");

        return text;
    }

    /**
     * Set value method
     * @param context
     * @param key
     * @param value
     * @param secure
     * @throws NoSuchAlgorithmException
     */
    private void setValue(@NonNull Context context, @NonNull String key, @NonNull String value, @NonNull Boolean secure) throws NoSuchAlgorithmException {
        SharedPreferences settings;
        Editor editor;
        EncryptionHandler eh = new EncryptionHandler();

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        // Check whether the value must encrypted as well
        if (secure) {
            String encryptedKey = eh.hashString(key);
            String encryptedValue = eh.hashString(value);
            editor.putString(encryptedKey, encryptedValue);
        } else {
            String encryptedKey = eh.hashString(key);
            editor.putString(encryptedKey, value);
        }

        editor.commit();

        // Log success
        Log.d(LOG_TAG, "Value set");
    }

    /**
     * Delete value method
     * @param context
     * @param key
     * @throws NoSuchAlgorithmException
     */
    private void deleteValue(@NonNull Context context, @NonNull String key) throws NoSuchAlgorithmException {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(key);
        editor.apply();

        // Log success
        Log.d(LOG_TAG, "Value deleted");
    }

    /**
     * Clear Shared Preferences method
     * @param context
     */
    public void clearSharedPreference(@NonNull Context context) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();

        // Log success
        Log.d(LOG_TAG, "Cleared shared prefs");
    }
}
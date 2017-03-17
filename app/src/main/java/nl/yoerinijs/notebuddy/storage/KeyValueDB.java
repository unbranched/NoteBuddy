package nl.yoerinijs.notebuddy.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.security.NoSuchAlgorithmException;
import nl.yoerinijs.notebuddy.security.StringEncryptor;

/**
 * A class that holds all methods related to Android's Shared Preferences
 */
public class KeyValueDB {

    public static final String PREFS_NAME = "NoteBuddyPrefs";
    public static final String KEY_USERNAME = "username_scrt_key";
    public static final String KEY_PASSWORD = "password_scrt_key";
    public static final String KEY_SALT = "salt_scrt_key";
    public static final String KEY_SECRET_QUESTION = "secret_question_scrt_key";
    public static final String KEY_SECRET_ANWER = "secret_answer_scrt_key";
    public static final String KEY_SETUP = "setup_scrt_key";
    public static final String VALUE_SETUP = "set_scrt_value";
    private static final String LOG_TAG = "Key Value DB";

    public KeyValueDB() {
        super();
    }

    /**
     * Set salt method
     * @param context
     * @param salt
     * @throws NoSuchAlgorithmException
     */
    public void setSalt(Context context, String salt) throws NoSuchAlgorithmException {
        Log.d(LOG_TAG, "Set salt");
        setValue(context, KEY_SALT, salt, true);
    }

    /**
     * Get salt method
     * @param context
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String getSalt(Context context) throws NoSuchAlgorithmException {
        Log.d(LOG_TAG, "Get salt");
        return getValue(context, KEY_SALT);
    }

    /**
     * Set setup status method
     * @param context
     * @throws NoSuchAlgorithmException
     */
    public void setSetup(Context context) throws NoSuchAlgorithmException {
        Log.d(LOG_TAG, "Set setup status");
        setValue(context, KEY_SETUP, VALUE_SETUP, true);
    }

    /**
     * Get setup status method
     * @param context
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String getSetup(Context context) throws NoSuchAlgorithmException {
        Log.d(LOG_TAG, "Get setup status");
        return getValue(context, KEY_SETUP);
    }

    /**
     * Set username method
     * @param context
     * @param value
     * @throws NoSuchAlgorithmException
     */
    public void setUsername(Context context, String value) throws NoSuchAlgorithmException {
        Log.d(LOG_TAG, "Set username");
        setValue(context, KEY_USERNAME, value, false);
    }

    /**
     * Get username method
     * @param context
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String getUsername(Context context) throws NoSuchAlgorithmException {
        Log.d(LOG_TAG, "Get username");
        return getValue(context, KEY_USERNAME);
    }

    /**
     * Set secret question method
     * @param context
     * @param value
     * @throws NoSuchAlgorithmException
     */
    public void setSecretQuestion(Context context, String value) throws NoSuchAlgorithmException {
        Log.d(LOG_TAG, "Set secret question");
        setValue(context, KEY_SECRET_QUESTION, value, false);
    }

    /**
     * Get secret question method
     * @param context
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String getSecretQuestion(Context context) throws NoSuchAlgorithmException {
        Log.d(LOG_TAG, "Get secret question");
        return getValue(context, KEY_SECRET_QUESTION);
    }

    /**
     * Delete secret question method
     * @param context
     * @throws NoSuchAlgorithmException
     */
    public void deleteSecretQuestion(Context context) throws NoSuchAlgorithmException {
        Log.d(LOG_TAG, "Delete secret question");
        deleteValue(context, KEY_SECRET_QUESTION);
    }

    /**
     * Set secret answer method
     * @param context
     * @param value
     * @throws NoSuchAlgorithmException
     */
    public void setSecretAnswer(Context context, String value) throws NoSuchAlgorithmException {
        Log.d(LOG_TAG, "Set secret answer");
        setValue(context, KEY_SECRET_ANWER, value.toLowerCase(), true);
    }

    /**
     * Get secret answer method
     * @param context
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String getSecretAnswer(Context context) throws NoSuchAlgorithmException {
        Log.d(LOG_TAG, "Get secret answer");
        return getValue(context, KEY_SECRET_ANWER);
    }

    /**
     * Delete secret answer method
     * @param context
     * @throws NoSuchAlgorithmException
     */
    public void deleteSecretAnswer(Context context) throws NoSuchAlgorithmException {
        Log.d(LOG_TAG, "Delete secret answer");
        deleteValue(context, KEY_SECRET_ANWER);
    }

    /**
     * Set password method
     * @param context
     * @param value
     * @throws NoSuchAlgorithmException
     */
    public void setPassword(Context context, String value) throws NoSuchAlgorithmException {
        Log.d(LOG_TAG, "Set password");
        setValue(context, KEY_PASSWORD, value, true);
    }

    /**
     * Get password method
     * @param context
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String getPassword(Context context) throws NoSuchAlgorithmException {
        Log.d(LOG_TAG, "Get password");
        return getValue(context, KEY_PASSWORD);
    }

    /**
     * Method to delete credentials only
     * @param context
     * @throws NoSuchAlgorithmException
     */
    public void deleteCredentials(Context context) throws NoSuchAlgorithmException {
        Log.d(LOG_TAG, "Delete credentials");
        deleteValue(context, KEY_USERNAME);
        deleteValue(context, KEY_PASSWORD);
    }

    /**
     * Get value method
     * @param context
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     */
    private String getValue(Context context, String key) throws NoSuchAlgorithmException {
        SharedPreferences settings;
        String text;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        StringEncryptor e = new StringEncryptor();
        text = settings.getString(e.encrypt(key), null);

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
    private void setValue(Context context, String key, String value, Boolean secure) throws NoSuchAlgorithmException {
        SharedPreferences settings;
        Editor editor;
        StringEncryptor e = new StringEncryptor();

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        // Check whether the value must encrypted as well
        if (secure) {
            String encryptedKey = e.encrypt(key);
            String encryptedValue = e.encrypt(value);
            editor.putString(encryptedKey, encryptedValue);
        } else {
            String encryptedKey = e.encrypt(key);
            editor.putString(encryptedKey, value);
        }

        editor.commit();
    }

    /**
     * Delete value method
     * @param context
     * @param key
     * @throws NoSuchAlgorithmException
     */
    private void deleteValue(Context context, String key) throws NoSuchAlgorithmException {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(key);
        editor.apply();
    }

    /**
     * Clear Shared Preferences method
     * @param context
     */
    public void clearSharedPreference(Context context) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
    }
}
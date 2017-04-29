package nl.yoerinijs.notebuddy.files.misc;

import android.content.Context;
import android.util.Log;

import nl.yoerinijs.notebuddy.files.text.TextfileReader;

/**
 * This class checks if a file exists already.
 */
public class FileChecker {

    private static final String LOG_TAG = "File Checker";

    public Boolean fileExists(String location, String fileName, String password, Context context) {
        // Log input
        Log.d(LOG_TAG, "Location: " + location + ". Filename: " + fileName);

        // Verify if file exists
        TextfileReader t = new TextfileReader();
        String textContent =  t.getText(location, fileName, password, context, true);
        if (textContent.equals("empty") || textContent.equals("") || textContent == null) {

            // Log failure
            Log.d(LOG_TAG, "File does not exist");
            Log.d(LOG_TAG, "Text content: " + textContent);

            // Return failure
            return false;
        } else {

            // Log success
            Log.d(LOG_TAG, "File does exist");
            Log.d(LOG_TAG, "Text content: " + textContent);

            // Return success
            return true;
        }
    }
}

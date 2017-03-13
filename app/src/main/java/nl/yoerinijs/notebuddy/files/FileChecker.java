package nl.yoerinijs.notebuddy.files;

import android.content.Context;
import android.util.Log;

/**
 * Created by Yoeri on 11-3-2017.
 */

public class FileChecker {

    private static final String LOG_TAG = "File Checker";

    public Boolean fileExists(String location, String fileName, Context context) {
        // Log input
        Log.d(LOG_TAG, "Location: " + location + ". Filename: " + fileName);

        // Verify if file exists
        TextfileReader t = new TextfileReader();
        String textContent =  t.getText(location, fileName, context);
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

package nl.yoerinijs.notebuddy.validators;

import android.util.Log;

/**
 * A simple note validator class
 */
public class NoteBodyValidator {

    private static final String LOG_TAG = "Note Body Validator";

    public boolean isNoteBodyValid(String noteBody) {
        return checkNoteBody(noteBody);
    }

    private boolean checkNoteBody(String noteBody) {
        boolean length = (noteBody.length() < 1) ? false : true;
        boolean result = length;

        // Log result
        Log.d(LOG_TAG, "Note body validation is: " + result);

        // Return result
        return result;
    }
}

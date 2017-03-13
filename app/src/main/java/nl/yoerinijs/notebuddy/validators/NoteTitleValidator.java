package nl.yoerinijs.notebuddy.validators;

import android.util.Log;

/**
 * A simple note title validator
 */
public class NoteTitleValidator {

    private static final String LOG_TAG = "Note Title Validator";

    public boolean isNoteTitleValid(String noteTitle) {
        return checkNoteTitle(noteTitle);
    }

    private boolean checkNoteTitle(String noteTitle) {
        boolean length = (noteTitle.length() < 4) ? false : true;
        boolean result = length;

        // Log result
        Log.d(LOG_TAG, "Note title validation is: " + result);

        // Return result
        return result;
    }
}

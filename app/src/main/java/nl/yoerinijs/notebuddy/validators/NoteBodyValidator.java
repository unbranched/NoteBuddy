package nl.yoerinijs.notebuddy.validators;

/**
 * A simple note validator class
 */
public class NoteBodyValidator {
    public static boolean isNoteBodyValid(String noteBody) {
        return noteBody.length() >= 1;
    }
}

package nl.yoerinijs.notebuddy.validators;

/**
 * A simple note title validator
 */
public class NoteTitleValidator {

    public boolean isNoteTitleValid(String noteTitle) {
        return checkNoteTitle(noteTitle);
    }

    private boolean checkNoteTitle(String noteTitle) {
        return noteTitle.length() >= 4;
    }
}

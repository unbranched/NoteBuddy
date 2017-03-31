package nl.yoerinijs.notebuddy.validators;

/**
 * A simple username validator
 */
public class UsernameValidator {

    public boolean isUsernameValid(String username) {
        return checkUsername(username);
    }

    private boolean checkUsername(String username) {
        boolean length = username.length() >= 4;
        boolean space = !username.contains(" ");
        return (length && space);
    }
}

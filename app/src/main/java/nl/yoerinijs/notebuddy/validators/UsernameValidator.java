package nl.yoerinijs.notebuddy.validators;

/**
 * A simple username validator
 */
public class UsernameValidator {
    public boolean isUsernameValid(String username) {
        return username.length() >= 4 && !username.contains(" ");
    }
}

package nl.yoerinijs.notebuddy.validators;

/**
 * A simple username validator
 */
public class UsernameValidator {
    public static boolean isUsernameValid(String username) {
        return username.length() >= 4 && !username.contains(" ");
    }
}

package nl.yoerinijs.nb.validators;

/**
 * A simple username validator
 */
public class UsernameValidator {
    public static boolean isUsernameValid(String username) {
        return username.length() >= 4 && !username.contains(" ");
    }
}

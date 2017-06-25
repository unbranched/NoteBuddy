package nl.yoerinijs.nb.validators;

/**
 * A simple password validator
 */
public class PasswordValidator {

    public static boolean isPasswordValid(String password) {
        return (password.length() >= 6 && !password.contains(" "));
    }
}

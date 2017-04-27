package nl.yoerinijs.notebuddy.validators;

/**
 * A simple password validator
 */
public class PasswordValidator {

    public boolean isPasswordValid(String password) {
        return (password.length() >= 6 && !password.contains(" "));
    }
}

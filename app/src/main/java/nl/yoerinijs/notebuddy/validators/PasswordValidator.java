package nl.yoerinijs.notebuddy.validators;

/**
 * A simple password validator
 */
public class PasswordValidator {

    public boolean isPasswordValid(String password) {
        return checkPassword(password);
    }

    private boolean checkPassword(String password) {
        boolean length = password.length() >= 6;
        boolean space = !password.contains(" ");
        return (length && space);
    }
}

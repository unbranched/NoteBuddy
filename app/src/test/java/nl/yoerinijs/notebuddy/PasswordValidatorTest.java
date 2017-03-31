package nl.yoerinijs.notebuddy;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import nl.yoerinijs.notebuddy.validators.PasswordValidator;

import static org.junit.Assert.*;

/**
 * Unit test for the PasswordValidator class
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class PasswordValidatorTest {
    @Before
    public void setup() {
        PowerMockito.mockStatic(Log.class);
    }

    @Test
    public void passwordValidator_checkPassword() {
        PasswordValidator pv = new PasswordValidator();
        assertTrue(pv.isPasswordValid("thispasswordisnotvalid"));
        assertTrue(pv.isPasswordValid("123456"));
        assertFalse(pv.isPasswordValid("this password is not valid as well"));
        assertFalse(pv.isPasswordValid("12345"));
    }
}
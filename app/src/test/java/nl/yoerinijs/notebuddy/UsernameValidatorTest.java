package nl.yoerinijs.notebuddy;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import nl.yoerinijs.notebuddy.validators.UsernameValidator;

import static org.junit.Assert.*;

/**
 * Unit test for the UsernameValidator class
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class UsernameValidatorTest {
    @Before
    public void setup() {
        PowerMockito.mockStatic(Log.class);
    }

    @Test
    public void usernameValidator_CheckUsername() {
        UsernameValidator uv = new UsernameValidator();
        assertTrue(uv.isUsernameValid("Username"));
        assertFalse(uv.isUsernameValid("us"));
        assertFalse(uv.isUsernameValid("us ername"));
    }
}
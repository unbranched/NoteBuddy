package nl.yoerinijs.notebuddy;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import nl.yoerinijs.notebuddy.validators.SecretValidator;

import static org.junit.Assert.*;

/**
 * Unit test for the SecretValidator class
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class SecretValidatorTest {
    @Before
    public void setup() {
        PowerMockito.mockStatic(Log.class);
    }

    @Test
    public void secretValidator_CheckSecret() {
        SecretValidator sv = new SecretValidator();
        assertTrue(sv.isSecretValid("What's my mother's name?"));
        assertFalse(sv.isSecretValid("no"));
    }
}
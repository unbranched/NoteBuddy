package nl.yoerinijs.nb.validators;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import nl.yoerinijs.nb.validators.NoteBodyValidator;

import static org.junit.Assert.*;

/**
 * Unit test for the NoteBodyValidator class
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class NoteBodyValidatorTest {
    @Before
    public void setup() {
        PowerMockito.mockStatic(Log.class);
    }

    @Test
    public void NoteBodyValidator_checkNoteBody() {
        NoteBodyValidator nbv = new NoteBodyValidator();
        assertFalse(nbv.isNoteBodyValid(""));
        assertTrue(nbv.isNoteBodyValid("note"));
    }
}
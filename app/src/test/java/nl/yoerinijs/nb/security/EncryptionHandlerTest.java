package nl.yoerinijs.nb.security;

import android.content.Context;
import android.content.SharedPreferences;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import nl.yoerinijs.nb.storage.KeyValueDB;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link EncryptionHandler} class.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AesCbcWithIntegrity.class)
public class EncryptionHandlerTest {

    private static final String PLAIN_TEXT = "myPlainText";

    private static final String PASSWORD = "myPassword";

    private static final String SALT_STRING = "6973518216a9fdf916a4";

    @Mock
    private Context m_mockContext;

    @Mock
    private SharedPreferences m_sharedPreferences;

    @BeforeClass
    public static void mockCryptoLib() throws Exception {
        PowerMockito.mockStatic(AesCbcWithIntegrity.class);
    }

    @Before
    public void mockSharedPrefs() throws Exception {
        when(m_mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(m_sharedPreferences);
    }

    @Test
    public void encryptFile_runWithValidParamsNoSaltSet_expectEmptyString() throws Exception {
        // Execute
        String encryptedText = EncryptionHandler.encryptFile(PLAIN_TEXT, PASSWORD, m_mockContext);

        // Validate
        Assert.assertTrue(encryptedText.isEmpty());
    }

    @Test
    public void encryptFile_runWithValidParamsSaltSet_expectEmptyString() throws Exception {
        // Setup
        when(KeyValueDB.getDerivedKeySalt(m_mockContext)).thenReturn(SALT_STRING);
        when(EncryptionHandler.getDerivedKey(PASSWORD, SALT_STRING)).thenReturn(null);

        // Execute
        String encryptedText = EncryptionHandler.encryptFile(PLAIN_TEXT, PASSWORD, m_mockContext);

        // Validate
        Assert.assertTrue(encryptedText.isEmpty());
    }
}

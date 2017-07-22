package nl.yoerinijs.nb.security;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.os.Process;
import android.util.Log;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.InvocationTargetException;

import nl.yoerinijs.nb.activities.MainActivity;
import nl.yoerinijs.nb.storage.KeyValueDB;

import static nl.yoerinijs.nb.security.AesCbcWithIntegrity.generateSalt;
import static nl.yoerinijs.nb.security.AesCbcWithIntegrity.saltString;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by <a href="mailto:yoeri@posteo.net">Yoeri</a> on 22-7-2017.
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

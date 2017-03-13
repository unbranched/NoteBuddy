package nl.yoerinijs.notebuddy.security;

import android.util.Log;

import static nl.yoerinijs.notebuddy.security.AesCbcWithIntegrity.generateKeyFromPassword;

/**
 * A class that encrypts and decrypts text files
 */
public class TextCryptor {

    private static final String PASSWORD = "jU%C?xy?^jmf8b&d--_XXhKfNvMX_d8tsymBu8jQuBrfe+pxnseDbGKWjVbM7!p5pwaxF9jmDahpUk7qCx@DL^UxBz=k=v!d!e2^";
    private static final String LOG_TAG = "Text Cryptor";

    AesCbcWithIntegrity.SecretKeys key;
    AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac;

    /**
     * Get key method
     * @param salt
     * @return
     */
    private AesCbcWithIntegrity.SecretKeys getKey(String salt) {
        try {
            // Generate key
            key = generateKeyFromPassword(PASSWORD, salt);

            // Log success
            Log.d(LOG_TAG, "Key generated");
        } catch (Exception e) {
            // Log failure
            Log.d(LOG_TAG, e.getMessage());
        }
        return key;
    }

    /**
     * Encrypt file method
     * @param plainText
     * @param salt
     * @return
     */
    public String encryptFile(String plainText, String salt) {
        String encryptedText = "";
        try {
            // Encrypt plain text
            cipherTextIvMac = AesCbcWithIntegrity.encrypt(plainText, getKey(salt));
            encryptedText = cipherTextIvMac.toString();

            // Log success
            Log.d(LOG_TAG, "Text encrypted");
        } catch (Exception e) {
            // Log failure
            Log.d(LOG_TAG, e.getMessage());
        }
        return encryptedText;
    }

    /**
     * Decrypt file method
     * @param encryptedText
     * @param salt
     * @return
     */
    public String decryptFile(String encryptedText, String salt) {
        String decryptedText = "";
        try {
            // Decrypt encrypted file
            cipherTextIvMac = new AesCbcWithIntegrity.CipherTextIvMac(encryptedText);
            decryptedText = AesCbcWithIntegrity.decryptString(cipherTextIvMac, getKey(salt));

            // Log success
            Log.d(LOG_TAG, "Text decrypted");
        } catch (Exception e) {
            // Log failure
            Log.d(LOG_TAG, e.getMessage());
        }
        return decryptedText;
    }
}

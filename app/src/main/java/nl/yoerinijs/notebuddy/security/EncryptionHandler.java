package nl.yoerinijs.notebuddy.security;

import android.content.Context;
import android.support.annotation.NonNull;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static nl.yoerinijs.notebuddy.security.AesCbcWithIntegrity.generateKeyFromPassword;
import static nl.yoerinijs.notebuddy.security.AesCbcWithIntegrity.keyString;

/**
 * A class that encrypts and decrypts text files
 */
public class EncryptionHandler {

    private static AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac;

    /**
     * Method to get a key derived of a password
     * @param password
     * @param salt
     * @return
     * @throws GeneralSecurityException
     */
    public static String getDerivedKey(@NonNull String password, @NonNull String salt) throws GeneralSecurityException {
        return keyString(createDerivedKey(password, salt));
    }

    /**
     * Create a password derived key
     * @param password
     * @param derivedKeySalt
     * @return
     * @throws GeneralSecurityException
     */
    private static AesCbcWithIntegrity.SecretKeys createDerivedKey(@NonNull String password, @NonNull String derivedKeySalt) throws GeneralSecurityException {
        return generateKeyFromPassword(password, derivedKeySalt);
    }

    /**
     * Get the master key
     * @param derivedKey
     * @param masterKeySalt
     * @return
     * @throws GeneralSecurityException
     */
    private static AesCbcWithIntegrity.SecretKeys getMasterKey(@NonNull String derivedKey, @NonNull String masterKeySalt) throws GeneralSecurityException {
        return generateKeyFromPassword(derivedKey, masterKeySalt);
    }

    /**
     * Method to encrypt a string
     * @param plainText
     * @param password
     * @param context
     * @return
     * @throws Exception
     */
    public static String encryptFile(@NonNull String plainText, @NonNull String password, @NonNull Context context) throws Exception {
        String encryptedText = "";
        SaltHandler saltHandler = new SaltHandler();
        String salt = saltHandler.getDerivedKeySalt(context);
        if (salt == null) {
            return encryptedText;
        }
        String derivedKey = getDerivedKey(password, salt);
        if (derivedKey == null) {
            return encryptedText;
        }
        cipherTextIvMac = AesCbcWithIntegrity.encrypt(plainText, getMasterKey(derivedKey, saltHandler.getMasterKeySalt(context)));
        encryptedText = cipherTextIvMac.toString();
        return encryptedText;
    }

    /**
     * Method to decrypt a string
     * @param encryptedText
     * @param password
     * @param context
     * @return
     * @throws Exception
     */
    public static String decryptFile(@NonNull String encryptedText, @NonNull String password, @NonNull Context context) throws Exception {
        String decryptedText = "";
        SaltHandler saltHandler = new SaltHandler();
        String derivedKey = getDerivedKey(password, saltHandler.getDerivedKeySalt(context));
        if (derivedKey == null) {
            return decryptedText;
        }
        cipherTextIvMac = new AesCbcWithIntegrity.CipherTextIvMac(encryptedText);
        decryptedText = AesCbcWithIntegrity.decryptString(cipherTextIvMac, getMasterKey(derivedKey, saltHandler.getMasterKeySalt(context)));
        return decryptedText;
    }

    /**
     * Method to hash a string
     * @param string
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String hashString(@NonNull String string) throws NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("SHA-256");
        m.update(string.getBytes());
        return new String(m.digest());
    }
}

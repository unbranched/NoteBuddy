package nl.yoerinijs.notebuddy.security;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import nl.yoerinijs.notebuddy.storage.KeyValueDB;

import static nl.yoerinijs.notebuddy.security.AesCbcWithIntegrity.generateSalt;
import static nl.yoerinijs.notebuddy.security.AesCbcWithIntegrity.saltString;

/**
 * A class that generates salts for encryption purposes
 */
public class SaltHandler {

    public void setSalt(@NonNull Context context) throws Exception {
        KeyValueDB k = new KeyValueDB();
        if (k.getDerivedKeySalt(context) == null || k.getMasterKeySalt(context) == null) {
            // Generate salt for derived key
            String derivedKeySalt = saltString(generateSalt());

            // Store salt for derived key
            k.setDerivedKeySalt(context, derivedKeySalt);

            // Generate salt for master key
            String masterKeySalt = saltString(generateSalt());

            // Store salt for master key
            k.setMasterKeySalt(context, masterKeySalt);
        }
    }

    @Nullable
    public String getDerivedKeySalt(@NonNull Context context) throws Exception {
        KeyValueDB k = new KeyValueDB();
        return k.getDerivedKeySalt(context);
    }

    @Nullable
    public String getMasterKeySalt(@NonNull Context context) throws Exception {
        KeyValueDB k = new KeyValueDB();
        return k.getMasterKeySalt(context);
    }
}

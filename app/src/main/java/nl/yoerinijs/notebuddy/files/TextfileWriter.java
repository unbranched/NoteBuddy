package nl.yoerinijs.notebuddy.files;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;

import nl.yoerinijs.notebuddy.security.SaltGenerator;
import nl.yoerinijs.notebuddy.security.TextCryptor;

/**
 * Created by Yoeri on 11-3-2017.
 */

public class TextfileWriter {

    private static final String LOG_TAG = "Textfile Writer";

    public void writeFile(Context context, String fileName, String fileContent) {
        FileOutputStream fos;
        try {
            // Encrypt the note
            TextCryptor sf = new TextCryptor();
            SaltGenerator sg = new SaltGenerator();
            String salt = sg.getSalt(context);
            String encryptedFileContent = sf.encryptFile(fileContent, salt);

            Log.d(LOG_TAG, "Encrypted: " + encryptedFileContent);

            // Write text to file
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(encryptedFileContent.getBytes());
            fos.close();

            // Log success
            Log.d(LOG_TAG, "Text written");
        } catch (Exception e) {

            // Log failure
            Log.d(LOG_TAG, e.getMessage());
        }
    }
}

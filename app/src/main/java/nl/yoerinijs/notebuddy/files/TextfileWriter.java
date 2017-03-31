package nl.yoerinijs.notebuddy.files;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;

import nl.yoerinijs.notebuddy.security.EncryptionHandler;

/**
 * A class responsible for writing text files.
 */
public class TextfileWriter {

    private static final String LOG_TAG = "Textfile Writer";

    public void writeFile(Context context, String fileName, String fileContent, String password) {
        FileOutputStream fos;
        try {
            // Encrypt the note
            EncryptionHandler tc = new EncryptionHandler();
            String encryptedFileContent = tc.encryptFile(fileContent, password, context);

            // Log encrypted value
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

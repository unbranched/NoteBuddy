package nl.yoerinijs.notebuddy.files.text;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import nl.yoerinijs.notebuddy.security.EncryptionHandler;

/**
 * This class reads a text file
 */
public class TextfileReader {

    private static final String LOG_TAG = "Textfile reader";

    /**
     * Text getter
     * @param location
     * @param fileName
     * @param context
     * @return
     */
    public String getText(String location, String fileName, String password, Context context, boolean decrypt){
        return readFile(location, fileName, password, context, decrypt);
    }

    /**
     * Read file method
     * @param location
     * @param fileName
     * @param context
     * @return
     */
    private String readFile(String location, String fileName, String password, Context context, boolean decrypt) {
        File file = new File(location, fileName);
        StringBuilder encryptedText = new StringBuilder();
        String decryptedText = "";

        // Try to read the encrypted file
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                encryptedText.append(line);
                Log.d(LOG_TAG, encryptedText.toString());
            }
            br.close();

            // Next, if selected, decrypt the text file
            // Log decrypted text for debugging purposes
            if(decrypt) {
                EncryptionHandler sf = new EncryptionHandler();
                decryptedText = sf.decryptFile(encryptedText.toString(), password, context);
                Log.d(LOG_TAG, "Decrypted text: " +  decryptedText);
            }
        } catch (Exception e) {
            // Log failure
            Log.d(LOG_TAG, e.getMessage());
        }

        // Return decrypted or encrypted text
        return decrypt ? decryptedText : encryptedText.toString();
    }
}

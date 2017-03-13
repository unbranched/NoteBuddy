package nl.yoerinijs.notebuddy.files;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import nl.yoerinijs.notebuddy.security.SaltGenerator;
import nl.yoerinijs.notebuddy.security.TextCryptor;

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
    public String getText(String location, String fileName, Context context){
        return readFile(location, fileName, context);
    }

    /**
     * Read file method
     * @param location
     * @param fileName
     * @param context
     * @return
     */
    private String readFile(String location, String fileName, Context context) {
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

            // Next, decrypt the text file
            TextCryptor sf = new TextCryptor();
            SaltGenerator sg = new SaltGenerator();
            String salt = sg.getSalt(context);
            decryptedText = sf.decryptFile(encryptedText.toString(), salt);

            // Log success
            Log.d(LOG_TAG, "Decrypted text: " +  decryptedText);
        } catch (Exception e) {
            // Log failure
            Log.d(LOG_TAG, e.getMessage());
        }

        // Return decrypted text
        return decryptedText;
    }
}

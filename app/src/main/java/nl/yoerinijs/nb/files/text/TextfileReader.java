package nl.yoerinijs.nb.files.text;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import nl.yoerinijs.nb.security.EncryptionHandler;

/**
 * This class reads a text file
 */
public class TextfileReader {

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
        StringBuilder encryptedText = new StringBuilder();
        String decryptedText = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(location, fileName)));
            String line;
            while ((line = br.readLine()) != null) {
                encryptedText.append(line);
            }
            br.close();

            if(decrypt) {
                decryptedText = EncryptionHandler.decryptFile(encryptedText.toString(), password, context);
            }
        } catch (Exception e) {
            // Something went wrong. Skip.
        }
        return decrypt ? decryptedText : encryptedText.toString();
    }

    public boolean fileExists(String location, String fileName, String password, Context context) {
        String textContent = getText(location, fileName, password, context, true);
        return !("empty".equals(textContent) || "".equals(textContent) || null == textContent);
    }
}

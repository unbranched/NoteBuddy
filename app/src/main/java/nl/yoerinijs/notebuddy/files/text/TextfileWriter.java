package nl.yoerinijs.notebuddy.files.text;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import nl.yoerinijs.notebuddy.security.EncryptionHandler;

/**
 * A class responsible for writing text files.
 */
public class TextfileWriter {

    public void writeFile(Context context, String fileName, String fileContent, String password) throws Exception {
        EncryptionHandler tc = new EncryptionHandler();
        String encryptedFileContent = tc.encryptFile(fileContent, password, context);
        writeFileContent(null, fileName, encryptedFileContent, context);
    }

    public void writeExternalFile(File file, String fileContent) throws Exception {
        writeFileContent(file, null, fileContent, null);
    }

    private void writeFileContent(@Nullable File file, @Nullable String fileName, String fileContent, @Nullable Context context) throws Exception {
        if(null == file && null == fileName && context == null) {
            throw new IllegalStateException("File or file name must be provided!");
        }
        FileOutputStream fileOutputStream = null == file && null != context ? context.openFileOutput(fileName, Context.MODE_PRIVATE) : new FileOutputStream(file);
        try {
            fileOutputStream.write(fileContent.getBytes());
        } catch (FileNotFoundException f) {
            file.createNewFile();
            fileOutputStream.write(fileContent.getBytes());
        }
        fileOutputStream.close();
    }
}

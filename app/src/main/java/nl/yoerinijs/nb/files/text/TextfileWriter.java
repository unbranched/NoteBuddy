package nl.yoerinijs.nb.files.text;

import android.content.Context;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import nl.yoerinijs.nb.security.EncryptionHandler;

/**
 * A class responsible for writing text files.
 */
public class TextfileWriter {

    public void writeFile(Context context, String fileName, String fileContent, String password) throws Exception {
        String encryptedFileContent = EncryptionHandler.encryptFile(fileContent, password, context);
        writeFileContent(null, fileName, encryptedFileContent, context);
    }

    public void writeExternalFile(File file, String fileContent) throws Exception {
        writeFileContent(file, null, fileContent, null);
    }

    private void writeFileContent(@Nullable File file, @Nullable String fileName, String fileContent, @Nullable Context context) throws Exception {
        if(null == file && null == fileName && context == null) {
            throw new IllegalStateException("File or file name must be provided!");
        }
        FileOutputStream fileOutputStream;
        if(null == file && null != context) {
            fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        } else if(null != file) {
            fileOutputStream = new FileOutputStream(file);
        } else {
            throw new IllegalStateException("Expecting file is not null!");
        }
        try {
            fileOutputStream.write(fileContent.getBytes());
        } catch (FileNotFoundException f) {
            if(null != file && file.createNewFile()){
                fileOutputStream.write(fileContent.getBytes());
            } else {
                throw new IllegalStateException("Expecting file is not null!");
            }
        }
        fileOutputStream.close();
    }
}

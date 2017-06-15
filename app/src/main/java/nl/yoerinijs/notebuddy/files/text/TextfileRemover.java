package nl.yoerinijs.notebuddy.files.text;

import android.util.Log;

import java.io.File;

/**
 * This class removes text files
 */
public class TextfileRemover {

    /**
     * A method that deletes one file
     * Returns true when the file is deleted correctly
     * @param filesDir
     * @param fileName
     * @return
     */
    public boolean deleteFile(String filesDir, String fileName) {
        try {
            new File(filesDir, fileName).delete();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * A method that deletes all files
     * Returns true when the files are deleted correctly
     * @param filesDir
     * @return
     */
    public boolean deleteAllFiles(String filesDir) {
        try {
            File file = new File(filesDir);
            for (int i = 0; i < file.list().length; i++) {
                File note = new File(file, file.list()[i]);
                note.delete();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

package nl.yoerinijs.notebuddy.files.text;

import android.util.Log;

import java.io.File;

/**
 * This class removes text files
 */
public class TextfileRemover {
    private static final String LOG_TAG = "Textfile Remover";

    /**
     * A method that deletes one file
     * Returns true when the file is deleted correctly
     * @param filesDir
     * @param fileName
     * @return
     */
    public boolean deleteFile(String filesDir, String fileName) {

        // Try to remove the file
        try {
            File file = new File(filesDir, fileName);
            file.delete();

            // Log success
            Log.d(LOG_TAG, "File deleted");
            return true;
        } catch (Exception e) {

            // Log failure
            Log.d(LOG_TAG, e.getMessage());
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

        // Try to remove all files
        try {
            File file = new File(filesDir);
            String[] files;
            files = file.list();
            for (int i = 0; i < files.length; i++) {
                File note = new File(file, files[i]);
                note.delete();
            }

            // Log success
            Log.d(LOG_TAG, "All files deleted");
            return true;
        } catch (Exception e) {

            // Log failure
            Log.d(LOG_TAG, e.getMessage());
            return false;
        }
    }
}

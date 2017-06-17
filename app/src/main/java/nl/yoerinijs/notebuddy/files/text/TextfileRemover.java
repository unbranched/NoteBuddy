package nl.yoerinijs.notebuddy.files.text;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import nl.yoerinijs.notebuddy.R;

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
    public static boolean deleteFile(String filesDir, String fileName) {
        return new File(filesDir, fileName).delete();
    }

    /**
     * A method that deletes all files
     * Returns the number of files that are not deleted
     * @param filesDir
     * @return
     */
    public static int deleteAllFiles(@NonNull String filesDir) {
        File dir = new File(filesDir);
        int numberFilesNotDeleted = 0;
        if(dir.isDirectory()) {
            for(String child : dir.list()) {
                File childFile = new File(dir, child);
                if(!childFile.delete()) {
                    numberFilesNotDeleted += 1;
                }
            }
        }
        return numberFilesNotDeleted;
    }
}

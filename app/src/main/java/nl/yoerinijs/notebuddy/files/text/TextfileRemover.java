package nl.yoerinijs.notebuddy.files.text;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.List;

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
     * Returns true when the files are deleted correctly
     * @param filesDir
     * @return
     */
    public static boolean deleteAllFiles(String filesDir) {
        File dir = new File(filesDir);
        if(dir.isDirectory()) {
            for(String child : dir.list()) {
                File childFile = new File(dir, child);
                if(!childFile.delete()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}

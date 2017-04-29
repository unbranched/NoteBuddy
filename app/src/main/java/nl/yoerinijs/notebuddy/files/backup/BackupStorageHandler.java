package nl.yoerinijs.notebuddy.files.backup;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import nl.yoerinijs.notebuddy.files.misc.DirectoryReader;
import nl.yoerinijs.notebuddy.files.text.TextfileRemover;

/**
 * This handler holds everything related to the external storage.
 */
public class BackupStorageHandler {

    private static final String LOG_TAG = "BackupStorageHandler";
    private final static String BACKUP_DIRECTORY = "NoteBuddy";

    /**
     * Returns whether external storage is writable
     * @return
     */
    public boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * Returns the backup directory as String
     * @return
     */
    public String getBackupDirectory() {
        return Environment.getExternalStorageDirectory().getPath() + "/" + BACKUP_DIRECTORY + "/";
    }

    /**
     * Method for requesting writing permissions
     * @param context
     */
    public void requestWritingPermissions(@NonNull Context context) {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    /**
     * Returns the external storage dir as File
     * @param context
     * @return
     * @throws Exception
     */
    public File getStorageDir(@NonNull Context context) throws Exception {
        // Check if NoteBuddy has the correct permissions
        requestWritingPermissions(context);

        // Get the directory
        File docsFolder = new File(getBackupDirectory());
        if (!docsFolder.exists()) {
            final String present = docsFolder.mkdirs() ? "present" : "not present";
            Log.d(LOG_TAG, "Document storage is " + present);
        }
        return docsFolder;
    }

    /**
     * Clears the external storage dir
     * @param context
     * @return
     */
    public void clearStorageDir(@NonNull Context context)  {
        final TextfileRemover textfileRemover = new TextfileRemover();
        try {
            textfileRemover.deleteAllFiles(getStorageDir(context).toString());
        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage());
        }
    }

    /**
     * Returns whether the external storage dir is empty
     * @param context
     * @return
     * @throws Exception
     */
    public boolean isStorageDirEmpty(@NonNull Context context) throws Exception {
        return getNumberOfFilesInStorageDir(context) <= 0 ? true : false;
    }

    /**
     * Get number of files in external storage dir
     * @param context
     * @return
     * @throws Exception
     */
    public int getNumberOfFilesInStorageDir(@NonNull Context context) throws Exception {
        final DirectoryReader directoryReader = new DirectoryReader();
        ArrayList<String> filesInStorageDir = directoryReader.getFileNames(getStorageDir(context).toString(), 0);
        if(null == filesInStorageDir) {
            return 0;
        }
        return filesInStorageDir.size();
    }
}

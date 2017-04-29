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
    private void requestWritingPermissions(@NonNull Context context) {
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

        File docsFolder = new File(getBackupDirectory());
        if (!docsFolder.exists()) {
            final String present = docsFolder.mkdirs() ? "present" : "not present";
            Log.d(LOG_TAG, "Document storage is " + present);
        }
        return docsFolder;
    }
}

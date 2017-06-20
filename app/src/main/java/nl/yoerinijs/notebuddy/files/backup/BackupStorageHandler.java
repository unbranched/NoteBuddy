package nl.yoerinijs.notebuddy.files.backup;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.File;

import nl.yoerinijs.notebuddy.files.misc.DirectoryReader;
import nl.yoerinijs.notebuddy.files.misc.LocationCentral;
import nl.yoerinijs.notebuddy.files.text.TextfileRemover;

/**
 * This handler holds everything related to the external storage.
 */
public class BackupStorageHandler {

    private final static String BACKUP_DIRECTORY = LocationCentral.BACKUP_DIR;

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
        requestWritingPermissions(context);
        return new File(getBackupDirectory());
    }

    /**
     * Clears the external storage dir
     * @param context
     * @return
     */
    public void clearStorageDir(@NonNull Context context) throws Exception  {
        TextfileRemover.deleteAllFiles(getStorageDir(context).toString());
    }

    /**
     * Returns whether the external storage dir is empty
     * @param context
     * @return
     * @throws Exception
     */
    public boolean isStorageDirEmpty(@NonNull Context context) throws Exception {
        return getNumberOfFilesInStorageDir(context) <= 0;
    }

    /**
     * Get number of files in external storage dir
     * @param context
     * @return
     * @throws Exception
     */
    public int getNumberOfFilesInStorageDir(@NonNull Context context) throws Exception {
        return null == DirectoryReader.getFileNames(getStorageDir(context).toString(), 0) ? 0 : DirectoryReader.getFileNames(getStorageDir(context).toString(), 0).size();
    }
}

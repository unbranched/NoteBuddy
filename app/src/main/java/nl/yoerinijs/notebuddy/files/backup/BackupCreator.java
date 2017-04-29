package nl.yoerinijs.notebuddy.files.backup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import nl.yoerinijs.notebuddy.files.misc.DirectoryReader;
import nl.yoerinijs.notebuddy.files.text.TextfileReader;
import nl.yoerinijs.notebuddy.files.text.TextfileWriter;

/**
 * This holds everything related to backup creation.
 */
public class BackupCreator {

    private final static String BEGIN_ENCRYPTED_FILE = "<enc>";
    private final static String LOG_TAG = "BackupCreator";
    private final static String BACKUP_FILE_EXT = "txt";

    private BackupStorageHandler mBackupStorageHandler;
    private int mNumberOfNotes;

    /**
     * Get backup location
     * @return
     */
    public String getBackupLocation() {
        return mBackupStorageHandler.getBackupDirectory();
    }

    /**
     * Get backup file extension
     * @return
     */
    public String getBackupFileExt() {
        return BACKUP_FILE_EXT;
    }

    /**
     * Get begin of encrypted file. This should be BEGIN_ENCRYPTED_FILE.
     * This part is needed to verify whether a file is encrypted or not.
     * @return
     */
    public String getBeginEncryptedFile() {
        return BEGIN_ENCRYPTED_FILE;
    }

    /**
     * Returns number of notes.
     * @return
     */
    public int getNumberOfNotes() {
        return mNumberOfNotes;
    }

    /**
     * Returns true if the backup is created successfully.
     * @param locationPath
     * @param password
     * @param context
     * @param decryptNotes
     * @return
     */
    public boolean isBackupCreated(@NonNull String locationPath, @NonNull String password, @NonNull Context context, boolean decryptNotes) {
        mBackupStorageHandler = new BackupStorageHandler();

        // Check if the permission is granted to write to the external storage
        if(!mBackupStorageHandler.isExternalStorageWritable()) {
            return false;
        }

        // Try to create the backup. If something goes wrong, return false.
        try {
            return isCreated(locationPath, password, context, decryptNotes);
        } catch (Exception e) {
            // Log exception
            Log.d(LOG_TAG, e.getMessage());
            return false;
        }
    }

    /**
     * Creates the backup. Returns true if everything went well.
     * @param locationPath
     * @param password
     * @param context
     * @param decryptNotes
     * @return
     * @throws Exception
     */
    private boolean isCreated(@NonNull String locationPath, @NonNull String password, @NonNull Context context, boolean decryptNotes) throws Exception {
        ArrayList<String> storedFiles = DirectoryReader.getFileNames(locationPath, 0);

        // Nullity check
        if(null == storedFiles) {
            return false;
        }

        // Check if there are some notes to backup
        mNumberOfNotes = storedFiles.size();
        if(mNumberOfNotes <= 0) {
            return false;
        }

        // Now we can backup the notes
        final File documentsStorageDir = mBackupStorageHandler.getStorageDir(context);
        final TextfileReader textfileReader = new TextfileReader();
        final TextfileWriter textfileWriter = new TextfileWriter();
        StringBuilder sb = new StringBuilder();
        for(String storedFile : storedFiles) {
            File file = new File(documentsStorageDir + "/" + storedFile + "." + BACKUP_FILE_EXT);

            // Check if user wants to decrypt the notes
            if(decryptNotes) {
                // User wants to decrypt, thus, 'true' is passed to the TextfileReader to decrypt the note
                sb.append(textfileReader.getText(locationPath, storedFile, password, context, true));
            } else {
                // User wants to store the note encrypted. First, a mark is added to the text so NoteBuddy can later
                // recognize the file as encrypted when the user wants to import it again
                sb.append(BEGIN_ENCRYPTED_FILE);

                // 'False' is passed to the TextfileReader to perform a raw copy
                sb.append(textfileReader.getText(locationPath, storedFile, password, context, false));
            }

            // Now, write the text to the external file
            textfileWriter.writeExternalFile(file, sb.toString());
        }

        // Let user now everything went smooth
        return true;
    }
}

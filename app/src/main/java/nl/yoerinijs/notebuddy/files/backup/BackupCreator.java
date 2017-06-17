package nl.yoerinijs.notebuddy.files.backup;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.List;

import nl.yoerinijs.notebuddy.files.misc.DirectoryReader;
import nl.yoerinijs.notebuddy.files.text.TextfileReader;
import nl.yoerinijs.notebuddy.files.text.TextfileWriter;

/**
 * This holds everything related to backup creation.
 */
public class BackupCreator {

    private final static String BEGIN_ENCRYPTED_FILE = "<enc>";

    final static String BACKUP_FILE_EXT = "txt";

    private static int m_numberOfNotes;

    /**
     * Get backup location
     * @return
     */
    public String getBackupLocation() {
        BackupStorageHandler b = new BackupStorageHandler();
        return b.getBackupDirectory();
    }

    /**
     * Get begin of encrypted file. This should be BEGIN_ENCRYPTED_FILE.
     * This part is needed to verify whether a file is encrypted or not.
     * @return
     */
    String getBeginEncryptedFile() {
        return BEGIN_ENCRYPTED_FILE;
    }

    /**
     * Returns number of notes.
     * @return
     */
    public int getNumberOfNotes() {
        return m_numberOfNotes;
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
        BackupStorageHandler b = new BackupStorageHandler();
        if(!b.isExternalStorageWritable()) {
            return false;
        }
        try {
            return isCreated(locationPath, password, context, decryptNotes, b);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Creates the backup. Returns true if everything went well.
     * @param locationPath
     * @param password
     * @param context
     * @param decryptNotes
     * @param backupStorageHandler
     * @return
     * @throws Exception
     */
    private boolean isCreated(@NonNull String locationPath, @NonNull String password, @NonNull Context context, boolean decryptNotes, BackupStorageHandler backupStorageHandler) throws Exception {
        List<String> storedFiles = DirectoryReader.getFileNames(locationPath, 0);
        if(null == storedFiles) {
            return false;
        }

        m_numberOfNotes = storedFiles.size();
        if(m_numberOfNotes <= 0) {
            return false;
        }

        TextfileReader textfileReader = new TextfileReader();
        TextfileWriter textfileWriter = new TextfileWriter();
        for(String storedFile : storedFiles) {
            StringBuilder sb = new StringBuilder();
            if(decryptNotes) {
                sb.append(textfileReader.getText(locationPath, storedFile, password, context, true));
            } else {
                sb.append(BEGIN_ENCRYPTED_FILE);
                sb.append(textfileReader.getText(locationPath, storedFile, password, context, false));
            }
            File file = new File(backupStorageHandler.getStorageDir(context) + "/" + storedFile + "." + BACKUP_FILE_EXT);
            textfileWriter.writeExternalFile(file, sb.toString());
        }
        return true;
    }
}

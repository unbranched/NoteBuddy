package nl.yoerinijs.notebuddy.files.backup;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import nl.yoerinijs.notebuddy.R;
import nl.yoerinijs.notebuddy.files.misc.DirectoryReader;
import nl.yoerinijs.notebuddy.files.text.TextfileReader;
import nl.yoerinijs.notebuddy.files.text.TextfileWriter;

/**
 * This holds everything related to backup creation.
 */
public class BackupCreator {

    private final static String BEGIN_ENCRYPTED_FILE = "<enc>";

    final static String BACKUP_FILE_EXT = "txt";

    private int m_numberOfNotes;

    private Context m_context;

    private String m_password;

    private String m_location;

    private boolean m_decrypt;

    private BackupStorageHandler m_handler;

    private List<String> m_files;

    private ProgressDialog m_dialog;

    private BackupStorageHandler getHandler() {
        if(null == m_handler) {
            m_handler = new BackupStorageHandler();
        }
        return m_handler;
    }

    private String getBackupLocation() {
        return getHandler().getBackupDirectory();
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
     * Starts the backup process.
     * @param locationPath
     * @param password
     * @param context
     * @param decryptNotes
     * @return
     */
    public void createBackup(@NonNull String locationPath, @NonNull String password, @NonNull Context context, boolean decryptNotes) {
        BackupStorageHandler handler = getHandler();
        if(!handler.isExternalStorageWritable()) {
            return;
        }

        List<String> storedFiles = DirectoryReader.getFileNames(locationPath, 0);
        if(null == storedFiles) {
            return;
        }

        m_numberOfNotes = storedFiles.size();
        if(m_numberOfNotes <= 0) {
            return;
        }

        m_files = storedFiles;
        m_handler = handler;
        m_location = locationPath;
        m_password = password;
        m_context = context;
        m_decrypt = decryptNotes;

        BackupNotes backupNotes = new BackupNotes();
        backupNotes.execute();
    }

    private class BackupNotes extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                TextfileReader textfileReader = new TextfileReader();
                TextfileWriter textfileWriter = new TextfileWriter();
                int counter = 0;
                for(String storedFile : m_files) {
                    StringBuilder sb = new StringBuilder();
                    if(m_decrypt) {
                        sb.append(textfileReader.getText(m_location, storedFile, m_password, m_context, true));
                    } else {
                        sb.append(BEGIN_ENCRYPTED_FILE);
                        sb.append(textfileReader.getText(m_location, storedFile, m_password, m_context, false));
                    }
                    File file = new File(m_handler.getStorageDir(m_context) + "/" + storedFile + "." + BACKUP_FILE_EXT);
                    textfileWriter.writeExternalFile(file, sb.toString());
                    m_dialog.setProgress(++counter);
                }
                return Boolean.TRUE;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            m_dialog = new ProgressDialog(m_context);
            m_dialog.setCancelable(true);
            m_dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            m_dialog.setProgress(0);
            m_dialog.setMax(m_numberOfNotes);
            m_dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean isCreated) {
            m_dialog.dismiss();
            provideBackupResult(m_numberOfNotes, isCreated, m_decrypt);
        }
    }

    /**
     * Notifies user and ourselves of backup result
     * @param isBackupCreated
     */
    private void provideBackupResult(int numberOfCreatedNotes, boolean isBackupCreated, boolean areNotesDecrypted) {
        final String notesEncrypted = areNotesDecrypted ? "(" + m_context.getResources().getString(R.string.backup_decrypted).toLowerCase() + ")" : "(" + m_context.getResources().getString(R.string.backup_encrypted).toLowerCase() + ")";
        Toast.makeText(m_context, isBackupCreated ? m_context.getResources().getString(R.string.backup_success) + " " + getBackupLocation() + " " + notesEncrypted : m_context.getResources().getString(R.string.backup_error) + ".", Toast.LENGTH_LONG).show();
        if(isBackupCreated) {
            Toast.makeText(m_context, numberOfCreatedNotes + " " + (numberOfCreatedNotes <= 1 ? m_context.getResources().getString(R.string.backup_number_created_singular).toLowerCase() + "." : m_context.getResources().getString(R.string.backup_number_created_plural).toLowerCase() + "."), Toast.LENGTH_LONG).show();
        }
    }
}

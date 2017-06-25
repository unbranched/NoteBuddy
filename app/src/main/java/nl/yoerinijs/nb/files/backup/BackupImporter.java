package nl.yoerinijs.nb.files.backup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.yoerinijs.nb.R;
import nl.yoerinijs.nb.activities.LoginActivity;
import nl.yoerinijs.nb.files.misc.DirectoryReader;
import nl.yoerinijs.nb.files.misc.LocationCentral;
import nl.yoerinijs.nb.files.text.TextfileReader;
import nl.yoerinijs.nb.files.text.TextfileRemover;
import nl.yoerinijs.nb.files.text.TextfileWriter;

/**
 * This holds everything related to importing external notes
 */
public class BackupImporter {

    private static final String PACKAGE_NAME = LocationCentral.PACKAGE;

    private static final String NOTES_ACTIVITY = LocationCentral.NOTES;

    private final static String TEMP_NAME = "TEMP";

    private String m_backupLocationString;

    private BackupStorageHandler m_backupStorageHandler = new BackupStorageHandler();

    private BackupCreator m_backupCreator = new BackupCreator();

    private String m_password;

    private Context m_context;

    private ProgressDialog m_dialog;

    /**
     * Imports external notes. Returns true if everything went well.
     * @param password
     * @param context
     * @return
     */
    public void importExternalNotes(@NonNull String password, @NonNull Context context) {
        if(!m_backupStorageHandler.isExternalStorageWritable()) {
            return;
        }

        if(getNumberOfFilesInDir(context) <= 0) {
            return;
        }

        if(getExternalNotes() == null || getExternalNotes().size() <= 0) {
            return;
        }

        m_password = password;
        m_context = context;

        ImportNotes importNotes = new ImportNotes();
        importNotes.execute();
    }

    /**
     * Returns the number of notes in external dir
     * @return
     */
    private int getNumberOfFilesInDir(Context context) {
        m_backupStorageHandler.requestWritingPermissions(context);
        m_backupLocationString = m_backupStorageHandler.getBackupDirectory();
        return null == DirectoryReader.getFileNames(m_backupLocationString, 0) ? 0 : DirectoryReader.getFileNames(m_backupLocationString, 0).size();
    }

    /**
     * Returns a list of all external notes
     * @return
     */
    private List<String> getExternalNotes() {
        List<String> cleanListOfExternalNotes = new ArrayList<>();
        final List<String> listOfExternalFiles = DirectoryReader.getFileNames(m_backupLocationString, 0);
        if(null == listOfExternalFiles) {
            return cleanListOfExternalNotes;
        }
        for(int i = 0; i < listOfExternalFiles.size(); i++) {
            if(listOfExternalFiles.get(i).contains("." + BackupCreator.BACKUP_FILE_EXT)) {
                cleanListOfExternalNotes.add(listOfExternalFiles.get(i));
            }
        }
        return cleanListOfExternalNotes;
    }

    private class ImportNotes extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                TextfileReader textfileReader = new TextfileReader();
                TextfileWriter textfileWriter = new TextfileWriter();

                int counter = 0;
                for(String noteName : getExternalNotes()) {
                    String noteContent = textfileReader.getText(m_backupLocationString, noteName, m_password, m_context, false);
                    if(!noteContent.isEmpty()) {
                        if(isEncrypted(noteContent)) {
                            final String tempNoteName = getTempNoteName(noteName);
                            textfileWriter.writeExternalFile(new File(m_backupStorageHandler.getStorageDir(m_context) + "/" + tempNoteName), cleanNoteContent(noteContent));
                            noteContent = textfileReader.getText(m_backupLocationString, tempNoteName, m_password, m_context, true);
                            TextfileRemover.deleteFile(m_backupLocationString, tempNoteName);
                        }
                    }
                    if(noteName.contains(BackupCreator.BACKUP_FILE_EXT)) {
                        noteName = noteName.replace("." + BackupCreator.BACKUP_FILE_EXT, "");
                    }
                    textfileWriter.writeFile(m_context, noteName, noteContent, m_password);
                    m_dialog.setProgress(++counter);
                }
            } catch (Exception e) {
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }

        @Override
        protected void onPreExecute() {
            m_dialog = new ProgressDialog(m_context);
            m_dialog.setCancelable(true);
            m_dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            m_dialog.setProgress(0);
            m_dialog.setMax(getExternalNotes().size());
            m_dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean isImported) {
            m_dialog.dismiss();
            provideImportResult(isImported);
        }
    }

    /**
     * Returns a name for a temporarily note file
     * @param noteName
     * @return
     */
    private String getTempNoteName(@NonNull String noteName) {
        return noteName.replace("." + BackupCreator.BACKUP_FILE_EXT, "") + "_" + TEMP_NAME + "." + BackupCreator.BACKUP_FILE_EXT;
    }

    /**
     * Returns whether the note contains NoteBuddy's encrypted note mark (i.e. <enc>).
     * If the note contains the mark it is most likely encrypted. Therefore, return true.
     * @param noteContent
     * @return
     */
    private boolean isEncrypted(@NonNull String noteContent) {
        final char[] encryptedBegin = m_backupCreator.getBeginEncryptedFile().toCharArray();
        final char[] noteContentArray = noteContent.toCharArray();
        if(noteContentArray[0] == encryptedBegin[0]) {
            if(noteContentArray[1] == encryptedBegin[1]) {
                if(noteContentArray[2] == encryptedBegin[2]) {
                    if(noteContentArray[3] == encryptedBegin[3]) {
                        if(noteContentArray[4] == encryptedBegin[4]) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns the note content without NoteBuddy's encrypted note mark (i.e. <enc>)
     * @param noteContent
     * @return
     */
    private String cleanNoteContent(@NonNull String noteContent) {
        return noteContent.replace(m_backupCreator.getBeginEncryptedFile(), "");
    }

    /**
     * Notifies user and ourselves of import result
     * @param areNotesImported
     */
    private void provideImportResult(boolean areNotesImported) {
        Toast.makeText(m_context, areNotesImported ?  m_context.getResources().getString(R.string.import_success) :  m_context.getResources().getString(R.string.import_error) + ".", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setClassName(m_context, PACKAGE_NAME + "." + NOTES_ACTIVITY);
        intent.putExtra(LoginActivity.KEY_PASSWORD, m_password);
        m_context.startActivity(intent);
    }
}

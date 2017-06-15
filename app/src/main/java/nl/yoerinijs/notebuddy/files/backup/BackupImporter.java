package nl.yoerinijs.notebuddy.files.backup;

import android.content.Context;
import android.support.annotation.NonNull;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.yoerinijs.notebuddy.files.misc.DirectoryReader;
import nl.yoerinijs.notebuddy.files.text.TextfileReader;
import nl.yoerinijs.notebuddy.files.text.TextfileRemover;
import nl.yoerinijs.notebuddy.files.text.TextfileWriter;

/**
 * This holds everything related to importing external notes
 */
public class BackupImporter {

    private final static String TEMP_NAME = "TEMP";

    private static String m_backupLocationString;

    private BackupStorageHandler m_backupStorageHandler = new BackupStorageHandler();

    private BackupCreator m_backupCreator = new BackupCreator();

    /**
     * Imports external notes. Returns true if everything went well.
     * @param password
     * @param context
     * @return
     */
    public boolean areNotesImported(@NonNull String password, @NonNull Context context) {
        if(!m_backupStorageHandler.isExternalStorageWritable()) {
            return false;
        }
        if(getNumberOfFilesInDir(context) <= 0) {
            return false;
        }
        try {
            return areNotesImported(context, password);
        } catch (Exception e) {
            return false;
        }
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

    /**
     * This imports all external notes. Should return true.
     * @param context
     * @param password
     * @return
     * @throws Exception
     */
    private boolean areNotesImported(@NonNull Context context, @NonNull String password) throws Exception {
        final List<String> externalNotes = getExternalNotes();
        if(externalNotes == null || externalNotes.size() <= 0) {
            return false;
        }

        TextfileReader textfileReader = new TextfileReader();
        TextfileWriter textfileWriter = new TextfileWriter();
        TextfileRemover textfileRemover = new TextfileRemover();
        for(String noteName : externalNotes) {
            String noteContent = textfileReader.getText(m_backupLocationString, noteName, password, context, false);
            if(!noteContent.isEmpty()) {
                if(isEncrypted(noteContent)) {
                    final String tempNoteName = getTempNoteName(noteName);
                    textfileWriter.writeExternalFile(new File(m_backupStorageHandler.getStorageDir(context) + "/" + tempNoteName), cleanNoteContent(noteContent));
                    noteContent = textfileReader.getText(m_backupLocationString, tempNoteName, password, context, true);
                    textfileRemover.deleteFile(m_backupLocationString, tempNoteName);
                }
            }
            if(noteName.contains(BackupCreator.BACKUP_FILE_EXT)) {
                noteName = noteName.replace("." + BackupCreator.BACKUP_FILE_EXT, "");
            }
            textfileWriter.writeFile(context, noteName, noteContent, password);
        }
        return true;
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
}

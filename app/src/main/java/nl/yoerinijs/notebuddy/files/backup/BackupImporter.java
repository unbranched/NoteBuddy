package nl.yoerinijs.notebuddy.files.backup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import nl.yoerinijs.notebuddy.files.misc.DirectoryReader;
import nl.yoerinijs.notebuddy.files.text.TextfileReader;
import nl.yoerinijs.notebuddy.files.text.TextfileRemover;
import nl.yoerinijs.notebuddy.files.text.TextfileWriter;

/**
 * This holds everything related to importing external notes
 */
public class BackupImporter {

    private final static String LOG_TAG = "BackupImporter";
    private final static String TEMP_NAME = "TEMP";

    private BackupStorageHandler mBackupStorageHandler;
    private DirectoryReader mDirectoryReader;
    private String mBackupLocationString;
    private BackupCreator mBackupCreator;
    private String mBackupFileExt;

    /**
     * Imports external notes. Returns true if everything went well.
     * @param password
     * @param context
     * @return
     */
    public boolean areNotesImported(@NonNull String password, @NonNull Context context) {
        mBackupStorageHandler = new BackupStorageHandler();

        // Check if NoteBuddy obtained the read and write permissions
        if(!mBackupStorageHandler.isExternalStorageWritable()) {
            return false;
        }

        // Verify if there are external notes
        if(getNumberOfFilesInDir(context) <= 0) {
            return false;
        }

        // Try to import the notes
        try {
            return areNotesImported(context, password);
        } catch (Exception e) {
            // Log exception
            Log.d(LOG_TAG, e.getMessage());
            return false;
        }
    }

    /**
     * Returns the number of notes in external dir
     * @return
     */
    private int getNumberOfFilesInDir(Context context) {
        // Request reading and writing permissions
        mBackupStorageHandler.requestWritingPermissions(context);

        // Set backup location string
        mBackupLocationString = mBackupStorageHandler.getBackupDirectory();

        // Read external files
        mDirectoryReader = new DirectoryReader();
        final ArrayList<String> listOfNoteNames = mDirectoryReader.getFileNames(mBackupLocationString, 0);
        return null == listOfNoteNames ? 0 : listOfNoteNames.size();
    }

    /**
     * Returns a list of all external notes
     * @return
     */
    private ArrayList<String> getExternalNotes() {
        ArrayList<String> cleanListOfExternalNotes = new ArrayList<>();
        final ArrayList<String> listOfExternalFiles = mDirectoryReader.getFileNames(mBackupLocationString, 0);

        if(null == listOfExternalFiles) {
            return cleanListOfExternalNotes;
        }

        // Only select the files with the right file extension
        for(int i = 0; i < listOfExternalFiles.size(); i++) {
            if(listOfExternalFiles.get(i).contains("." + mBackupFileExt)) {
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
        mBackupCreator = new BackupCreator();
        mBackupFileExt = mBackupCreator.getBackupFileExt();

        // Get external note names. If there are none, return false
        final ArrayList<String> externalNotes = getExternalNotes();
        if(externalNotes == null || externalNotes.size() <= 0) {
            return false;
        }

        // Now, let's import those notes!
        final TextfileReader textfileReader = new TextfileReader();
        final TextfileWriter textfileWriter = new TextfileWriter();
        final TextfileRemover textfileRemover = new TextfileRemover();
        for(String noteName : externalNotes) {
            String noteContent = textfileReader.getText(mBackupLocationString, noteName, password, context, false);

            // Check if note is not empty
            if(!noteContent.isEmpty()) {

                // Check if note is encrypted. If so, create a temporarily file, remove NoteBuddy's encrypted file mark,
                // and add the clean content to the note content local variable. Finally, remove the temporarily file to
                // keep the user's external storage decently.
                // If note is not encrypted, then copy the content raw.
                if(isEncrypted(noteContent)) {
                    final File backupLocation = mBackupStorageHandler.getStorageDir(context);
                    final String tempNoteName = getTempNoteName(noteName);
                    textfileWriter.writeExternalFile(new File(backupLocation + "/" + tempNoteName), cleanNoteContent(noteContent));
                    noteContent = textfileReader.getText(mBackupLocationString, tempNoteName, password, context, true);
                    textfileRemover.deleteFile(mBackupLocationString, tempNoteName);
                }
            }

            // Before NoteBuddy copies the note, it removes the file extension from the external note file name.
            // The file name is considered as the note name in NoteBuddy.
            if(noteName.contains(mBackupFileExt)) {
                noteName = noteName.replace("." + mBackupFileExt, "");
            }

            // Finally, NoteBuddy can write the note to his own storage \0/
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
        return noteName.replace("." + mBackupFileExt, "") + "_" + TEMP_NAME + "." + mBackupFileExt;
    }

    /**
     * Returns whether the note contains NoteBuddy's encrypted note mark (i.e. <enc>).
     * If the note contains the mark it is most likely encrypted. Therefore, return true.
     * @param noteContent
     * @return
     */
    private boolean isEncrypted(@NonNull String noteContent) {
        final char[] encryptedBegin = mBackupCreator.getBeginEncryptedFile().toCharArray();
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
        return noteContent.replace(mBackupCreator.getBeginEncryptedFile(), "");
    }
}

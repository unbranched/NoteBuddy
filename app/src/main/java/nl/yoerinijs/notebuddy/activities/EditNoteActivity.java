package nl.yoerinijs.notebuddy.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import nl.yoerinijs.notebuddy.R;
import nl.yoerinijs.notebuddy.files.FileChecker;
import nl.yoerinijs.notebuddy.files.TextfileRemover;
import nl.yoerinijs.notebuddy.files.TextfileWriter;
import nl.yoerinijs.notebuddy.validators.NoteBodyValidator;
import nl.yoerinijs.notebuddy.validators.NoteTitleValidator;

/**
 * A class for creating and editing notes
 */
public class EditNoteActivity extends AppCompatActivity {

    // Activity references
    private static final String PACKAGE_NAME = "nl.yoerinijs.notebuddy.activities";
    private static final String NOTES_ACTIVITY = "NotesActivity";
    private static final String LOG_TAG = "Edit Note Activity";

    // UI references
    private FloatingActionButton mBackButton;
    private FloatingActionButton mSaveButton;
    private FloatingActionButton mDeleteButton;
    private FloatingActionButton mShareButton;
    private EditText mNoteTitle;
    private EditText mNoteBody;
    private Context mContext;
    private View mFocusView;

    // Commonly used variables
    private String mLocation;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        // Context
        mContext = this;

        // Set up the UI
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFocusView = null;

        mSaveButton = (FloatingActionButton) findViewById(R.id.saveButton);
        mBackButton = (FloatingActionButton) findViewById(R.id.backButton);
        mDeleteButton = (FloatingActionButton) findViewById(R.id.deleteButton);
        mShareButton = (FloatingActionButton) findViewById(R.id.shareButton);
        mNoteTitle = (EditText) findViewById(R.id.noteTitle);
        mNoteBody = (EditText) findViewById(R.id.noteText);

        // Get password from Login activity.
        // Password is needed to derivate a secret key for encrypting and decrypting the data.
        password = getIntent().getStringExtra("PASSWORD");

        // Get absolute internal storage path
        // Log location as well
        mLocation = getFilesDir().getAbsolutePath();
        Log.d(LOG_TAG, "Location: " + mLocation);

        // Get note and note name
        final String note = getIntent().getStringExtra("SELECTED_NOTE");
        final String noteFileName = getIntent().getStringExtra("SELECTED_NOTE_FILENAME");

        // Check if note and note name are null
        if (note == null && noteFileName == null) {
            // Then the user wants to create a note
            // Thus, remove delete and share buttons from UI
            mDeleteButton.setVisibility(View.GONE);
            mShareButton.setVisibility(View.GONE);

            // Log that user wants to create a note
            Log.d(LOG_TAG, "Create new note");
        } else {
            // Set note title
            mNoteTitle.setText(noteFileName);

            // Set note body
            mNoteBody.setText(note);

            // Log that user wants to edit a note
            Log.d(LOG_TAG, "Edit existing note");
        }

        // Save button
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get note title and note body
                final String noteTitle = mNoteTitle.getText().toString();
                final String noteBody = mNoteBody.getText().toString();

                // Validate input
                if (!onSave(noteTitle, noteBody)) {
                    // Verify whether file with current note title already exists
                    // If file exists, display warning dialog
                    FileChecker fc = new FileChecker();
                    if (fc.fileExists(mLocation, noteTitle, password, mContext)) {
                        new AlertDialog.Builder(mContext)
                                .setTitle(getString(R.string.dialog_title_note_exists))
                                .setMessage(getString(R.string.dialog_question_overwrite_note))
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Save note
                                        writeNote(noteTitle, noteFileName, noteBody);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Log that note will not be overwritten
                                        Log.d(LOG_TAG, "Cancelled. Note not overwritten");
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        // File does not exist, so it can be saved without problems
                        writeNote(noteTitle, noteFileName, noteBody);
                    }
                }
            }
        });

        // Back button
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log action
                Log.d(LOG_TAG, "Go back");

                // Finish this activity and go to Notes Activity
                startNotesActivity();
            }
        });

        // Share button
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log action
                Log.d(LOG_TAG, "Share note");

                // Share note content
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mNoteBody.getText().toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        // Delete button
        mDeleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Delete the note
                try {
                    // Display warning dialog
                    new AlertDialog.Builder(mContext)
                            .setTitle(getString(R.string.dialog_title_delete_note))
                            .setMessage(getString(R.string.dialog_question_delete_note))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    // Try to delete the file
                                    TextfileRemover tr = new TextfileRemover();
                                    String deleteMessage = tr.deleteFile(mLocation, noteFileName) ? getString(R.string.success_deleted) : getString(R.string.error_cannot_delete);

                                    // When file is deleted, the deleteMessage will hold the success_deleted string. If an error occurred, the error_cannot_delete string will be displayed.
                                    Toast.makeText(getApplicationContext(), deleteMessage + ". ", Toast.LENGTH_SHORT).show();

                                    // Log result
                                    Log.d(LOG_TAG, deleteMessage);

                                    // Proceed to notes activity
                                    startNotesActivity();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Log that note will not deleted
                                    Log.d(LOG_TAG, "Cancelled. Note not deleted");
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } catch (Exception e) {
                    // Let the user know that the file cannot deleted
                    Toast.makeText(getApplicationContext(), getString(R.string.error_cannot_delete) + ". ", Toast.LENGTH_SHORT).show();

                    // Log failure
                    Log.d(LOG_TAG, e.getMessage());
                }
            }
        });
    }

    /**
     * A method that validates the note input
     * @param noteTitle
     * @param noteBody
     * @return
     */
    @NonNull
    private boolean onSave(@Nullable String noteTitle, @Nullable String noteBody) {
        boolean error = false;

        // Check note title
        NoteTitleValidator ntv = new NoteTitleValidator();
        if (!ntv.isNoteTitleValid(noteTitle)) {
            mNoteTitle.setError(getString(R.string.error_invalid_note_title));
            mFocusView = mNoteTitle;
            error = true;
        }

        // Check note body
        NoteBodyValidator nbv = new NoteBodyValidator();
        if (!nbv.isNoteBodyValid(noteBody)) {
            mNoteBody.setError(getString(R.string.error_invalid_note_body));
            mFocusView = mNoteBody;
            error = true;
        }

        if (error) {
            // There was an error; don't attempt saving and focus the first
            // form field with an error
            mFocusView.requestFocus();

            // Log error
            Log.d(LOG_TAG, "Note title and/or note body is invalid");
        }

        return error;

    }

    /**
     * Write note. Should delete a note when the current note title is not the same as the old note title (i.e. title is changed).
     * @param currentNoteTile
     * @param oldNoteTitle
     * @param noteBody
     */
    private void writeNote(@NonNull String currentNoteTile, @Nullable final String oldNoteTitle, @NonNull String noteBody){
        // Write note
        // Logs will be handled by the TextfileWriter class
        TextfileWriter t = new TextfileWriter();
        t.writeFile(mContext, currentNoteTile, noteBody, password);

        // If the current note title is not the same as the old note title,
        // ask if the note with the old title should be deleted
        if(null != oldNoteTitle) {
            if(!currentNoteTile.equalsIgnoreCase(oldNoteTitle)) {
                // Log that note title is changed
                Log.d(LOG_TAG, "Note title is changed");

                new AlertDialog.Builder(mContext)
                        .setTitle(getString(R.string.dialog_title_old_note))
                        .setMessage(getString(R.string.dialog_question_delete_old_note))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Try to delete note with old tile
                                TextfileRemover tr = new TextfileRemover();
                                tr.deleteFile(mLocation, oldNoteTitle);

                                // Continue
                                postWriting();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Log that old note is not deleted
                                Log.d(LOG_TAG, "Old note not deleted");

                                // Continue
                                postWriting();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                // Log that note title is not changed
                Log.d(LOG_TAG, "Note title not changed");

                // Continue
                postWriting();
            }
        } else {
            // Continue
            postWriting();
        }
    }

    /**
     * This method holds everything related to post writing.
     */
    private void postWriting() {
        // Log post writing
        Log.d(LOG_TAG, "Post writing");

        // Notify user
        Toast.makeText(getApplicationContext(), getString(R.string.success_saved) + ".", Toast.LENGTH_SHORT).show();

        // Proceed to notes activity
        startNotesActivity();
    }

    /**
     * A simple method to start the common notes activity
     */
    private void startNotesActivity() {
        // Log activity
        Log.d(LOG_TAG, "Proceed to " + NOTES_ACTIVITY);

        // Construct activity
        Intent intent = new Intent();
        intent.setClassName(mContext, PACKAGE_NAME + "." + NOTES_ACTIVITY);
        intent.putExtra("PASSWORD", password);

        // Start activity
        startActivity(intent);

        // Close activity for security purposes
        finish();
    }
}

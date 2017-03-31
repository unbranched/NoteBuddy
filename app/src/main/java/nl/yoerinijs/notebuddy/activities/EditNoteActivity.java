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
                if (onSave(noteTitle, noteBody) == false) {
                    // Verify whether file already exists
                    // If file exists, display warning dialog
                    FileChecker fc = new FileChecker();
                    if (fc.fileExists(mLocation, noteTitle, password, mContext)) {
                        new AlertDialog.Builder(mContext)
                                .setTitle(getString(R.string.dialog_title_note_exists))
                                .setMessage(getString(R.string.dialog_question_overwrite_note))
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Save note
                                        writeNote(noteTitle, noteBody);

                                        // Proceed to notes activity
                                        startNotesActivity();
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
                        // Save note
                        writeNote(noteTitle, noteBody);

                        // Proceed to notes activity
                        startNotesActivity();
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

                // Finish this activity
                finish();
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
                TextfileRemover tr = new TextfileRemover();
                try {
                    // Display warning dialog
                    if (tr.deleteFile(mLocation, noteFileName) == true) {
                        new AlertDialog.Builder(mContext)
                                .setTitle(getString(R.string.dialog_title_delete_note))
                                .setMessage(getString(R.string.dialog_question_delete_note))
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Let the user know that the file is deleted successfully
                                        String successMessage = getString(R.string.success_deleted);
                                        Toast.makeText(getApplicationContext(), successMessage + ". ", Toast.LENGTH_SHORT).show();

                                        // Log success
                                        Log.d(LOG_TAG, successMessage);

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
                    } else {
                        throw new IOException();
                    }
                } catch (Exception e) {
                    // Let the user know that the file cannot deleted
                    String errorMessage = getString(R.string.error_cannot_delete);
                    Toast.makeText(getApplicationContext(), errorMessage + ". ", Toast.LENGTH_SHORT).show();

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
     * Write note
     * @param noteTitle
     * @param noteBody
     */
    private void writeNote(@NonNull String noteTitle, @NonNull String noteBody){
        // Write note
        // Logs will be handled by the TextfileWriter class
        TextfileWriter t = new TextfileWriter();
        t.writeFile(mContext, noteTitle, noteBody, password);

        // Notify user
        Toast.makeText(getApplicationContext(), getString(R.string.success_saved) + ".", Toast.LENGTH_SHORT).show();
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

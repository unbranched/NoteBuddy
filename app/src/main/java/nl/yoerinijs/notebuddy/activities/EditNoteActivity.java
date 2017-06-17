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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import nl.yoerinijs.notebuddy.R;
import nl.yoerinijs.notebuddy.files.text.TextfileReader;
import nl.yoerinijs.notebuddy.files.text.TextfileRemover;
import nl.yoerinijs.notebuddy.files.text.TextfileWriter;
import nl.yoerinijs.notebuddy.validators.NoteBodyValidator;
import nl.yoerinijs.notebuddy.validators.NoteTitleValidator;

/**
 * A class for creating and editing notes
 */
public class EditNoteActivity extends AppCompatActivity {

    private static final String PACKAGE_NAME = "nl.yoerinijs.notebuddy.activities";

    private static final String NOTES_ACTIVITY = "NotesActivity";

    private final Context m_context = this;

    private EditText m_noteTitle;

    private EditText m_noteBody;

    private View m_focusView;

    private String m_location;

    private String m_password;

    private TextfileReader m_textFileReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        m_focusView = null;

        FloatingActionButton m_saveButton = (FloatingActionButton) findViewById(R.id.saveButton);
        FloatingActionButton m_backButton = (FloatingActionButton) findViewById(R.id.backButton);
        FloatingActionButton m_deleteButton = (FloatingActionButton) findViewById(R.id.deleteButton);
        FloatingActionButton m_shareButton = (FloatingActionButton) findViewById(R.id.shareButton);

        m_noteTitle = (EditText) findViewById(R.id.noteTitle);
        m_noteBody = (EditText) findViewById(R.id.noteText);
        m_password = getIntent().getStringExtra(LoginActivity.KEY_PASSWORD);
        m_location = getFilesDir().getAbsolutePath();
        m_textFileReader = new TextfileReader();

        final String note = getIntent().getStringExtra(NotesActivity.KEY_NOTE);
        final String noteFileName = getIntent().getStringExtra(NotesActivity.KEY_NOTE_TITLE);
        if (null == note && null == noteFileName) {
            m_deleteButton.setVisibility(View.GONE);
            m_shareButton.setVisibility(View.GONE);
        } else {
            m_noteTitle.setText(noteFileName);
            m_noteBody.setText(note);
        }

        m_saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String noteTitle = m_noteTitle.getText().toString();
                final String noteBody = m_noteBody.getText().toString();
                if(!onSave(noteTitle, noteBody)) {
                    if(m_textFileReader.fileExists(m_location, noteTitle, m_password, m_context)) {
                        new AlertDialog.Builder(m_context)
                                .setTitle(getString(R.string.dialog_title_note_exists))
                                .setMessage(getString(R.string.dialog_question_overwrite_note))
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        writeNote(noteTitle, noteFileName, noteBody);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {}
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        writeNote(noteTitle, noteFileName, noteBody);
                    }
                }
            }
        });

        m_backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNotesActivity();
            }
        });

        m_shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, m_noteBody.getText().toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        m_deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    new AlertDialog.Builder(m_context)
                            .setTitle(getString(R.string.dialog_title_delete_note))
                            .setMessage(getString(R.string.dialog_question_delete_note))
                            .setPositiveButton(getString(R.string.dialog_question_confirm), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), TextfileRemover.deleteFile(m_location, noteFileName) ?
                                            getString(R.string.success_deleted) : getString(R.string.error_cannot_delete) + ". ",
                                            Toast.LENGTH_SHORT).show();
                                    startNotesActivity();
                                }
                            })
                            .setNegativeButton(getString(R.string.dialog_question_deny), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {    }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_cannot_delete) + ". ", Toast.LENGTH_SHORT).show();
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
    private boolean onSave(@Nullable String noteTitle, @Nullable String noteBody) {
        boolean error = false;
        if(!NoteTitleValidator.isNoteTitleValid(noteTitle)) {
            m_noteTitle.setError(getString(R.string.error_invalid_note_title));
            m_focusView = m_noteTitle;
            error = true;
        }
        if(!NoteBodyValidator.isNoteBodyValid(noteBody)) {
            m_noteBody.setError(getString(R.string.error_invalid_note_body));
            m_focusView = m_noteBody;
            error = true;
        }
        if(error) {
            m_focusView.requestFocus();
        }
        return error;
    }

    /**
     * Write note. Should delete a note when the current note title is not the same as the old note title (i.e. title is changed).
     * @param currentNoteTile
     * @param oldNoteTitle
     * @param noteBody
     */
    private void writeNote(@NonNull String currentNoteTile, @Nullable final String oldNoteTitle, @NonNull String noteBody) {
        try {
            TextfileWriter textfileWriter = new TextfileWriter();
            textfileWriter.writeFile(m_context, currentNoteTile, noteBody, m_password);
            if(null != oldNoteTitle) {
                if(!currentNoteTile.equalsIgnoreCase(oldNoteTitle)) {
                    new AlertDialog.Builder(m_context)
                            .setTitle(getString(R.string.dialog_title_old_note))
                            .setMessage(getString(R.string.dialog_question_delete_old_note))
                            .setPositiveButton(getString(R.string.dialog_answer_delete_old_note), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    TextfileRemover.deleteFile(m_location, oldNoteTitle);
                                    postWriting();
                                }
                            })
                            .setNegativeButton(getString(R.string.dialog_answer_keep_old_note), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    postWriting();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    postWriting();
                }
            } else {
                postWriting();
            }
        } catch (Exception e) {
            // Something went wrong. Skip.
        }
    }

    /**
     * This method holds everything related to post writing.
     */
    private void postWriting() {
        Toast.makeText(getApplicationContext(), getString(R.string.success_saved) + ".", Toast.LENGTH_SHORT).show();
        startNotesActivity();
    }

    /**
     * A simple method to start the common notes activity
     */
    private void startNotesActivity() {
        Intent intent = new Intent();
        intent.setClassName(m_context, PACKAGE_NAME + "." + NOTES_ACTIVITY);
        intent.putExtra(LoginActivity.KEY_PASSWORD, m_password);
        startActivity(intent);
        finish();
    }
}

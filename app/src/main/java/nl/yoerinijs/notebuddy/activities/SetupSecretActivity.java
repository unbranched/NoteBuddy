package nl.yoerinijs.notebuddy.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import nl.yoerinijs.notebuddy.R;
import nl.yoerinijs.notebuddy.storage.KeyValueDB;
import nl.yoerinijs.notebuddy.validators.SecretValidator;

/**
 * This class sets up the secret question
 */
public class SetupSecretActivity extends AppCompatActivity {

    // Activity references
    private static final String PACKAGE_NAME = "nl.yoerinijs.notebuddy.activities";
    private static final String NOTES_ACTIVITY = "NotesActivity";
    private static final String LOG_TAG = "Setup Activity";

    // UI references
    private AutoCompleteTextView mSecretQuestionView;
    private EditText mSecretAnswerView;
    private Button mSecretButton;
    private View mProgressView;
    private View mLoginFormView;
    private Context mContext;

    // Key value storage
    private KeyValueDB keyValueDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_secret);

        keyValueDB = new KeyValueDB();
        mContext = this;

        // Check if there are a secret question and secret answer already
        try {
            if (keyValueDB.getSecretQuestion(mContext) != null && keyValueDB.getSecretAnswer(mContext) != null) {
                new AlertDialog.Builder(mContext)
                        .setTitle(getString(R.string.dialog_title_setup_secret))
                        .setMessage(getString(R.string.dialog_question_setup_secret))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Log delete notes
                                Log.d(LOG_TAG, "Delete secret");

                                // Delete credentials
                                try {
                                    keyValueDB.deleteSecretQuestion(mContext);
                                    keyValueDB.deleteSecretAnswer(mContext);

                                    // Notify user
                                    Toast.makeText(getApplicationContext(), getString(R.string.success_deleted) + ".", Toast.LENGTH_SHORT).show();

                                    // Log success
                                    Log.d(LOG_TAG, "Secret deleted");
                                } catch (Exception e) {
                                    // Something went wrong
                                    // Notify user
                                    Toast.makeText(getApplicationContext(), getString(R.string.error_cannot_delete) + ".", Toast.LENGTH_SHORT).show();

                                    // Log failure
                                    Log.d(LOG_TAG, e.getMessage());

                                    // Go to notes activity
                                    startNotesActivity();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Log credentials not deleted
                                Log.d(LOG_TAG, "Credentials not deleted");

                                // Stop activity
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        } catch (Exception e) {
            // Log failure
            Log.d(LOG_TAG, e.getMessage());

            // Stop activity
            finish();
        }

        // Set up the login form
        mSecretQuestionView = (AutoCompleteTextView) findViewById(R.id.secretQuestion);

        mSecretAnswerView = (EditText) findViewById(R.id.secretAnswer);
        mSecretAnswerView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptCreateSecret();
                    return true;
                }
                return false;
            }
        });

        mSecretButton = (Button)findViewById(R.id.secret_button);
        mSecretButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCreateSecret();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to set up the secret.
     * If there are form errors, the errors are presented
     * and no actual secret is made.
     */
    private void attemptCreateSecret() {

        // Reset errors
        mSecretQuestionView.setError(null);
        mSecretAnswerView.setError(null);

        // Store values at the time of the login attempt
        String question = mSecretQuestionView.getText().toString();
        String answer = mSecretAnswerView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        SecretValidator sv = new SecretValidator();

        // Check for a secret question
        if (!sv.isSecretValid(question)) {
            mSecretQuestionView.setError(getString(R.string.error_invalid_secret_question));
            focusView = mSecretQuestionView;
            cancel = true;
        }

        // Check for a valid answer
        if (!sv.isSecretValid(answer)) {
            mSecretAnswerView.setError(getString(R.string.error_invalid_secret_answer));
            focusView = mSecretAnswerView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt creating a secret and focus the first
            // form field with an error
            focusView.requestFocus();

            // Log error
            Log.d(LOG_TAG, "Form validation went wrong");
        } else {
            // Log success
            Log.d(LOG_TAG, "Form validation succeeded");

            // Save question and answer to shared preferences
            try {
                keyValueDB.setSecretQuestion(mContext, question);
                keyValueDB.setSecretAnswer(mContext, answer);

                // Notify user
                Toast.makeText(getApplicationContext(), getString(R.string.success_valid_question) + ".", Toast.LENGTH_SHORT).show();

                // Show a progress spinner, and kick off a background task to
                // perform the user registration attempt
                showProgress(true);

                // Proceed to notes activity
                startNotesActivity();

                // Log new activity
                Log.d(LOG_TAG, "Go to: " + NOTES_ACTIVITY);
            } catch (Exception e) {
                // Let the user know something went wrong
                Toast.makeText(getApplicationContext(), getString(R.string.error_cannot_save) + ". ", Toast.LENGTH_SHORT).show();

                // There was an exception; log exception
                Log.d(LOG_TAG, e.getMessage());
            }
        }
    }

    /**
     * Simple method for starting the notes activity
     */
    private void startNotesActivity() {
        // Log activity
        Log.d(LOG_TAG, "Proceed to " + NOTES_ACTIVITY);

        // Construct activity
        Intent intent = new Intent();
        intent.setClassName(mContext, PACKAGE_NAME + "." + NOTES_ACTIVITY);

        // Start activity
        startActivity(intent);

        // Close activity for security purposes
        finish();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

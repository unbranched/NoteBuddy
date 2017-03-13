package nl.yoerinijs.notebuddy.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import nl.yoerinijs.notebuddy.R;
import nl.yoerinijs.notebuddy.security.StringEncryptor;
import nl.yoerinijs.notebuddy.storage.KeyValueDB;
import nl.yoerinijs.notebuddy.validators.SecretValidator;

/**
 * A sreen for creating a secret that can be used when one is forgotten his password.
 */
public class ProvideSecretActivity extends AppCompatActivity {

    // Activity references
    private static final String PACKAGE_NAME = "nl.yoerinijs.notebuddy.activities";
    private static final String NOTES_ACTIVITY = "NotesActivity";
    private static final String LOG_TAG = "Setup Activity";

    // UI references
    private TextView secretQuestion;
    private EditText mSecretAnswerView;
    private Button mSecretButton;
    private View mProgressView;
    private View mSecretFormView;
    private Context mContext;
    private KeyValueDB keyValueDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_secret);

        // Set up
        keyValueDB = new KeyValueDB();
        mContext = this;

        // Set up the secret question
        secretQuestion = (TextView) findViewById(R.id.secretQuestion);
        try {
            // Get secret from key value store
            String question = keyValueDB.getSecretQuestion(mContext);

            // Display question
            secretQuestion.setText(question);

            // Log success
            Log.d(LOG_TAG, "Secret question retrieved from key value store");
        } catch (Exception e) {
            // Log failure
            Log.d(LOG_TAG, "Cannot get secret question");

            // Notify user
            Toast.makeText(getApplicationContext(), getString(R.string.error_retrieving_question) + ".", Toast.LENGTH_SHORT).show();
        }

        // Set up secret answer field
        mSecretAnswerView = (EditText) findViewById(R.id.secretAnswer);
        mSecretAnswerView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                // If valid, attempt to provide the secret answer
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptProvideSecret();
                    return true;
                }
                return false;
            }
        });

        // When clicked on the secret button, attempt to provide the secret answer as well
        mSecretButton = (Button)findViewById(R.id.secret_button);
        mSecretButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptProvideSecret();
            }
        });

        // Set up the form view and the progress view
        mSecretFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to set up the secret.
     * If there are form errors, the errors are presented
     * and no actual secret is made.
     */
    private void attemptProvideSecret() {

        try {
            // Reset errors
            mSecretAnswerView.setError(null);

            // Store value at the time of the attempt
            String answer = mSecretAnswerView.getText().toString();

            boolean cancel = false;
            View focusView = null;

            // Check for a valid answer for the secret
            SecretValidator sv = new SecretValidator();
            if (!sv.isSecretValid(answer)) {
                mSecretAnswerView.setError(getString(R.string.error_invalid_secret_answer));
                focusView = mSecretAnswerView;
                cancel = true;

                // Log failure
                Log.d(LOG_TAG, "Answer is invalid");
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

                // Encrypt answer to compare it with encrypted stored answer
                StringEncryptor se = new StringEncryptor();
                String encryptedAnswer = se.encrypt(answer.toLowerCase());

                if (encryptedAnswer.equals(keyValueDB.getSecretAnswer(mContext))) {
                    // Answer is correct
                    // Notify user to reset credentials and create a new secret
                    Toast.makeText(getApplicationContext(), getString(R.string.success_valid_answer) + ".", Toast.LENGTH_SHORT).show();

                    // Log success
                    Log.d(LOG_TAG, "Answer is correct");

                    // Start notes activity
                    startNewActivity(NOTES_ACTIVITY);
                } else {
                    // Notify the user
                    Toast.makeText(getApplicationContext(), getString(R.string.error_invalid_answer) + ".", Toast.LENGTH_SHORT).show();

                    // Log failure
                    Log.d(LOG_TAG, "Answer is incorrect");

                    // Finish this activity
                    finish();
                }
            }
        } catch (Exception e) {
            // Notify the user
            Toast.makeText(getApplicationContext(), getString(R.string.error_general) + ".", Toast.LENGTH_SHORT).show();

            // Log failure
            Log.d(LOG_TAG, e.getMessage());
        }
    }

    /**
     * Redirects the user to the notes activity.
     */
    private void startNewActivity(String activity) {
        // Log activity
        Log.d(LOG_TAG, "Proceed to " + activity);

        // Construct activity
        Intent intent = new Intent();
        intent.setClassName(mContext, PACKAGE_NAME + "." + activity);

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

            mSecretFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSecretFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSecretFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mSecretFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}


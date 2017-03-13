package nl.yoerinijs.notebuddy.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import nl.yoerinijs.notebuddy.R;
import nl.yoerinijs.notebuddy.storage.KeyValueDB;
import nl.yoerinijs.notebuddy.validators.PasswordValidator;
import nl.yoerinijs.notebuddy.validators.UsernameValidator;

/**
 * A setup screen that offers user registration
 */
public class SetupActivity extends AppCompatActivity {

    // Activity references
    private static final String PACKAGE_NAME = "nl.yoerinijs.notebuddy.activities";
    private static final String NOTES_ACTIVITY = "NotesActivity";
    private static final String LOG_TAG = "Setup Activity";

    // UI references
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private Button mRegisterButton;
    private View mProgressView;
    private View mLoginFormView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        // Context
        mContext = this;

        // Set up the form
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        mRegisterButton = (Button)findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in with the login form.
     * If there are form errors, the errors are presented
     * and no actual login attempt is made.
     */
    private void attemptRegistration() {
        // Reset errors
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password
        PasswordValidator pv = new PasswordValidator();
        if (!pv.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username
        UsernameValidator uv = new UsernameValidator();
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!uv.isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt registration and focus the first
            // form field with an error
            focusView.requestFocus();

            // Log error
            Log.d(LOG_TAG, "Form validation went wrong");
        } else {
            // Log success
            Log.d(LOG_TAG, "Form validation succeeded");

            // Save username and password to shared preferences
            KeyValueDB keyValueDB = new KeyValueDB();
            try {
                keyValueDB.setPassword(mContext, password);
                keyValueDB.setUsername(mContext, username);

                // Show a progress spinner, and kick off a background task to
                // perform the user registration attempt
                showProgress(true);

                // Proceed to notes activity
                Intent intent = new Intent();
                intent.setClassName(mContext, PACKAGE_NAME + "." + NOTES_ACTIVITY);
                startActivity(intent);
                finish();

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


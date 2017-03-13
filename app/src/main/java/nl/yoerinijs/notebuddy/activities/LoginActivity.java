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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import nl.yoerinijs.notebuddy.R;
import nl.yoerinijs.notebuddy.security.StringEncryptor;
import nl.yoerinijs.notebuddy.storage.KeyValueDB;

/**
 * A login screen that offers login via username and password
 */
public class LoginActivity extends AppCompatActivity {

    // Activity references
    private static final String PACKAGE_NAME = "nl.yoerinijs.notebuddy.activities";
    private static final String NOTES_ACTIVITY = "NotesActivity";
    private static final String SECRET_ACTIVITY = "ProvideSecretActivity";
    private static final String LOG_TAG = "Login Activity";

    // UI references
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Context mContext;
    private Button mSecretButton;

    // Key value storage
    private KeyValueDB keyValueDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the UI
        mPasswordView = (EditText) findViewById(R.id.password);
        mSecretButton = (Button) findViewById(R.id.secretQuestion);
        mContext = this;

        // Set up the key value database
        keyValueDB = new KeyValueDB();

        // Verify whether the secret question is set
        // If the secret is not set, hide the forgot password button
        try {
            if (keyValueDB.getSecretQuestion(mContext) == null || keyValueDB.getSecretAnswer(mContext) == null) {
                mSecretButton.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            // Log failure
            Log.d(LOG_TAG, e.getMessage());
        }

        // Set up the login form
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // Fill in secret answer when password is forgotten
        // Activity will not finish in order to let the user to go back
        mSecretButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start activity to provide secret
                Intent intent = new Intent();
                intent.setClassName(mContext, PACKAGE_NAME + "." + SECRET_ACTIVITY);
                startActivity(intent);
            }
        });

        // Sign in button
        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
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
    private void attemptLogin() {

        try {
            // Reset errors
            mPasswordView.setError(null);

            // Store values at the time of the login attempt
            String password = mPasswordView.getText().toString();

            boolean cancel = false;
            View focusView = null;

            // Check if password field is empty
            if (!TextUtils.isEmpty(password)) {
                mPasswordView.setError(getString(R.string.error_field_required));
                focusView = mPasswordView;
                cancel = true;
            }

            // Check if provided password is the same as stored one
            // Encrypt provided password
            StringEncryptor e = new StringEncryptor();
            String encryptedPassword = e.encrypt(password);

            // Retrieve stored encrypted password
            String storedPassword = keyValueDB.getPassword(mContext);

            // Check if passwords are the same
            if (encryptedPassword.equals(storedPassword)) {
                cancel = false;
            } else {
                mPasswordView.setError(getString(R.string.error_login_wrong_password));
                focusView = mPasswordView;
                cancel = true;
            }

            if (cancel) {
                // There was an error; don't attempt login and focus
                // password form field with an error
                focusView.requestFocus();

                // Clear password field
                mPasswordView.setText(null);

                // Log error
                Log.d(LOG_TAG, "Form validation went wrong");
            } else {
                // Let the user know he is logged in
                Toast.makeText(getApplicationContext(), getString(R.string.success_login_general)
                        + ". " + getString(R.string.greeting_general) + ", " + keyValueDB.getUsername(mContext)
                        + "!", Toast.LENGTH_SHORT).show();

                // Show a progress spinner
                showProgress(true);

                // Log success
                Log.d(LOG_TAG, "Proceed to: " + NOTES_ACTIVITY);

                // Proceed to notes activity
                Intent intent = new Intent();
                intent.setClassName(mContext, PACKAGE_NAME + "." + NOTES_ACTIVITY);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            // Let the user know he cannot login
            Toast.makeText(getApplicationContext(), getString(R.string.error_login_general) + ". " + getString(R.string.action_try_again) + ".", Toast.LENGTH_SHORT).show();

            // Log error
            Log.d(LOG_TAG, e.getMessage());
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


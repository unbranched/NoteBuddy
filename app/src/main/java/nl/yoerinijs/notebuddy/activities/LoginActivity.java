package nl.yoerinijs.notebuddy.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import nl.yoerinijs.notebuddy.security.LoginHashCreator;
import nl.yoerinijs.notebuddy.storage.KeyValueDB;

/**
 * A login screen that offers login via username and mPassword
 */
public class LoginActivity extends AppCompatActivity {

    // Activity references
    private static final String PACKAGE_NAME = "nl.yoerinijs.notebuddy.activities";
    private static final String NOTES_ACTIVITY = "NotesActivity";
    private static final String MAIN_ACTIVITY = "MainActivity";
    private static final String LOG_TAG = "Login Activity";

    // UI references
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Context mContext;
    private Button mClearPrefsButton;

    // Key value storage
    private KeyValueDB keyValueDB;

    // Password
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Context
        mContext = this;

        // Set key value store
        keyValueDB = new KeyValueDB();

        // Set up the UI
        mPasswordView = (EditText) findViewById(R.id.password);
        mClearPrefsButton = (Button) findViewById(R.id.clear_prefs_button);

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

        // Clear prefs button, only when dev mode is enabled
        if (!getIntent().getBooleanExtra("DEVMODE", false)) {
            mClearPrefsButton.setVisibility(View.GONE);
        }

        mClearPrefsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                keyValueDB.clearSharedPreference(mContext);

                // Proceed to main activity
                startActivity(MAIN_ACTIVITY, false);
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
            mPassword = mPasswordView.getText().toString();

            boolean cancel = false;
            View focusView = null;

            // Check if mPassword field is empty
            if (TextUtils.isEmpty(mPassword)) {
                mPasswordView.setError(getString(R.string.error_field_required));
                focusView = mPasswordView;
                cancel = true;
            }

            // Check if provided mPassword is correct
            // TODO: create a better way without storing the mPassword
            LoginHashCreator lhc = new LoginHashCreator();
            String currentHash = lhc.getLoginHash(mContext, mPassword);
            String verificationHash = keyValueDB.getVerificationPasswordHash(mContext);
            Log.d(LOG_TAG, currentHash);
            Log.d(LOG_TAG, verificationHash);
            boolean correctPassword = (currentHash.equals(verificationHash));

            // If hash is not correct, display a warning
            if (!correctPassword) {
                mPasswordView.setError(getString(R.string.error_login_wrong_password));
                focusView = mPasswordView;
                cancel = true;
            }

            if (cancel) {
                // There was an error; don't attempt login and focus
                // mPassword form field with an error
                focusView.requestFocus();

                // Clear mPassword field
                mPasswordView.setText(null);

                // Log error
                Log.d(LOG_TAG, "Form validation went wrong");
            } else if (correctPassword) {
                // Log success
                Log.d(LOG_TAG, "Form validation went smooth");

                // Let the user know he is logged in
                Toast.makeText(getApplicationContext(), getString(R.string.success_login_general)
                        + ". " + getString(R.string.greeting_general) + ", " + keyValueDB.getUsername(mContext)
                        + "!", Toast.LENGTH_SHORT).show();

                // Show a progress spinner
                showProgress(true);

                // Log success
                Log.d(LOG_TAG, "Proceed to: " + NOTES_ACTIVITY);

                // Proceed to notes activity
                startActivity(NOTES_ACTIVITY, true);
            }
        } catch (Exception e) {
            // Let the user know he cannot login
            Toast.makeText(getApplicationContext(), getString(R.string.error_login_general) + ". " + getString(R.string.action_try_again) + ".", Toast.LENGTH_SHORT).show();

            // Log error
            Log.d(LOG_TAG, e.getMessage());
        }
    }

    /**
     * Method to start an activity.
     * With the boolean providePassword it is possible
     * to determine whether to send the password to the next intent.
     * Current activity will finish by all means.
     * @param activityName
     * @param providePassword
     */
    private void startActivity(@NonNull String activityName, @NonNull boolean providePassword) {
        Intent intent = new Intent();
        if (providePassword) {
            intent.putExtra("PASSWORD", mPassword);
        }
        intent.setClassName(mContext, PACKAGE_NAME + "." + activityName);
        startActivity(intent);
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


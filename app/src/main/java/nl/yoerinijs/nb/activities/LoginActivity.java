package nl.yoerinijs.nb.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import nl.yoerinijs.nb.R;
import nl.yoerinijs.nb.files.misc.LocationCentral;
import nl.yoerinijs.nb.security.LoginHashCreator;
import nl.yoerinijs.nb.storage.KeyValueDB;

/**
 * A login screen that offers login via username and m_password
 */
public class LoginActivity extends AppCompatActivity {

    public static final String KEY_PASSWORD = "password";

    private static final String PACKAGE_NAME = LocationCentral.PACKAGE;

    private static final String NOTES_ACTIVITY = LocationCentral.NOTES;

    private static final String MAIN_ACTIVITY = LocationCentral.MAIN;

    private final Context m_context = this;

    private EditText m_passwordView;

    private View m_progressView;

    private View m_loginFormView;

    private String m_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        m_passwordView = (EditText) findViewById(R.id.password);
        Button m_clearPrefsButton = (Button) findViewById(R.id.clear_prefs_button);

        m_passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if(id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        if (!getIntent().getBooleanExtra(MainActivity.KEY_DEVMODE, false)) {
            m_clearPrefsButton.setVisibility(View.GONE);
        }

        m_clearPrefsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyValueDB.clearSharedPreference(m_context);
                startActivity(MAIN_ACTIVITY, false);
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        m_loginFormView = findViewById(R.id.login_form);
        m_progressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in with the login form.
     * If there are form errors, the errors are presented
     * and no actual login attempt is made.
     */
    private void attemptLogin() {
        try {
            m_passwordView.setError(null);
            m_password = m_passwordView.getText().toString();
            boolean cancel = false;
            View focusView = null;

            if(TextUtils.isEmpty(m_password)) {
                m_passwordView.setError(getString(R.string.error_field_required));
                focusView = m_passwordView;
                cancel = true;
            }

            String currentHash = LoginHashCreator.getLoginHash(m_context, m_password);
            String verificationHash = KeyValueDB.getVerificationPasswordHash(m_context);
            boolean correctPassword = (currentHash.equals(verificationHash));

            if(!correctPassword) {
                m_passwordView.setError(getString(R.string.error_login_wrong_password));
                focusView = m_passwordView;
                cancel = true;
            }

            if(cancel) {
                focusView.requestFocus();
                m_passwordView.setText(null);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.success_login_general)
                        + ". " + getString(R.string.greeting_general) + ", " + KeyValueDB.getUsername(m_context)
                        + "!", Toast.LENGTH_SHORT).show();
                showProgress(true);
                startActivity(NOTES_ACTIVITY, true);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_login_general) + ". " + getString(R.string.action_try_again) + ".", Toast.LENGTH_SHORT).show();
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
    private void startActivity(@NonNull String activityName, boolean providePassword) {
        Intent intent = new Intent();
        if(providePassword) {
            intent.putExtra(KEY_PASSWORD, m_password);
        }
        if(getIntent().getStringExtra(MainActivity.KEY_TEXT_TO_SEND) != null) {
            if(!getIntent().getStringExtra(MainActivity.KEY_TEXT_TO_SEND).isEmpty()) {
                intent.putExtra(MainActivity.KEY_TEXT_TO_SEND, getIntent().getStringExtra(MainActivity.KEY_TEXT_TO_SEND));
            }
        }
        intent.setClassName(m_context, PACKAGE_NAME + "." + activityName);
        startActivity(intent);
        finish();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            m_loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            m_loginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    m_loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            m_progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            m_progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    m_progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            m_progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            m_loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}


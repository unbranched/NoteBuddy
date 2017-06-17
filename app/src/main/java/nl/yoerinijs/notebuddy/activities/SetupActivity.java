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
import nl.yoerinijs.notebuddy.security.LoginHashCreator;
import nl.yoerinijs.notebuddy.security.SaltHandler;
import nl.yoerinijs.notebuddy.storage.KeyValueDB;
import nl.yoerinijs.notebuddy.validators.PasswordValidator;
import nl.yoerinijs.notebuddy.validators.UsernameValidator;

/**
 * A setup screen that offers user registration
 */
public class SetupActivity extends AppCompatActivity {

    private static final String PACKAGE_NAME = "nl.yoerinijs.notebuddy.activities";

    private static final String NOTES_ACTIVITY = "NotesActivity";

    private final Context m_context = this;

    private AutoCompleteTextView m_username;

    private EditText m_passwordView;

    private EditText m_passwordCheckView;

    private View m_progressView;

    private View m_loginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        m_username = (AutoCompleteTextView) findViewById(R.id.username);
        m_passwordView = (EditText) findViewById(R.id.password);
        m_passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if(id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });
        m_passwordCheckView = (EditText) findViewById(R.id.passwordCheck);

        Button m_registerButton = (Button) findViewById(R.id.register_button);
        m_registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
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
    private void attemptRegistration() {

        m_username.setError(null);
        m_passwordView.setError(null);

        String username = m_username.getText().toString();
        String password = m_passwordView.getText().toString();
        String passwordCheck = m_passwordCheckView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(!PasswordValidator.isPasswordValid(password)) {
            m_passwordView.setError(getString(R.string.error_invalid_password));
            focusView = m_passwordView;
            cancel = true;
        }
        if(!password.equals(passwordCheck)) {
            m_passwordCheckView.setError(getString(R.string.error_invalid_password_check));
            focusView = m_passwordCheckView;
            cancel = true;
        }
        if(TextUtils.isEmpty(username)) {
            m_username.setError(getString(R.string.error_field_required));
            focusView = m_username;
            cancel = true;
        } else if(!UsernameValidator.isUsernameValid(username)) {
            m_username.setError(getString(R.string.error_invalid_username));
            focusView = m_username;
            cancel = true;
        }
        if(cancel) {
            focusView.requestFocus();
        } else {
            try {
                KeyValueDB.setUsername(m_context, username);
                SaltHandler.setSalt(m_context);
                KeyValueDB.setVerificationPasswordHash(m_context, LoginHashCreator.getLoginHash(m_context, password));
                showProgress(true);
                Intent intent = new Intent();
                intent.setClassName(m_context, PACKAGE_NAME + "." + NOTES_ACTIVITY);
                intent.putExtra(LoginActivity.KEY_PASSWORD, password);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_cannot_save) + ". ", Toast.LENGTH_SHORT).show();
            }
        }
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


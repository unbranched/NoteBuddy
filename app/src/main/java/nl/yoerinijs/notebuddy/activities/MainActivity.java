package nl.yoerinijs.notebuddy.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

import nl.yoerinijs.notebuddy.R;
import nl.yoerinijs.notebuddy.helpers.ActivityChoser;

/**
 * The main activity that starts NoteBuddy.
 * This activity will forward the user either to the setup activity or to the login activity.
 */
public class MainActivity extends AppCompatActivity {

    private static final String PACKAGE_NAME = "nl.yoerinijs.notebuddy.activities";
    private static final String LOGIN_ACTIVITY = "LoginActivity";
    private static final String NOTES_ACTIVITY = "NotesActivity";
    private static final String LOG_TAG = "Main Activity";

    private boolean devMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable or disable dev mode
        // Dev mode can be used to test the application more convenient
        devMode = true;

        // Log that the program has started
        String devModeEnabled = (devMode) ? "is enabled" : "is disabled";
        Log.d(LOG_TAG, "Notebuddy has started! Dev mode " + devModeEnabled);

        // Trying to start the setup or the login activity
        try {
            // Log action
            Log.d(LOG_TAG, "Trying to determine whether to run setup or login activity");

            // Select and start activity
            setActivity();
        } catch (Exception e) {
            // If something goes wrong, notify the user
            String errorMessage = getString(R.string.error_general);
            Toast.makeText(getApplicationContext(), errorMessage + ".", Toast.LENGTH_SHORT).show();

            // Log failure as well
            Log.d(LOG_TAG, errorMessage + ". Details: " + e.getMessage());
        }
    }

    /**
     * Determine whether the user must run setup or must login
     */
    private void setActivity() throws NoSuchAlgorithmException {
        Intent intent = new Intent();

        // Get activity
        ActivityChoser a = new ActivityChoser();
        String activity = PACKAGE_NAME + "." + a.determineActivity(this);

        // Add activity name to intent
        intent.setClassName(this, activity);
        intent.putExtra("DEVMODE", devMode);

        String textToSend = getPossibleTextByOtherApps();
        if (textToSend != null && !textToSend.isEmpty()) {
            if (activity.contains(NOTES_ACTIVITY) || activity.contains(LOGIN_ACTIVITY)) {
                Log.d(LOG_TAG, "Send incoming text to next activity...");
                intent.putExtra("TEXTTOSEND", textToSend);
            }
        }

        // Start activity
        startActivity(intent);
        finish();

        // Log whether the user must follow the setup or must login
        Log.d(LOG_TAG, "Proceed to: " + activity);
    }

    /**
     * Check if another app is sending plain text. Returns full string with text when true, otherwise empty string.
     * @return
     */
    @NonNull
    private String getPossibleTextByOtherApps() {
        Intent intent = getIntent();
        if (Intent.ACTION_SEND.equals(intent.getAction()) && "text/plain".equals(intent.getType())) {
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (sharedText != null) {
                Log.d(LOG_TAG, "Incoming text: " + sharedText);
                return sharedText;
            }
        }
        Log.d(LOG_TAG, "No incoming text");
        return "";
    }
}

package nl.yoerinijs.notebuddy.activities;

import android.content.Intent;
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
    private static final String LOG_TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Log that the program has started
        Log.d(LOG_TAG, "Notebudy has started!");

        // Trying to start the setup or the login activity
        try {
            // Log action
            Log.d(LOG_TAG, "Trying to determine whether to run setup or login activity");

            // Select and start activity
            setActivity();
        } catch (Exception e) {
            // If something goes wrong, notify the user
            String errorMessage = getString(R.string.error_starting_app);
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

        // Start activity
        startActivity(intent);
        finish();

        // Log whether the user must follow the setup or must login
        Log.d(LOG_TAG, "Proceed to: " + activity);
    }
}

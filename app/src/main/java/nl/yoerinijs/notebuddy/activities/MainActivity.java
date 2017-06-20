package nl.yoerinijs.notebuddy.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import nl.yoerinijs.notebuddy.R;
import nl.yoerinijs.notebuddy.files.misc.LocationCentral;
import nl.yoerinijs.notebuddy.helpers.ActivityChoser;

/**
 * The main activity that starts NoteBuddy.
 * This activity will forward the user either to the setup activity or to the login activity.
 */
public class MainActivity extends AppCompatActivity {

    private static final String PACKAGE_NAME = LocationCentral.PACKAGE;

    private static final String LOGIN_ACTIVITY = LocationCentral.LOGIN;

    private static final String NOTES_ACTIVITY = LocationCentral.NOTES;

    private static final String LOG_TAG = "Main Activity";

    public static final String KEY_DEVMODE = "devmode";

    public static final String KEY_TEXT_TO_SEND = "texttosend";

    private boolean m_dev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable or disable dev mode
        // Dev mode can be used to test the application more convenient
        m_dev = false;
        Log.d(LOG_TAG, "Notebuddy has started! Dev mode " + (m_dev ? "is enabled" : "is disabled"));

        try {
            Intent intent = new Intent();
            intent.putExtra(KEY_DEVMODE, m_dev);
            String activity = PACKAGE_NAME + "." + ActivityChoser.determineActivity(this);
            if(!getPossibleTextByOtherApps().isEmpty()) {
                if(activity.contains(NOTES_ACTIVITY) || activity.contains(LOGIN_ACTIVITY)) {
                    intent.putExtra(KEY_TEXT_TO_SEND, getPossibleTextByOtherApps());
                }
            }
            intent.setClassName(this, activity);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_general) + ".", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Check if another app is sending plain text. Returns full string with text when true, otherwise empty string.
     * @return
     */
    @NonNull
    private String getPossibleTextByOtherApps() {
        String textByOtherApps = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        return null == textByOtherApps ? "" : textByOtherApps;
    }
}

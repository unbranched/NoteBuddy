package nl.yoerinijs.notebuddy.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nl.yoerinijs.notebuddy.R;
import nl.yoerinijs.notebuddy.credits.CreditsBuilder;

/**
 * A simple activity to display credits
 */
public class CreditsActivity extends AppCompatActivity {

    private static final String PACKAGE_NAME = "nl.yoerinijs.notebuddy.activities";

    private static final String NOTES_ACTIVITY = "NotesActivity";

    private TextView m_textView;

    private Button m_button;

    private Context m_context = this;

    private String m_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        m_password = getIntent().getStringExtra(LoginActivity.KEY_PASSWORD);
        m_textView = (TextView) findViewById(R.id.creditsView);
        m_textView.setText(CreditsBuilder.getCredits(m_context));

        m_button = (Button) findViewById(R.id.backButton);
        m_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(LoginActivity.KEY_PASSWORD, m_password);
                intent.setClassName(m_context, PACKAGE_NAME + "." + NOTES_ACTIVITY);
                startActivity(intent);
                finish();
            }
        });
    }
}

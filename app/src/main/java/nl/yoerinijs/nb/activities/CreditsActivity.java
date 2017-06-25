package nl.yoerinijs.nb.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nl.yoerinijs.nb.R;
import nl.yoerinijs.nb.credits.CreditsBuilder;
import nl.yoerinijs.nb.files.misc.LocationCentral;

/**
 * A simple activity to display credits
 */
public class CreditsActivity extends AppCompatActivity {

    private static final String PACKAGE_NAME = LocationCentral.PACKAGE;

    private static final String NOTES_ACTIVITY = LocationCentral.NOTES;

    private Context m_context = this;

    private String m_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        m_password = getIntent().getStringExtra(LoginActivity.KEY_PASSWORD);
        TextView m_textView = (TextView) findViewById(R.id.creditsView);
        m_textView.setText(CreditsBuilder.getCredits(m_context));

        Button m_button = (Button) findViewById(R.id.backButton);
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

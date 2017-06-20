package nl.yoerinijs.notebuddy.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import nl.yoerinijs.notebuddy.R;

public class ColorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton imageButtonPink = (ImageButton) findViewById(R.id.colorButtonPink);
        imageButtonPink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffd9ff04")));
            }
        });


        ImageButton imageButtonGreen = (ImageButton) findViewById(R.id.colorButtonGreen);
        ImageButton imageButtonLightBlue = (ImageButton) findViewById(R.id.colorButtonLightBlue);
    }

}

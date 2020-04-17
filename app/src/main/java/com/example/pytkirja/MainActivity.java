package com.example.pytkirja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView text;
    Button buttonAsssociation, buttonMeeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.textWelcome);
        text.setText("Tervetuloa sovellukseen!");

        buttonAsssociation = (Button) findViewById(R.id.button_association);
        buttonAsssociation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openAssociationActivity();
            }
        });

        buttonMeeting = (Button) findViewById(R.id.button_meeting);
        buttonMeeting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openMeetingActivity();
            }
        });


    }

    public void openAssociationActivity() {
        Intent intent = new Intent(this, AssociationActivity.class);
        startActivityForResult(intent, 123);
    }

    public void openMeetingActivity() {
        Intent intent = new Intent(this, MeetingActivity.class);
        startActivity(intent);
    }

    //GETTING VALUE FROM THE BACK PRESSED BUTTON
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent name) {
        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                String value = name.getStringExtra("name");
                text.setText("Käytä sovellusta viisaasti " + value + "!");
            }
        }

    }
}

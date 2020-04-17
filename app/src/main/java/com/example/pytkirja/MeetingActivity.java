package com.example.pytkirja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.layout.simple_spinner_item;

public class MeetingActivity extends AppCompatActivity {

    Button buttonCreate, buttonStart, buttonFill;
    EditText inputDate, inputTime, inputType, inputLocation;
    int mtChosenInt;
    ArrayAdapter<Meeting> aa2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        //Setting the spinner to get the meeting to start or fill the agenda
        //On selected will return current meeting which has always 2 documents
        final Spinner spinStart = findViewById(R.id.spinner_start);
        aa2 = new ArrayAdapter<Meeting>(this, simple_spinner_item, new ArrayList<Meeting>());
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinStart.setAdapter(aa2);


        //GETTING THE RIGHT POSITION FOR THE MEETING
        spinStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mtChosenInt = parent.getSelectedItemPosition();
                Toast.makeText(parent.getContext(), "Valinta: " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        final int choiceSpin = spinStart.getSelectedItemPosition();
        inputDate = (EditText) findViewById(R.id.editText_date);
        inputTime = (EditText) findViewById(R.id.editText_time);
        inputType = (EditText) findViewById(R.id.editText_type);
        inputLocation = (EditText) findViewById(R.id.editText_location);

        //HERE WE CREATE FIRST THE MEETING AND THEN CORRESPONDING DOCUMENTS
        //AND ADD THEM TO THE RIGHT MEETING
        buttonCreate = (Button) findViewById(R.id.button_createMeeting);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            //Here is done a lot: creating a meeting, agenda and minutes at the same time
            @Override
            public void onClick(View v) {
                String d = inputDate.getText().toString();
                String ti = inputTime.getText().toString();
                String ty = inputType.getText().toString();
                String l = inputLocation.getText().toString();
                Meeting mt = new Meeting(ty, d, ti, l);
                Agenda agenda = new Agenda();
                Minutes minutes = new Minutes();

                //Setting agenda info
                agenda.setType(ty);
                agenda.setDate(d);
                agenda.setTime(ti);
                agenda.setLocation(l);

                //Setting minutes info
                minutes.setType(ty);
                minutes.setDate(d);
                minutes.setTime(ti);
                minutes.setLocation(l);

                mt.agendas.add(agenda);
                mt.minutes.add(minutes);

                Association.getInstance().meetings.add(mt);
                aa2.add(mt);
                aa2.notifyDataSetChanged(); //THIS WAS MY PROBLEM INITIALLY, I DID NOT NOTIFY ABOYT CHANGES
                //THE SPINNER DISPLAYED CHANGED INFO BUT DID NOT ADD THE ACTUAL ITEMS THERE

            }
        });

        //OPENS PLACE TO FILL AGENDA WHICH WAS CREATED ACCORDING THE MEETING
        //CREATES ALSO AT THE SAME TIME THE AGENDA DOCUMENTATION;
        buttonFill = (Button) findViewById(R.id.button_Agenda);
        buttonFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Meeting meetingChosen = Association.getInstance().meetings.get(mtChosenInt);
                openFillActivity(meetingChosen.getDate());
            }
        });

        //OPENS PLACE TO GO THROUGH THE MINUTES
        buttonStart = (Button) findViewById(R.id.button_startMeet);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Meeting meetingChosen = Association.getInstance().meetings.get(mtChosenInt);
                openMeetingMinutes(meetingChosen.getDate());
            }
        });



    }



    //HERE I SEND DATE INFO AS PART OF THE ACTIVITY OPENER, TO GET
    //THE RIGHT AGENDA OPEN IN THE NEXT ACTIVITY
    public void openFillActivity(String s) {
        Intent intent = new Intent(this, FillAgenda.class);
        intent.putExtra("date", s);
        startActivity(intent);
    }

    //HERE I SEND DATE INFO AS PART OF THE ACTIVITY OPENER, TO GET
    //THE RIGHT AGENDA OPEN IN THE NEXT ACTIVITY
    public void openMeetingMinutes(String s) {
        Intent intent = new Intent(this, MinutesMeeting.class);
        intent.putExtra("date", s);
        startActivity(intent);
    }



}

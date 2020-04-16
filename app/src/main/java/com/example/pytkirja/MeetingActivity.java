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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        //Setting the spinner to get the meeting to start or fill the agenda
        //On selected will return current meeting which has always 2 documents
        final Spinner spinStart = findViewById(R.id.spinner_start);
        ArrayAdapter<Meeting> aa2 = new ArrayAdapter<Meeting>(this, simple_spinner_item, Association.getInstance().meetings);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinStart.setAdapter(aa2);


        spinStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Meeting meetingChosen = Association.getInstance().meetings.get(position);
                mtChosenInt = parent.getSelectedItemPosition();
                System.out.print("***\n" + mtChosenInt + " " + (mtChosenInt + 1) + "\n*****" );
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
                Association.getInstance().meetings.add(mt);
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

            }
        });

        //OPENS PLACE TO FILL AGENDA WHICH WAS CREATED ACCORDING THE MEETING
        //CREATES ALSO AT THE SAME TIME THE AGENDA DOCUMENTATION;
        buttonFill = (Button) findViewById(R.id.button_Agenda);
        buttonFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO remove check prints
                System.out.println("*****\n" + "*****\n");
                Meeting meetingChosen = Association.getInstance().meetings.get(mtChosenInt);

                System.out.println(meetingChosen.toString());
                openFillActivity(meetingChosen.getDate());
            }
        });

        //OPENS PLACE TO GO THROUGH THE AGENDA
        buttonStart = (Button) findViewById(R.id.button_startMeet);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Meeting meetingChosen = Association.getInstance().meetings.get(mtChosenInt);
                System.out.println(meetingChosen.toString() + "Johannes");
                openMeetingMinutes(meetingChosen.getDate());
            }
        });



    }



    public void openFillActivity(String s) {
        Intent intent = new Intent(this, FillAgenda.class);
        intent.putExtra("date", s);
        startActivity(intent);
    }

    public void openMeetingMinutes(String s) {
        Intent intent = new Intent(this, MinutesMeeting.class);
        intent.putExtra("date", s);
        startActivity(intent);
    }



}

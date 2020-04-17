package com.example.pytkirja;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.R.layout.simple_spinner_item;

public class MinutesMeeting extends AppCompatActivity {

    Minutes minutes = null;
    TextView intro, sectionsNumber, proposal, title;
    EditText decision;
    Spinner sectionsSpinner;
    Button buttonSaveDecision, buttonCreatePDF;
    int choiceSection;
    ArrayAdapter<Section> aa3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minutes_meeting);
        intro = (TextView) findViewById(R.id.textView_minutesIntro);
        proposal = (TextView) findViewById(R.id.textView_minutesProposal);
        sectionsNumber = (TextView) findViewById(R.id.textView_section2);
        title = (TextView) findViewById(R.id.textView_minutesTitle);
        decision = (EditText) findViewById(R.id.editText_decision);

        //With this we save the information to ArrayList
        buttonSaveDecision = (Button) findViewById(R.id.button_saveDecision);

        //Here we will get the right minutes corresponding the right meeting
        String date = getIntent().getStringExtra("date");
        int listLength = Association.getInstance().meetings.size();
        for (int i = 0; i < listLength; i++) {
            if (Association.getInstance().meetings.get(i).getDate().equalsIgnoreCase(date)) {
                minutes = Association.getInstance().meetings.get(i).minutes.get(0);
                sectionsSpinner = (Spinner) findViewById(R.id.spinner_chooseSection2);
                aa3 = new ArrayAdapter<Section>(this, simple_spinner_item, minutes.sections);
                aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sectionsSpinner.setAdapter(aa3);
                break;
            }
        }

        buttonSaveDecision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minutes.sections.get(choiceSection).setDecision(decision.getText().toString());
                aa3.notifyDataSetChanged();

                //Left out as it did not work with below, no IF needed
                /*
                if(minutes.sections != null || minutes.sections.size() == 0){
                    minutes.sections = new ArrayList<Section>();
                    Section section = new Section();
                    section.setDecision(decision.getText().toString());
                    minutes.sections.add(section);
                    aa3.add(section);
                    aa3.notifyDataSetChanged();
                } else {
                    minutes.sections.get(choiceSection).setDecision(decision.getText().toString());
                    aa3.notifyDataSetChanged();
                }*/
            }
        });

        //With this we create the PDF of the Agenda
        buttonCreatePDF = (Button) findViewById(R.id.button_createPDF2);
        buttonCreatePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullMinutes = minutes.createMinutesText();
                createPdf(fullMinutes);

            }
        });

        //I did it beofe here, but some reason doing at the same "level" with finding the right minutes
        //did not populate the spinner
//        sectionsSpinner = (Spinner) findViewById(R.id.spinner_chooseSection2);
//        aa3 = new ArrayAdapter<Section>(this, simple_spinner_item, minutes.sections);
//        aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sectionsSpinner.setAdapter(aa3);

        //HERE WE CHOOSE THE RIGHT SECTION TO FILL AND TO GET THE TITLE AND PROPOSAL
        sectionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choiceSection = position;
                Section sectionChosen = minutes.sections.get(position);
                intro.setText(sectionChosen.getIntroduction());
                proposal.setText(sectionChosen.getProposal());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing

            }
        });


    }

    //HERE WE CREATE PDF DOCUMENT
    private void createPdf(String fullMinutes) {

        //FIRST CREATING AND CHECKING
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/pöytäkirja/";
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        String fileName = minutes.getDocType() + "_" + minutes.getDate();

        //THEN START THE OUTPUT PROCESS,
        try {
            final File file = new File(dir, fileName + ".pdf");
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            if (!file.exists())
                file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);


            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new
                    PdfDocument.PageInfo.Builder(300, 600, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            TextPaint paint = new TextPaint();
            paint.setTextSize(10);
            paint.setColor(Color.BLACK);
            int width = 250;
            StaticLayout.Builder builder = StaticLayout.Builder.obtain(fullMinutes, 0, fullMinutes.length(), paint, width);
            StaticLayout staticLayout = builder.build();
            staticLayout.draw(canvas);

            //canvas.drawText(fullMinutes, 10, 50, paint);

            document.finishPage(page);
            document.writeTo(fOut);
            document.close();

            Toast.makeText(this, "PDF valmis", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            Log.i("error", e.getLocalizedMessage());
            Toast.makeText(this, "Jokin meni pieleen: " + e.toString(), Toast.LENGTH_LONG).show();
        }

    }


}

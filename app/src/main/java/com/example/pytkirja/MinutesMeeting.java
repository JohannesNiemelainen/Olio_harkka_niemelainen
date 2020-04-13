package com.example.pytkirja;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
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

import static android.R.layout.simple_spinner_item;

public class MinutesMeeting extends AppCompatActivity {

    Minutes minutes = null;
    TextView intro, sectionsNumber, proposal, title;
    EditText decision;
    Spinner sectionsSpinner;
    Button buttonSaveDecision, buttonCreatePDF;
    int choiceSection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Here we will get the right minutes corresponding the right meeting
        String date = getIntent().getStringExtra("date");
        int listLength = Association.getInstance().meetings.size();
        for (int i = 0; i < listLength; i++) {
            if (Association.getInstance().meetings.get(i).getDate().contains(date)) {
                minutes = Association.getInstance().meetings.get(i).minutes.get(0);
            };


            setContentView(R.layout.activity_minutes_meeting);

            intro = (TextView) findViewById(R.id.textView_minutesIntro);
            proposal = (TextView) findViewById(R.id.textView_minutesProposal);
            sectionsNumber = (TextView) findViewById(R.id.textView_section2);
            title = (TextView) findViewById(R.id.textView_minutesTitle);
            decision = (EditText) findViewById(R.id.editText_decision);

            //With this we save the information to ArrayList
            buttonSaveDecision = (Button) findViewById(R.id.button_saveDecision);
            buttonSaveDecision.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    minutes.sections.get(choiceSection).setDecision(decision.getText().toString());
                }
            });

            //With this we create the PDF of the Agenda
            buttonCreatePDF = (Button) findViewById(R.id.button_createPDF2);
            buttonCreatePDF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String fullMinutes= minutes.createMinutesText();
                    createPdf(fullMinutes);

                }
            });

            sectionsSpinner = (Spinner) findViewById(R.id.spinner_chooseSection2);
            ArrayAdapter<Section> aa3 = new ArrayAdapter<Section>(this, simple_spinner_item, minutes.sections);
            aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sectionsSpinner.setAdapter(aa3);

            sectionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    choiceSection = sectionsSpinner.getSelectedItemPosition();
                    Section sectionChosen = (Section) sectionsSpinner.getSelectedItem();
                    intro.setText(sectionChosen.getIntroduction());
                    proposal.setText(sectionChosen.getProposal());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //Do nothing

                }
            });


        }
    }


    private void createPdf(String fullMinutes) {

        // create a new document
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setTextSize(10);
        paint.setColor(Color.BLACK);
        canvas.drawText(fullMinutes, 80, 50, paint);
        document.finishPage(page);

        String fileName = minutes.getDocType() + " " + minutes.getDate();

        String filePath = Environment.getExternalStorageDirectory().getPath() + fileName + ".pdf";

        File file = new File(filePath);

        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF valmis", Toast.LENGTH_LONG).show();
            System.out.println("Onnistui");
        } catch (IOException e) {
            Log.e("main", "error " + e.toString());
            Toast.makeText(this, "Jokin meni pieleen: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        document.close();

    }


}

package com.example.pytkirja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.R.layout.simple_spinner_item;

public class FillAgenda extends AppCompatActivity {

    Button buttonSetSections, buttonSaveInfo, buttonPDF;
    SeekBar setSections;
    Context context2;
    Agenda agenda = null;
    TextView sections;
    EditText sectionTitle, sectionProposal;
    Spinner chooseSection;
    int choiceSection;

    private static final int STORAGE_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sections = (TextView) findViewById(R.id.textView_section);
        sectionTitle = (EditText) findViewById(R.id.editText_sectionTitle);
        sectionProposal = (EditText) findViewById(R.id.editText_proposal);



        //Here we will get the right agenda corresponding the right meeting
        String date = getIntent().getStringExtra("date");
        System.out.println(date);
        int listLength = Association.getInstance().meetings.size();
        for (int i = 0; i < listLength; i++) {
            if (Association.getInstance().meetings.get(i).getDate().contains(date)) {
                agenda = Association.getInstance().meetings.get(i).agendas.get(0);
                System.out.println(agenda);
            }
        }

            agenda = Association.getInstance().meetings.get(0).agendas.get(0);

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_fill_agenda);
            context2 = FillAgenda.this;

            //With this we save the amount of sections and add them to ArrayList sections
            buttonSetSections = (Button) findViewById(R.id.button_setSections);
            buttonSetSections.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agenda.setSectionCount(setSections.getProgress());

                    for (int i = 0; i < agenda.getSectionCount(); i++) {
                        Section section = new Section();
                        section.setRunningNumber(i + 1);
                        agenda.sections.add(section);
                    }
                }
            });

            //With this we save the information to ArrayList
            buttonSaveInfo = (Button) findViewById(R.id.button_saveSection);
            buttonSaveInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agenda.sections.get(choiceSection).setIntroduction(sectionTitle.getText().toString());
                    agenda.sections.get(choiceSection).setProposal(sectionProposal.getText().toString());
                }
            });

            //With this we create the PDF of the Agenda
            buttonPDF = (Button) findViewById(R.id.button_createPDF);
            buttonPDF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Check if we have a permission to write the PDF
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {
                        //permission was not granted, request it
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, STORAGE_CODE);
                    } else {
                        //Permission was granted
                        String fullAgenda = agenda.createAgendaText();
                        createPdf(fullAgenda);

                    }
                }
            });

            //With this seekbar we will get the number of sections
            setSections = (SeekBar) findViewById(R.id.seekBar_sectionCount);
            setSections.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Toast.makeText(getApplicationContext(),"Pykäliä: "+progress, Toast.LENGTH_SHORT).show();
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    //TODO Add Toast about progressed change
                }
            });

            chooseSection = (Spinner) findViewById(R.id.spinner_chooseSection);
            ArrayAdapter<Section> aa3 = new ArrayAdapter<Section>(this, simple_spinner_item, agenda.sections);
            aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            chooseSection.setAdapter(aa3);

            chooseSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    choiceSection = position;
                    System.out.print("****\n" + position + "****");
                    System.out.print("****\n" + choiceSection + "****");
                    sectionTitle.setText(("Lisää otsikko pykälälle: " + (choiceSection + 1)));
                    sectionProposal.setText(("Lisää päätösehdotus pykälälle: " + (choiceSection + 1)));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }




    private void createPdf(String fullAgenda) {

        // create a new document
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setTextSize(10);
        paint.setColor(Color.BLACK);
        canvas.drawText(fullAgenda, 80, 50, paint);
        document.finishPage(page);

        String fileName = agenda.getDocType() + " " + agenda.getDate();

        String filePath = Environment.getExternalStorageDirectory().getPath() + "/" + fileName + ".pdf";

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

    //handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission was granted from popup, call savepdf method
                    String fullAgenda = agenda.createAgendaText();
                    createPdf(fullAgenda);
                }
                else {
                    //permission was denied from popup, show error message
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



}



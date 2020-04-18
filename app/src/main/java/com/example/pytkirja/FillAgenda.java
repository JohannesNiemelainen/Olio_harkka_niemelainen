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
import android.text.StaticLayout;
import android.text.TextPaint;
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
import java.util.ArrayList;

import static android.R.layout.simple_spinner_item;

public class FillAgenda extends AppCompatActivity {

    Button buttonSetSections, buttonSaveInfo, buttonPDF;
    SeekBar setSections;
    Context context2;
    Agenda agenda = null;
    Minutes minutes = null;
    Meeting meeting = null;
    TextView sections;
    EditText sectionTitle, sectionProposal;
    Spinner chooseSection;
    int choiceSection;
    ArrayAdapter<Section> aa3;
    String date;

    private static final int STORAGE_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_agenda);
        context2 = FillAgenda.this;
        sections = (TextView) findViewById(R.id.textView_section);
        sectionTitle = (EditText) findViewById(R.id.editText_sectionTitle);
        sectionProposal = (EditText) findViewById(R.id.editText_proposal);
        //Here we will get the right agenda corresponding the right meeting
        date = getIntent().getStringExtra("date");
        System.out.println(date);
        int listLength = Association.getInstance().meetings.size();
        for (int i = 0; i < listLength; i++) {
            if (Association.getInstance().meetings.get(i).getDate().equals(date)) {
                meeting = Association.getInstance().meetings.get(i);
                agenda = Association.getInstance().meetings.get(i).agendas.get(0);
                minutes = Association.getInstance().meetings.get(i).minutes.get(0);
                System.out.println(agenda);
                break;
            }
        }

        agenda = Association.getInstance().meetings.get(0).agendas.get(0);
        //With this we save the amount of sections and add them to ArrayList sections
        buttonSetSections = (Button) findViewById(R.id.button_setSections);
        buttonSetSections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agenda.setSectionCount(setSections.getProgress());
                agenda.sections.clear();
                for (int i = 0; i < agenda.getSectionCount(); i++) {
                    Section section = new Section();
                    section.setRunningNumber(i + 1);
                    agenda.sections.add(section);
                    minutes.sections.add(section);
                }
                aa3.addAll(agenda.sections);
                aa3.notifyDataSetChanged();
                updateAgenda();
            }
        });

        //With this we save the information to ArrayList
        buttonSaveInfo = (Button) findViewById(R.id.button_saveSection);
        buttonSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agenda.sections != null && agenda.sections.size() > 0) {
                    agenda.sections.get(choiceSection).setIntroduction(String.valueOf(sectionTitle.getText()));
                    agenda.sections.get(choiceSection).setProposal(String.valueOf(sectionProposal.getText()));
                    minutes.sections.get(choiceSection).setIntroduction(String.valueOf(sectionTitle.getText()));
                    minutes.sections.get(choiceSection).setProposal(String.valueOf(sectionProposal.getText()));
                    updateAgenda();
                } else {
                    Toast.makeText(getApplicationContext(), "Pykäliä: " + "Empty Sections", Toast.LENGTH_SHORT).show();
                }
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
                    createNewPdf(fullAgenda);

                }
            }
        });

        //With this seekbar we will get the number of sections
        setSections = (SeekBar) findViewById(R.id.seekBar_sectionCount);
        setSections.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(getApplicationContext(), "Pykäliä: " + progress, Toast.LENGTH_SHORT).show();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                //TODO Add Toast about progressed change
            }
        });

        chooseSection = (Spinner) findViewById(R.id.spinner_chooseSection);
        aa3 = new ArrayAdapter<Section>(this, simple_spinner_item, new ArrayList<Section>());
        aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseSection.setAdapter(aa3);

        //HERE WE CHOOSE THE RIGHT SECTION TO FILL
        chooseSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choiceSection = position;
                sectionTitle.setText(("Lisää otsikko pykälälle: " + (choiceSection + 1)));
                sectionProposal.setText(("Lisää päätösehdotus pykälälle: " + (choiceSection + 1)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //HERE WE CREATE PDF DOCUMENT
    private void createNewPdf(String fullAgenda) {

        //FIRST CHECK FOR THE DIRECTORY
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/pöytäkirja/";
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        String fileName = agenda.getDocType() + "_" + agenda.getDate();

        //THEN START THE OUTPUT PROCESS
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

            //HERE COMES HARD PART: Without StaticLayout.Builder there would be only one line
            //SO to get a long text into a PDF you need TextPaint and StaticLayout.Builder
            TextPaint paint = new TextPaint();
            paint.setTextSize(10);
            paint.setColor(Color.BLACK);
            int width = 250;
            StaticLayout.Builder builder = StaticLayout.Builder.obtain(fullAgenda, 0, fullAgenda.length(), paint, width);
            StaticLayout staticLayout = builder.build();
            staticLayout.draw(canvas);

            //canvas.drawText(fullMinutes, 10, 50, paint);

            document.finishPage(page);
            document.writeTo(fOut);
            document.close();
            Toast.makeText(this, "PDF valmis", Toast.LENGTH_LONG).show();
            System.out.println("Onnistui");

        } catch (IOException e) {
            Log.i("error", e.getLocalizedMessage());
            Toast.makeText(this, "Jokin meni pieleen: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    //THIS METHOD IS USED TO UPDATE THE AGENDA FOR CHANGES
    //SO THAT THE SPINNER WORKS CORRECTLY
    private void updateAgenda(){
        int listLength = Association.getInstance().meetings.size();
        for (int i = 0; i < listLength; i++) {
            if (Association.getInstance().meetings.get(i).getDate().equals(date)) {
                Association.getInstance().meetings.remove(i);
                meeting.agendas.remove(0);
                meeting.agendas.add(0, agenda);
                Association.getInstance().meetings.add(i, meeting);
                System.out.println(agenda);
                break;
            }
        }
    }

    //HANDLE PERMISSION FOR CREATING THE PDF
    //I AM NOT REALLY SURE IF I REALLY USE IT, AS
    //THE PERMISSION IS GRANTED IN MANIFEST (LegacyExternalStorage=true)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted from popup, call savepdf method
                    String fullAgenda = agenda.createAgendaText();
                    createNewPdf(fullAgenda);
                } else {
                    //permission was denied from popup, show error message
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}



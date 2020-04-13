package com.example.pytkirja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.layout.simple_spinner_item;

public class AssociationActivity extends AppCompatActivity {

    SeekBar seekBar;
    Context context;
    Button buttonMemberCount, buttonMemberInfo;
    EditText inputName, inputAddress, inputMemberName, inputMemberStatus;
    TextView associationInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_association);

        final Association assoc = Association.getInstance();
        context = AssociationActivity.this;
        inputName = (EditText) findViewById(R.id.editText_name);
        inputAddress = (EditText)findViewById(R.id.editText_address);
        inputMemberName = (EditText) findViewById(R.id.editText_boardName);
        inputMemberStatus = (EditText) findViewById(R.id.editText_boardStatus);
        associationInfo = (TextView) findViewById(R.id.textView_info);

        //Setting the ArrayAdapter data on the Spinner, Lists all the board members
        Spinner spin = (Spinner) findViewById(R.id.spinnerMembers);
        ArrayAdapter<BoardMember> aa = new ArrayAdapter<BoardMember>(this, simple_spinner_item, assoc.boardMembers);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        //Here we will create association with relevant info
        buttonMemberCount = (Button) findViewById(R.id.button_association);
        buttonMemberCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assoc.setName(inputName.getText().toString());
                assoc.setAddress(inputAddress.getText().toString());
                assoc.setBoardSize(seekBar.getProgress());
                associationInfo.setText(assoc.setInfo());
            }


        }
    );

        //Here we add information for the association
        buttonMemberInfo = (Button) findViewById(R.id.button_memberInfo);
        buttonMemberInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = inputMemberName.getText().toString();
                String s = inputMemberStatus.getText().toString();
                BoardMember bm = new BoardMember(n, s);
                assoc.boardMembers.add(bm);
                Toast.makeText(context, "Lisätty.", Toast.LENGTH_SHORT).show();
            }
        });

        seekBar = (SeekBar) findViewById(R.id.seekBar_boardMember);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(getApplicationContext(),"Jäseniä: "+progress, Toast.LENGTH_SHORT).show();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                //TODO Auto-generated method stub
            }
        });


    }

    @Override
    public void onBackPressed () {
        String name = Association.getInstance().name;
        Intent intent = new Intent();
        intent.putExtra("name", name);
        setResult(RESULT_OK, intent);
        finish();
    }

    //HERE I Do not get to work this so that I could set Association.setBoardSize
    public int setBoardMemberCount () {
        int seekbarNumber = seekBar.getProgress();
        return seekbarNumber;
    }
}

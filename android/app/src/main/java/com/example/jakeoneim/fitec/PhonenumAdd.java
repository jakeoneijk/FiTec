package com.example.jakeoneim.fitec;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//number and name write & number add class
public class PhonenumAdd extends AppCompatActivity {
    EditText editname;
    EditText editnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnumber);

        editname = (EditText)findViewById(R.id.name_edit);
        editnum = (EditText)findViewById(R.id.number_edit);

    }

    public void save(View v){
        String name = editname.getText().toString();
        String number = editnum.getText().toString();
        Intent resultIntent = new Intent(getApplicationContext(),PhonenumberSave.class);
        resultIntent.putExtra("name",name);
        resultIntent.putExtra("number",number);
        setResult(RESULT_OK,resultIntent);
        finish();
    }

}

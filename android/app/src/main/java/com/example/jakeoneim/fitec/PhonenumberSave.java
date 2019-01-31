package com.example.jakeoneim.fitec;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

//saved number show and add number class
public class PhonenumberSave extends AppCompatActivity {
    Database db;
    SQLiteDatabase sqLiteDatabase;
    String name_edit;
    String number_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonenumber_save);

        //show db data - list
        db = new Database(this);
        sqLiteDatabase = db.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM address",null);

        startManagingCursor(cursor);

        String[] from = {"name","number"};
        int[] to = {android.R.id.text1,android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,cursor,from,to);
        ListView list = (ListView)findViewById(R.id.num_list);
        list.setAdapter(adapter);

        //list item click -> delete
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PhonenumberSave.this);
                dialog.setTitle("data delete")
                        .setMessage("delete this number?").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PhonenumberSave.this,"delete sucessful",Toast.LENGTH_LONG).show();
                        db.deleteColumn(position+1);
                    }
                }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PhonenumberSave.this,"cancel",Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
            }
        });

        //add button click -> intent transform
        Button add_btn = (Button)findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PhonenumberSave.this,PhonenumAdd.class);
                startActivityForResult(i,3000);
            }
        });

    }

    //received name and number
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            if(requestCode == 3000){
                name_edit = data.getStringExtra("name");
                number_edit = data.getStringExtra("number");
                db.insertColumn(name_edit, number_edit);//add to db
                finish();
                startActivity(new Intent(getApplicationContext(),PhonenumberSave.class));
            }
        }
    }
}

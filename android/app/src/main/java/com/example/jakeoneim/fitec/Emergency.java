package com.example.jakeoneim.fitec;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Toast;

//1 is falling down, 2 is heart failure
public class Emergency extends Activity{

    Database db;
    SQLiteDatabase sqLiteDatabase;

    private String call_number = "17657015379";

    public void sendMessage(int typeofemergency){
        db = new Database(this);
        sqLiteDatabase = db.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM address",null);

        startManagingCursor(cursor);

        String message = "Neighbors are fall down!!! Please visit and call 911!!";
        while(cursor.moveToNext()){
            if(typeofemergency==1){
                message += "[Fall Detection]\n";
            }else if(typeofemergency==2){
                message += "[Heart Failure Detection]\n";
            }
            try{
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(cursor.getString(cursor.getColumnIndex("number")),null,message,null,null);
                Toast.makeText(getApplicationContext(),"Send Sucessful!",Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"SMS faile, please try again later!",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
    public void call(){
        startActivity(new Intent("android.intent.action.CALL", Uri.parse(call_number)));
    }
}

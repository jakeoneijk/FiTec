package com.example.jakeoneim.fitec;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Toast;

//1 is falling down, 2 is heart failure
public class Emergency extends Activity{
    private String call_number = "17657015379";
    private String[] neibor_numbers = {"17657015379","17655864549"};


    public void sendMessage(int typeofemergency){
        int temp = 0;
        String message = "Neighbors are fall down!!! Please visit and call 911!!";
        while(temp == neibor_numbers.length-1){
            if(typeofemergency==1){
                message += "[Fall Detection]\n";
            }else if(typeofemergency==2){
                message += "[Heart Failure Detection]\n";
            }
            try{
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(neibor_numbers[temp],null,message,null,null);
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

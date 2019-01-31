package com.example.jakeoneim.fitec;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    FallDownCheck fTest;
    HeartRateCheck hTest;
    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fTest = new FallDownCheck(); // fallDown Object create
        hTest = new HeartRateCheck();// heartBeat Object create
        isConnected = false;

        findViewById(R.id.connectButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected){
                    if(makeConnectionWithSensor()){
                        Toast.makeText(getApplicationContext(),"start FitBit",Toast.LENGTH_LONG);
                    }else{
                        Toast.makeText(getApplicationContext(),"connection problem",Toast.LENGTH_LONG);
                    }
                }else{
                    isConnected = false;
                    Toast.makeText(getApplicationContext(),"stop FitBit",Toast.LENGTH_LONG);
                }
            }
        });

    }

    public boolean makeConnectionWithSensor(){
        //isConnected should be changed here
        isConnected = true;
        receiveDataFromSensor();
        return false;
    }

    public int[] receiveDataFromSensor(){ // receive data
        return new int[7];
    }

    public void controller(){ //receive data and use it
        int [] data;
        while (isConnected){
            data = receiveDataFromSensor();
            //receiveData here
            if(hTest.isProblem(data[0])){
                hTest.start();
            }

            if(fTest.isFallenDown(data[1],data[2],data[3],data[4],data[5],data[6])){
                fTest.start();
            }

        }


    }

    //intent transform -> neighbors number show
    public void neighbors(View view){
        Intent i = new Intent(getApplicationContext(),PhonenumberSave.class);
        startActivity(i);
    }

}

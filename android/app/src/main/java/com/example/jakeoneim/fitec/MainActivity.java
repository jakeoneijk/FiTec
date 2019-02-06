package com.example.jakeoneim.fitec;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    public static Context context;
    FallDownCheck fTest;
    HeartRateCheck hTest;
    boolean isConnected;
    public static BluetoothSPP bt;

    Emergency eg ;

    private SensorManager sensorManager;
    private Sensor accelSensor; // Sensor object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //******************accel Sensor*****************
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //******************bluetooth communication*****************
        bt = new BluetoothSPP(this);

        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
                eg = Emergency.getInstance(getApplicationContext());
            }

            public void onDeviceDisconnected() { //연결해제
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnConnect = findViewById(R.id.connectButton); //연결시도
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });
        // 블루투스 데이터 수신시 처리
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                Log.i("Check", "Message : " + message);
            }
        });

        //*********************************************************************

        fTest = new FallDownCheck(); // fallDown Object create
        hTest = new HeartRateCheck();// heartBeat Object create
        isConnected = false;
    }

    /*emergency발생시를 버튼클릭으로 일단 대체*/
    public void emergencyClick(View v) {
        eg.send();
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리

            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        //Sensor Delay Normal 0.2
        sensorManager.registerListener(this, accelSensor , SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //블루투스 중지
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);

            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    //****************************************************************************

    public boolean makeConnectionWithSensor(){
        //isConnected should be changed here
        isConnected = true;
        controller();
        return false;
    }

    public double[] receiveDataFromSensor(){ // receive data
        return new double[4];
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this){
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                Log.d("Sensor",event.values[0]+" , "+event.values[1]+" , "+event.values[2]);
                if(fTest.isFallenDown(event.values[0],event.values[1],event.values[2])){
                    Toast.makeText(getApplicationContext(),"fall down detected",Toast.LENGTH_SHORT).show();
                    fTest.start();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void controller(){ //receive data and use it
        double[] data;
        while (isConnected){
            data = receiveDataFromSensor();
            //receiveData here
            if(hTest.isProblem(data[0])){
                hTest.start();
            }
        }
    }
    //intent transform -> neighbors number show
    public void neighbors(View view){
        Intent i = new Intent(getApplicationContext(),PhonenumberSave.class);
        startActivity(i);
    }

    public void call_test(View view){
        Emergency emergency = Emergency.getInstance(MainActivity.this);
        emergency.call();
    }
    public void msg_test(View view){
        Emergency emergency = Emergency.getInstance(MainActivity.this);
        emergency.sendMessage(1);
    }

}

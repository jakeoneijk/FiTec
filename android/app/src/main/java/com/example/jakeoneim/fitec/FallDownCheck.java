package com.example.jakeoneim.fitec;

public class FallDownCheck extends Thread{

    boolean isProbremOccured;

    public FallDownCheck(){
        isProbremOccured=false;
    }

    public boolean isFallenDown(int gyroX, int gyroY , int gyroZ , int accelX , int accelY , int accelZ){
        if(isProbremOccured) return false;
        return false;
    }

    public void run(){ //thread start
        problemOccured();
    }

    public void problemOccured(){
        isProbremOccured = true;
        isProbremOccured = false;

    }


}

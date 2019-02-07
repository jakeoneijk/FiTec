package com.example.jakeoneim.fitec;

import android.util.Log;

public class FallDownCheck extends Thread{

    final double gravityAccel = 9.8;
    final double thresholdGsvm = gravityAccel*2.5;
    final double thresholdAngle = 60;

    public FallDownCheck(){

    }

    public boolean isFallenDown(double accelX , double accelY , double accelZ){

        double value = gsvm(accelX,accelY,accelZ);

        //Log.d("Debug",value+" , "+thresholdGsvm);

        if(value > thresholdGsvm){
            if(angle(accelX,accelY,accelZ)>thresholdAngle){
                return true;
            }
        }
        return false;
    }

    public double svm(double accelX , double accelY , double accelZ){
        double squareSizeOfVector = accelX*accelX + accelY*accelY + accelZ*accelZ;
        return Math.sqrt(squareSizeOfVector);
    }

    public double angle(double accelX , double accelY , double accelZ){
        double parameterOfInverseTangent = Math.sqrt(accelY*accelY + accelZ*accelZ) / accelX;
       return Math.atan(parameterOfInverseTangent) * (180/Math.PI);
    }

    public double gsvm(double accelX , double accelY , double accelZ){
        return (angle(accelX,accelY,accelZ)/90) * svm(accelX,accelY,accelZ);
    }


}

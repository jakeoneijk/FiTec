package com.example.jakeoneim.fitec;

public class FallDownCheck extends Thread{

    boolean isProbremOccured;
    final double gravityAccel = 9.8;
    final double thresholdGsvm = gravityAccel*2.5;
    final double thresholdAngle = 60;

    public FallDownCheck(){
        isProbremOccured=false;
    }

    public boolean isFallenDown(double accelX , double accelY , double accelZ){
        if(isProbremOccured) return false;
        if(gsvm(accelX,accelY,accelZ) > thresholdGsvm){
            if(angle(accelX,accelY,accelZ)>thresholdAngle){
                return true;
            }
        }
        return false;
    }

    public void run(){ //thread start
        problemOccured();
    }

    public void problemOccured(){
        isProbremOccured = true;
        isProbremOccured = false;

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

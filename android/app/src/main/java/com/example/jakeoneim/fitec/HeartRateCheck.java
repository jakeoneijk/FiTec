package com.example.jakeoneim.fitec;

public class HeartRateCheck extends Thread{
    private final int limitNumberOfDetectingProblem = 4;

    int numberOfProblemChecked;
    int standardHeartRateOfUser;
    Emergency em;

    public HeartRateCheck(){

        this.numberOfProblemChecked = 0;
        this.standardHeartRateOfUser = 80;
        //this.em = new Emergency();

    }

    public boolean isProblem(double currentHertRate){ // if there is problem true
        if(currentHertRate < standardHeartRateOfUser/2 || currentHertRate > standardHeartRateOfUser +40){
            this.numberOfProblemChecked++;
            if(numberOfProblemChecked > limitNumberOfDetectingProblem){
                return true;
            }
        }else{
            numberOfProblemChecked = 0;
        }
        return false;
    }

}

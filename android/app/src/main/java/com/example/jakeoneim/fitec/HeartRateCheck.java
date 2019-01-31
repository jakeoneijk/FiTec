package com.example.jakeoneim.fitec;

public class HeartRateCheck extends Thread{
    private final int limitNumberOfDetectingProblem = 4;

    boolean isProbremOccured;
    int numberOfProblemChecked;
    int standardHeartRateOfUser;
    Emergency em;

    public HeartRateCheck(){

        this.isProbremOccured = false;
        this.numberOfProblemChecked = 0;
        this.standardHeartRateOfUser = 80;
        this.em = new Emergency();

    }

    public boolean isProblem(int currentHertRate){ // if there is problem true
        if(isProbremOccured) return false;
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

    public void run(){ //thread start
        problemOccured();
    }

    public void problemOccured(){
        isProbremOccured = true;

        isProbremOccured = false;

    }

}

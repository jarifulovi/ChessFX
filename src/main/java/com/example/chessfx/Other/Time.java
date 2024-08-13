package com.example.chessfx.Other;

public class Time {
    private long startTime1;
    private double elapsedTime1;
    private long startTime2;
    private double elapsedTime2;
    private int duration;   // In minutes

    public Time(int duration) {
        this.duration = duration;
        reset();
    }

    public void reset() {
        startTime1 = 0;
        elapsedTime1 = 0.0;
        startTime2 = 0;
        elapsedTime2 = 0.0;
    }
    public void startWhite() {
        startTime1 = System.nanoTime();
    }
    public void startBlack(){ startTime2 = System.nanoTime(); }

    public void stopWhite() {
        elapsedTime1 += getCurrentElapsed(startTime1); // Hold the elapsed time
        startTime1 = 0;
    }
    public void stopBlack(){
        elapsedTime2 += getCurrentElapsed(startTime2); // Hold the elapsed time
        startTime2 = 0;
    }

    private double getCurrentElapsed(long startTime){

        if(startTime == 0) return 0.0;
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000_000.0;
    }
    public double getWhiteTime(){

        double elapsedTime = getCurrentElapsed(startTime1);
        elapsedTime += elapsedTime1;

        int minute = (int)(elapsedTime / 60);
        int second = (int)(elapsedTime % 60);

        return minute + (second / 100.0);
    }
    public double getBlackTime(){

        double elapsedTime = getCurrentElapsed(startTime2);
        elapsedTime += elapsedTime2;

        int minute = (int)(elapsedTime / 60);
        int second = (int)(elapsedTime % 60);

        return minute + (second / 100.0);
    }
    public double getRemainingTime(double time){

        if(time >= duration) return 0.0;
        if(time == 0.0) return duration;
        // Convert `time` to total seconds
        int elapsedMinutes = (int) time;
        double elapsedSeconds = (time - elapsedMinutes) * 100;
        double elapsedTimeInSeconds = elapsedMinutes * 60 + elapsedSeconds;

        // Total duration in seconds
        double totalDurationInSeconds = duration * 60;

        // Calculate remaining time in seconds
        double remainingTimeInSeconds = totalDurationInSeconds - elapsedTimeInSeconds;

        // Calculate remaining minutes and seconds
        int remainingMinutes = (int) (remainingTimeInSeconds / 60);
        double remainingSeconds = remainingTimeInSeconds % 60;

        // Return remaining time as a double in minutes
        return remainingMinutes + remainingSeconds / 100;
    }
    public boolean isTimeOver(double time){
        return duration <= time;
    }

}


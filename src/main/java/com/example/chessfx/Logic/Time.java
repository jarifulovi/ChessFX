package com.example.chessfx.Logic;

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

    public boolean isTimeOver(double time){
        return duration <= time;
    }
    public String formatTime() {
        return String.format("%.2f", elapsedTime1);
    }


    public static void main(String[] args) {

        Time time = new Time(10);
        time.startWhite();

        int sec = 1000000;

        //timer.startBlack();
        while(true){
            time.startWhite();
            time.stopBlack();
            for(int i=0;i<5;i++){
                for(int j=0;j<sec;j++){
                    System.out.println("First loop : "+ time.getWhiteTime()+" "+ time.getBlackTime());
                }
            }
            time.startBlack();
            time.stopWhite();
            for(int i=0;i<5;i++){
                for(int j=0;j<sec;j++){
                    System.out.println("Second loop : "+ time.getWhiteTime()+" "+ time.getBlackTime());
                }
            }
        }
    }
}


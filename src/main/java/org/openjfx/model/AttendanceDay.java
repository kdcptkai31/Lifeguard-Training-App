package org.openjfx.model;

public class AttendanceDay {

    private int traineeID;
    private int day;
    private double hours;

    /**
     * Default Constructor
     */
    public AttendanceDay(){}

    /**
     * Constructor
     * @param tID
     * @param day
     * @param hours
     */
    public AttendanceDay(int tID, int day, double hours){

        traineeID = tID;
        this.day = day;
        this.hours = hours;

    }

    //Getters
    public int getTraineeID() {
        return traineeID;
    }
    public int getDay() {
        return day;
    }
    public double getHours() {
        return hours;
    }

    //Setters
    public void setTraineeID(int traineeID) {
        this.traineeID = traineeID;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public void setHours(double hours) {
        this.hours = hours;
    }
}

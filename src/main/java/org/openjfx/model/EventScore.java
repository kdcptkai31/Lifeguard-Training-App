package org.openjfx.model;

public class EventScore {

    private int eventID;
    private int traineeID;
    private int score;

    /**
     * Constructor
     * @param eID
     * @param tID
     * @param s
     */
    public EventScore(int eID, int tID, int s){

        eventID = eID;
        traineeID = tID;
        score = s;

    }

    //Getters
    public int getEventID() { return eventID; }

    public int getTraineeID() { return traineeID; }

    public int getScore() { return score; }

    //Setters
    public void setEventID(int eventID) { this.eventID = eventID; }

    public void setTraineeID(int traineeID) { this.traineeID = traineeID; }

    public void setScore(int score) { this.score = score; }
}

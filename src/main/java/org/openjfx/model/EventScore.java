package org.openjfx.model;

public class EventScore {

    private int eventID;
    private int traineeID;
    private int place;

    /**
     * Constructor
     * @param eID
     * @param tID
     * @param p
     */
    public EventScore(int eID, int tID, int p){

        eventID = eID;
        traineeID = tID;
        place = p;

    }

    //Getters
    public int getEventID() { return eventID; }

    public int getTraineeID() { return traineeID; }

    public int getPlace() { return place; }

    //Setters
    public void setEventID(int eventID) { this.eventID = eventID; }

    public void setTraineeID(int traineeID) { this.traineeID = traineeID; }

    public void setPlace(int place) { this.place = place; }
}

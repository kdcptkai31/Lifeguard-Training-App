package org.openjfx.model;

/**
 * Represents an event object. Including its name, notes, and if it is scored.
 */
public class Event {

    private int eventID;
    private String name;
    private String notes;
    private boolean isScored;
    private int year;
    private int session;

    //Constructors
    /**
     * Default Constructor
     */
    public Event(){}

    /**
     * Constructor
     * @param eID
     * @param name
     * @param n
     * @param isScore
     * @param y
     * @param s
     */
    public Event(int eID, String name, String n, boolean isScore, int y, int s){

        eventID = eID;
        this.name = name;
        notes = n;
        isScored = isScore;
        year = y;
        session = s;

    }

    /**
     * Initial Event Constructor: No id parameter, as it has yet to be produced by the db.
     * @param name
     * @param n
     * @param isScore
     * @param y
     * @param s
     */
    public Event(String name, String n, boolean isScore, int y, int s){

        this(0, name, n, isScore, y, s);

    }

    /**
     * Defines what is output by printing a event object.
     * @return
     */
    @Override
    public String toString(){

        return name;

    }

    //Getters
    public int getEventID() {
        return eventID;
    }

    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isScored() { return isScored; }

    public int getYear() {
        return year;
    }

    public int getSession() {
        return session;
    }

    //Setters
    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setScored(boolean isScore) { this.isScored = isScore; }

    public void setYear(int year) {
        this.year = year;
    }

    public void setSession(int session) {
        this.session = session;
    }
}

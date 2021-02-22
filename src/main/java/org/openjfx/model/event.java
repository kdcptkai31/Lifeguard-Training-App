package org.openjfx.model;

public class Event {

    private int eventID;
    private String name;
    private int points;
    private String notes;
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
     * @param p
     * @param n
     * @param y
     * @param s
     */
    public Event(int eID, String name, int p, String n, int y, int s){

        eventID = eID;
        this.name = name;
        points = p;
        notes = n;
        year = y;
        session = s;

    }

    /**
     * Initial Event Constructor: No id parameter, as it has yet to be produced by the db.
     * @param name
     * @param p
     * @param n
     * @param y
     * @param s
     */
    public Event(String name, int p, String n, int y, int s){

        this(0, name, p, n, y, s);

    }

    //Getters
    public int getEventID() {
        return eventID;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public String getNotes() {
        return notes;
    }

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

    public void setPoints(int points) {
        this.points = points;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setSession(int session) {
        this.session = session;
    }
}

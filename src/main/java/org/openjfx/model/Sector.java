package org.openjfx.model;

/**
 * Represents a sector object. Mainly holds the name of itself.
 */
public class Sector {

    private int sectorID;
    private int year;
    private int session;
    private String name;

    /**
     * Default Constructor
     */
    public Sector(){}

    /**
     * Constructor
     * @param sID
     * @param y
     * @param s
     * @param n
     */
    public Sector(int sID, int y, int s, String n){

        sectorID = sID;
        year = y;
        session = s;
        name = n;

    }

    /**
     * Initial Info Constructor: Used for the initially built sector for filler districts to use.
     * @param y
     * @param s
     * @param n
     */
    public Sector(int y, int s, String n){

        this(0, y, s, n);

    }

    /**
     * Prints the name of the session if the object is output.
     * @return
     */
    @Override
    public String toString(){

        return name;

    }

    //Getters
    public int getSectorID() {
        return sectorID;
    }
    public int getYear() {
        return year;
    }
    public int getSession() {
        return session;
    }
    public String getName() {
        return name;
    }
    //Setters
    public void setSectorID(int sectorID) {
        this.sectorID = sectorID;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setSession(int session) {
        this.session = session;
    }
    public void setName(String name) {
        this.name = name;
    }
}

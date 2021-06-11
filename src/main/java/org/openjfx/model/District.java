package org.openjfx.model;

/**
 * Represents a District object. Holds its information.
 */
public class District {

    private int year;
    private int session;
    private String name;
    private String supervisorEmail;
    private int sectorID;

    /**
     * Default Constructor
     */
    public District(){}

    /**
     * Initial Constructor - Sets default sector ID to 1 which is the null sector.
     * @param y
     * @param s
     * @param n
     * @param sE
     */
    public District(int y, int s, String n, String sE){

        this(y, s, n, sE, 1);

    }

    /**
     * Constructor
     * @param y
     * @param s
     * @param n
     * @param sE
     */
    public District(int y, int s, String n, String sE, int sectorID){

        year = y;
        session = s;
        name = n;
        supervisorEmail = sE;
        this.sectorID = sectorID;

    }

    //Getters
    public int getYear() { return year; }

    public int getSession() { return session; }

    public String getName() { return name; }

    public String getSupervisorEmail() { return supervisorEmail; }

    public int getSectorID() { return sectorID; }

    //Setters
    public void setYear(int year) { this.year = year; }

    public void setSession(int session) { this.session = session; }

    public void setName(String name) { this.name = name; }

    public void setSupervisorEmail(String supervisorEmail) { this.supervisorEmail = supervisorEmail; }

    public void setSectorID(int sID) { sectorID = sID; }

}

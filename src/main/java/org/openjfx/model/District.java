package org.openjfx.model;

public class District {

    private int year;
    private int session;
    private String name;
    private String supervisorEmail;

    /**
     * Default Constructor
     */
    public District(){}

    /**
     * Constructor
     * @param y
     * @param s
     * @param n
     * @param sE
     */
    public District(int y, int s, String n, String sE){

        year = y;
        session = s;
        name = n;
        supervisorEmail = sE;

    }

    //Getters
    public int getYear() { return year; }

    public int getSession() { return session; }

    public String getName() { return name; }

    public String getSupervisorEmail() { return supervisorEmail; }

    //Setters
    public void setYear(int year) { this.year = year; }

    public void setSession(int session) { this.session = session; }

    public void setName(String name) { this.name = name; }

    public void setSupervisorEmail(String supervisorEmail) { this.supervisorEmail = supervisorEmail; }

}

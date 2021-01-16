package org.openjfx.model;

public class Instructor {

    private int year;
    private int session;
    private String name;

    /**
     * Constructor
     * @param y
     * @param s
     * @param n
     */
    public Instructor(int y, int s, String n){

        year = y;
        session = s;
        name = n;

    }

    //Getters
    public int getYear() { return year; }

    public int getSession() { return session; }

    public String getName() { return name; }

    //Setters
    public void setYear(int year) { this.year = year; }

    public void setSession(int session) { this.session = session; }

    public void setName(String name) { this.name = name; }

}

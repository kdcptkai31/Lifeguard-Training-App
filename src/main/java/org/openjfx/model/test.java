package org.openjfx.model;

import java.io.Serializable;

public class Test implements Serializable {

    private int testID;
    private String name;
    private int points;
    private int year;
    private int session;

    /**
     * Constructor
     * @param tID
     * @param name
     * @param p
     * @param y
     * @param s
     */
    public Test(int tID, String name, int p, int y, int s){

        testID = tID;
        this.name = name;
        points = p;
        year = y;
        session = s;

    }

    //Getters
    public int getTestID() { return testID; }

    public String getName() { return name; }

    public int getPoints() { return points; }

    public int getYear() { return year; }

    public int getSession() { return session; }

    //Setters
    public void setTestID(int testID) { this.testID = testID; }

    public void setName(String name) { this.name = name; }

    public void setPoints(int points) { this.points = points; }

    public void setYear(int year) { this.year = year; }

    public void setSession(int session) { this.session = session; }

}

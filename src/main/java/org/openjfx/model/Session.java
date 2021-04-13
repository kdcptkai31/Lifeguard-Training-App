package org.openjfx.model;

public class Session {

    private int year;
    private int session;
    private String startDate;
    private String endDate;
    private int currentDay;

    //Default Constructor
    public Session(){}

    /**
     * Constructor
     * @param y
     * @param s
     * @param sDate
     * @param eDate
     * @param currentDa
     */
    public Session(int y, int s, String sDate, String eDate, int currentDa){

        year = y;
        session = s;
        startDate = sDate;
        endDate = eDate;
        currentDay = currentDa;

    }

    /**
     * Copy Constructor
     * @param tmp
     */
    public Session(Session tmp){

        this.year = tmp.year;
        this.session = tmp.session;
        this.startDate = tmp.startDate;
        this.endDate = tmp.endDate;
        this.currentDay = tmp.currentDay;

    }

    //Getters
    public int getYear() {
        return year;
    }
    public int getSession() {
        return session;
    }
    public String getStartDate() {
        return startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public int getCurrentDay() { return currentDay; }

    //Setters
    public void setYear(int year) {
        this.year = year;
    }
    public void setSession(int session) {
        this.session = session;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public void setCurrentDay(int num) { currentDay = num; }

}

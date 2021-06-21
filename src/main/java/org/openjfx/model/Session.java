package org.openjfx.model;

/**
 * Represents a session object. Includes basic info about itself.
 */
public class Session {

    private int year;
    private int session;
    private String startDate;
    private String endDate;
    private int currentDay;
    private int openedLast;
    private boolean isWeekends;
    private String firstDay;

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
    public Session(int y, int s, String sDate, String eDate, int currentDa, int openedLast, boolean isWeekends, String firstDay){

        year = y;
        session = s;
        startDate = sDate;
        endDate = eDate;
        currentDay = currentDa;
        this.openedLast = openedLast;
        this.isWeekends = isWeekends;
        this.firstDay = firstDay;

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
        this.openedLast = tmp.openedLast;
        this.isWeekends = tmp.isWeekends;
        this.firstDay = tmp.firstDay;

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
    public int getOpenedLast() { return openedLast; }
    public boolean isWeekends() { return isWeekends; }
    public String getFirstDay() { return firstDay; }

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
    public void setOpenedLast(int openedLast) { this.openedLast = openedLast; }
    public void setIsWeekends(boolean isWeekends) { this.isWeekends = isWeekends; }
    public void setFirstDay(String firstDay) { this.firstDay = firstDay; }

}

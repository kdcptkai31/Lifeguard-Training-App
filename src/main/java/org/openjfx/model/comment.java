package org.openjfx.model;

import org.openjfx.controller.DBManager;

public class Comment {

    private int traineeID; //Associated Trainee's ID
    private int id;
    private String date;
    private String rotation;
    private String instructorName;
    private String traineeName;
    private String incidentType;
    private String incidentDescription;
    private String instructorActions;
    private String nextSteps;
    private int year;
    private int session;
    private int currentDay;

    /**
     * Default Constructor
     */
    public Comment(){}

    /**
     * Main Constructor
     * @param tID
     * @param cID
     * @param d
     * @param r
     * @param iN
     * @param tN
     * @param iT
     * @param iDes
     * @param iA
     * @param nS
     * @param y
     * @param s
     * @param cD
     */
    public Comment(int cID, int tID, String d, String r, String iN, String tN, String iT, String iDes, String iA,
                   String nS, int y, int s, int cD){

        id = cID;
        traineeID = tID;
        date = d;
        rotation = r;
        instructorName = iN;
        traineeName = tN;
        incidentType = iT;
        incidentDescription = iDes;
        instructorActions = iA;
        nextSteps = nS;
        year = y;
        session = s;
        currentDay = cD;

    }

    /**
     * Used for making the final evaluation comment. Notice the hard coded changes to this type of comment.
     * @param traineeID
     * @param date
     * @param traineeName
     * @param finalEval
     * @param pointDifference
     * @param y
     * @param s
     */
    public Comment(int traineeID, String date, String traineeName, String finalEval, String pointDifference, int y,
                   int s){

        id = 0;
        this.traineeID = traineeID;
        this.date = date;
        rotation = "";
        instructorName = "";
        this.traineeName = traineeName;
        incidentType = "Final Evaluation";
        incidentDescription = finalEval;
        instructorActions = pointDifference;
        nextSteps = "";
        year = y;
        session = s;
        currentDay = 9;

    }

    /**
     * Initial Comment Constructor: The id has not been generated yet.
     * @param d
     * @param r
     * @param iN
     * @param tN
     * @param iT
     * @param iDes
     * @param iA
     * @param nS
     * @param y
     * @param s
     */
    public Comment(String d, String r, String iN, String tN, String iT, String iDes, String iA,
                   String nS, int y, int s, int cD){

        traineeID = DBManager.getTIDFromNameAndSession(tN, y, s);
        id = 0;
        date = d;
        rotation = r;
        instructorName = iN;
        traineeName = tN;
        incidentType = iT;
        incidentDescription = iDes;
        instructorActions = iA;
        nextSteps = nS;
        year = y;
        session = s;
        currentDay = cD;

    }

    @Override
    public String toString(){

        return "Day " + currentDay + ": " + incidentType + " | " + nextSteps;

    }

    //Getters
    public int getTraineeID() { return traineeID; }

    public int getId() { return id; }

    public String getDate() { return date; }

    public String getRotation() { return rotation; }

    public String getInstructorName() { return instructorName; }

    public String getTraineeName() { return traineeName; }

    public String getIncidentType() { return incidentType; }

    public String getIncidentDescription() { return incidentDescription; }

    public String getInstructorActions() { return instructorActions; }

    public String getNextSteps() { return nextSteps; }

    public int getYear() { return year; }

    public int getSession() { return session; }

    public int getCurrentDay() { return currentDay; }

    //Setters
    public void setTraineeID(int traineeID) { this.traineeID = traineeID; }

    public void setId(int id) { this.id = id; }

    public void setDate(String date) { this.date = date; }

    public void setRotation(String rotation) { this.rotation = rotation; }

    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }

    public void setTraineeName(String traineeName) { this.traineeName = traineeName; }

    public void setIncidentType(String incidentType) { this.incidentType = incidentType; }

    public void setIncidentDescription(String incidentDescription) { this.incidentDescription = incidentDescription; }

    public void setInstructorActions(String instructorActions) { this.instructorActions = instructorActions; }

    public void setNextSteps(String nextSteps) { this.nextSteps = nextSteps; }

    public void setYear(int year) { this.year = year; }

    public void setSession(int session) { this.session = session; }

    public void setCurrentDay(int cD) { currentDay = cD; }

}

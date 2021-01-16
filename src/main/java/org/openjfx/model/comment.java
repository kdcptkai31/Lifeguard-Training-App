package org.openjfx.model;

import java.io.Serializable;

public class Comment implements Serializable {

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

    /**
     * Constructor
     */
    public Comment(){}

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

}

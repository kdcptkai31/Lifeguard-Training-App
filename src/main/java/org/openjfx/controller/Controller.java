package org.openjfx.controller;

import java.util.Comparator;
import java.util.Objects;
import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.openjfx.model.*;

/**
 * Controls the flow and update of data from the database to local memory.
 * Maintains the DBManager throughout runtime
 */
public class Controller {

    private DBManager dbManager;

    private Session currentSession;
    private Vector<Trainee> currentTrainees;
    private Vector<Comment> currentComments;
    private Vector<Test> currentTests;
    private Vector<Event> currentEvents;

    /**
     * Constructor
     */
    public Controller(){

        //Initializes Database if it does not exist
        dbManager = new DBManager();
        currentSession = new Session();

        Session tmp = DBManager.getCurrentSession();
        if(tmp == null){

            currentSession.setYear(0);
            currentSession.setSession(0);
            currentSession.setStartDate("x");
            currentSession.setEndDate("x");
            currentSession.setCurrentDay(1);
            currentSession.setOpenedLast(1);

        }else
            currentSession = tmp;

        currentTrainees = new Vector<>();
        currentComments = new Vector<>();
        currentTests = new Vector<>();
        currentEvents = new Vector<>();
        updateCurrentTrainees();
        updateCurrentComments();
        updateCurrentTests();
        updateCurrentEvents();

    }

    /**
     * Updates the controller with the new session information, which updates all associated stored data.
     * @param ses
     */
    public void updateCurrentSession(Session ses){

        setCurrentSession(ses);
        updateCurrentTrainees();
        updateCurrentComments();
        updateCurrentTests();
        updateCurrentEvents();

    }

    /**
     * Loads the current trainees from the db into memory.
     */
    public void updateCurrentTrainees(){

        currentTrainees = DBManager.getAllTraineesFromSession(currentSession.getYear(), currentSession.getSession());

        Objects.requireNonNull(currentTrainees).removeIf(b -> !b.isActive());
        Objects.requireNonNull(currentTrainees).sort(new SortByLastName());

    }

    /**
     * Loads the current comments from the db into memory.
     */
    public void updateCurrentComments(){

        currentComments = DBManager.getAllCommentsFromSession(currentSession.getYear(), currentSession.getSession());

    }

    /**
     * Loads the current tests from the db into memory.
     */
    public void updateCurrentTests() {

        currentTests = DBManager.getAllTestsFromSession(currentSession.getYear(), currentSession.getSession());

    }

    /**
     * Loads the current events from the db into memory.
     */
    public void updateCurrentEvents() {

        currentEvents = DBManager.getAllEventsFromSession(currentSession.getYear(), currentSession.getSession());

    }

    /**
     * Returns the trainees as an observable list.
     * @return
     */
    public ObservableList<String> getTraineeNamesAsObservableList(){

        ObservableList<String> traineeList = FXCollections.observableArrayList();
        if (currentTrainees.size() < 1)
            return null;

        for(Trainee trainee : currentTrainees){

            StringBuilder tmpStr = new StringBuilder();
            if(trainee.isQuestionnaire2Complete() && trainee.isDisabled())
                tmpStr.append("* ");

            traineeList.add(tmpStr.append(trainee.getFullName()).toString());

        }

        return traineeList;

    }

    //Getters
    public Session getCurrentSession(){return currentSession;}
    public DBManager getDBManager(){return dbManager;}
    public Vector<Trainee> getCurrentTrainees() {return currentTrainees;}
    public Vector<Comment> getCurrentComments() {return currentComments;}
    public Vector<Test> getCurrentTests() {return currentTests;}
    public Vector<Event> getCurrentEvents() {return currentEvents;}

    //Setters

    /**
     * Finds the session last opened and sets the currentSession as that.
     * @param ses
     */
    public void setCurrentSession(Session ses){

        DBManager.changeSessionOpenedLast(currentSession, 0);
        DBManager.changeSessionOpenedLast(ses, 1);
        currentSession = ses;

    }

    /**
     * Used to compare for sorting, sorts by last name first.
     */
    class SortByLastName implements Comparator<Trainee>{
        @Override
        public int compare(Trainee o1, Trainee o2) {
            return o1.getLastName().compareTo(o2.getLastName());
        }
    }

    /**
     * Determines if the entered day is a valid day of the week.
     * @param str
     * @return
     */
    public boolean isDayOfWeek(String str){

        String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
        str = str.toLowerCase().trim();
        for (String day : days) {
            if (str.equals(day))
                return true;
        }

        return false;

    }

    /**
     * Returns the day of the week in correct casing format.
     * @param str
     * @return
     */
    public String getDayOfWeek(String str){

        str = str.toLowerCase().trim();

        switch (str) {
            case "monday":
                return "Monday";
            case "tuesday":
                return "Tuesday";
            case "wednesday":
                return "Wednesday";
            case "thursday":
                return "Thursday";
            case "friday":
                return "Friday";
            case "saturday":
                return "Saturday";
            case "sunday":
                return "Sunday";
            default:
                return "ERROR";
        }

    }

    /**
     * Returns the next day of the week, for the given day of the week.
     * @param str
     * @return
     */
    public String getNextDayOfWeek(String str){

        str = str.toLowerCase().trim();

        switch (str) {
            case "monday":
                return "Tuesday";
            case "tuesday":
                return "Wednesday";
            case "wednesday":
                return "Thursday";
            case "thursday":
                return "Friday";
            case "friday":
                return "Saturday";
            case "saturday":
                return "Sunday";
            case "sunday":
                return "Monday";
            default:
                return "ERROR";
        }


    }

}

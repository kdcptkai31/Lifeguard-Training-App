package org.openjfx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.openjfx.model.Comment;
import org.openjfx.model.Session;
import org.openjfx.model.Trainee;

import java.util.Vector;

public class Controller {

    private DBManager dbManager;
    private Session currentSession;

    private Vector<Trainee> currentTrainees;
    private Vector<Comment> currentComments;

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

        }else
            currentSession = tmp;

        currentTrainees = new Vector<>();
        currentComments = new Vector<>();
        updateCurrentTrainees();

    }

    /**
     * Updates the controller with the new session information, which updates all associated stored data.
     * @param ses
     */
    public void updateCurrentSession(Session ses){

        currentSession = ses;
        updateCurrentTrainees();
        updateCurrentComments();

    }

    /**
     * Loads the current trainees from the db into memory.
     */
    public void updateCurrentTrainees(){

        currentTrainees = DBManager.getAllTraineesFromSession(currentSession.getYear(), currentSession.getSession());

    }

    /**
     * Loads the current comments from the db into memory.
     */
    public void updateCurrentComments(){

        currentComments = DBManager.getAllCommentsFromSession(currentSession.getYear(), currentSession.getSession());

    }

    /**
     * Returns the trainees as an observable list.
     * @return
     */
    public ObservableList<Trainee> getTraineesAsObservableList(){

        ObservableList<Trainee> traineeList = FXCollections.observableArrayList();
        if (currentTrainees.size() > 0)
            traineeList.addAll(currentTrainees);
        return traineeList;

    }

    //Getters
    public Session getCurrentSession(){return currentSession;}
    public DBManager getDBManager(){return dbManager;}
    public Vector<Trainee> getCurrentTrainees() {return currentTrainees;}
    public Vector<Comment> getCurrentComments() {return currentComments;}

    //Setters
    public void setCurrentSession(Session ses){currentSession = ses;}

}

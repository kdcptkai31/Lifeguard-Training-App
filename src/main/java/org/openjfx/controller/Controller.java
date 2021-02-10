package org.openjfx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import org.openjfx.model.Trainee;

import java.util.Vector;

public class Controller {

    private DBManager dbManager;

    private int currentYear;
    private int currentSession;

    private Vector<Trainee> currentTrainees;

    /**
     * Constructor
     */
    public Controller(){

        //Initializes Database if it does not exist
        dbManager = new DBManager();

        Pair<Integer, Integer> tmp = DBManager.getCurrentSession();
        if(tmp == null){

            currentYear = 0;
            currentSession = 0;

        }else{

            currentYear = tmp.getKey();
            currentSession = tmp.getValue();

        }

        currentTrainees = new Vector<>();

    }

    /**
     * Loads the current trainees from the db into memory.
     */
    public void updateCurrentTrainees(){

        currentTrainees = DBManager.getAllTraineesFromSession(currentYear, currentSession);

    }

    /**
     * Returns the
     * @return
     */
    public ObservableList<Trainee> getTraineesAsObservableList(){

        ObservableList<Trainee> traineeList = FXCollections.observableArrayList();
        if (currentTrainees.size() > 0)
            traineeList.addAll(currentTrainees);
        return traineeList;

    }

    //Getters
    public int getCurrentYear() { return currentYear; }
    public int getCurrentSession(){return currentSession;}
    public DBManager getDBManager(){return dbManager;}
    public Vector<Trainee> getCurrentTrainees() {return currentTrainees;}

    //Setters
    public void setCurrentYear(int year){currentYear = year;}
    public void setCurrentSession(int session){currentSession = session;}

}

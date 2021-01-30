package org.openjfx.controller;

import javafx.util.Pair;

public class Controller {

    private DBManager dbManager;

    private int currentYear;
    private int currentSession;

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

    }

    //Getters

    public int getCurrentYear() { return currentYear; }

    public int getCurrentSession(){return currentSession;}

    public DBManager getDBManager(){return dbManager;}

    //Setters

    public void setCurrentYear(int year){currentYear = year;}

    public void setCurrentSession(int session){currentSession = session;}


}

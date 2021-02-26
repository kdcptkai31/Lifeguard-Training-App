package org.openjfx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.openjfx.model.Comment;
import org.openjfx.model.Session;
import org.openjfx.model.Trainee;

import java.util.Comparator;
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
        class SortByLastName implements Comparator<Trainee>{
            @Override
            public int compare(Trainee o1, Trainee o2) {
                return o1.getLastName().compareTo(o2.getLastName());
            }
        }
        currentTrainees = DBManager.getAllTraineesFromSession(currentSession.getYear(), currentSession.getSession());
        currentTrainees.sort(new SortByLastName());

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
    public ObservableList<String> getTraineeNamesAsObservableList(){

        ObservableList<String> traineeList = FXCollections.observableArrayList();
        if (currentTrainees.size() < 1)
            return null;

        for(Trainee trainee : currentTrainees){

            StringBuilder tmpStr = new StringBuilder();
            if(trainee.isQuestionnaire2Complete() && trainee.isDisabled())
                tmpStr.append("* ");

            if(trainee.getMiddleName() != null)
                traineeList.add(tmpStr.append(trainee.getFirstName() + " " + trainee.getMiddleName() + " " + trainee.getLastName()).toString());
            else
                traineeList.add(tmpStr.append(trainee.getFirstName() + " " + trainee.getLastName()).toString());

        }

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

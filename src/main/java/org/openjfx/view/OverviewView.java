package org.openjfx.view;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import org.openjfx.controller.Controller;
import org.openjfx.controller.DBManager;
import org.openjfx.controller.LifeguardTrainingApplication;
import org.openjfx.model.*;

import java.io.IOException;
import java.util.*;

public class OverviewView {

    private Controller controller;

    @FXML
    private Label yearLabel;
    @FXML
    private Label sessionLabel;
    @FXML
    private Label datesLabel;

    //Body OBJECTS
    @FXML
    private ListView<String> traineePlacementListView;
    private Vector<Trainee> traineeVector;
    private Vector<Double> traineeTotalScores;

    @FXML
    private ListView<Test> testListView;
    @FXML
    private ListView<Event> eventListView;

    @FXML
    private TableView<TableData> enterInfoTableView;
    @FXML
    private TableColumn<TableData, String> traineeColumn;
    @FXML
    private TableColumn<TableData, String> scoreColumn;
    @FXML
    private Label addScoresErrorLabel;
    @FXML
    private Button saveScoresButton;

    @FXML
    protected void initialize(){

        traineeVector = new Vector<>();
        traineeTotalScores = new Vector<>();
        controller = LifeguardTrainingApplication.getController();
        controller.updateCurrentSession(controller.getCurrentSession());

        enterInfoTableView.setPlaceholder(new Label("Select a Test or Event"));
        enterInfoTableView.requestFocus();
        enterInfoTableView.layout();

        traineeColumn.setCellValueFactory(new PropertyValueFactory<>("traineeName"));
        scoreColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        //Action event where the user presses enter to enter that row's score/place, and it increments the edit focus
        //until the end of the list.
        scoreColumn.setOnEditCommit((TableColumn.CellEditEvent<TableData, String> t) -> {

            t.getTableView().getItems().get(t.getTablePosition().getRow()).setScore(t.getNewValue());
            int index = enterInfoTableView.getSelectionModel().getSelectedIndex();
            if(index + 1 < controller.getCurrentTrainees().size()) {
                Platform.runLater(() -> {

                    enterInfoTableView.getSelectionModel().select(index + 1);
                    enterInfoTableView.edit(index + 1, scoreColumn);

                });
            }

        });

        refresh();

    }

    /**
     * Refreshes the page with current data.
     */
    private void refresh(){

        yearLabel.setText(String.valueOf(controller.getCurrentSession().getYear()));
        sessionLabel.setText("Session " + controller.getCurrentSession().getSession());
        datesLabel.setText(controller.getCurrentSession().getStartDate() + " - " + controller.getCurrentSession().getEndDate());

        enterInfoTableView.getItems().clear();
        saveScoresButton.setVisible(false);
        addScoresErrorLabel.setVisible(false);

        //Fill Placement List//////////////////////////////////////////////////////////////////////////////////////////
        traineeVector = DBManager.getAllTraineesFromSession(controller.getCurrentSession().getYear(),
                                                            controller.getCurrentSession().getSession());
        int numberOfEvents = DBManager.getAllEventsFromSession(controller.getCurrentSession().getYear(),
                                                                controller.getCurrentSession().getSession()).size();
        traineeTotalScores = new Vector<>();
        Vector<Pair<Integer, Double>> averagePlacement = new Vector<>();
        int counter = 0;
        for(Trainee trainee : traineeVector){

            double totalScore = 0;
            Vector<TestScore> traineeTestScores = DBManager.getAllTestScoresFromTraineeID(trainee.getId());
            Vector<EventScore> traineeEventScores = DBManager.getAllEventScoresFromTraineeID(trainee.getId());
            if(!traineeTestScores.isEmpty()) {
                for (TestScore score : traineeTestScores)
                    totalScore += score.getScore();

            }
            traineeTotalScores.add(totalScore);
            int totalPlace = 0;
            if(!traineeEventScores.isEmpty()) {
                for (EventScore score : traineeEventScores)
                    totalPlace += score.getPlace();

            }
            if(traineeEventScores.size() != 0){
                averagePlacement.add(new Pair<>(counter, totalPlace * 1.0 / traineeEventScores.size()));
            }
            counter++;

        }

        averagePlacement.sort(new SortByAveragePlacement());
        Collections.reverse(averagePlacement);

        int num = 100;
        for(Pair<Integer, Double> pair : averagePlacement) {

            traineeTotalScores.setElementAt(traineeTotalScores.get(pair.getKey()) + num, pair.getKey());
            num--;

        }

        Vector<Pair<Trainee, Double>> sortTraineeVector = new Vector<>();
        for(int i = 0; i < traineeVector.size(); i++)
            sortTraineeVector.add(new Pair<>(traineeVector.get(i), traineeTotalScores.get(i)));

        sortTraineeVector.sort(Comparator.comparing(p -> -p.getValue()));
        ObservableList<String> placingStringList = FXCollections.observableArrayList();
        counter = 1;
        for(Pair<Trainee, Double> pair : sortTraineeVector){

            placingStringList.add(counter + ". " + pair.getKey().getFullName() + " | Points: " + pair.getValue());
            counter++;

        }

        traineePlacementListView.setItems(placingStringList);
        //END Fill Placement List//////////////////////////////////////////////////////////////////////////////////////

        Vector<Test> displayTests = controller.getCurrentTests();
        displayTests.removeIf(Test::isScored);
        testListView.setItems(FXCollections.observableArrayList(displayTests));

        Vector<Event> displayEvents = controller.getCurrentEvents();
        displayEvents.removeIf(Event::isScored);
        eventListView.setItems(FXCollections.observableArrayList(displayEvents));
    }

    /**
     * Fills the table view will all eligible trainees and sets score column to "Score"
     */
    public void onTestListViewClicked(){

        int selectedIndex = testListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1)
            return;

        eventListView.getSelectionModel().clearSelection();
        scoreColumn.setText("Score");
        saveScoresButton.setText("Save Scores");
        ObservableList<TableData> data = FXCollections.observableArrayList();
        for(Trainee trainee: controller.getCurrentTrainees())
            data.add(new TableData(trainee.getFullName(), "0"));

        enterInfoTableView.setItems(data);
        saveScoresButton.setVisible(true);


    }

    /**
     * Fills the tableview with all eligible trainees and sets score column to "Place"
     */
    public void onEventListViewClicked(){

        int selectedIndex = eventListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1)
            return;

        testListView.getSelectionModel().clearSelection();
        scoreColumn.setText("Place");
        saveScoresButton.setText("Save Placements");
        ObservableList<TableData> data = FXCollections.observableArrayList();
        for(Trainee trainee: controller.getCurrentTrainees()){
            data.add(new TableData(trainee.getFullName(), "0"));
        }

        enterInfoTableView.setItems(data);
        saveScoresButton.setVisible(true);

    }

    /**
     * Checks if scores/placements are valid, saves and refreshes page if so
     */
    public void onSaveScoresClicked(){

        //Save Test Scores
        if(eventListView.getSelectionModel().getSelectedIndex() == -1){

            int maxTestScore = testListView.getSelectionModel().getSelectedItem().getPoints();

            //Verify Input Scores
            for(int i = 0; i < controller.getCurrentTrainees().size(); i++){

                String tmpCheck = scoreColumn.getCellObservableValue(i).getValue();
                if(!isInteger(tmpCheck) || Integer.parseInt(tmpCheck) < 0 || Integer.parseInt(tmpCheck) > maxTestScore){

                    addScoresErrorLabel.setText("*ERROR* Enter valid scores less than or equal to " + maxTestScore);
                    addScoresErrorLabel.setVisible(true);
                    return;

                }

            }

            Test selectedTest = testListView.getSelectionModel().getSelectedItem();

            for(int i = 0; i < controller.getCurrentTrainees().size(); i++)
                DBManager.addTestScore(new TestScore(selectedTest.getTestID(),
                                                     controller.getCurrentTrainees().elementAt(i).getId(),
                                                   Integer.parseInt(scoreColumn.getCellObservableValue(i).getValue())));

            DBManager.testScoredUpdate(selectedTest);
            controller.updateCurrentTests();
            controller.updateCurrentTrainees();
            enterInfoTableView.setPlaceholder(new Label("SUCCESS - Scores Added"));
            refresh();
            return;

        }

        //OR

        //Save Event Placements
        if(testListView.getSelectionModel().getSelectedIndex() == -1){

            int maxPlacement = controller.getCurrentTrainees().size();

            //Verify Input Placements
            for(int i = 0; i < controller.getCurrentTrainees().size(); i++){

                String tmpCheck = scoreColumn.getCellObservableValue(i).getValue();
                if(!isInteger(tmpCheck) || Integer.parseInt(tmpCheck) < 1 || Integer.parseInt(tmpCheck) > maxPlacement){

                    addScoresErrorLabel.setText("*ERROR* Enter valid places less than or equal to " + maxPlacement);
                    addScoresErrorLabel.setVisible(true);
                    return;

                }

            }

            Event selectedEvent = eventListView.getSelectionModel().getSelectedItem();

            for(int i = 0; i < controller.getCurrentTrainees().size(); i++)
                DBManager.addEventScore(new EventScore(selectedEvent.getEventID(),
                                                       controller.getCurrentTrainees().elementAt(i).getId(),
                                                   Integer.parseInt(scoreColumn.getCellObservableValue(i).getValue())));

            DBManager.eventScoredUpdate(selectedEvent);
            controller.updateCurrentEvents();
            controller.updateCurrentTrainees();
            enterInfoTableView.setPlaceholder(new Label("SUCCESS - Places Added"));
            refresh();

        }

    }

    /**
     * Handler for clicking this button.
     */
    public void onOverviewButtonClicked(){/*Nothing, already on the overview page*/}

    /**
     * Navigates to the Edit/Import page.
     */
    public void onEditImportClicked(){

        try {
            LifeguardTrainingApplication.getCoordinator().showEditImportScene();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Navigates to the Reports page.
     */
    public void onReportsClicked(){

        try {
            LifeguardTrainingApplication.getCoordinator().showReportsScene();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Prompts user to pick a year session pair, then loads it if valid.
     */
    public void onChangeSessionClicked(){

        //Initialize Dialog box contents
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(LifeguardTrainingApplication.getCoordinator().getStage());
        VBox dialogVBox = new VBox();
        Label label = new Label("Select the year and session:");
        ListView<String> sessionListView = new ListView<>();
        ObservableList<String> sessionObList = FXCollections.observableArrayList();
        Button sessionButton = new Button("Load Session");
        Button cancelButton = new Button("Cancel");
        HBox buttonHBox = new HBox(sessionButton, cancelButton);

        //Make and sort session list
        Vector<Session> sessions = DBManager.getAllSessions();
        if(sessions == null)
            return;

        sessions.sort(new SortByNewestSession());
        for(Session session: sessions)
            sessionObList.add("Year: " + session.getYear() + " Session: " + session.getSession() + " | " +
                    session.getStartDate() + " - " + session.getEndDate());

        sessionListView.setItems(sessionObList);
        sessionListView.getSelectionModel().select(0);
        dialogVBox.getChildren().addAll(label, sessionListView, buttonHBox);

        //Button Events
        sessionButton.setOnMouseClicked(event -> {

            if(sessionListView.getSelectionModel().isEmpty())
                dialog.close();

            int index = sessionListView.getSelectionModel().getSelectedIndex();
            if(sessions.get(index).getYear() == controller.getCurrentSession().getYear() &&
                    sessions.get(index).getSession() == controller.getCurrentSession().getSession())
                dialog.close();

            controller.updateCurrentSession(sessions.get(index));
            dialog.close();
            refresh();

        });
        cancelButton.setOnMouseClicked(event -> dialog.close());

        //Set dialog box style
        buttonHBox.setSpacing(10);
        buttonHBox.setAlignment(Pos.CENTER);
        dialogVBox.setAlignment(Pos.CENTER);
        dialogVBox.setSpacing(10);
        VBox.setMargin(label, new Insets(10, 0, 0, 0));
        VBox.setMargin(buttonHBox, new Insets(10));
        VBox.setMargin(sessionListView, new Insets(0, 5, 0, 5));

        Scene dialogScene = new Scene(dialogVBox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();

    }

    /**
     * Zoom in effect on Year and Session HBox
     */
    public void onMouseEnteredSession(){

        yearLabel.setScaleX(1.2);
        yearLabel.setScaleY(1.2);
        sessionLabel.setScaleX(1.2);
        sessionLabel.setScaleY(1.2);

    }

    /**
     * Zoom out effect on Year and Session HBox
     */
    public void onMouseExitedSession(){

        yearLabel.setScaleX(1);
        yearLabel.setScaleY(1);
        sessionLabel.setScaleX(1);
        sessionLabel.setScaleY(1);

    }

    /**
     * Used to sort the sessions by the newest session.
     */
    class SortByNewestSession implements Comparator<Session> {
        @Override
        public int compare(Session o1, Session o2) {

            int yearDiff = o1.getYear() - o2.getYear();
            if(yearDiff == 0)
                return -1 * (o1.getSession() - o2.getSession());
            else
                return -1 * yearDiff;
        }
    }

    class SortByAveragePlacement implements Comparator<Pair<Integer, Double>>{
        @Override
        public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {

            if(o1.getValue() == 0 && o2.getValue() != 0)
                return -1;
            else if(o2.getValue() == 0 && o1.getValue() != 0)
                return 1;
            else if(o1.getValue() == 0 && o2.getValue() == 0)
                return 0;

            return ((int)Math.round(o1.getValue() - o2.getValue()) * -1);

        }
    }

    /**
     * Used to hold the table view data
     */
    public class TableData{

        SimpleStringProperty traineeName;
        SimpleStringProperty score;

        TableData(String traineeName, String score){

            this.traineeName = new SimpleStringProperty(traineeName);
            this.score = new SimpleStringProperty(score);

        }

        //Getters
        public String getTraineeName(){return traineeName.get();}
        public String getScore(){return score.get();}
        //Setters
        public void setTraineeName(String name){traineeName.set(name);}
        public void setScore(String sco){score.set(sco);}

    }

    /**
     * Checks if the given string is an integer
     * @param s
     * @return
     */
    public static boolean isInteger(String s) {

        if(s.equals(""))
            return false;

        s = s.trim();

        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        }

        return true;
    }

}

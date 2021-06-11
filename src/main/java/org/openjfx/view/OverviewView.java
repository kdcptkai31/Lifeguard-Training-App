package org.openjfx.view;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

import static org.apache.commons.lang3.math.NumberUtils.isCreatable;

import org.openjfx.controller.Controller;
import org.openjfx.controller.DBManager;
import org.openjfx.controller.LifeguardTrainingApplication;
import org.openjfx.model.*;

/**
 * Handles mapping the Overview fxml UI file to methods.
 */
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
    private Button finalEvalButton;
    @FXML
    private Label finalEvalErrorLabel;
    private int evalIndex;
    private Vector<Pair<String, Integer>> finalEvals;

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
    private Label attendanceDayLabel;
    @FXML
    private TableView<TableData> enterHoursTableView;
    @FXML
    private TableColumn<TableData, String> traineeHoursColumn;
    @FXML
    private TableColumn<TableData, String> hoursColumn;
    @FXML
    private Label addHoursErrorLabel;

    /**
     * Constructor that initializes the current session data.
     */
    @FXML
    protected void initialize(){

        traineeVector = new Vector<>();
        traineeTotalScores = new Vector<>();
        finalEvals = new Vector<>();
        controller = LifeguardTrainingApplication.getController();
        controller.updateCurrentSession(controller.getCurrentSession());

        enterInfoTableView.setPlaceholder(new Label("Select a Test or Event"));
        enterInfoTableView.requestFocus();
        enterInfoTableView.layout();

        traineeColumn.setCellValueFactory(new PropertyValueFactory<>("traineeName"));
        scoreColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        scoreColumn.setMaxWidth(50);
        scoreColumn.setMinWidth(50);

//        testListView.setCellFactory(stringListView -> new CenteredTestListViewCell());
//        eventListView.setCellFactory(stringListView -> new CenteredEventListViewCell());

        enterHoursTableView.requestFocus();
        enterHoursTableView.layout();
        traineeHoursColumn.setCellValueFactory(new PropertyValueFactory<>("traineeName"));
        hoursColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        hoursColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        hoursColumn.setMinWidth(60);
        hoursColumn.setMaxWidth(60);

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

        //Action event where the user presses enter to enter that row's hours and it increments the edit focus until the
        //end of the list.
        hoursColumn.setOnEditCommit((TableColumn.CellEditEvent<TableData, String> t) -> {

            t.getTableView().getItems().get(t.getTablePosition().getRow()).setScore(t.getNewValue());
            int index = enterHoursTableView.getSelectionModel().getSelectedIndex();
            if(index + 1 < controller.getCurrentTrainees().size()){

                Platform.runLater(() -> {

                    enterHoursTableView.getSelectionModel().select(index + 1);
                    enterHoursTableView.edit(index + 1, hoursColumn);

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
        finalEvals.clear();
        evalIndex = 0;

        enterInfoTableView.getItems().clear();
        enterHoursTableView.getItems().clear();
        saveScoresButton.setVisible(false);
        addScoresErrorLabel.setVisible(false);
        addHoursErrorLabel.setVisible(false);
        finalEvalErrorLabel.setVisible(false);

        //Fill Placement List//////////////////////////////////////////////////////////////////////////////////////////
        traineeVector = DBManager.getAllTraineesFromSession(controller.getCurrentSession().getYear(),
                                                            controller.getCurrentSession().getSession());

        traineeTotalScores = new Vector<>();
        Vector<Pair<Integer, Double>> averagePlacement = new Vector<>();
        int counter = 0;
        for(Trainee trainee : Objects.requireNonNull(traineeVector)){

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
                    if(score.getPlace() == 0)
                        totalPlace += traineeVector.size();
                    else
                        totalPlace += score.getPlace();

            }
            if(traineeEventScores.size() != 0){
                averagePlacement.add(new Pair<>(counter, totalPlace * 1.0 / traineeEventScores.size()));
            }
            counter++;

        }

        averagePlacement.sort(new SortByAveragePlacement());
        Collections.reverse(averagePlacement);

        List<Pair<Integer, Double>> tmpList = averagePlacement.stream()
                .filter(b -> !traineeVector.get(b.getKey()).isActive())
                .collect(Collectors.toList());
        averagePlacement.removeAll(tmpList);
        averagePlacement.addAll(tmpList);

        int num = 100;
        for(Pair<Integer, Double> pair : averagePlacement) {

            traineeTotalScores.setElementAt(traineeTotalScores.get(pair.getKey()) + num, pair.getKey());
            num--;

        }

        Vector<Pair<Trainee, Double>> sortTraineeVector = new Vector<>();
        for(int i = 0; i < traineeVector.size(); i++)
            sortTraineeVector.add(new Pair<>(traineeVector.get(i), traineeTotalScores.get(i)));

        sortTraineeVector.removeIf(b -> !b.getKey().isActive());

        //Take into account the final evaluation score modifications, if applicable
        if(controller.getCurrentSession().getCurrentDay() == 10){

            counter = 0;
            for(Pair<Trainee, Double> pair : sortTraineeVector){

                Vector<Comment> traineeComments = new Vector<>(controller.getCurrentComments());
                traineeComments.removeIf(comment -> !comment.getIncidentType().equals("Final Evaluation"));
                for(Comment comment : traineeComments){

                    if(comment.getTraineeID() == pair.getKey().getId()){

                        Double tmpDouble = pair.getValue() + Integer.parseInt(comment.getInstructorActions());
                        sortTraineeVector.set(counter, new Pair<>(pair.getKey(), tmpDouble));
                        break;

                    }
                }
                counter++;
            }
        }

        sortTraineeVector.sort(Comparator.comparing(p -> -p.getValue()));
        ObservableList<String> placingStringList = FXCollections.observableArrayList();
        counter = 1;
        for(Pair<Trainee, Double> pair : sortTraineeVector){

            placingStringList.add(counter + ". " + pair.getKey().getFirstName() + " " + pair.getKey().getLastName()
                                    + " | Points: " + pair.getValue());
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


        if(controller.getCurrentSession().getCurrentDay() > 8){

            enterHoursTableView.setPlaceholder(new Label("All Done!"));
            attendanceDayLabel.setText("Attendance is Complete");

        }else{

            ObservableList<TableData> hoursTableData = FXCollections.observableArrayList();
            for(Trainee trainee: controller.getCurrentTrainees())
                hoursTableData.add(new TableData(trainee.getFirstName() + " "+ trainee.getLastName(), "10"));

            enterHoursTableView.setItems(hoursTableData);
            attendanceDayLabel.setText("Add Day " + controller.getCurrentSession().getCurrentDay() + " Attendance");

        }

        if(controller.getCurrentSession().getCurrentDay() == 10)
            finalEvalButton.setVisible(false);

    }

    /**
     * If ready, the user will be prompted to enter final comments, as well as a final score adjustment, for each
     * active trainee.
     */
    public void onFinalEvaluationsClicked(){

        if(controller.getCurrentSession().getCurrentDay() < 9) {
            finalEvalErrorLabel.setVisible(true);
            return;
        }

        for(Trainee ignored : controller.getCurrentTrainees())
            finalEvals.add(new Pair<>("", 0));
        System.out.print("HERE");
        if(finalEvals.size() == 0)
            return;

        System.out.print("HERE");

        final Stage dialog = new Stage();
        dialog.setTitle("Final Evaluations");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(LifeguardTrainingApplication.getCoordinator().getStage());
        VBox dialogVBox = new VBox();
        dialogVBox.setStyle("-fx-background: #3476f7;");
        dialogVBox.setAlignment(Pos.CENTER);
        dialogVBox.setSpacing(10);

        Label commentLabel = new Label(controller.getCurrentTrainees().get(0).getFullName() + "'s");
        commentLabel.setStyle("-fx-text-fill: #efb748; -fx-font-size: 16; -fx-font-weight: bold;");
        commentLabel.setAlignment(Pos.CENTER);
        Label message = new Label("Final Evaluation Comment:");
        message.setStyle("-fx-text-fill: #efb748; -fx-font-size: 16; -fx-font-weight: bold;");
        message.setAlignment(Pos.CENTER);

        TextArea evalComment = new TextArea();
        evalComment.setMaxSize(300, 200);
        evalComment.setWrapText(true);

        Label pointsLabel = new Label("Final Point Adjustment (negative number to subtract)");
        pointsLabel.setStyle("-fx-text-fill: #efb748; -fx-font-size: 14; -fx-font-weight: bold;");

        TextField pointValueTextField = new TextField();
        pointValueTextField.setPromptText(String.valueOf(finalEvals.get(0).getValue()));
        pointValueTextField.setMaxWidth(50);
        pointValueTextField.setAlignment(Pos.CENTER);

        Label errorLabel = new Label("*Error - Enter Valid Data*");
        errorLabel.setStyle("-fx-text-fill: #57ff8c; -fx-font-size: 12;");
        errorLabel.setVisible(false);

        Button backButton = new Button("<- Back");
        Button forwardButton = new Button("Next ->");
        if(finalEvals.size() == 1)
            forwardButton.setText("*SAVE ALL*");

        forwardButton.setOnMouseClicked(e -> forwardButtonClicked(commentLabel, evalComment, pointValueTextField,
                                                                  forwardButton, errorLabel, dialog));
        backButton.setOnMouseClicked(e -> backButtonClicked(commentLabel, evalComment, pointValueTextField,
                                                            forwardButton, errorLabel));

        HBox buttonHBox = new HBox(backButton, forwardButton);
        buttonHBox.setAlignment(Pos.CENTER);
        buttonHBox.setSpacing(50);

        dialogVBox.getChildren().addAll(commentLabel, message, evalComment, pointsLabel, pointValueTextField, errorLabel,
                                        buttonHBox);
        Scene dialogScene = new Scene(dialogVBox, 400, 400);
        dialog.setScene(dialogScene);
        dialog.show();

    }

    /**
     * Used in final evaluations to move to the next trainee's eval. If they are done, this will act as the save button
     * (see the first if statement for this).
     * @param commentLabel
     * @param evalComment
     * @param pointValueTextField
     * @param forwardButton
     * @param errorLabel
     * @param dialogWindow
     */
    private void forwardButtonClicked(Label commentLabel, TextArea evalComment, TextField pointValueTextField,
                                      Button forwardButton, Label errorLabel, Stage dialogWindow){

        //Save Evaluations
        if(evalIndex + 1 == controller.getCurrentTrainees().size()){

            //Verify entered data
            if(!evalComment.getText().isEmpty()){

                String tmp;
                if(pointValueTextField.getText().isEmpty())
                    tmp = pointValueTextField.getPromptText();
                else{

                    if(!isNotInteger(pointValueTextField.getText()))
                        tmp = pointValueTextField.getText();
                    else{
                        errorLabel.setVisible(true);
                        return;
                    }

                }

                finalEvals.set(evalIndex, new Pair<>(evalComment.getText(), Integer.parseInt(tmp)));

            }else{
                errorLabel.setVisible(true);
                return;
            }

            //Save the evaluations
            Session currentSes = new Session(controller.getCurrentSession());
            int counter = 0;
            for(Trainee trainee : controller.getCurrentTrainees()){

                Pair<String, Integer> currentEval = finalEvals.get(counter);


                //Makes a special type of comment, see Comment.java for constructor details
                DBManager.addComment(new Comment(trainee.getId(), currentSes.getEndDate(),
                                                 trainee.getFullName(), currentEval.getKey(),
                                                 String.valueOf(currentEval.getValue()), currentSes.getYear(),
                                                 currentSes.getSession()));

                counter++;

            }

            currentSes.setCurrentDay(currentSes.getCurrentDay() + 1);
            dialogWindow.close();
            DBManager.updateSessionDay(currentSes);
            controller.updateCurrentSession(currentSes);
            refresh();

            return;


            //Prep Button for Save Label
        }else if(evalIndex + 2 == controller.getCurrentTrainees().size())
            forwardButton.setText("*SAVE ALL*");

        //Verify entered data
        if(!evalComment.getText().isEmpty()){

            String tmp;
            if(pointValueTextField.getText().isEmpty())
                tmp = pointValueTextField.getPromptText();
            else{
                if(!isNotInteger(pointValueTextField.getText()))
                    tmp = pointValueTextField.getText();
                else{
                    errorLabel.setVisible(true);
                    return;
                }
            }

            finalEvals.set(evalIndex, new Pair<>(evalComment.getText(), Integer.parseInt(tmp)));

        }else{
            errorLabel.setVisible(true);
            return;
        }

        evalIndex++;
        commentLabel.setText(controller.getCurrentTrainees().get(evalIndex).getFullName() + "'s");
        evalComment.setText(finalEvals.get(evalIndex).getKey());
        pointValueTextField.setPromptText(String.valueOf(finalEvals.get(evalIndex).getValue()));
        pointValueTextField.clear();
        errorLabel.setVisible(false);

    }


    /**
     * Used in final evaluations to move to the previous trainee's eval.
     * @param commentLabel
     * @param evalComment
     * @param pointValueTextField
     * @param forwardButton
     * @param errorLabel
     */
    private void backButtonClicked(Label commentLabel, TextArea evalComment, TextField pointValueTextField,
                                   Button forwardButton, Label errorLabel){

        if(evalIndex < 1)
            return;

        if(evalIndex + 1 == controller.getCurrentTrainees().size())
            forwardButton.setText("Next ->");

        //Verify entered data
        if(!evalComment.getText().isEmpty() || !pointValueTextField.getPromptText().equals("0")){

            String tmp;
            if(pointValueTextField.getText().isEmpty())
                tmp = pointValueTextField.getPromptText();
            else{
                if(!isNotInteger(pointValueTextField.getText()))
                    tmp = pointValueTextField.getText();
                else {
                    errorLabel.setVisible(true);
                    return;
                }
            }

            finalEvals.set(evalIndex, new Pair<>(evalComment.getText(), Integer.parseInt(tmp)));

        }

        evalIndex--;
        commentLabel.setText(controller.getCurrentTrainees().get(evalIndex).getFullName() + "'s");
        evalComment.setText(finalEvals.get(evalIndex).getKey());
        pointValueTextField.setPromptText(String.valueOf(finalEvals.get(evalIndex).getValue()));
        pointValueTextField.clear();
        errorLabel.setVisible(false);

    }

    /**
     * Validates then saves the entered or automatically used hour values, increments the Session's currentDay.
     */
    public void onSaveHoursClicked(){

        //Validate Entries
        Set<Double> hourValues = new HashSet<>();
        for(int i = 0; i < controller.getCurrentTrainees().size(); i++){

            String tmpCheck =  hoursColumn.getCellObservableValue(i).getValue();
            if(!isCreatable(tmpCheck) || Double.parseDouble(tmpCheck) < 1){
                addHoursErrorLabel.setVisible(true);
                return;
            }
            hourValues.add(Double.parseDouble(tmpCheck));

        }

        //Initialize Dialog box contents
        final Stage dialog = new Stage();
        dialog.setTitle("Add Attendance Hours");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(LifeguardTrainingApplication.getCoordinator().getStage());
        VBox dialogVBox = new VBox();
        dialogVBox.setStyle("-fx-background: #3476f7;");
        dialogVBox.setAlignment(Pos.CENTER);
        dialogVBox.setSpacing(10);
        Label cautionMessage = new Label(" WAIT! Make sure to import today's\n       comments before you take\n                   attendance!");
        cautionMessage.setStyle("-fx-text-fill: #efb748; -fx-font-size: 16; -fx-font-weight: bold;");
        Label message = new Label();
        message.setText("The highest hours to be added is " + Collections.max(hourValues) + ".\n              The minimum is " +
                        Collections.min(hourValues) + ".\n      If this is correct, click Apply.");
        message.setStyle("-fx-text-fill: #efb748; -fx-font-size: 14; -fx-font-weight: bold;");
        HBox buttonHBox = new HBox();
        buttonHBox.setAlignment(Pos.CENTER);
        buttonHBox.setSpacing(10);
        Button applyHoursButton = new Button("Apply");
        applyHoursButton.setStyle("-fx-font-weight: bold");

        //Saves the data and increments the currentDay value of the session.
        applyHoursButton.setOnMouseClicked(event -> {

            int counter = 0;
            for(Trainee trainee : controller.getCurrentTrainees()){

                trainee.setHoursAttended(trainee.getHoursAttended() + Double.parseDouble(hoursColumn.getCellObservableValue(counter).getValue()));
                DBManager.updateTraineeHours(trainee);
                counter++;

            }

            Session tmp = controller.getCurrentSession();
            tmp.setCurrentDay(tmp.getCurrentDay() + 1);
            DBManager.updateSessionDay(tmp);
            controller.setCurrentSession(tmp);
            controller.updateCurrentTrainees();
            refresh();
            dialog.close();

        });

        Button cancelHoursButton = new Button("Cancel");
        cancelHoursButton.setOnMouseClicked(event -> {dialog.close();});

        buttonHBox.getChildren().addAll(applyHoursButton, cancelHoursButton);
        dialogVBox.getChildren().addAll(cautionMessage, message, buttonHBox);
        Scene dialogScene = new Scene(dialogVBox, 275, 200);
        dialog.setScene(dialogScene);
        dialog.show();

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
            data.add(new TableData(trainee.getFirstName() + " " + trainee.getLastName(), "0"));

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
            data.add(new TableData("#" + trainee.getCapNumber() + " " + trainee.getFirstName() + " " +
                                    trainee.getLastName(), "0"));
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
                if(isNotInteger(tmpCheck) || Integer.parseInt(tmpCheck) < 0 || Integer.parseInt(tmpCheck) > maxTestScore){

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

                if(isNotInteger(tmpCheck) || Integer.parseInt(tmpCheck) < 0 || Integer.parseInt(tmpCheck) > maxPlacement){

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

        Scene dialogScene = new Scene(dialogVBox, 300, 400);
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

    public static class SortByAveragePlacement implements Comparator<Pair<Integer, Double>>{
        @Override
        public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {

            if(o1.getValue() == 0 && o2.getValue() != 0)
                return -1;
            else if(o2.getValue() == 0 && o1.getValue() != 0)
                return 1;
            else if(o1.getValue() == 0 && o2.getValue() == 0)
                return 0;

            return ((int)Math.ceil(o1.getValue() - o2.getValue()) * -1);

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
    public static boolean isNotInteger(String s) {

        if(s.equals(""))
            return true;

        s = s.trim();

        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return true;
        }

        return false;
    }

}

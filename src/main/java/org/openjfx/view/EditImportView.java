package org.openjfx.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javafx.fxml.FXML;

import javafx.stage.FileChooser;

import org.openjfx.controller.Controller;
import org.openjfx.controller.DBManager;
import org.openjfx.controller.LifeguardTrainingApplication;
import org.openjfx.model.Comment;
import org.openjfx.model.EmergencyContact;
import org.openjfx.model.Session;
import org.openjfx.model.Trainee;

import java.io.*;
import java.util.*;

public class EditImportView {

    Controller controller;

    @FXML
    private Label yearLabel;
    @FXML
    private Label sessionLabel;
    @FXML
    private Label datesLabel;

    //Import Data
    @FXML
    private ComboBox<String> importComboBox;

    //Show Trainees
    @FXML
    private ListView<Trainee> traineeListView;

    @FXML
    protected void initialize() {

        controller = LifeguardTrainingApplication.getController();
        refresh();

        //Sets up listeners
        importComboBox.getItems().addAll("Choose Import Type", "Comments", "Trainee Information", "Questionnaire 1",
                "Questionnaire 2");
        importComboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.intValue()){

                //Comments picked
                case 1: onAddCommentsClicked();
                    break;
                //Trainee Info picked
                case 2: onAddTraineesClicked();
                    break;
                //Questionnaire 1 Picked
                case 3: onAddQ1Clicked();
                    break;
                //Questionnaire 2 Picked
                case 4: onAddQ2Clicked();
                    break;
                default:break;
            }
        });

    }

    /**
     * Refreshes the page's data with the most current data from Controller.
     */
    private void refresh() {

        yearLabel.setText(String.valueOf(controller.getCurrentSession().getYear()));
        sessionLabel.setText("Session " + controller.getCurrentSession().getSession());
        datesLabel.setText(controller.getCurrentSession().getStartDate() + " - " + controller.getCurrentSession().getEndDate());
        traineeListView.setItems(controller.getTraineesAsObservableList());

    }

    /**
     * Save the comment .csv data to the db if valid.
     */
    private void onAddCommentsClicked(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(LifeguardTrainingApplication.getCoordinator().getStage());
        if(selectedFile == null){

            importComboBox.getSelectionModel().selectFirst();
            return;

        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to load " +
                selectedFile.getName() + " into the Comments database?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        //If this was a mistake, leave, if not, continue
        if (alert.getResult() == ButtonType.CANCEL) {

            importComboBox.getSelectionModel().selectFirst();
            return;

        }

        List<Comment> tmpComments = new ArrayList<>();
        //Handle Comments
        try{

            CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
            CSVParser parser = new CSVParser(new FileReader(selectedFile), format);
            //Checks that only the initial trainee .csv is being read
            Vector<CSVRecord> records = new Vector<>(parser.getRecords());
            if(records.get(0).size() != 9)
                throw new Exception("Wrong File");

            for(CSVRecord record : records)
                tmpComments.add(new Comment(record.get(1), record.get(2), record.get(3), record.get(4), record.get(5),
                                            record.get(6), record.get(7), record.get(8),
                                            controller.getCurrentSession().getYear(),
                                            controller.getCurrentSession().getSession()));

            //Adds to db if does not exist
            for(Comment comment : tmpComments){

                boolean isFound = false;
                for(Comment storedComment : controller.getCurrentComments()){

                    if(comment.getDate().equals(storedComment.getDate()) &&
                        comment.getTraineeName().equals(storedComment.getTraineeName()) &&
                        comment.getInstructorName().equals(storedComment.getInstructorName()) &&
                        comment.getIncidentDescription().equals(storedComment.getIncidentDescription())) {

                        isFound = true;
                        break;

                    }

                }

                if(!isFound)
                    DBManager.addComment(comment);

            }

            controller.updateCurrentComments();
            importComboBox.getSelectionModel().selectFirst();
            refresh();


        }catch (Exception e){

            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR, "There was a problem loading " +
                    selectedFile.getName() + " into the Comment database.\nDouble check you selected the right file.",
                    ButtonType.CLOSE);
            error.showAndWait();
            importComboBox.getSelectionModel().selectFirst();

        }

    }

    /**
     * Saves the trainee info .csv file to the database, if valid.
     */
    private void onAddTraineesClicked() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(LifeguardTrainingApplication.getCoordinator().getStage());
        if (selectedFile == null) {

            importComboBox.getSelectionModel().selectFirst();
            return;

        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to load " +
                selectedFile.getName() + " into the Trainee database?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        //If this was a mistake, leave, if not, continue
        if (alert.getResult() == ButtonType.CANCEL) {

            importComboBox.getSelectionModel().selectFirst();
            return;

        }

        List<Trainee> tmpTrainees = new ArrayList<>();
        //Handle Trainees
        try {

            CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
            CSVParser parser = new CSVParser(new FileReader(selectedFile), format);
            //Checks that only the initial trainee .csv is being read
            Vector<CSVRecord> records = new Vector<>(parser.getRecords());
            if (records.get(0).size() != 20)
                throw new Exception("Wrong File");

            for (CSVRecord record : records)
                tmpTrainees.add(new Trainee(record.get(2), record.get(3), record.get(4), record.get(5), record.get(6),
                        record.get(7), record.get(8), record.get(9), record.get(10),
                        record.get(11).charAt(0) == 'Y',
                        new EmergencyContact(record.get(12), record.get(13), record.get(14),
                                record.get(15), record.get(16), record.get(17),
                                record.get(18)),
                        0, false, false, true,
                        controller.getCurrentSession().getYear(), controller.getCurrentSession().getSession()));

            parser.close();

            //Adds to db if does not exist
            for (int i = 0; i < tmpTrainees.size(); i++) {
                boolean isFound = false;
                for (int j = 0; j < controller.getCurrentTrainees().size(); j++) {

                    if (tmpTrainees.get(i).getFirstName().equals(controller.getCurrentTrainees().get(j).getFirstName()) &&
                            tmpTrainees.get(i).getLastName().equals(controller.getCurrentTrainees().get(j).getLastName()))
                        isFound = true;

                }

                if (!isFound)
                    DBManager.addInitialTrainee(tmpTrainees.get(i));

            }

            controller.updateCurrentTrainees();
            importComboBox.getSelectionModel().selectFirst();
            refresh();

        } catch (Exception e) {

            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR, "There was a problem loading " +
                    selectedFile.getName() + " into the Trainee database.\nDouble check you selected the right file.",
                    ButtonType.CLOSE);
            error.showAndWait();
            importComboBox.getSelectionModel().selectFirst();

        }

    }

    /**
     * Saves the trainee questionnaire p1 info .csv file to memory, if selected.
     */
    private void onAddQ1Clicked() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(LifeguardTrainingApplication.getCoordinator().getStage());
        if (selectedFile == null) {

            importComboBox.getSelectionModel().selectFirst();
            return;

        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to load " +
                selectedFile.getName() + " into the Trainee database?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        //If this was a mistake, leave, if not, continue
        if (alert.getResult() == ButtonType.CANCEL) {

            importComboBox.getSelectionModel().selectFirst();
            return;

        }

        List<Trainee> tmpTraineeQ1 = new ArrayList<>();
        //Handle Trainees

        try {

            CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
            CSVParser parser = new CSVParser(new FileReader(selectedFile), format);
            //Checks that only the questionnaire 1 .csv is being read
            Vector<CSVRecord> records = new Vector<>(parser.getRecords());
            if (records.get(0).size() != 23)
                throw new Exception("Wrong File");

            for (CSVRecord record : records)
                tmpTraineeQ1.add(new Trainee(record.get(3), record.get(2), record.get(1), record.get(4), record.get(5),
                        record.get(6),
                        !(record.get(7).charAt(0) == 'N' && record.get(7).charAt(1) == '/'),
                        (record.get(7).charAt(0) == 'N' && record.get(7).charAt(1) == '/') ? null : record.get(7),
                        record.get(8), record.get(9), record.get(10).charAt(0) == 'Y',
                        record.get(11), record.get(12).charAt(0) == 'Y',
                        record.get(13), record.get(14).charAt(0) == 'Y',
                        record.get(15), record.get(16).charAt(0) == 'Y', record.get(17),
                        record.get(18).charAt(0) == 'Y',
                        record.get(19), record.get(20).charAt(0) == 'Y',
                        record.get(21), record.get(22)));

            parser.close();

            //Adds to db if does not exist
            for (int i = 0; i < tmpTraineeQ1.size(); i++) {
                boolean isFound = false;
                boolean isFilled = false;
                for (int j = 0; j < controller.getCurrentTrainees().size() && !isFound; j++) {

                    if (tmpTraineeQ1.get(i).getFirstName().equals(controller.getCurrentTrainees().get(j).getFirstName()) &&
                            tmpTraineeQ1.get(i).getLastName().equals(controller.getCurrentTrainees().get(j).getLastName())) {

                        tmpTraineeQ1.get(i).setId(controller.getCurrentTrainees().get(j).getId());
                        isFound = true;

                        if (controller.getCurrentTrainees().get(j).isQuestionnaire1Complete())
                            isFilled = true;

                    }

                }

                if (isFound && !isFilled)
                    DBManager.addExistingTraineeQuestionnaire1Data(tmpTraineeQ1.get(i));

            }

            controller.updateCurrentTrainees();
            importComboBox.getSelectionModel().selectFirst();
            refresh();

        } catch (Exception e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR, "There was a problem loading " +
                    selectedFile.getName() + " into the Trainee database.\nDouble check you selected the right file.",
                    ButtonType.CLOSE);
            error.showAndWait();
            importComboBox.getSelectionModel().selectFirst();

        }

    }

    /**
     * Saves the trainee questionnaire p2 info .csv file to memory, if selected.
     */
    private void onAddQ2Clicked() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(LifeguardTrainingApplication.getCoordinator().getStage());
        if (selectedFile == null) {

            importComboBox.getSelectionModel().selectFirst();
            return;

        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to load " +
                selectedFile.getName() + " into the Trainee database?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        //If this was a mistake, leave, if not, continue
        if (alert.getResult() == ButtonType.CANCEL) {

            importComboBox.getSelectionModel().selectFirst();
            return;

        }

        List<Trainee> tmpTraineeQ2 = new ArrayList<>();
        //Handle Trainees

        try {

            CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
            CSVParser parser = new CSVParser(new FileReader(selectedFile), format);
            //Checks that only the questionnaire 2 .csv is being read
            Vector<CSVRecord> records = new Vector<>(parser.getRecords());
            if (records.get(0).size() != 18)
                throw new Exception("Wrong File");

            for (CSVRecord record : records)
                tmpTraineeQ2.add(new Trainee(record.get(1), record.get(2), record.get(3), record.get(4),
                        Integer.parseInt(record.get(5)), Integer.parseInt(record.get(6)),
                        Integer.parseInt(record.get(7)), Integer.parseInt(record.get(8)),
                        Integer.parseInt(record.get(9)), record.get(10), record.get(11),
                        record.get(12), record.get(13), record.get(14), record.get(15),
                        record.get(16).charAt(0) == 'Y'));

            parser.close();

            //Adds to db if does not exist
            for (int i = 0; i < tmpTraineeQ2.size(); i++) {
                boolean isFound = false;
                boolean isFilled = false;
                for (int j = 0; j < controller.getCurrentTrainees().size() && !isFound; j++) {

                    if (tmpTraineeQ2.get(i).getFirstName().equals(controller.getCurrentTrainees().get(j).getFirstName()) &&
                            tmpTraineeQ2.get(i).getLastName().equals(controller.getCurrentTrainees().get(j).getLastName())) {

                        tmpTraineeQ2.get(i).setId(controller.getCurrentTrainees().get(j).getId());
                        isFound = true;

                        if (controller.getCurrentTrainees().get(j).isQuestionnaire2Complete())
                            isFilled = true;

                    }

                }

                if (isFound && !isFilled)
                    DBManager.addExistingTraineeQuestionnaire2Data(tmpTraineeQ2.get(i));

            }

            controller.updateCurrentTrainees();
            importComboBox.getSelectionModel().selectFirst();
            refresh();

        } catch (Exception e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR, "There was a problem loading " +
                    selectedFile.getName() + " into the Trainee database.\nDouble check you selected the right file.",
                    ButtonType.CLOSE);
            error.showAndWait();
            importComboBox.getSelectionModel().selectFirst();

        }

    }

    private boolean isGoodNumber(String str) {

        //Checks if numeric
        try {
            double d = Double.parseDouble(str);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }

    }

    /**
     * Navigates to the Overview page.
     */
    public void onOverviewButtonClicked() {

        try {
            LifeguardTrainingApplication.getCoordinator().showOverviewScene();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Navigates to the Edit/Import page.
     */
    public void onEditImportClicked() {/*Nothing, already on the edit/import page*/}

    /**
     * Navigates to the Reports page.
     */
    public void onReportsClicked() {

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
        Collections.sort(sessions, new SortByNewestSession());
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
        dialogVBox.setMargin(label, new Insets(10, 0, 0, 0));
        dialogVBox.setMargin(buttonHBox, new Insets(10));
        dialogVBox.setMargin(sessionListView, new Insets(0, 5, 0, 5));

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

}

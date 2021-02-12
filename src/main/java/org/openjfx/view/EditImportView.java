package org.openjfx.view;

import javafx.scene.control.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javafx.fxml.FXML;

import javafx.stage.FileChooser;

import org.openjfx.controller.Controller;
import org.openjfx.controller.DBManager;
import org.openjfx.controller.LifeguardTrainingApplication;
import org.openjfx.model.EmergencyContact;
import org.openjfx.model.Trainee;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class EditImportView {

    Controller controller;

    @FXML
    private Label yearLabel;
    @FXML
    private Label sessionLabel;

    //Import Data
    @FXML
    private Button importTraineesButton;
    @FXML
    private Button importQ1Button;
    @FXML
    private Button importQ2Button;

    //Show Trainees
    @FXML
    private ListView<Trainee> traineeListView;

    @FXML
    protected void initialize() {

        controller = LifeguardTrainingApplication.getController();
        yearLabel.setText(String.valueOf(controller.getCurrentYear()));
        sessionLabel.setText("Session " + controller.getCurrentSession());

        controller.updateCurrentTrainees();
        refresh();

    }

    /**
     * Refreshes the page's data with the most current data from Controller.
     */
    private void refresh() {

        traineeListView.setItems(controller.getTraineesAsObservableList());

    }

    /**
     * Saves the trainee info .csv file to the database, if valid.
     */
    public void onAddTraineesClicked() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(LifeguardTrainingApplication.getCoordinator().getStage());
        if (selectedFile == null) {

            importTraineesButton.setText("Trainee Information");
            return;

        }

        importTraineesButton.setText(selectedFile.getName());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to load " +
                selectedFile.getName() + " into the Trainee database?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        //If this was a mistake, leave, if not, continue
        if (alert.getResult() == ButtonType.CANCEL) {

            importTraineesButton.setText("Trainee Information");
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
                        controller.getCurrentYear(), controller.getCurrentSession()));

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
            refresh();
            importTraineesButton.setText("Successfully Added");

        } catch (Exception e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR, "There was a problem loading " +
                    selectedFile.getName() + " into the Trainee database.\nDouble check you selected the right file",
                    ButtonType.CLOSE);
            error.showAndWait();

            importTraineesButton.setText("Trainee Information");

        }

    }

    /**
     * Saves the trainee questionnaire p1 info .csv file to memory, if selected.
     */
    public void onAddQ1Clicked() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(LifeguardTrainingApplication.getCoordinator().getStage());
        if (selectedFile == null) {

            importQ1Button.setText("Questionnaire Part 1");
            return;

        }

        importQ1Button.setText(selectedFile.getName());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to load " +
                selectedFile.getName() + " into the Trainee database?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        //If this was a mistake, leave, if not, continue
        if (alert.getResult() == ButtonType.CANCEL) {

            importQ1Button.setText("Questionnaire Part 1");
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
            refresh();
            importQ1Button.setText("Successfully Added");

        } catch (Exception e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR, "There was a problem loading " +
                    selectedFile.getName() + " into the Trainee database.\nDouble check you selected the right file",
                    ButtonType.CLOSE);
            error.showAndWait();
            importQ1Button.setText("Questionnaire Part 1");

        }

    }

    /**
     * Saves the trainee questionnaire p2 info .csv file to memory, if selected.
     */
    public void onAddQ2Clicked() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(LifeguardTrainingApplication.getCoordinator().getStage());
        if (selectedFile == null) {

            importQ2Button.setText("Questionnaire Part 2");
            return;

        }

        importQ2Button.setText(selectedFile.getName());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to load " +
                selectedFile.getName() + " into the Trainee database?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        //If this was a mistake, leave, if not, continue
        if (alert.getResult() == ButtonType.CANCEL) {

            importQ2Button.setText("Questionnaire Part 2");
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
            refresh();
            importQ2Button.setText("Successfully Added");

        } catch (Exception e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR, "There was a problem loading " +
                    selectedFile.getName() + " into the Trainee database.\nDouble check you selected the right file",
                    ButtonType.CLOSE);
            error.showAndWait();
            importQ2Button.setText("Questionnaire Part 2");

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

}

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

    private File tFile;
    private File q1File;
    private File q2File;

    //Show Trainees
    @FXML
    private ListView<Trainee> traineeListView;

    @FXML
    protected void initialize(){

        controller = LifeguardTrainingApplication.getController();
        yearLabel.setText(String.valueOf(controller.getCurrentYear()));
        sessionLabel.setText("Session " + controller.getCurrentSession());

        controller.updateCurrentTrainees();
        refresh();

        tFile = null;
        q1File = null;
        q2File = null;

    }

    /**
     * Refreshes the page's data with the most current data from Controller.
     */
    private void refresh(){

        traineeListView.setItems(controller.getTraineesAsObservableList());

    }

    /**
     * Saves the trainee info .csv file to the database, if valid.
     */
    public void onAddTraineesClicked(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(LifeguardTrainingApplication.getCoordinator().getStage());
        if(selectedFile == null){

            importTraineesButton.setText("Trainee Information");
            tFile = null;
            return;

        }

        tFile = selectedFile;
        importTraineesButton.setText(selectedFile.getName());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to load " +
                                tFile.getName() + " into the Trainee database?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        //If this was a mistake, leave, if not, continue
        if(alert.getResult() == ButtonType.CANCEL){

            importTraineesButton.setText("Trainee Information");
            tFile = null;
            return;

        }

        List<Trainee> tmpTrainees = new ArrayList<>();
        //Handle Trainees
        if (tFile != null){

            try {

                CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
                CSVParser parser = new CSVParser(new FileReader(tFile), format);
                for(CSVRecord record : parser)
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
                for(int i = 0; i < tmpTrainees.size(); i++){
                    boolean isFound = false;
                    for(int j = 0; j < controller.getCurrentTrainees().size(); j++){

                        if(tmpTrainees.get(i).getFirstName().equals(controller.getCurrentTrainees().get(j).getFirstName()) &&
                           tmpTrainees.get(i).getLastName().equals(controller.getCurrentTrainees().get(j).getLastName()))
                            isFound = true;

                    }

                    if(!isFound)
                        DBManager.addInitialTrainee(tmpTrainees.get(i));

                }

                controller.updateCurrentTrainees();
                refresh();
                importTraineesButton.setText("Successfully Added");

            } catch (Exception e) {
                e.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR, "There was a problem loading " +
                        tFile.getName() + " into the Trainee database.\nDouble check you selected the right file",
                        ButtonType.CLOSE);
                error.showAndWait();

                importTraineesButton.setText("Trainee Information");

            }

            tFile = null;

        }

    }

    /**
     * Saves the trainee questionnaire p1 info .csv file to memory, if selected.
     */
    public void onAddQ1Clicked(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(LifeguardTrainingApplication.getCoordinator().getStage());
        if(selectedFile == null){

            importQ1Button.setText("Questionnaire Part 1");
            q1File = null;
            return;

        }

        q1File = selectedFile;
        importQ1Button.setText(selectedFile.getName());

    }

    /**
     * Saves the trainee questionnaire p2 info .csv file to memory, if selected.
     */
    public void onAddQ2Clicked(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(LifeguardTrainingApplication.getCoordinator().getStage());
        if(selectedFile == null){

            importQ2Button.setText("Questionnaire Part 2");
            q2File = null;
            return;

        }

        q2File = selectedFile;
        importQ2Button.setText(selectedFile.getName());


    }

    /**
     * Navigates to the Overview page.
     */
    public void onOverviewButtonClicked(){

        try {
            LifeguardTrainingApplication.getCoordinator().showOverviewScene();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Navigates to the Edit/Import page.
     */
    public void onEditImportClicked(){/*Nothing, already on the edit/import page*/}

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

    public class TraineeColumns{

        public TraineeColumns(String firstName, String middleName, String lastName, String birthDate, String city, String state, String phoneNumber, String email, String district, String lodging, String ecName, String ecRelation, String ecPhoneNumber, String ecAddress, String ecCity, String ecState, String ecZipcode) {
            this.firstName = firstName;
            this.middleName = middleName;
            this.lastName = lastName;
            this.birthDate = birthDate;
            this.city = city;
            this.state = state;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.district = district;
            this.lodging = lodging;
            this.ecName = ecName;
            this.ecRelation = ecRelation;
            this.ecPhoneNumber = ecPhoneNumber;
            this.ecAddress = ecAddress;
            this.ecCity = ecCity;
            this.ecState = ecState;
            this.ecZipcode = ecZipcode;
        }

        private String firstName;
        private String middleName;
        private String lastName;
        private String birthDate;
        private String city;
        private String state;
        private String phoneNumber;
        private String email;
        private String district;
        private String lodging;
        private String ecName;
        private String ecRelation;
        private String ecPhoneNumber;
        private String ecAddress;
        private String ecCity;
        private String ecState;
        private String ecZipcode;

        //Getters
        public String getfirstName() {
            return firstName;
        }
        public String getmiddleName() {
            return middleName;
        }
        public String getlastName() {
            return lastName;
        }
        public String getbirthDate() {
            return birthDate;
        }
        public String getcity() {
            return city;
        }
        public String getstate() {
            return state;
        }
        public String getphoneNumber() {
            return phoneNumber;
        }
        public String getemail() {
            return email;
        }
        public String getdistrict() {
            return district;
        }
        public String getlodging() {
            return lodging;
        }
        public String getecName() {
            return ecName;
        }
        public String getecRelation() {
            return ecRelation;
        }
        public String getecPhoneNumber() {
            return ecPhoneNumber;
        }
        public String getecAddress() {
            return ecAddress;
        }
        public String getecCity() {
            return ecCity;
        }
        public String getecState() {
            return ecState;
        }
        public String getecZipcode() {
            return ecZipcode;
        }

        //Setters
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        public void setBirthDate(String birthDate) {
            this.birthDate = birthDate;
        }
        public void setCity(String city) {
            this.city = city;
        }
        public void setState(String state) {
            this.state = state;
        }
        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public void setDistrict(String district) {
            this.district = district;
        }
        public void setLodging(String lodging) {
            this.lodging = lodging;
        }
        public void setEcName(String ecName) {
            this.ecName = ecName;
        }
        public void setEcRelation(String ecRelation) {
            this.ecRelation = ecRelation;
        }
        public void setEcPhoneNumber(String ecPhoneNumber) {
            this.ecPhoneNumber = ecPhoneNumber;
        }
        public void setEcAddress(String ecAddress) {
            this.ecAddress = ecAddress;
        }
        public void setEcCity(String ecCity) { this.ecCity = ecCity; }
        public void setEcState(String ecState) {
            this.ecState = ecState;
        }
        public void setEcZipcode(String ecZipcode) {
            this.ecZipcode = ecZipcode;
        }

    }

}

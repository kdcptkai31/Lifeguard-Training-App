package org.openjfx.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.openjfx.controller.Controller;
import org.openjfx.controller.DBManager;
import org.openjfx.controller.LifeguardTrainingApplication;
import org.openjfx.model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import static java.lang.Character.isDigit;

public class EditImportView {

    Controller controller;
    Image defaultImage;

    //Dashboard
    @FXML
    private Label yearLabel;
    @FXML
    private Label sessionLabel;
    @FXML
    private Label datesLabel;

    //Trainee Tab
    @FXML
    private ComboBox<String> importComboBox;
    @FXML
    private ListView<String> traineeListView;
    @FXML
    private ImageView traineePFPImageView;
    private Image tmpTraineeImage;
    @FXML
    private CheckBox newTraineeCheckBox;
    @FXML
    private Label tInfoErrorLabel;
    @FXML
    private TextField tFirstNameTextField;
    @FXML
    private TextField tMiddleNameTextField;
    @FXML
    private TextField tLastNameTextField;
    @FXML
    private TextField tBirthdayTextField;
    @FXML
    private TextField tCityTextField;
    @FXML
    private TextField tStateTextField;
    @FXML
    private TextField tPhoneNumberTextField;
    @FXML
    private TextField tEmailTextField;
    @FXML
    private TextField tDistrictTextField;
    @FXML
    private CheckBox tIsLodgingComboBox;
    @FXML
    private Button editQuestionnaireButton;
    @FXML
    private Button addTraineeButton;
    private Trainee holdsEditQuestionnaireData;

    @FXML
    private TextField tECNameTextField;
    @FXML
    private TextField tECRelationshipTextField;
    @FXML
    private TextField tECPhoneNumberTextField;
    @FXML
    private TextField tECAddressTextField;
    @FXML
    private TextField tECCityTextField;
    @FXML
    private TextField tECStateTextField;
    @FXML
    private TextField tECZipcodeTextField;
    @FXML
    private Label tECErrorLabel;

    @FXML
    private ListView<String> traineeEventScoresListView;
    private Vector<EventScore> traineeEventScores;
    private Vector<Event> associatedTraineeEvents;
    @FXML
    private Label eventScoreNameLabel;
    @FXML
    private TextField eventScoreTextField;
    @FXML
    private Label eventScoreErrorLabel;
    @FXML
    private ListView<String> traineeTestScoresListView;
    private Vector<TestScore> traineeTestScores;
    private Vector<Test> associatedTraineeTests;
    @FXML
    private Label testScoreNameLabel;
    @FXML
    private TextField testScoreTextField;
    @FXML
    private Label testScoreErrorLabel;

    @FXML
    protected void initialize() {

        controller = LifeguardTrainingApplication.getController();
        defaultImage = traineePFPImageView.getImage();
        tmpTraineeImage = null;
        traineeEventScores = new Vector<>();
        traineeTestScores = new Vector<>();
        refresh();
        importComboBox.getItems().addAll("Choose Import Type", "Comments", "Trainee Information", "Questionnaire 1",
                "Questionnaire 2");

        //Sets up listeners
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

        newTraineeCheckBox.selectedProperty().addListener((observable, oldValue, newValue)  -> {

            holdsEditQuestionnaireData = new Trainee();
            if(newValue){

                VBox.setMargin(traineePFPImageView, new Insets(20, 0, 0, 0));
                traineePFPImageView.setImage(new Image(Objects.requireNonNull(getClass().getClassLoader()
                        .getResourceAsStream("org/openjfx/images/blankpfp.png"))));
                traineeListView.getSelectionModel().clearSelection();
                tFirstNameTextField.clear();
                tFirstNameTextField.setPromptText("");
                tMiddleNameTextField.clear();
                tMiddleNameTextField.setPromptText("");
                tLastNameTextField.clear();
                tLastNameTextField.setPromptText("");
                tBirthdayTextField.clear();
                tBirthdayTextField.setPromptText("mm/dd/yyyy");
                tCityTextField.clear();
                tCityTextField.setPromptText("");
                tStateTextField.clear();
                tStateTextField.setPromptText("");
                tPhoneNumberTextField.clear();
                tPhoneNumberTextField.setPromptText("xxx-xxx-xxxx");
                tEmailTextField.clear();
                tEmailTextField.setPromptText("");
                tDistrictTextField.clear();
                tDistrictTextField.setPromptText("");
                tIsLodgingComboBox.setSelected(false);
                editQuestionnaireButton.setVisible(true);
                addTraineeButton.setText("Add New Trainee");

                tECNameTextField.setPromptText("");
                tECNameTextField.clear();
                tECRelationshipTextField.setPromptText("");
                tECRelationshipTextField.clear();
                tECPhoneNumberTextField.setPromptText("xxx-xxx-xxxx");
                tECPhoneNumberTextField.clear();
                tECAddressTextField.setPromptText("");
                tECAddressTextField.clear();
                tECCityTextField.setPromptText("");
                tECCityTextField.clear();
                tECStateTextField.setPromptText("");
                tECStateTextField.clear();
                tECZipcodeTextField.setPromptText("xxxxx");
                tECZipcodeTextField.clear();

                traineeEventScores.clear();
                traineeTestScores.clear();
                eventScoreNameLabel.setText("Event Score: ");
                testScoreNameLabel.setText("Test Score: ");
                eventScoreTextField.setPromptText("");
                eventScoreTextField.clear();
                testScoreTextField.setPromptText("");
                testScoreTextField.clear();
                eventScoreErrorLabel.setVisible(false);
                testScoreErrorLabel.setVisible(false);
                traineeEventScoresListView.getItems().clear();
                traineeTestScoresListView.getItems().clear();

            }else{

                tFirstNameTextField.clear();
                tMiddleNameTextField.clear();
                tLastNameTextField.clear();
                tBirthdayTextField.clear();
                tCityTextField.clear();
                tStateTextField.clear();
                tPhoneNumberTextField.clear();
                tEmailTextField.clear();
                tDistrictTextField.clear();

            }
        });

        if(controller.getCurrentTrainees().size() != 0){

            traineeListView.getSelectionModel().select(0);
            onTraineeListViewClicked();

        }

    }

    /**
     * Refreshes the page's data with the most current data from Controller.
     */
    private void refresh() {

        addTraineeButton.setText("Add New Trainee");
        holdsEditQuestionnaireData = new Trainee();
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(getClass().getClassLoader().getResource("org/openjfx/images/blankpfp.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        traineePFPImageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
        yearLabel.setText(String.valueOf(controller.getCurrentSession().getYear()));
        sessionLabel.setText("Session " + controller.getCurrentSession().getSession());
        datesLabel.setText(controller.getCurrentSession().getStartDate() + " - " + controller.getCurrentSession().getEndDate());
        ObservableList<String> tmpList = controller.getTraineeNamesAsObservableList();
        traineeListView.setItems(tmpList);
        tFirstNameTextField.setPromptText("");
        tFirstNameTextField.clear();
        tMiddleNameTextField.setPromptText("");
        tMiddleNameTextField.clear();
        tLastNameTextField.setPromptText("");
        tLastNameTextField.clear();
        tBirthdayTextField.setPromptText("mm/dd/yyyy");
        tBirthdayTextField.clear();
        tCityTextField.setPromptText("");
        tCityTextField.clear();
        tStateTextField.setPromptText("");
        tStateTextField.clear();
        tPhoneNumberTextField.setPromptText("xxx-xxx-xxxx");
        tPhoneNumberTextField.clear();
        tEmailTextField.setPromptText("");
        tEmailTextField.clear();
        tDistrictTextField.setPromptText("");
        tDistrictTextField.clear();
        traineeListView.getSelectionModel().clearSelection();

        tECNameTextField.clear();
        tECNameTextField.setPromptText("");
        tECRelationshipTextField.clear();
        tECRelationshipTextField.setPromptText("");
        tECPhoneNumberTextField.clear();
        tECPhoneNumberTextField.setPromptText("xxx-xxx-xxxx");
        tECAddressTextField.clear();
        tECAddressTextField.setPromptText("");
        tECCityTextField.clear();
        tECCityTextField.setPromptText("");
        tECStateTextField.clear();
        tECStateTextField.setPromptText("");
        tECZipcodeTextField.clear();
        tECZipcodeTextField.setPromptText("");

        traineeEventScores.clear();
        traineeTestScores.clear();
        traineeEventScoresListView.getItems().clear();
        traineeTestScoresListView.getItems().clear();
        eventScoreNameLabel.setText("Event Score: ");
        testScoreNameLabel.setText("Test Score: ");
        eventScoreTextField.setPromptText("");
        eventScoreTextField.clear();
        testScoreTextField.setPromptText("");
        testScoreTextField.clear();
        eventScoreErrorLabel.setVisible(false);
        testScoreErrorLabel.setVisible(false);

    }

    /**
     * Populates the text fields with the selected trainee's info.
     */
    public void onTraineeListViewClicked(){

        traineeEventScoresListView.getItems().clear();
        traineeTestScoresListView.getItems().clear();
        tInfoErrorLabel.setVisible(false);
        tECErrorLabel.setVisible(false);
        testScoreErrorLabel.setVisible(false);
        eventScoreErrorLabel.setVisible(false);

        int selectedIndex = traineeListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1)
            return;

        //Populate trainee data fields.
        Trainee tmp = controller.getCurrentTrainees().get(selectedIndex);
        //Makes sure the image resizing does not affect the other UI objects
        traineePFPImageView.setPreserveRatio(true);
        traineePFPImageView.setSmooth(true);
        traineePFPImageView.setCache(true);
        traineePFPImageView.setImage(tmp.getActualImage());
        VBox.setMargin(traineePFPImageView, new Insets(20, 0,
                                187 - Math.ceil(traineePFPImageView.getBoundsInLocal().getHeight()), 0));
        tFirstNameTextField.setPromptText(tmp.getFirstName());
        tMiddleNameTextField.setPromptText(tmp.getMiddleName());
        tLastNameTextField.setPromptText(tmp.getLastName());
        tBirthdayTextField.setPromptText(tmp.getBirthDate());
        tCityTextField.setPromptText(tmp.getCity());
        tStateTextField.setPromptText(tmp.getState());
        tPhoneNumberTextField.setPromptText(tmp.getPhoneNumber());
        tEmailTextField.setPromptText(tmp.getEmail());
        tDistrictTextField.setPromptText(tmp.getDistrictChoice());
        tIsLodgingComboBox.setSelected(tmp.isLodging());
        newTraineeCheckBox.setSelected(false);
        addTraineeButton.setText("Update Trainee");

        //Populates Emergency Contact data fields.
        if(tmp.getEmergencyContact() != null){

            tECNameTextField.clear();
            tECRelationshipTextField.clear();
            tECPhoneNumberTextField.clear();
            tECAddressTextField.clear();
            tECCityTextField.clear();
            tECStateTextField.clear();
            tECZipcodeTextField.clear();
            tECNameTextField.setPromptText(tmp.getEmergencyContact().getFullName());
            tECRelationshipTextField.setPromptText(tmp.getEmergencyContact().getRelationship());
            tECPhoneNumberTextField.setPromptText(tmp.getEmergencyContact().getPhoneNumber());
            tECAddressTextField.setPromptText(tmp.getEmergencyContact().getAddress());
            tECCityTextField.setPromptText(tmp.getEmergencyContact().getCity());
            tECStateTextField.setPromptText(tmp.getEmergencyContact().getState());
            tECZipcodeTextField.setPromptText(tmp.getEmergencyContact().getZipcode());

        }else{

            tECNameTextField.clear();
            tECNameTextField.setPromptText("");
            tECRelationshipTextField.clear();
            tECRelationshipTextField.setPromptText("");
            tECPhoneNumberTextField.clear();
            tECAddressTextField.clear();
            tECAddressTextField.setPromptText("");
            tECCityTextField.clear();
            tECCityTextField.setPromptText("");
            tECStateTextField.clear();
            tECStateTextField.setPromptText("");
            tECZipcodeTextField.clear();
            tECPhoneNumberTextField.setPromptText("xxx-xxx-xxxx");
            tECZipcodeTextField.setPromptText("xxxxx");

        }

        //Populates trainee event scores info.
        traineeEventScores = DBManager.getAllEventScoresFromTraineeID(tmp.getId());
        if(traineeEventScores.size() != 0){

            Vector<Integer> eventIDs = new Vector<>();
            for(EventScore score : traineeEventScores)
                eventIDs.add(score.getEventID());
            associatedTraineeEvents = DBManager.getAllEventsFromEventIDs(eventIDs);

            ObservableList<String> traineeEventScoresList = FXCollections.observableArrayList();
            for(EventScore score : traineeEventScores){

                for(Event event : associatedTraineeEvents){

                    if(event.getEventID() == score.getEventID()) {

                        traineeEventScoresList.add(event.getName() + " | " + score.getScore() + "/" + event.getPoints());
                        break;

                    }
                }
            }

            traineeEventScoresListView.setItems(traineeEventScoresList);

        }

        //Populates trainee test scores info.
        traineeTestScores = DBManager.getAllTestScoresFromTraineeID(tmp.getId());
        if(traineeTestScores.size() != 0){

            Vector<Integer> testIDs = new Vector<>();
            for(TestScore score :  traineeTestScores)
                testIDs.add(score.getTestID());
            associatedTraineeTests = DBManager.getAllTestFromTestIDs(testIDs);

            ObservableList<String> traineeTestScoresList = FXCollections.observableArrayList();
            for(TestScore score : traineeTestScores){

                for(Test test : associatedTraineeTests){

                    if(test.getTestID() == score.getTestID()){

                        traineeTestScoresList.add(test.getName() + " | " + score.getScore() + "/" + test.getPoints());
                        break;

                    }
                }
            }

            traineeTestScoresListView.setItems(traineeTestScoresList);

        }

    }

    /**
     * Validates and saves the added or updated emergency contact for the selected trainee.
     */
    public void onAddECClicked(){

        int selectedIndex = traineeListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1)
            return;

        EmergencyContact tmpEC = new EmergencyContact();
        tmpEC.setTraineeID(controller.getCurrentTrainees().get(selectedIndex).getId());

        //New Emergency Contact
        if(tECNameTextField.getPromptText().equals("")){

            //Run Data Field Validation
            int validator = 0;
            if(!tECNameTextField.getText().isEmpty())
                validator++;
            if(!tECRelationshipTextField.getText().isEmpty())
                validator++;
            if(!tECPhoneNumberTextField.getText().isEmpty() && isGoodPhoneNumber(tECPhoneNumberTextField.getText()))
                validator++;
            if(!tECAddressTextField.getText().isEmpty())
                validator++;
            if(!tECCityTextField.getText().isEmpty())
                validator++;
            if(!tECStateTextField.getText().isEmpty())
                validator++;
            if(!tECZipcodeTextField.getText().isEmpty() && isGoodNumber(tECZipcodeTextField.getText()) &&
                    tECZipcodeTextField.getText().length() == 5)
                validator++;

            if(validator == 0)
                return;
            if(validator != 7){

                tECErrorLabel.setVisible(true);
                return;

            }

            tmpEC.setFullName(tECNameTextField.getText());
            tmpEC.setRelationship(tECRelationshipTextField.getText());
            tmpEC.setPhoneNumber(tECPhoneNumberTextField.getText());
            tmpEC.setAddress(tECAddressTextField.getText());
            tmpEC.setCity(tECCityTextField.getText());
            tmpEC.setState(tECStateTextField.getText());
            tmpEC.setZipcode(tECZipcodeTextField.getText());

            //Save Emergency Contact
            DBManager.addEmergencyContact(tmpEC);

            //Update Emergency Contact and validate
        }else{

            if(tECNameTextField.getText().isEmpty())
                tmpEC.setFullName(tECNameTextField.getPromptText());
            else
                tmpEC.setFullName(tECNameTextField.getText());
            if(tECRelationshipTextField.getText().isEmpty())
                tmpEC.setRelationship(tECRelationshipTextField.getPromptText());
            else
                tmpEC.setRelationship(tECRelationshipTextField.getText());
            if(tECPhoneNumberTextField.getText().isEmpty())
                tmpEC.setPhoneNumber(tECPhoneNumberTextField.getPromptText());
            else if(tECPhoneNumberTextField.getText().length() == 12 && isGoodPhoneNumber(tECPhoneNumberTextField.getText()))
                tmpEC.setPhoneNumber(tECPhoneNumberTextField.getText());
            else{
                tECErrorLabel.setVisible(true);
                return;
            }
            if(tECAddressTextField.getText().isEmpty())
                tmpEC.setAddress(tECAddressTextField.getPromptText());
            else
                tmpEC.setAddress(tECAddressTextField.getText());
            if(tECCityTextField.getText().isEmpty())
                tmpEC.setCity(tECCityTextField.getPromptText());
            else
                tmpEC.setCity(tECCityTextField.getText());
            if(tECStateTextField.getText().isEmpty())
                tmpEC.setState(tECStateTextField.getPromptText());
            else
                tmpEC.setState(tECStateTextField.getText());
            if(tECZipcodeTextField.getText().isEmpty())
                tmpEC.setZipcode(tECZipcodeTextField.getPromptText());
            else if(isGoodNumber(tECZipcodeTextField.getText()) && tECZipcodeTextField.getText().length() == 5)
                tmpEC.setZipcode(tECZipcodeTextField.getText());
            else{
                tECErrorLabel.setVisible(true);
                return;
            }

            //Save Emergency Contact
            DBManager.updateEmergencyContact(tmpEC);

        }

        int index = traineeListView.getSelectionModel().getSelectedIndex();
        controller.updateCurrentTrainees();
        refresh();
        traineeListView.getSelectionModel().select(index);
        onTraineeListViewClicked();

    }

    /**
     * Populates the event scores edit fields with the selected event.
     */
    public void onTraineeEventScoresListViewClicked(){

        int selectedIndex = traineeEventScoresListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1)
            return;

        String str = traineeEventScoresListView.getSelectionModel().getSelectedItem();
        String[] strParts = str.split(" ");
        String[] eventRatio = strParts[strParts.length - 1].split("/");
        eventScoreTextField.setPromptText(eventRatio[0]);

    }

    /**
     * Populates the test scores edit fields with the selected test.
     */
    public void onTraineeTestScoresListViewClicked(){

        int selectedIndex = traineeTestScoresListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1)
            return;

        String str = traineeTestScoresListView.getSelectionModel().getSelectedItem();
        String[] strParts = str.split(" ");
        String[] testRatio = strParts[strParts.length - 1].split("/");
        testScoreTextField.setPromptText(testRatio[0]);

    }

    /**
     * Saves the given event score if valid.
     */
    public void onSaveEventScoreClicked(){

        int selectedIndex = traineeEventScoresListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1 || eventScoreTextField.getText().isEmpty())
            return;

        String[] strParts = traineeEventScoresListView.getSelectionModel().getSelectedItem().split(" ");
        int maxNum = Integer.parseInt(strParts[strParts.length - 1].split("/")[1]);
        if(!isGoodNumber(eventScoreTextField.getText()) || Integer.parseInt(eventScoreTextField.getText()) < 0 ||
                Integer.parseInt(eventScoreTextField.getText()) > maxNum){
            eventScoreErrorLabel.setVisible(true);
            return;
        }

        StringBuilder name = new StringBuilder();
        for(String str : strParts){
            if(!str.equals("|"))
                name.append(str).append(" ");
            else
                break;
        }
        name.delete(name.length() - 1, name.length());
        Event tmp = null;
        for(Event event : associatedTraineeEvents){
            if(event.getName().contentEquals(name)){
                tmp = event;
                break;
            }
        }

        DBManager.updateEventScore(new EventScore(tmp.getEventID(), traineeEventScores.elementAt(0).getTraineeID(),
                Integer.parseInt(eventScoreTextField.getText())));
        int index = traineeListView.getSelectionModel().getSelectedIndex();
        refresh();
        traineeListView.getSelectionModel().select(index);
        onTraineeListViewClicked();

    }

    /**
     * Saves the given test score if valid.
     */
    public void onSaveTestScoreClicked(){

        int selectedIndex = traineeTestScoresListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1 || testScoreTextField.getText().isEmpty())
            return;

        String[] strParts = traineeTestScoresListView.getSelectionModel().getSelectedItem().split(" ");
        int maxNum = Integer.parseInt(strParts[strParts.length - 1].split("/")[1]);
        if(!isGoodNumber(testScoreTextField.getText()) || Integer.parseInt(testScoreTextField.getText()) < 0 ||
            Integer.parseInt(testScoreTextField.getText()) > maxNum){
            testScoreErrorLabel.setVisible(true);
            return;
        }

        StringBuilder name = new StringBuilder();
        for(String str : strParts){
            if(!str.equals("|"))
                name.append(str).append(" ");
            else
                break;
        }
        name.delete(name.length() - 1, name.length());
        Test tmp = null;
        for(Test test : associatedTraineeTests){
            if(test.getName().contentEquals(name)){
                tmp = test;
                break;
            }
        }

        DBManager.updateTestScore(new TestScore(tmp.getTestID(), traineeTestScores.elementAt(0).getTraineeID(),
                Integer.parseInt(testScoreTextField.getText())));
        int index = traineeListView.getSelectionModel().getSelectedIndex();
        refresh();
        traineeListView.getSelectionModel().select(index);
        onTraineeListViewClicked();

    }

    /**
     * Prompts user to select an image for the selected trainee's or the new trainee's pfp.
     */
    public void onTPFPClicked(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG Images", "*.jpg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Images", "*.png"));
        File selectedFile = fileChooser.showOpenDialog(LifeguardTrainingApplication.getCoordinator().getStage());
        if(selectedFile == null)
            return;

        //If successful, save image to memory for later.
        try {
            BufferedImage bufferedImage = ImageIO.read(selectedFile);
            tmpTraineeImage = SwingFXUtils.toFXImage(bufferedImage, null);
            traineePFPImageView.setImage(tmpTraineeImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Opens the Questionnaire data fields for viewing/editing.
     */
    public void onEditQuestionnaireClicked(){

        boolean isUpdate = false;
        Trainee tmp = null;
        if(addTraineeButton.getText().equals("Update Trainee")){

            isUpdate = true;
            tmp = controller.getCurrentTrainees().get(traineeListView.getSelectionModel().getSelectedIndex());

        }

        //Initialize Dialog box contents
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(LifeguardTrainingApplication.getCoordinator().getStage());

        VBox dialogVBox = new VBox();
        dialogVBox.setSpacing(5);
        dialogVBox.setStyle("-fx-background: #3476f7");

        //Initialize Q1 contents
        VBox q1VBox = new VBox();
        q1VBox.setSpacing(5);
        q1VBox.setAlignment(Pos.TOP_CENTER);
        q1VBox.setStyle("-fx-background: #3476f7; -fx-border-width: 2px; -fx-border-color: #efb748");

        Label q1dataLabel = new Label("Questionnaire 1 Data:");

        q1dataLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold; -fx-font-size: 14;");
        Label shirtSizeLabel = new Label("Shirt Size:");
        shirtSizeLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        shirtSizeLabel.setTextFill(Paint.valueOf("#efb748"));
        ComboBox<String> shirtSizeComboBox = new ComboBox<>();
        shirtSizeComboBox.setPromptText("Shirt Size");
        shirtSizeComboBox.getItems().addAll("S", "M", "L", "XL", "XXL");
        Label shortSizeLabel = new Label("Short Size:");
        shortSizeLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        ComboBox<String> shortSizeComboBox = new ComboBox<>();
        shortSizeComboBox.setPromptText("Short Size");
        shortSizeComboBox.getItems().addAll("S", "M", "L", "XL", "XXL");
        HBox h2 = new HBox(shirtSizeLabel, shirtSizeComboBox, shortSizeLabel, shortSizeComboBox);
        h2.setAlignment(Pos.CENTER);
        h2.setSpacing(5);

        Label swimSuitSizeLabel = new Label("Swim Suit Size: ");
        swimSuitSizeLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        ComboBox<String> swimSuitSizeComboBox = new ComboBox<>();
        swimSuitSizeComboBox.setPromptText("Swim Suit Size");
        swimSuitSizeComboBox.getItems().addAll("Female - Onesie 26", "Female  - Onesie 28",
                "Female - Onesie 30", "Female - Onesie 32", "Female - Onesie 34", "Female - Onesie  36",
                "Female - Onesie 38", "Male - Racing Suit 26", "Male - Racing Suit 28", "Male - Racing Suit 30",
                "Male - Racing Suit 32", "Male - Racing Suit 34", "Male - Racing Suit 36", "Male - Racing Suit 38");
        HBox h3 = new HBox(swimSuitSizeLabel, swimSuitSizeComboBox);
        h3.setAlignment(Pos.CENTER);

        CheckBox isReturningCheckBox = new CheckBox("Returning Trainee?");
        isReturningCheckBox.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        Label whyReturningLabel = new Label("Why returning: ");
        whyReturningLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        TextField whyReturningTextField = new TextField();
        HBox h4 = new HBox(whyReturningLabel, whyReturningTextField);
        h4.setAlignment(Pos.CENTER);
        isReturningCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> h4.setVisible(newValue));
        h4.setVisible(false);
        whyReturningTextField.setPromptText("Why are they returning?");

        Label whyBeStateLGLabel = new Label("Why be a State LG: ");
        whyBeStateLGLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        TextField whyBeStateLGTextField = new TextField();
        whyBeStateLGTextField.setPromptText("Why be State LG?");
        HBox h5 = new HBox(whyBeStateLGLabel, whyBeStateLGTextField);
        h5.setAlignment(Pos.CENTER);

        Label whatLearnTrainingLabel = new Label("What they want to learn: ");
        whatLearnTrainingLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        TextField whatLearnInTrainingTextField = new TextField();
        whatLearnInTrainingTextField.setPromptText("What they want to learn?");
        HBox h6 = new HBox(whatLearnTrainingLabel, whatLearnInTrainingTextField);
        h6.setAlignment(Pos.CENTER);

        CheckBox isJGCheckBox = new CheckBox("Jr. Lifeguard?");
        isJGCheckBox.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        Label jgInfoLabel = new Label("JG Info: ");
        jgInfoLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        TextField jgInfoTextField = new TextField();
        HBox h7 = new HBox(jgInfoLabel, jgInfoTextField);
        h7.setAlignment(Pos.CENTER);
        isJGCheckBox.selectedProperty().addListener((observable, oldValue, newValue)-> h7.setVisible(newValue));
        h7.setVisible(false);
        jgInfoTextField.setPromptText("JG Info");

        CheckBox isOpenWaterLGCheckBox = new CheckBox("Open Water Lifeguard?");
        isOpenWaterLGCheckBox.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        Label openWaterInfoLabel = new Label("Open Water LG Info: ");
        openWaterInfoLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        TextField openWaterLGInfoTextField = new TextField();
        HBox h8 = new HBox(openWaterInfoLabel, openWaterLGInfoTextField);
        h8.setAlignment(Pos.CENTER);
        isOpenWaterLGCheckBox.selectedProperty().addListener((observable, oldValue, newValue)-> h8.setVisible(newValue));
        h8.setVisible(false);
        openWaterLGInfoTextField.setPromptText("Open Water LG Info");

        CheckBox isPoolLGCheckBox = new CheckBox("Pool Lifeguard?");
        isPoolLGCheckBox.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        Label poolLGInfoLabel = new Label("Pool LG Info: ");
        poolLGInfoLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        TextField poolLGInfoTextField = new TextField();
        HBox h9 = new HBox(poolLGInfoLabel, poolLGInfoTextField);
        h9.setAlignment(Pos.CENTER);
        isPoolLGCheckBox.selectedProperty().addListener((observable, oldValue, newValue)-> h9.setVisible(newValue));
        h9.setVisible(false);
        poolLGInfoTextField.setPromptText("Pool LG Info");

        CheckBox isEMTCheckBox = new CheckBox("EMT?");
        isEMTCheckBox.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        Label emtInfoLabel = new Label("EMT Info: ");
        emtInfoLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        TextField emtInfoTextField = new TextField();
        HBox h10 = new HBox(emtInfoLabel, emtInfoTextField);
        h10.setAlignment(Pos.CENTER);
        isEMTCheckBox.selectedProperty().addListener((observable, oldValue, newValue)-> h10.setVisible(newValue));
        h10.setVisible(false);
        emtInfoTextField.setPromptText("EMT Info");

        CheckBox isOtherMedicalCheckBox = new CheckBox("Other Advanced Medical Training?");
        isOtherMedicalCheckBox.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        Label otherMedicalInfoLabel = new Label("Other Medical Info: ");
        otherMedicalInfoLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        TextField otherMedicalInfoTextField = new TextField();
        HBox h11 = new HBox(otherMedicalInfoLabel, otherMedicalInfoTextField);
        h11.setAlignment(Pos.CENTER);
        isOtherMedicalCheckBox.selectedProperty().addListener((observable, oldValue, newValue)-> h11.setVisible(newValue));
        h11.setVisible(false);
        otherMedicalInfoTextField.setPromptText("Other Medical Training Info");

        CheckBox isFirstJobCheckBox = new CheckBox("First Job?");
        isFirstJobCheckBox.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        Label firstJobLabel = new Label("Previous Employment Info: ");
        firstJobLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        TextField jobInfoTextField = new TextField();
        HBox h12 = new HBox(firstJobLabel, jobInfoTextField);
        h12.setAlignment(Pos.CENTER);
        isFirstJobCheckBox.selectedProperty().addListener((observable, oldValue, newValue)-> h12.setVisible(!newValue));
        jobInfoTextField.setPromptText("Previous Employment Info");

        Label anyOtherInfoLabel = new Label("Any other info: ");
        anyOtherInfoLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        TextField anyOtherInfoTextField = new TextField();
        HBox h13 = new HBox(anyOtherInfoLabel, anyOtherInfoTextField);
        h13.setAlignment(Pos.CENTER);
        anyOtherInfoTextField.setPromptText("Any other extra info?");

        //Populates the Questionnaire 1 Data Fields with trainee data if trainee selected
        if(isUpdate && tmp.isQuestionnaire1Complete()){

            shirtSizeComboBox.getSelectionModel().select(tmp.getShirtSize());
            shortSizeComboBox.getSelectionModel().select(tmp.getShortSize());
            swimSuitSizeComboBox.getSelectionModel().select(tmp.getSwimSuitSize());
            isReturningCheckBox.setSelected(tmp.isReturningTrainee());
            whyReturningTextField.setPromptText(tmp.getWhyReturning());
            whyBeStateLGTextField.setPromptText(tmp.getWhyBeStateLG());
            whatLearnInTrainingTextField.setPromptText(tmp.getWhatWantLearnTraining());
            isJGCheckBox.setSelected(tmp.isJG());
            jgInfoTextField.setPromptText(tmp.getJgInfo());
            isOpenWaterLGCheckBox.setSelected(tmp.isOpenWaterLG());
            openWaterLGInfoTextField.setPromptText(tmp.getOpenWaterLGInfo());
            isPoolLGCheckBox.setSelected(tmp.isPoolLG());
            poolLGInfoTextField.setPromptText(tmp.getPoolLGInfo());
            isEMTCheckBox.setSelected(tmp.isEMT());
            emtInfoTextField.setPromptText(tmp.getEmtInfo());
            isOtherMedicalCheckBox.setSelected(tmp.isOtherAdvancedMedicalTraining());
            otherMedicalInfoTextField.setPromptText(tmp.getAdvancedMedicalTrainingInfo());
            isFirstJobCheckBox.setSelected(tmp.isFirstJob());
            jobInfoTextField.setPromptText(tmp.getJobExperienceInfo());
            anyOtherInfoTextField.setPromptText(tmp.getAnyExtraInfo());

        }

        q1VBox.getChildren().addAll(q1dataLabel, h2, h3, isReturningCheckBox, h4, h5, h6, isJGCheckBox, h7,
                isOpenWaterLGCheckBox, h8, isPoolLGCheckBox, h9, isEMTCheckBox, h10, isOtherMedicalCheckBox, h11,
                isFirstJobCheckBox, h12, h13);


        //Initialize Q2 contents
        VBox q2VBox = new VBox();
        q2VBox.setSpacing(5);
        q2VBox.setStyle("-fx-background: #3476f7; -fx-border-width: 2px; -fx-border-color: #efb748");
        q2VBox.setAlignment(Pos.TOP_CENTER);

        Label q2dataLabel = new Label("Questionnaire 2 Data:");
        q2dataLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold; -fx-font-size: 14;");

        Label expectedChallengeLabel = new Label("Expected Challenge: ");
        expectedChallengeLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        TextField expectedChallengeTextField = new TextField();
        expectedChallengeTextField.setPromptText("Expected challenges?");
        HBox h14 = new HBox(expectedChallengeLabel, expectedChallengeTextField);
        h14.setAlignment(Pos.CENTER);

        Label prepInfoLabel = new Label("Preparation: ");
        prepInfoLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        TextField prepInfoTextField = new TextField();
        prepInfoTextField.setPromptText(" What have they done to prep?");
        HBox h15 = new HBox(prepInfoLabel, prepInfoTextField);
        h15.setAlignment(Pos.CENTER);

        Label confidenceLabel = new Label("Confidence Ratings:");
        confidenceLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");

        Label firstAidLabel = new Label("Medical: ");
        firstAidLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        ComboBox<Integer> firstAidComboBox = new ComboBox<>();
        firstAidComboBox.setPromptText("Pick");
        firstAidComboBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Label cprLabel = new Label("CPR: ");
        cprLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        ComboBox<Integer> cprComboBox = new ComboBox<>();
        cprComboBox.setPromptText("Pick");
        cprComboBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        HBox h16 = new HBox(firstAidLabel, firstAidComboBox, cprLabel, cprComboBox);
        h16.setAlignment(Pos.CENTER);
        h16.setSpacing(5);

        Label physicalLabel = new Label("Physical: ");
        physicalLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        ComboBox<Integer> physicalComboBox = new ComboBox<>();
        physicalComboBox.setPromptText("Pick");
        physicalComboBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Label mentalLabel = new Label("Mental: ");
        mentalLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        ComboBox<Integer> mentalComboBox = new ComboBox<>();
        mentalComboBox.setPromptText("Pick");
        mentalComboBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        HBox h17 = new HBox(physicalLabel, physicalComboBox, mentalLabel, mentalComboBox);
        h17.setAlignment(Pos.CENTER);
        h17.setSpacing(5);

        Label trainingCountLabel = new Label("# of Pre-Trainings Attended: ");
        trainingCountLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        TextField trainingCountTextField = new TextField();
        trainingCountTextField.setPromptText("# of Trainings Attended");
        HBox h18 = new HBox(trainingCountLabel, trainingCountTextField);
        h18.setAlignment(Pos.CENTER);

        Label frequencyLabel = new Label("Select Frequency of Below Fields:");
        frequencyLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");

        Label orgSwimFreqLabel = new Label("Org. Swim: ");
        orgSwimFreqLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        ComboBox<String> orgSwimFreqComboBox = new ComboBox<>();
        orgSwimFreqComboBox.setPromptText("Pick");
        orgSwimFreqComboBox.getItems().addAll("Every day", "2-4 /week",
                                                "1 /week", "Every few weeks", "NA / Never");
        Label personalSwimFreqLabel = new Label("Personal Swim: ");
        personalSwimFreqLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        ComboBox<String> personalSwimFreqComboBox = new ComboBox<>();
        personalSwimFreqComboBox.setPromptText("Pick");
        personalSwimFreqComboBox.getItems().addAll("Every day", "2-4 /week",
                                                 "1 /week", "Every few weeks", "NA / Never");
        HBox h19 = new HBox(orgSwimFreqLabel, orgSwimFreqComboBox, personalSwimFreqLabel, personalSwimFreqComboBox);
        h19.setAlignment(Pos.CENTER);
        h19.setSpacing(5);

        Label gymFreqLabel = new Label("Gym: ");
        gymFreqLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        ComboBox<String> gymFreqComboBox = new ComboBox<>();
        gymFreqComboBox.setPromptText("Pick");
        gymFreqComboBox.getItems().addAll("Every day", "2-4 /week",
                "1 /week", "Every few weeks", "NA / Never");
        Label oceanFreqLabel = new Label("Ocean: ");
        oceanFreqLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        ComboBox<String> oceanFreqComboBox = new ComboBox<>();
        oceanFreqComboBox.setPromptText("Pick");
        oceanFreqComboBox.getItems().addAll("Every day", "2-4 /week",
                "1 /week", "Every few weeks", "NA / Never");
        HBox h20 = new HBox(gymFreqLabel, gymFreqComboBox, oceanFreqLabel, oceanFreqComboBox);
        h20.setAlignment(Pos.CENTER);
        h20.setSpacing(5);

        Label runningFreqLabel = new Label("Runs: ");
        runningFreqLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        ComboBox<String> runningFreqComboBox = new ComboBox<>();
        runningFreqComboBox.setPromptText("Pick");
        runningFreqComboBox.getItems().addAll("Every day", "2-4 /week",
                "1 /week", "Every few weeks", "NA / Never");
        Label surfFreqLabel = new Label("Surfing: ");
        surfFreqLabel.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");
        ComboBox<String> surfFreqComboBox = new ComboBox<>();
        surfFreqComboBox.setPromptText("Pick");
        surfFreqComboBox.getItems().addAll("Every day", "2-4 /week",
                "1 /week", "Every few weeks", "NA / Never");
        HBox h21 = new HBox(runningFreqLabel, runningFreqComboBox, surfFreqLabel, surfFreqComboBox);
        h21.setAlignment(Pos.CENTER);
        h21.setSpacing(5);

        CheckBox isDisabledCheckBox = new CheckBox("I Have a Disability");
        isDisabledCheckBox.setStyle("-fx-text-fill: #efb748; -fx-font-weight: bold;");

        Label errorLabel = new Label("*ERROR* If you start a questionnaire, finish it!");
        errorLabel.setVisible(false);

        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-font-weight: bold;");

        //Checks and then saves data
        boolean finalIsUpdate = isUpdate;
        Trainee finalTmp = tmp;
        saveButton.setOnAction(e ->{

            //Validate Q1 Fields
            int q1Validator = 0;
            if(!shirtSizeComboBox.getSelectionModel().isEmpty())
                q1Validator++;
            if(!shortSizeComboBox.getSelectionModel().isEmpty())
                q1Validator++;
            if(!swimSuitSizeComboBox.getSelectionModel().isEmpty())
                q1Validator++;

            //Validate Q2 Fields
            int q2Validator = 0;
            if(!firstAidComboBox.getSelectionModel().isEmpty())
                q2Validator++;
            if(!cprComboBox.getSelectionModel().isEmpty())
                q2Validator++;
            if(!physicalComboBox.getSelectionModel().isEmpty())
                q2Validator++;
            if(!mentalComboBox.getSelectionModel().isEmpty())
                q2Validator++;
            if(!orgSwimFreqComboBox.getSelectionModel().isEmpty())
                q2Validator++;
            if(!personalSwimFreqComboBox.getSelectionModel().isEmpty())
                q2Validator++;
            if(!gymFreqComboBox.getSelectionModel().isEmpty())
                q2Validator++;
            if(!oceanFreqComboBox.getSelectionModel().isEmpty())
                q2Validator++;
            if(!runningFreqComboBox.getSelectionModel().isEmpty())
                q2Validator++;
            if(!surfFreqComboBox.getSelectionModel().isEmpty())
                q2Validator++;

            //Test Validation
            if(!((q1Validator == 0 || q1Validator == 3) && (q2Validator == 0 || q2Validator == 10)) ||
                    !isGoodNumber(trainingCountTextField.getText())){
                errorLabel.setVisible(true);
                return;
            }

            ///Fill tmp trainee with recorded data.
            if(finalIsUpdate){
                if(finalTmp.isQuestionnaire1Complete()){

                    holdsEditQuestionnaireData.setQuestionnaire1Complete(true);
                    holdsEditQuestionnaireData.setShirtSize(shirtSizeComboBox.getSelectionModel().getSelectedItem());
                    holdsEditQuestionnaireData.setShortSize(shortSizeComboBox.getSelectionModel().getSelectedItem());
                    holdsEditQuestionnaireData.setSwimSuitSize(swimSuitSizeComboBox.getSelectionModel().getSelectedItem());
                    holdsEditQuestionnaireData.setReturningTrainee(isReturningCheckBox.isSelected());
                    if(isReturningCheckBox.isSelected()) {
                        if (whyReturningTextField.getText().isEmpty())
                            holdsEditQuestionnaireData.setWhyReturning(whyReturningTextField.getPromptText());
                        else
                            holdsEditQuestionnaireData.setWhyReturning(whyReturningTextField.getText());

                    }
                    if(whyBeStateLGTextField.getText().isEmpty())
                        holdsEditQuestionnaireData.setWhyBeStateLG(whyBeStateLGTextField.getPromptText());
                    else
                        holdsEditQuestionnaireData.setWhyBeStateLG(whyBeStateLGTextField.getText());
                    if(whatLearnInTrainingTextField.getText().isEmpty())
                        holdsEditQuestionnaireData.setWhatWantLearnTraining(whatLearnInTrainingTextField.getPromptText());
                    else
                        holdsEditQuestionnaireData.setWhatWantLearnTraining(whatLearnInTrainingTextField.getText());
                    holdsEditQuestionnaireData.setJG(isJGCheckBox.isSelected());
                    if(isJGCheckBox.isSelected()){
                        if(jgInfoTextField.getText().isEmpty())
                            holdsEditQuestionnaireData.setJgInfo(jgInfoTextField.getPromptText());
                        else
                            holdsEditQuestionnaireData.setJgInfo(jgInfoTextField.getText());
                    }
                    holdsEditQuestionnaireData.setOpenWaterLG(isOpenWaterLGCheckBox.isSelected());
                    if(isOpenWaterLGCheckBox.isSelected()){
                        if(openWaterLGInfoTextField.getText().isEmpty())
                            holdsEditQuestionnaireData.setOpenWaterLGInfo(openWaterLGInfoTextField.getPromptText());
                        else
                            holdsEditQuestionnaireData.setOpenWaterLGInfo(openWaterLGInfoTextField.getText());
                    }
                    holdsEditQuestionnaireData.setPoolLG(isPoolLGCheckBox.isSelected());
                    if(isPoolLGCheckBox.isSelected()){
                        if(poolLGInfoTextField.getText().isEmpty())
                            holdsEditQuestionnaireData.setPoolLGInfo(poolLGInfoTextField.getPromptText());
                        else
                            holdsEditQuestionnaireData.setPoolLGInfo(poolLGInfoTextField.getText());
                    }
                    holdsEditQuestionnaireData.setEMT(isEMTCheckBox.isSelected());
                    if(isEMTCheckBox.isSelected()){
                        if(emtInfoTextField.getText().isEmpty())
                            holdsEditQuestionnaireData.setEmtInfo(emtInfoTextField.getPromptText());
                        else
                            holdsEditQuestionnaireData.setEmtInfo(emtInfoTextField.getText());
                    }
                    holdsEditQuestionnaireData.setOtherAdvancedMedicalTraining(isOtherMedicalCheckBox.isSelected());
                    if(isOtherMedicalCheckBox.isSelected()){
                        if(otherMedicalInfoTextField.getText().isEmpty())
                            holdsEditQuestionnaireData.setAdvancedMedicalTrainingInfo(otherMedicalInfoTextField.getPromptText());
                        else
                            holdsEditQuestionnaireData.setAdvancedMedicalTrainingInfo(otherMedicalInfoTextField.getText());
                    }
                    holdsEditQuestionnaireData.setFirstJob(isFirstJobCheckBox.isSelected());
                    if(!isFirstJobCheckBox.isSelected()){
                        if(jobInfoTextField.getText().isEmpty())
                            holdsEditQuestionnaireData.setJobExperienceInfo(jobInfoTextField.getPromptText());
                        else
                            holdsEditQuestionnaireData.setJobExperienceInfo(jobInfoTextField.getText());
                    }
                    if(anyOtherInfoTextField.getText().isEmpty())
                        holdsEditQuestionnaireData.setAnyExtraInfo(anyOtherInfoTextField.getPromptText());
                    else
                        holdsEditQuestionnaireData.setAnyExtraInfo(anyOtherInfoTextField.getText());

                }
                if(finalTmp.isQuestionnaire2Complete()){

                    holdsEditQuestionnaireData.setQuestionnaire2Complete(true);
                    if(expectedChallengeTextField.getText().isEmpty())
                        holdsEditQuestionnaireData.setExpectedBiggestTrainingChallengeInfo(expectedChallengeTextField.getPromptText());
                    else
                        holdsEditQuestionnaireData.setExpectedBiggestTrainingChallengeInfo(expectedChallengeTextField.getText());
                    if(prepInfoTextField.getText().isEmpty())
                        holdsEditQuestionnaireData.setPreparationInfo(prepInfoTextField.getPromptText());
                    else
                        holdsEditQuestionnaireData.setPreparationInfo(prepInfoTextField.getText());
                    holdsEditQuestionnaireData.setMedicalConfidence(firstAidComboBox.getSelectionModel().getSelectedItem());
                    holdsEditQuestionnaireData.setCprConfidence(cprComboBox.getSelectionModel().getSelectedItem());
                    holdsEditQuestionnaireData.setPhysicalConfidence(physicalComboBox.getSelectionModel().getSelectedItem());
                    holdsEditQuestionnaireData.setMentalConfidence(mentalComboBox.getSelectionModel().getSelectedItem());
                    if(trainingCountTextField.getText().isEmpty())
                        holdsEditQuestionnaireData.setPreTrainingSeminarsAttended(Integer.parseInt(trainingCountTextField.getPromptText()));
                    else
                        holdsEditQuestionnaireData.setPreTrainingSeminarsAttended(Integer.parseInt(trainingCountTextField.getText()));
                    holdsEditQuestionnaireData.setOrganizedSwimPoloFreq(getRealString(orgSwimFreqComboBox.getSelectionModel().getSelectedItem()));
                    holdsEditQuestionnaireData.setPersonalSwimFreq(getRealString(personalSwimFreqComboBox.getSelectionModel().getSelectedItem()));
                    holdsEditQuestionnaireData.setGymFreq(getRealString(gymFreqComboBox.getSelectionModel().getSelectedItem()));
                    holdsEditQuestionnaireData.setOceanSwimFreq(getRealString(oceanFreqComboBox.getSelectionModel().getSelectedItem()));
                    holdsEditQuestionnaireData.setRunningFreq(getRealString(runningFreqComboBox.getSelectionModel().getSelectedItem()));
                    holdsEditQuestionnaireData.setSurfingFreq(getRealString(surfFreqComboBox.getSelectionModel().getSelectedItem()));
                    holdsEditQuestionnaireData.setDisabled(isDisabledCheckBox.isSelected());

                }

                dialog.close();

            }else{

                //Fill tmp trainee with recorded q1 data.
                if(q1Validator == 3) {
                    holdsEditQuestionnaireData.setQuestionnaire1Complete(true);
                    holdsEditQuestionnaireData.setShirtSize(shirtSizeComboBox.getSelectionModel().getSelectedItem());
                    holdsEditQuestionnaireData.setShortSize(shortSizeComboBox.getSelectionModel().getSelectedItem());
                    holdsEditQuestionnaireData.setSwimSuitSize(swimSuitSizeComboBox.getSelectionModel().getSelectedItem());
                    holdsEditQuestionnaireData.setReturningTrainee(isReturningCheckBox.isSelected());
                    if(isReturningCheckBox.isSelected())
                        holdsEditQuestionnaireData.setWhyReturning(whyReturningTextField.getText());
                    holdsEditQuestionnaireData.setWhyBeStateLG(whyBeStateLGTextField.getText());
                    holdsEditQuestionnaireData.setWhatWantLearnTraining(whatLearnInTrainingTextField.getText());
                    holdsEditQuestionnaireData.setJG(isJGCheckBox.isSelected());
                    if(isJGCheckBox.isSelected())
                        holdsEditQuestionnaireData.setJgInfo(jgInfoTextField.getText());
                    holdsEditQuestionnaireData.setOpenWaterLG(isOpenWaterLGCheckBox.isSelected());
                    if(isOpenWaterLGCheckBox.isSelected())
                        holdsEditQuestionnaireData.setOpenWaterLGInfo(openWaterLGInfoTextField.getText());
                    holdsEditQuestionnaireData.setPoolLG(isPoolLGCheckBox.isSelected());
                    if(isPoolLGCheckBox.isSelected())
                        holdsEditQuestionnaireData.setPoolLGInfo(poolLGInfoTextField.getText());
                    holdsEditQuestionnaireData.setEMT(isEMTCheckBox.isSelected());
                    if(isEMTCheckBox.isSelected())
                        holdsEditQuestionnaireData.setEmtInfo(emtInfoTextField.getText());
                    holdsEditQuestionnaireData.setOtherAdvancedMedicalTraining(isOtherMedicalCheckBox.isSelected());
                    if(isOtherMedicalCheckBox.isSelected())
                        holdsEditQuestionnaireData.setAdvancedMedicalTrainingInfo(otherMedicalInfoTextField.getText());
                    holdsEditQuestionnaireData.setFirstJob(isFirstJobCheckBox.isSelected());
                    if(!isFirstJobCheckBox.isSelected())
                        holdsEditQuestionnaireData.setJobExperienceInfo(jobInfoTextField.getText());
                    holdsEditQuestionnaireData.setAnyExtraInfo(anyOtherInfoTextField.getText());

                }else
                    holdsEditQuestionnaireData.setQuestionnaire1Complete(false);
                //Fill tmp trainee with recorded q2 data.
                if(q2Validator == 10){
                    holdsEditQuestionnaireData.setQuestionnaire2Complete(true);
                    holdsEditQuestionnaireData.setExpectedBiggestTrainingChallengeInfo(expectedChallengeTextField.getText());
                    holdsEditQuestionnaireData.setPreparationInfo(prepInfoTextField.getText());
                    holdsEditQuestionnaireData.setMedicalConfidence(firstAidComboBox.getSelectionModel().getSelectedItem());
                    holdsEditQuestionnaireData.setCprConfidence(cprComboBox.getSelectionModel().getSelectedItem());
                    holdsEditQuestionnaireData.setPhysicalConfidence(physicalComboBox.getSelectionModel().getSelectedItem());
                    holdsEditQuestionnaireData.setMentalConfidence(mentalComboBox.getSelectionModel().getSelectedItem());
                    holdsEditQuestionnaireData.setPreTrainingSeminarsAttended(Integer.parseInt(trainingCountTextField.getText()));
                    holdsEditQuestionnaireData.setOrganizedSwimPoloFreq(getRealString(orgSwimFreqComboBox.getSelectionModel().getSelectedItem()));
                    holdsEditQuestionnaireData.setPersonalSwimFreq(getRealString(personalSwimFreqComboBox.getSelectionModel().getSelectedItem()));
                    holdsEditQuestionnaireData.setGymFreq(getRealString(gymFreqComboBox.getSelectionModel().getSelectedItem()));
                    holdsEditQuestionnaireData.setOceanSwimFreq(getRealString(oceanFreqComboBox.getSelectionModel().getSelectedItem()));
                    holdsEditQuestionnaireData.setRunningFreq(getRealString(runningFreqComboBox.getSelectionModel().getSelectedItem()));
                    holdsEditQuestionnaireData.setSurfingFreq(getRealString(surfFreqComboBox.getSelectionModel().getSelectedItem()));
                    holdsEditQuestionnaireData.setDisabled(isDisabledCheckBox.isSelected());

                }else
                    holdsEditQuestionnaireData.setQuestionnaire2Complete(false);

            }

            dialog.close();

        });
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {holdsEditQuestionnaireData = new Trainee(); dialog.close();});
        HBox h22 = new HBox(saveButton, cancelButton);
        h22.setAlignment(Pos.CENTER);
        h22.setSpacing(5);

        //Populates the Questionnaire 2 Data Fields with trainee data if trainee selected
        if(isUpdate && tmp.isQuestionnaire2Complete()){

            expectedChallengeTextField.setPromptText(tmp.getExpectedBiggestTrainingChallengeInfo());
            prepInfoTextField.setPromptText(tmp.getPreparationInfo());
            firstAidComboBox.getSelectionModel().select(Integer.valueOf(tmp.getMedicalConfidence()));
            cprComboBox.getSelectionModel().select(Integer.valueOf(tmp.getCprConfidence()));
            physicalComboBox.getSelectionModel().select(Integer.valueOf(tmp.getPhysicalConfidence()));
            mentalComboBox.getSelectionModel().select(Integer.valueOf(tmp.getMentalConfidence()));
            trainingCountTextField.setPromptText(String.valueOf(tmp.getPreTrainingSeminarsAttended()));
            orgSwimFreqComboBox.getSelectionModel().select(getStringResponse(tmp.getOrganizedSwimPoloFreq()));
            personalSwimFreqComboBox.getSelectionModel().select(getStringResponse(tmp.getPersonalSwimFreq()));
            gymFreqComboBox.getSelectionModel().select(getStringResponse(tmp.getGymFreq()));
            oceanFreqComboBox.getSelectionModel().select(getStringResponse(tmp.getOceanSwimFreq()));
            runningFreqComboBox.getSelectionModel().select(getStringResponse(tmp.getRunningFreq()));
            surfFreqComboBox.getSelectionModel().select(getStringResponse(tmp.getSurfingFreq()));
            isDisabledCheckBox.setSelected(tmp.isDisabled());

        }

        q2VBox.getChildren().addAll(q2dataLabel, h14, h15, confidenceLabel, h16, h17, h18, frequencyLabel, h19, h20, h21,
                                    isDisabledCheckBox, errorLabel, h22);

        dialogVBox.getChildren().addAll(q1VBox, q2VBox);
        Scene dialogScene = new Scene(dialogVBox, 440, 925);
        dialog.setScene(dialogScene);
        //dialog.setResizable(false);
        dialog.show();

    }

    /**
     * Adds or updates the trainee who's data is entered.
     */
    public void onAddTraineeClicked(){

        boolean isUpdate = false;
        Trainee tmp = null;
        if(addTraineeButton.getText().equals("Update Trainee")){

            isUpdate = true;
            tmp = controller.getCurrentTrainees().get(traineeListView.getSelectionModel().getSelectedIndex());

        }

        //Handle saving update
        if(isUpdate){

            if(!tFirstNameTextField.getText().isEmpty())
                tmp.setFirstName(tFirstNameTextField.getText());
            if(!tMiddleNameTextField.getText().isEmpty())
                tmp.setMiddleName(tMiddleNameTextField.getText());
            if(!tLastNameTextField.getText().isEmpty())
                tmp.setLastName(tLastNameTextField.getText());
            if(!tBirthdayTextField.getText().isEmpty() && isGoodDate(tBirthdayTextField.getText()))
                tmp.setBirthDate(tBirthdayTextField.getText());
            if(!tCityTextField.getText().isEmpty())
                tmp.setCity(tCityTextField.getText());
            if(!tStateTextField.getText().isEmpty())
                tmp.setState(tStateTextField.getText());
            if(!tPhoneNumberTextField.getText().isEmpty() && isGoodPhoneNumber(tPhoneNumberTextField.getText()))
                tmp.setPhoneNumber(tPhoneNumberTextField.getText());
            String regex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
            if(!tEmailTextField.getText().isEmpty() && tEmailTextField.getText().matches(regex))
                tmp.setEmail(tEmailTextField.getText());
            if(!tDistrictTextField.getText().isEmpty())
                tmp.setDistrictChoice(tDistrictTextField.getText());
            tmp.setLodging(tIsLodgingComboBox.isSelected());
            holdsEditQuestionnaireData.setId(tmp.getId());

            //Updates General Information
            DBManager.updateTrainee(tmp);

            //Updates PFP if needed
            if(!traineePFPImageView.getImage().equals(tmp.getActualImage())) {
                tmp.setImage(traineePFPImageView.getImage());
                DBManager.addTraineeProfileImage(tmp);
            }

        //Handle saving new trainee
        }else{

            int validator = 0;
            if(!tFirstNameTextField.getText().isEmpty())
                validator++;
            if(!tLastNameTextField.getText().isEmpty())
                validator++;
            if(!tBirthdayTextField.getText().isEmpty() && isGoodDate(tBirthdayTextField.getText()))
                validator++;
            if(!tCityTextField.getText().isEmpty())
                validator++;
            if(!tStateTextField.getText().isEmpty())
                validator++;
            if(!tPhoneNumberTextField.getText().isEmpty() && isGoodPhoneNumber(tPhoneNumberTextField.getText()))
                validator++;
            String regex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
            if(!tEmailTextField.getText().isEmpty() && tEmailTextField.getText().matches(regex))
                validator++;
            if(!tDistrictTextField.getText().isEmpty())
                validator++;

            if((validator == 0))
                return;

            if(validator != 8){

                tInfoErrorLabel.setVisible(true);
                return;

            }

            holdsEditQuestionnaireData.setFirstName(tFirstNameTextField.getText());
            holdsEditQuestionnaireData.setMiddleName(tMiddleNameTextField.getText());
            holdsEditQuestionnaireData.setLastName(tLastNameTextField.getText());
            holdsEditQuestionnaireData.setBirthDate(tBirthdayTextField.getText());
            holdsEditQuestionnaireData.setCity(tCityTextField.getText());
            holdsEditQuestionnaireData.setState(tStateTextField.getText());
            holdsEditQuestionnaireData.setPhoneNumber(tPhoneNumberTextField.getText());
            holdsEditQuestionnaireData.setEmail(tEmailTextField.getText());
            holdsEditQuestionnaireData.setDistrictChoice(tDistrictTextField.getText());
            holdsEditQuestionnaireData.setLodging(tIsLodgingComboBox.isSelected());
            holdsEditQuestionnaireData.setEmergencyContact(null);
            holdsEditQuestionnaireData.setYear(controller.getCurrentSession().getYear());
            holdsEditQuestionnaireData.setSession(controller.getCurrentSession().getSession());
            holdsEditQuestionnaireData.setActive(true);
            DBManager.addInitialTrainee(holdsEditQuestionnaireData);
            String tmpName = holdsEditQuestionnaireData.getFirstName() + " " + holdsEditQuestionnaireData.getLastName();
            holdsEditQuestionnaireData.setId(DBManager.getTIDFromNameAndSession(tmpName,
                    controller.getCurrentSession().getYear(), controller.getCurrentSession().getSession()));
            DBManager.addTraineeProfileImage(holdsEditQuestionnaireData);

        }

        //Updates Q1 Data if needed
        if(holdsEditQuestionnaireData.isQuestionnaire1Complete())
            DBManager.addExistingTraineeQuestionnaire1Data(holdsEditQuestionnaireData);

        //Updates Q2 Data if needed
        if(holdsEditQuestionnaireData.isQuestionnaire2Complete())
            DBManager.addExistingTraineeQuestionnaire2Data(holdsEditQuestionnaireData);

        //Updates PFP if needed
        if(!traineePFPImageView.getImage().equals(defaultImage)) {
            holdsEditQuestionnaireData.setImage(traineePFPImageView.getImage());
            DBManager.addTraineeProfileImage(holdsEditQuestionnaireData);
        }

        controller.updateCurrentTrainees();
        int index = traineeListView.getItems().size();
        System.out.println(index);
        refresh();
        traineeListView.getSelectionModel().select(index);
        onTraineeListViewClicked();

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
     * Returns the matching response, used in the Questionnaire Data editor.
     * @param str
     * @return
     */
    private String getStringResponse(String str){

//        String responses[] = {"Almost every day", "A few times (2-4) per week",
//                "About once every week", "Once every few weeks", "NA / Never"};

        switch (str){
            case "Almost every day": return "Every day";
            case "A few times (2-4) per week": return "2-4 /week";
            case "About once every week": return "1 /week";
            case "Once every few weeks": return "Every few weeks";
            case "NA / Never": return "NA / Never";
        }

        return null;

    }

    /**
     * Returns the real stream response for database storage, used in the Questionnaire Data editor.
     * @param str
     * @return
     */
    private String getRealString(String str){

        String[] responses = {"Almost every day", "A few times (2-4) per week",
                "About once every week", "Once every few weeks", "NA / Never"};

        switch (str){
            case "Every day": return responses[0];
            case "2-4 /week": return responses[1];
            case "1 /week": return responses[2];
            case "Every few weeks": return responses[3];
            case "NA / Never": return responses[4];
        }

        return null;

    }

    /**
     * Checks if the given date is valid " mm/dd/yyyy"
     * @param date
     * @return
     */
    private boolean isGoodDate(String date){

        //Checks if empty
        if(date == null || date.equals(""))
            return false;

        date = date.trim();

        //Checks if correct format
        return date.length() == 10 && isDigit(date.charAt(0)) && isDigit(date.charAt(1)) && date.charAt(2) == '/' &&
                isDigit(date.charAt(3)) && isDigit(date.charAt(4)) && date.charAt(5) == '/' && isDigit(date.charAt(6))
                && isDigit(date.charAt(7)) && isDigit(date.charAt(8)) && isDigit(date.charAt(9));

    }

    /**
     * Checks if the given phone number is in the correct format "xxx-xxx-xxxx"
     * @param num
     * @return
     */
    private boolean isGoodPhoneNumber(String num){

        String[] stringSplit = num.split("-");
        return stringSplit[0].length() == 3 && stringSplit[1].length() == 3 && stringSplit[2].length() == 4;

    }

    private boolean isGoodNumber(String str) {

        if(str.equals(""))
            return true;

        str = str.trim();
        //Checks if numeric
        try {
            double d = Double.parseDouble(str);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }

    }

}

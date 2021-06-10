package org.openjfx.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import org.openjfx.controller.Controller;
import org.openjfx.controller.DBManager;
import org.openjfx.controller.LifeguardTrainingApplication;
import org.openjfx.model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import static java.lang.Character.isDigit;

public class SetUpView {

    private Controller controller;

    //Add Session
    @FXML
    private TextField yearTextField;
    @FXML
    private TextField sessionTextField;
    @FXML
    private TextField startDateTextField;
    @FXML
    private TextField endDateTextField;
    @FXML
    private Label yearSessionErrorLabel;

    //Add Districts
    @FXML
    private TextField districtNameTextField;
    @FXML
    private TextField supervisorEmailTextField;
    @FXML
    private Label districtNameErrorLabel;
    @FXML
    private Label supervisorEmailErrorLabel;
    @FXML
    private ListView<String> districtListView;
    @FXML
    private Button addDistrictButton;

    private ObservableList<String> districtViewList;
    private Vector<Pair<String, String>> pendingDistrictData;

    //Add Instructors
    @FXML
    private TextField instructorNameTextField;
    @FXML
    private Button chooseImageButton;
    @FXML
    private Label instructorNameErrorLabel;
    @FXML
    private ListView<String> instructorListView;
    @FXML
    private Button addInstructorButton;

    private Image instructorPFP;
    private ObservableList<String> instructorViewList;
    private Vector<Pair<String, Image>> pendingInstructorData;

    //Add Tests
    @FXML
    private TextField testNameTextField;
    @FXML
    private TextField testPointsTextField;
    @FXML
    private Label testNameErrorLabel;
    @FXML
    private Label testPointsErrorLabel;
    @FXML
    private Button addTestButton;
    @FXML
    private ListView<String> testListView;

    private ObservableList<String> testViewList;
    private Vector<Pair<String, Integer>> pendingTestData;

    //Add Events
    @FXML
    private TextField eventNameTextField;
    @FXML
    private Label eventNameErrorLabel;
    @FXML
    private Button addEventButton;
    @FXML
    private ListView<String> eventListView;

    private ObservableList<String> eventViewList;
    private Vector<String> pendingEventData;



    //Constructor
    @FXML
    protected void initialize(){

        controller = LifeguardTrainingApplication.getController();
        districtViewList = FXCollections.observableArrayList();
        pendingDistrictData = new Vector<>();
        instructorViewList = FXCollections.observableArrayList();
        pendingInstructorData = new Vector<>();
        instructorPFP = null;
        testViewList = FXCollections.observableArrayList();
        pendingTestData = new Vector<>();
        eventViewList = FXCollections.observableArrayList();
        pendingEventData = new Vector<>();

    }

    /**
     * Attempts to add the typed in district information to the temporary district list.
     */
    public void onAddDistrictClicked(){

        districtNameErrorLabel.setVisible(false);
        supervisorEmailErrorLabel.setVisible(false);

        //Checks if the year and session are valid
        if(!(isGoodYear(yearTextField.getText()) && isGoodSession(sessionTextField.getText()) &&
                isGoodDate(startDateTextField.getText()) && isGoodDate(endDateTextField.getText()))) {

            yearSessionErrorLabel.setVisible(true);
            return;

        }
        yearSessionErrorLabel.setVisible(false);

        //Checks if the new district information is valid
        if(!isGoodName(districtNameTextField.getText()))
            districtNameErrorLabel.setVisible(true);

        String regex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

        if(!supervisorEmailTextField.getText().matches(regex))
            supervisorEmailErrorLabel.setVisible(true);

        if(districtNameErrorLabel.isVisible() || supervisorEmailErrorLabel.isVisible())
            return;

        districtNameErrorLabel.setVisible(false);
        supervisorEmailErrorLabel.setVisible(false);

        //Checks if already in pending district list
        if(pendingDistrictData.contains(new Pair<>(districtNameTextField.getText(), supervisorEmailTextField.getText()))){

            districtNameErrorLabel.setVisible(true);
            supervisorEmailErrorLabel.setVisible(true);
            return;

        }

        //Passed all tests, adds to district list
        pendingDistrictData.add(new Pair<>(districtNameTextField.getText(), supervisorEmailTextField.getText()));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(districtNameTextField.getText());
        stringBuilder.append(" | ");
        stringBuilder.append(supervisorEmailTextField.getText());
        districtViewList.add(stringBuilder.toString());
        districtListView.setItems(districtViewList);

        districtNameTextField.clear();
        supervisorEmailTextField.clear();
        addDistrictButton.requestFocus();

    }

    /**
     * Attempts to delete the selected district from the temporary district list.
     */
    public void onDeleteDistrictClicked(){

        if(districtListView.getSelectionModel().isEmpty())
            return;

        pendingDistrictData.remove(districtListView.getSelectionModel().getSelectedIndex());
        districtViewList.remove(districtListView.getSelectionModel().getSelectedItem());
        districtListView.setItems(districtViewList);

    }

    /**
     * Prompts the user for the file they want to use for the instructor's image.
     */
    public void onChooseImageClicked(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG Images", "*.jpg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Images", "*.png"));
        File selectedFile = fileChooser.showOpenDialog(LifeguardTrainingApplication.getCoordinator().getStage());
        if(selectedFile == null){

            chooseImageButton.setText("Choose Image");
            return;

        }

        //If successful, save image
        instructorPFP = new Image(selectedFile.toURI().toString(),
                0, 250, true, true);
        chooseImageButton.setText(selectedFile.getName());

    }

    /**
     * Attempts to add an instructor to the temporary instructor list
     */
    public void onAddInstructorClicked(){

        //Checks if year and session are valid
        if(!(isGoodYear(yearTextField.getText()) && isGoodSession(sessionTextField.getText()) &&
                isGoodDate(startDateTextField.getText()) && isGoodDate(endDateTextField.getText()))){

            yearSessionErrorLabel.setVisible(true);
            return;

        }
        yearSessionErrorLabel.setVisible(false);

        //Checks if the name is valid
        if(!isGoodName(instructorNameTextField.getText())){

            instructorNameErrorLabel.setVisible(true);
            return;

        }
        instructorNameErrorLabel.setVisible(false);

        //Search if instructor already exists
        for(int i = 0; i < pendingInstructorData.size(); i++){

            if(instructorNameTextField.getText().equals(pendingInstructorData.elementAt(i).getKey())){

                instructorNameErrorLabel.setVisible(true);
                return;

            }

        }

        pendingInstructorData.add(new Pair<>(instructorNameTextField.getText(), instructorPFP));
        instructorViewList.add(instructorNameTextField.getText());
        instructorListView.setItems(instructorViewList);

        instructorNameTextField.clear();
        chooseImageButton.setText("Choose Image");
        addInstructorButton.requestFocus();
        instructorPFP = null;

    }

    /**
     * Attempts to remove the instructor from the temporary instructor list.
     */
    public void onDeleteInstructorClicked(){

        if(instructorListView.getSelectionModel().isEmpty())
            return;

        pendingInstructorData.remove(instructorListView.getSelectionModel().getSelectedIndex());
        instructorViewList.remove(instructorListView.getSelectionModel().getSelectedItem());
        instructorListView.setItems(instructorViewList);

    }

    /**
     * Attempts to add the test to the temporary test list.
     */
    public void onAddTestClicked(){

        //Checks if year and session are valid
        if(!(isGoodYear(yearTextField.getText()) && isGoodSession(sessionTextField.getText()) &&
                isGoodDate(startDateTextField.getText()) && isGoodDate(endDateTextField.getText()))){

            yearSessionErrorLabel.setVisible(true);
            return;

        }
        yearSessionErrorLabel.setVisible(false);

        //Checks if the name is valid
        if(testNameTextField.getText() == null || testNameTextField.getText().equals("")){

            testNameErrorLabel.setVisible(true);
            return;

        }
        testNameErrorLabel.setVisible(false);

        if(!isGoodPoints(testPointsTextField.getText())){

            testPointsErrorLabel.setVisible(true);
            return;

        }
        testPointsErrorLabel.setVisible(false);

        //Search if test already exists
        for(int i = 0; i < pendingTestData.size(); i++){

            if(testNameTextField.getText().equals(pendingTestData.elementAt(i).getKey())){

                testNameErrorLabel.setVisible(true);
                return;

            }

        }

        pendingTestData.add(new Pair<>(testNameTextField.getText(), Integer.parseInt(testPointsTextField.getText())));
        testViewList.add(testNameTextField.getText() + " | " + testPointsTextField.getText());
        testListView.setItems(testViewList);

        testNameTextField.clear();
        testPointsTextField.clear();
        addTestButton.requestFocus();

    }

    /**
     * Attempts to remove the test from the temporary test list.
     */
    public void onDeleteTestClicked(){

        if(testListView.getSelectionModel().isEmpty())
            return;

        pendingTestData.remove(testListView.getSelectionModel().getSelectedIndex());
        testViewList.remove(testListView.getSelectionModel().getSelectedIndex());
        testListView.setItems(testViewList);

    }

    /**
     * Attempts to add the event to the temporary event list.
     */
    public void onAddEventClicked(){

        //Checks if year and session are valid
        if(!(isGoodYear(yearTextField.getText()) && isGoodSession(sessionTextField.getText()) &&
                isGoodDate(startDateTextField.getText()) && isGoodDate(endDateTextField.getText()))){

            yearSessionErrorLabel.setVisible(true);
            return;

        }
        yearSessionErrorLabel.setVisible(false);

        //Checks if the name is valid
        if(eventNameTextField.getText() == null || eventNameTextField.getText().equals("")){

            eventNameErrorLabel.setVisible(true);
            return;

        }

        //Search if event already exists
        for(int i = 0; i < pendingEventData.size(); i++){

            if(eventNameTextField.getText().equals(pendingEventData.elementAt(i))){

                eventNameErrorLabel.setVisible(true);
                return;

            }

        }

        pendingEventData.add(eventNameTextField.getText());
        eventViewList.add(eventNameTextField.getText());
        eventListView.setItems(eventViewList);

        eventNameTextField.clear();
        addEventButton.requestFocus();

    }

    /**
     * Attempts to remove the even tfrom the temporary event list.
     */
    public void onDeleteEventClicked(){

        if(eventListView.getSelectionModel().isEmpty())
            return;

        pendingEventData.remove(eventListView.getSelectionModel().getSelectedIndex());
        eventViewList.remove(eventListView.getSelectionModel().getSelectedIndex());
        eventListView.setItems(eventViewList);

    }

    /**
     * Saves all inputted temporary data to the database and loads the main program.
     */
    public void onFinishedSetupClicked(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you done entering this initial information?\nYou can edit or add more information later.",
                        ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();

        //If this was a mistake, leave, if not, continue
        if(alert.getResult() == ButtonType.CANCEL || !(isGoodYear(yearTextField.getText()) && isGoodSession(sessionTextField.getText()) &&
                isGoodDate(startDateTextField.getText()) && isGoodDate(endDateTextField.getText())))
            return;

        Session tmp = new Session(Integer.parseInt(yearTextField.getText().trim()), Integer.parseInt(sessionTextField.getText().trim()),
                                  startDateTextField.getText() + "/" + yearTextField.getText().trim(),
                                  endDateTextField.getText() + "/" + yearTextField.getText().trim(), 1, 0);

        //Save the entered data in the database
        //Save Year and Session
        if(!DBManager.addNewSession(tmp.getYear(), tmp.getSession(), tmp.getStartDate(), tmp.getEndDate())) {
            System.out.println("COULD NOT ADD SESSION");
            System.exit(1);
        }

        //Save districts

        for(int i = 0; i < pendingDistrictData.size(); i++)
            DBManager.addDistrict(new District(tmp.getYear(), tmp.getSession(), pendingDistrictData.elementAt(i).getKey(),
                                                               pendingDistrictData.elementAt(i).getValue()));

        //Save instructors
        for(int i = 0; i < pendingInstructorData.size(); i++)
            DBManager.addInstructor(new Instructor(tmp.getYear(), tmp.getSession(), pendingInstructorData.elementAt(i).getKey(),
                                                                   pendingInstructorData.elementAt(i).getValue()));

        //Save Tests
        for(int i = 0; i < pendingTestData.size(); i++)
            DBManager.addTest(new Test(pendingTestData.elementAt(i).getKey(), pendingTestData.elementAt(i).getValue(),
                                       false, tmp.getYear(), tmp.getSession()));

        //Save Events
        for(int i = 0; i < pendingEventData.size(); i++)
            DBManager.addEvent(new Event(pendingEventData.elementAt(i), "", false, tmp.getYear(),
                                         tmp.getSession()));

        try {
            controller.setCurrentSession(tmp);
            LifeguardTrainingApplication.getCoordinator().showOverviewScene();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    //Helpers

    /**
     * Checks if the given year is valid.
     * @param year
     * @return true if valid, false if not.
     */
    private boolean isGoodYear(String year){

        //Checks if empty
        if (year == null || year.equals(""))
            return false;

        //Removes whitespaces
        year = year.trim();

        //Checks if numeric
        try {
            double d = Double.parseDouble(year);
        } catch (NumberFormatException nfe) {
            return false;
        }

        //Fits 2XXX format
        return year.charAt(0) == '2' && year.length() == 4;

    }

    /**
     * Checks if the given date is valid " mm/dd "
     * @param date
     * @return
     */
    private boolean isGoodDate(String date){

        //Checks if empty
        if(date == null || date.equals(""))
            return false;

        date = date.trim();

        //Checks if correct format
        return date.length() == 5 && isDigit(date.charAt(0)) && isDigit(date.charAt(1)) && date.charAt(2) == '/' &&
                isDigit(date.charAt(3)) && isDigit(date.charAt(4));

    }

    /**
     * Checks if the given session is valid.
     * @param session
     * @return true if valid, false if not.
     */
    private boolean isGoodSession(String session){

        //Checks if empty
        if(session == null || session.equals(""))
            return false;

        //Removes whitespaces
        session = session.trim();

        //Checks if numeric
        try{
            double d = Double.parseDouble(session);
        }catch(NumberFormatException nfe){
            return false;
        }

        return Integer.parseInt(session) > 0 && Integer.parseInt(session) < 11;

    }

    /**
     * Checks if the given string contains only letters and exists.
     * @param str
     * @return true if valid, false if not.
     */
    private boolean isGoodName(String str){

        //Checks if empty
        if(str == null || str.equals(""))
            return false;

        //Removes whitespaces
        str = str.trim();
        int tmpCounter = 0;
        for(int i = 0; i < str.length(); i++){
            if(Character.isLetter(str.charAt(i)) || str.charAt(i) == ' ')
                tmpCounter++;
        }


        return tmpCounter == str.length();

    }

    /**
     * Checks if the given string contains only a number and exists.
     * @param str
     * @return
     */
    private boolean isGoodPoints(String str){

        //Checks if empty
        if(str == null || str.equals(""))
            return false;

        //Removes whitespaces
        str = str.trim();
        int tmpCounter = 0;
        for(int i = 0; i < str.length(); i++){
            if(isDigit(str.charAt(i)))
                tmpCounter++;
        }

        return tmpCounter == str.length();

    }

}

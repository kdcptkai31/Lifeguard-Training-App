package org.openjfx.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Pair;
import org.apache.commons.validator.routines.EmailValidator;
import org.openjfx.controller.Controller;
import org.openjfx.controller.LifeguardTrainingApplication;

import java.util.Vector;

public class SetUpView {

    private Controller controller;
    private ObservableList<String> districtViewList;
    private Vector<Pair<String, String>> pendingDistrictData;

    //Add Session
    @FXML
    private TextField yearTextField;
    @FXML
    private TextField sessionTextField;
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

    //Constructor
    @FXML
    protected void initialize(){

        controller = LifeguardTrainingApplication.getController();
        districtViewList = FXCollections.observableArrayList();
        pendingDistrictData = new Vector<>();

    }

    /**
     * Attempts to add the typed in district information to the temporary district list.
     */
    public void onAddDistrictClicked(){

        districtNameErrorLabel.setVisible(false);
        supervisorEmailErrorLabel.setVisible(false);

        //Checks if the year and session are valid
        if(!(isGoodYear(yearTextField.getText()) && isGoodSession(sessionTextField.getText()))) {

            yearSessionErrorLabel.setVisible(true);
            return;

        }
        yearSessionErrorLabel.setVisible(false);

        //Checks if the new district information is valid
        if(!isGoodString(districtNameTextField.getText()))
            districtNameErrorLabel.setVisible(true);

        if(!EmailValidator.getInstance().isValid(supervisorEmailTextField.getText()))
            supervisorEmailErrorLabel.setVisible(true);

        if(districtNameErrorLabel.isVisible() || supervisorEmailErrorLabel.isVisible())
            return;

        districtNameErrorLabel.setVisible(false);
        supervisorEmailErrorLabel.setVisible(false);

        //Checks if already in pending district list
        if(pendingDistrictData.contains(new Pair(districtNameTextField.getText(), supervisorEmailTextField.getText()))){

            districtNameErrorLabel.setVisible(true);
            supervisorEmailErrorLabel.setVisible(true);
            return;

        }

        //Passed all tests, adds to district list
        pendingDistrictData.add(new Pair(districtNameTextField.getText(), supervisorEmailTextField.getText()));
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

        //String[] selection = districtListView.getSelectionModel().getSelectedItem().split("|");
        pendingDistrictData.remove(districtListView.getSelectionModel().getSelectedIndex());
        districtViewList.remove(districtListView.getSelectionModel().getSelectedItem());
        districtListView.setItems(districtViewList);

    }

    //Helpers

    /**
     * Checks if the given year is valid.
     * @param year
     * @return true if valid, false if not.
     */
    private boolean isGoodYear(String year){

        //Checks if empty
        if (year == null || year == "")
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
        if(year.charAt(0) == '2' && year.length() == 4)
            return true;

        return false;

    }

    /**
     * Checks if the given session is valid.
     * @param session
     * @return true if valid, false if not.
     */
    private boolean isGoodSession(String session){

        //Checks if empty
        if(session == null || session == "")
            return false;

        //Removes whitespaces
        session = session.trim();

        //Checks if numeric
        try{
            double d = Double.parseDouble(session);
        }catch(NumberFormatException nfe){
            return false;
        }

        if(Integer.parseInt(session) > 0 && Integer.parseInt(session) < 100)
            return true;

        return false;

    }

    /**
     * Checks if the given string contains only letters and exists.
     * @param str
     * @return true if valid, false if not.
     */
    private boolean isGoodString(String str){

        //Checks if empty
        if(str == null || str == "")
            return false;

        //Removes whitespaces
        str = str.trim();
        int tmpCounter = 0;
        for(int i = 0; i < str.length(); i++){
            if(Character.isLetter(str.charAt(i)) || str.charAt(i) == ' ')
                tmpCounter++;
        }
        System.out.println(tmpCounter);
        System.out.println(str.length());

        return tmpCounter == str.length();

    }

}

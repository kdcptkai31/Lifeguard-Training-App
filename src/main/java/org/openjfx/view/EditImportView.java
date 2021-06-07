package org.openjfx.view;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.imageio.ImageIO;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.openjfx.controller.Controller;
import org.openjfx.controller.DBManager;
import org.openjfx.controller.LifeguardTrainingApplication;
import org.openjfx.model.*;

import static java.lang.Character.isDigit;

public class EditImportView {

    private Controller controller;
    private Image defaultImage;

    @FXML
    private TabPane tabPane;

    //Dashboard
    @FXML
    private Label yearLabel;
    @FXML
    private Label sessionLabel;
    @FXML
    private Label datesLabel;

    ///////////////////////////////////////////////////////////////////////Trainee Tab
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
    private TextField eventPlaceTextField;
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

    private Vector<Comment> traineeComments;
    @FXML
    private ListView<Comment> traineeCommentsListView;
    @FXML
    private CheckBox tCNewCheckBox;
    @FXML
    private ComboBox<String> tCIncidentComboBox;
    @FXML
    private TextField tCIncidentDescriptionTextField;
    @FXML
    private TextField tCActionsTextField;
    @FXML
    private ComboBox<String> tCRotationComboBox;
    @FXML
    private TextField tCInstructorTextField;
    @FXML
    private ComboBox<String> tCNextStepsComboBox;
    @FXML
    private TextField tCDateTextField;
    @FXML
    private Label tCErrorLabel;

    ////////////////////////////////////////////////////////////////////Events | Instructors | Tests Tab
    //Events
    @FXML
    private ListView<String> editEventsListView;
    @FXML
    private CheckBox newEventCheckBox;
    @FXML
    private TextField editEventNameTextField;
    @FXML
    private TextField editEventNotesTextField;
    private Vector<Event> editEventVector;
    @FXML
    private Button addEventButton;
    @FXML
    private Label editEventErrorLabel;

    //Tests
    @FXML
    private ListView<String> editTestsListView;
    @FXML
    private CheckBox newTestCheckBox;
    @FXML
    private TextField editTestNameTextField;
    @FXML
    private TextField editTestPointsTextField;
    private Vector<Test> editTestVector;
    @FXML
    private Button addTestButton;
    @FXML
    private Label editTestErrorLabel;

    //Instructors
    @FXML
    private ImageView instructorPFPImageView;
    private Image tmpInstructorImage;
    @FXML
    private CheckBox newInstructorCheckBox;
    @FXML
    private ListView<String> editInstructorListView;
    @FXML
    private TextField editInstructorNameTextField;
    private Vector<Instructor> editInstructorVector;
    @FXML
    private Button addInstructorButton;
    @FXML
    private Label editInstructorErrorLabel;

    ///////////////////////////////////////////////////////////////////////All Other Tab
    //Districts
    @FXML
    private ListView<String> editDistrictsListView;
    @FXML
    private CheckBox newDistrictCheckBox;
    @FXML
    private TextField editDistrictNameTextField;
    @FXML
    private TextField editSupervisorEmailTextField;
    private Vector<District> editDistrictVector;
    @FXML
    private Button addDistrictButton;
    @FXML
    private Label editDistrictErrorLabel;

    //Sectors
    @FXML
    private ListView<String> editSectorsListView;
    private Vector<Sector> editSectorVector;
    @FXML
    private CheckBox newSectorCheckBox;
    @FXML
    private TextField editSectorNameTextField;
    @FXML
    private Label editSectorErrorLabel;
    @FXML
    private Label sectorInstructionsLabel;
    @FXML
    private ListView<String> sectorDistrictsListView;
    private Vector<District> sectorDistrictsVector;
    private Vector<District> pendingRemovalsVector;
    @FXML
    private Button addSectorButton;


    @FXML
    protected void initialize() {

        controller = LifeguardTrainingApplication.getController();
        defaultImage = traineePFPImageView.getImage();
        tmpTraineeImage = null;
        tmpInstructorImage = null;
        traineeEventScores = new Vector<>();
        traineeTestScores = new Vector<>();
        traineeComments = new Vector<>();
        editEventVector = new Vector<>();
        editTestVector = new Vector<>();
        editDistrictVector = new Vector<>();
        editInstructorVector = new Vector<>();
        editSectorVector = new Vector<>();
        sectorDistrictsVector = new Vector<>();
        pendingRemovalsVector = new Vector<>();
//        traineeListView.setCellFactory(stringListView -> new CenteredListViewCell());
        traineeEventScoresListView.setCellFactory(stringListView -> new CenteredListViewCell());
        traineeTestScoresListView.setCellFactory(stringListView -> new CenteredListViewCell());
//        editEventsListView.setCellFactory(stringListView -> new CenteredListViewCell());
//        editTestsListView.setCellFactory(stringListView -> new CenteredListViewCell());
        editDistrictsListView.setCellFactory(stringListView -> new CenteredListViewCell());
        editInstructorListView.setCellFactory(stringListView -> new CenteredListViewCell());
        editSectorsListView.setCellFactory(stringListView -> new CenteredListViewCell());
        sectorDistrictsListView.setCellFactory(stringListView -> new CenteredListViewCell());
        traineeTabRefresh();
        importComboBox.getItems().addAll("Choose Import Type", "Comments", "Questionnaire 1",
                "Questionnaire 2", "Year's Trainee Info");
        tCIncidentComboBox.getItems().addAll("Academics", "Aquatic Skills", "Behavioral", "EMS Skills",
                "Injury", "Physical Performance", "Safety Violation");
        tCRotationComboBox.getItems().addAll("PSFA", "AQUATICS");
        tCNextStepsComboBox.getItems().addAll("Meet with Supervisor (Formal)", "Instructor Check-in (Informal)",
                "No Action Needed");

        sectorInstructionsLabel.setText("Select a District from the far left\nDistricts table, then click the below\nbutton to add to this sector.");

        //Sets up listeners
        importComboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.intValue()){

                //Comments picked
                case 1: onAddCommentsClicked();
                    break;
                //Trainee Info picked
                case 2: onAddQ1Clicked();
                    break;
                //Questionnaire 1 Picked
                case 3: onAddQ2Clicked();
                    break;
                //Questionnaire 2 Picked
                case 4: onAddTraineesClicked();
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
                eventScoreNameLabel.setText("Event Place: ");
                testScoreNameLabel.setText("Test Score: ");
                eventPlaceTextField.setPromptText("");
                eventPlaceTextField.clear();
                testScoreTextField.setPromptText("");
                testScoreTextField.clear();
                eventScoreErrorLabel.setVisible(false);
                testScoreErrorLabel.setVisible(false);
                traineeEventScoresListView.getItems().clear();
                traineeTestScoresListView.getItems().clear();

                traineeCommentsListView.getItems().clear();
                tCIncidentComboBox.getSelectionModel().clearSelection();
                tCIncidentDescriptionTextField.clear();
                tCIncidentDescriptionTextField.setPromptText("");
                tCActionsTextField.clear();
                tCActionsTextField.setPromptText("");
                tCInstructorTextField.clear();
                tCInstructorTextField.setPromptText("");
                tCRotationComboBox.getSelectionModel().clearSelection();
                tCNextStepsComboBox.getSelectionModel().clearSelection();
                tCDateTextField.clear();
                tCDateTextField.setPromptText("mm/dd/yyyy");

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

        tCNewCheckBox.selectedProperty().addListener((observable, oldValue, newValue) ->{

            if(newValue)
                traineeCommentsListView.getSelectionModel().clearSelection();

            tCIncidentComboBox.getSelectionModel().clearSelection();
            tCIncidentDescriptionTextField.clear();
            tCIncidentDescriptionTextField.setPromptText("");
            tCActionsTextField.clear();
            tCActionsTextField.setPromptText("");
            tCInstructorTextField.clear();
            tCInstructorTextField.setPromptText("");
            tCRotationComboBox.getSelectionModel().clearSelection();
            tCNextStepsComboBox.getSelectionModel().clearSelection();
            tCDateTextField.clear();
            tCDateTextField.setPromptText("mm/dd/yyyy");

        });

        //Refreshes the tab objects if selected.
        tabPane.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue)->{

            controller.updateCurrentSession(new Session(controller.getCurrentSession()));
            switch (newValue.intValue()){

                case 0: traineeTabRefresh();
                    traineeListView.getSelectionModel().select(0);
                    onTraineeListViewClicked();
                    break;
                case 1: eventInstructorTestTabRefresh();
                    break;
                case 2: allOtherTabRefresh();
                    break;

            }

        });

        newEventCheckBox.selectedProperty().addListener((observable, oldValue, newValue) ->{

            if(newValue){

                editEventsListView.getSelectionModel().clearSelection();
                addEventButton.setText("Add Event");

            }else
                addEventButton.setText("Update Event");


            editEventNameTextField.clear();
            editEventNameTextField.setPromptText("");
            editEventNotesTextField.clear();
            editEventNotesTextField.setPromptText("");
            editEventErrorLabel.setVisible(false);


        });

        newTestCheckBox.selectedProperty().addListener((observable, oldValue, newValue)->{

            if(newValue){

                editTestsListView.getSelectionModel().clearSelection();
                addTestButton.setText("Add Test");

            }else
                addTestButton.setText("Update Test");

            editTestNameTextField.clear();
            editTestNameTextField.setPromptText("");
            editTestPointsTextField.clear();
            editTestPointsTextField.setPromptText("");
            editTestErrorLabel.setVisible(false);


        });

        newDistrictCheckBox.selectedProperty().addListener((observable, oldValue, newValue) ->{

            if(newValue){

                editDistrictsListView.getSelectionModel().clearSelection();
                addDistrictButton.setText("Add District");

            }else
                addDistrictButton.setText("Update District");

            editDistrictNameTextField.clear();
            editDistrictNameTextField.setPromptText("");
            editSupervisorEmailTextField.clear();
            editSupervisorEmailTextField.setPromptText("");
            editDistrictErrorLabel.setVisible(false);

        });

        newInstructorCheckBox.selectedProperty().addListener((observable, oldValue, newValue) ->{

            if(newValue){

                editInstructorListView.getSelectionModel().clearSelection();
                addInstructorButton.setText("Add Instructor");
                instructorPFPImageView.setImage(defaultImage);

            }else
                addInstructorButton.setText("Update Instructor");

            editInstructorNameTextField.clear();
            editInstructorNameTextField.setPromptText("");
            editInstructorErrorLabel.setVisible(false);

        });

        newSectorCheckBox.selectedProperty().addListener((observable, oldValue, newValue) ->{

            if(newValue){

                editSectorsListView.getSelectionModel().clearSelection();
                addSectorButton.setText("Add Sector");
                sectorDistrictsVector.clear();
                pendingRemovalsVector.clear();

            }else
                addSectorButton.setText("Update Sector");

            editSectorNameTextField.clear();
            editSectorNameTextField.setPromptText("");
            editSectorErrorLabel.setVisible(false);
            sectorDistrictsListView.getItems().clear();
            sectorDistrictsListView.getSelectionModel().clearSelection();

        });

        if(controller.getCurrentTrainees().size() != 0){

            traineeListView.getSelectionModel().select(0);
            onTraineeListViewClicked();

        }

    }
    /*******************************************************************************************************************
     *                                        All Other Tab Methods
     ******************************************************************************************************************/

    /**
     * Populates the name text field and the list of districts in that sector.
     */
    public void onEditSectorListViewClicked(){

        int selectedIndex = editSectorsListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1)
            return;

        newSectorCheckBox.setSelected(false);

        Sector tmp = editSectorVector.get(selectedIndex);
        editSectorNameTextField.setPromptText(tmp.getName());

        ObservableList<String> sectorDistrictsOL = FXCollections.observableArrayList();
        sectorDistrictsVector.clear();
        pendingRemovalsVector.clear();
        sectorDistrictsVector = DBManager.getAllDistrictsFromSectorID(tmp.getSectorID());
        for(District district : Objects.requireNonNull(sectorDistrictsVector))
            sectorDistrictsOL.add(district.getName());

        sectorDistrictsListView.setItems(sectorDistrictsOL);

    }

    /**
     * Deletes the selected sector, setting all districts within it to the default null sector.
     */
    public void onDeleteSectorClicked(){

        int selectedIndex = editSectorsListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1)
            return;

        String str = "Are you sure you want to delete " + editSectorVector.get(selectedIndex).getName() +
                     "? This will unlink all Districts within this sector for this session.";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, str, ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.CANCEL)
            return;

        if(!DBManager.deleteSector(editSectorVector.get(selectedIndex))){
            Alert alert1 = new Alert(Alert.AlertType.ERROR, "Error Deleting " + editSectorVector.get(selectedIndex),
                                     ButtonType.CLOSE);
            alert1.show();
        }else{

            allOtherTabRefresh();

        }

    }

    /**
     * Adds or updates the sector.
     */
    public void onAddSectorClicked(){

        int selectedIndex = editSectorsListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1 && newSectorCheckBox.isSelected() && editSectorNameTextField.getText().isEmpty())
            return;

        Sector tmp = new Sector();
        tmp.setYear(controller.getCurrentSession().getYear());
        tmp.setSession(controller.getCurrentSession().getSession());

        //New Sector
        if(newSectorCheckBox.isSelected()){

            if(editSectorNameTextField.getText().isEmpty())
                editSectorErrorLabel.setVisible(true);

            for(Sector sector : editSectorVector){
                if(sector.getName().equals(editSectorNameTextField.getText())){
                    editSectorErrorLabel.setVisible(true);
                    return;
                }
            }

            tmp.setName(editSectorNameTextField.getText());

            if(DBManager.addSector(tmp)){
                int sectorID = Objects.requireNonNull(DBManager.getSectorFromSectorNameAndSession(tmp.getName(),
                        controller.getCurrentSession().getYear(),
                        controller.getCurrentSession().getSession())).getSectorID();
                for(District district : sectorDistrictsVector){

                    district.setSectorID(sectorID);
                    DBManager.updateDistrict(district, district.getName());

                }

                allOtherTabRefresh();

            }else
                editSectorErrorLabel.setVisible(true);

            //Update Sector
        }else{

            tmp = editSectorVector.get(selectedIndex);

            if(!editSectorNameTextField.getText().isEmpty()) {

                for(Sector sector : editSectorVector) {

                    if(sector.getName().equals(editSectorNameTextField.getText().trim())) {

                        editSectorErrorLabel.setVisible(true);
                        return;

                    }
                }

                tmp.setName(editSectorNameTextField.getText().trim());

            }

            if(DBManager.updateSector(tmp)) {

                for(District district : sectorDistrictsVector){

                    district.setSectorID(tmp.getSectorID());
                    DBManager.updateDistrict(district, district.getName());

                }
                for(District district : pendingRemovalsVector){

                    district.setSectorID(1);
                    DBManager.updateDistrict(district, district.getName());

                }

                allOtherTabRefresh();
                editSectorsListView.getSelectionModel().select(selectedIndex);
                onEditSectorListViewClicked();

            }else
                editSectorErrorLabel.setVisible(true);

        }

    }

    /**
     * Removes the selected district from the list of districts belonging to the new or selected sector.
     */
    public void onRemoveDistrictClicked(){

        int selectedDistrictIndex = sectorDistrictsListView.getSelectionModel().getSelectedIndex();
        int selectedSectorIndex = editSectorsListView.getSelectionModel().getSelectedIndex();
        if((newSectorCheckBox.isSelected() && selectedDistrictIndex != -1) || (selectedSectorIndex != -1 && selectedDistrictIndex != -1)) {

            pendingRemovalsVector.add(sectorDistrictsVector.get(selectedDistrictIndex));
            sectorDistrictsVector.remove(selectedDistrictIndex);
            ObservableList<String> tmpOL = sectorDistrictsListView.getItems();
            tmpOL.remove(selectedDistrictIndex);
            sectorDistrictsListView.setItems(tmpOL);

        }

    }

    /**
     * Prompts the user on what is going to happen, then adds the selected district to the pending district list view.
     */
    public void onAddDistrictToSector(){

        int selectedDistrictIndex = editDistrictsListView.getSelectionModel().getSelectedIndex();
        int selectedSectorIndex = editSectorsListView.getSelectionModel().getSelectedIndex();
        if((selectedDistrictIndex == -1 && selectedSectorIndex == -1) || (newSectorCheckBox.isSelected() && selectedDistrictIndex == -1))
            return;

        District selectedDistrict = editDistrictVector.get(selectedDistrictIndex);

        if(selectedDistrict.getSectorID() == 1){

            if(!sectorDistrictsVector.contains(selectedDistrict)) {

                sectorDistrictsVector.add(selectedDistrict);
                ObservableList<String> tmpOL = sectorDistrictsListView.getItems();
                tmpOL.add(selectedDistrict.getName());
                sectorDistrictsListView.setItems(tmpOL);

            }

        }else {

            String str;
            if(newSectorCheckBox.isSelected()){

                str = selectedDistrict.getName() + " is currently under the " +
                        Objects.requireNonNull(DBManager.getSectorFromSectorID(selectedDistrict.getSectorID())).getName()
                        + " sector. Do you want to change it to belong to the new sector?";

            }else{

                str = selectedDistrict.getName() + " is currently under the " +
                        Objects.requireNonNull(DBManager.getSectorFromSectorID(selectedDistrict.getSectorID())).getName()
                        + " sector. Do you want to change it to the " +
                        editSectorVector.get(selectedSectorIndex).getName() + " sector?";

            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, str, ButtonType.OK, ButtonType.CANCEL);
            alert.showAndWait();

            if(alert.getResult() == ButtonType.CANCEL)
                return;

            if(!sectorDistrictsVector.contains(selectedDistrict)){

                sectorDistrictsVector.add(selectedDistrict);
                ObservableList<String> tmpOL = sectorDistrictsListView.getItems();
                tmpOL.add(selectedDistrict.getName());
                sectorDistrictsListView.setItems(tmpOL);

            }

        }

    }

    /**
     * Refreshes the page's data with the most current data from controller.
     */
    public void allOtherTabRefresh(){

        editDistrictVector = DBManager.getAllDistrictsFromSession(controller.getCurrentSession().getYear(),
                                                                    controller.getCurrentSession().getSession());
        editSectorVector = DBManager.getAllSectorsFromSession(controller.getCurrentSession().getYear(),
                                                              controller.getCurrentSession().getSession());
        ObservableList<String> districtOL = FXCollections.observableArrayList();
        ObservableList<String> sectorOL = FXCollections.observableArrayList();

        for(District district : Objects.requireNonNull(editDistrictVector)){
            if(district.getSupervisorEmail().isEmpty())
                districtOL.add(district.getName());
            else
                districtOL.add(district.getName() + " | " + district.getSupervisorEmail());
        }

        for(Sector sector : Objects.requireNonNull(editSectorVector))
            sectorOL.add(sector.getName());

        editDistrictsListView.setItems(districtOL);
        editDistrictErrorLabel.setVisible(false);
        editDistrictsListView.getSelectionModel().clearSelection();
        editDistrictNameTextField.clear();
        editDistrictNameTextField.setPromptText("");
        editSupervisorEmailTextField.clear();
        editSupervisorEmailTextField.setPromptText("");

        editSectorsListView.setItems(sectorOL);
        editSectorsListView.getSelectionModel().clearSelection();
        editSectorErrorLabel.setVisible(false);
        sectorDistrictsListView.getItems().clear();
        sectorDistrictsListView.getSelectionModel().clearSelection();
        editSectorNameTextField.clear();
        editSectorNameTextField.setPromptText("");

    }

    /**
     * Populates the textfields with the data of the selected district, if applicable.
     */
    public void onEditDistrictListViewClicked(){

        int selectedIndex = editDistrictsListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1)
            return;

        newDistrictCheckBox.setSelected(false);

        District tmp = editDistrictVector.get(selectedIndex);
        editDistrictNameTextField.setPromptText(tmp.getName());
        editSupervisorEmailTextField.setPromptText(tmp.getSupervisorEmail());

    }

    /**
     * Adds or updates the district.
     */
    public void onAddDistrictClicked(){

        int selectedIndex = editDistrictsListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1 && !newDistrictCheckBox.isSelected())
            return;

        District tmp = new District();
        tmp.setYear(controller.getCurrentSession().getYear());
        tmp.setSession(controller.getCurrentSession().getSession());
        //Email address regex
        String regex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

        //New District
        if(newDistrictCheckBox.isSelected()){

            if(editDistrictNameTextField.getText().isEmpty()){
                editDistrictErrorLabel.setVisible(true);
            }

            boolean isFound = false;
            for(District district : editDistrictVector)
                if(district.getName().equals(editDistrictNameTextField.getText())){
                    isFound = true;
                    break;
                }

            if(isFound){
                editDistrictErrorLabel.setVisible(true);
                return;
            }

            tmp.setName(editDistrictNameTextField.getText().trim());


            if(editSupervisorEmailTextField.getText().isEmpty())
                tmp.setSupervisorEmail("");
            else {

                if (!editSupervisorEmailTextField.getText().matches(regex)) {
                    editDistrictErrorLabel.setVisible(true);
                    return;
                } else
                    tmp.setSupervisorEmail(editSupervisorEmailTextField.getText().trim());

            }
            DBManager.addDistrict(tmp);
            eventInstructorTestTabRefresh();

            //Update District
        }else{

            String oldName = editDistrictVector.get(selectedIndex).getName();
            tmp = editDistrictVector.get(selectedIndex);

            if(!editDistrictNameTextField.getText().isEmpty()) {

                boolean isFound = false;
                for (District district : editDistrictVector) {
                    if (district.getName().equals(editEventNameTextField.getText().trim())) {
                        isFound = true;
                        break;
                    }
                }
                if (isFound) {
                    editDistrictErrorLabel.setVisible(true);
                    return;
                }

                tmp.setName(editDistrictNameTextField.getText().trim());

            }

            if(!editSupervisorEmailTextField.getText().isEmpty() && editSupervisorEmailTextField.getText().matches(regex))
                tmp.setSupervisorEmail(editSupervisorEmailTextField.getText());
            else if(!editSupervisorEmailTextField.getText().isEmpty()){

                editDistrictErrorLabel.setVisible(true);
                return;

            }else
                tmp.setSupervisorEmail(editSupervisorEmailTextField.getPromptText());

            DBManager.updateDistrict(tmp, oldName);
            eventInstructorTestTabRefresh();
            editDistrictsListView.getSelectionModel().select(selectedIndex);
            onEditDistrictListViewClicked();

        }

    }

    /**
     * Deletes the selected district.
     */
    public void onDeleteDistrictClicked(){

        District tmp = editDistrictVector.get(editDistrictsListView.getSelectionModel().getSelectedIndex());

        String str = "Deleting " + tmp.getName() + ".\nDo you want to continue?";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, str, ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.CANCEL)
            return;

        DBManager.deleteDistrict(tmp);
        allOtherTabRefresh();

    }


    /*******************************************************************************************************************
     *                                        Events | Instructors | Tests Tab Methods
     ******************************************************************************************************************/

    /**
     * Runs when the Instructor pfp image view is clicked, allowing the user to change their image.
     */
    public void onIPFPClicked(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG Images", "*.jpg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Images", "*.png"));
        File selectedFile = fileChooser.showOpenDialog(LifeguardTrainingApplication.getCoordinator().getStage());
        if(selectedFile == null)
            return;

        //If successful, save image to memory for later.
        tmpInstructorImage = new Image(selectedFile.toURI().toString(),
                0, 250, true, true);
        instructorPFPImageView.setPreserveRatio(true);
        instructorPFPImageView.setImage(tmpInstructorImage);
        VBox.setMargin(instructorPFPImageView, new Insets(0, 0,
                139 - Math.ceil(instructorPFPImageView.getBoundsInLocal().getHeight()), 0));

    }

    /**
     * Populates the data fields with the selected instructor's data, if applicable.
     */
    public void onEditInstructorListViewClicked(){

        int selectedIndex = editInstructorListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1)
            return;

        newInstructorCheckBox.setSelected(false);

        Instructor tmp = editInstructorVector.get(selectedIndex);
        editInstructorNameTextField.setPromptText(tmp.getName());

        //Makes sure the image resizing does not affect the other UI objects
        instructorPFPImageView.setPreserveRatio(true);
        instructorPFPImageView.setSmooth(true);
        instructorPFPImageView.setCache(true);
        instructorPFPImageView.setImage(tmp.getActualImage());
        VBox.setMargin(instructorPFPImageView, new Insets(0, 0,
                139 - Math.ceil(instructorPFPImageView.getBoundsInLocal().getHeight()), 0));

    }

    /**
     * Adds or updates the selected instructor, if valid.
     */
    public void onAddInstructorClicked(){

        int selectedIndex = editInstructorListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1 && !newInstructorCheckBox.isSelected())
            return;

        Instructor tmp = new Instructor();
        tmp.setYear(controller.getCurrentSession().getYear());
        tmp.setSession(controller.getCurrentSession().getSession());

        //New Instructor
        if(newInstructorCheckBox.isSelected()){

            if(editInstructorNameTextField.getText().isEmpty()){
                editInstructorErrorLabel.setVisible(true);
                return;
            }

            boolean isFound = false;
            for(Instructor instructor : editInstructorVector){
                if(instructor.getName().equals(editInstructorNameTextField.getText())){
                    isFound = true;
                    break;
                }
            }
            if(isFound){
                editInstructorErrorLabel.setVisible(true);
                return;
            }

            tmp.setName(editInstructorNameTextField.getText());
            //Updates PFP if needed
            if(!instructorPFPImageView.getImage().equals(defaultImage))
                tmp.setImage(instructorPFPImageView.getImage());
            else
                tmp.setImage(defaultImage);

            DBManager.addInstructor(tmp);
            eventInstructorTestTabRefresh();

            //Update Instructor
        }else{

            String oldName = editInstructorVector.get(selectedIndex).getName();
            tmp = editInstructorVector.get(selectedIndex);

            if(!editInstructorNameTextField.getText().isEmpty()){

                boolean isFound = false;
                for(Instructor instructor : editInstructorVector){
                    if(instructor.getName().equals(editInstructorNameTextField.getText())){
                        isFound = true;
                        break;
                    }
                }
                if(isFound){
                    editEventErrorLabel.setVisible(true);
                    return;
                }

                tmp.setName(editInstructorNameTextField.getText());

            }

            if(!instructorPFPImageView.getImage().equals(tmp.getImage()))
                tmp.setImage(instructorPFPImageView.getImage());

            DBManager.updateInstructor(tmp, oldName);
            eventInstructorTestTabRefresh();
            editInstructorListView.getSelectionModel().select(selectedIndex);
            onEditInstructorListViewClicked();

        }

    }

    /**
     * Deletes the selected instructor.
     */
    public void onDeleteInstructorClicked(){

        Instructor tmp = editInstructorVector.get(editInstructorListView.getSelectionModel().getSelectedIndex());

        String str = "Deleting " + tmp.getName() + ".\nDo you want to continue?";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, str, ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.CANCEL)
            return;

        DBManager.deleteInstructor(tmp);
        eventInstructorTestTabRefresh();

    }

    /**
     * Refreshes the page's data with the most current data from Controller
     */
    public void eventInstructorTestTabRefresh(){

        instructorPFPImageView.setImage(defaultImage);
        newInstructorCheckBox.setSelected(true);
        editEventVector = controller.getCurrentEvents();
        editTestVector = controller.getCurrentTests();

        editInstructorVector = DBManager.getAllInstructorsFromSession(controller.getCurrentSession().getYear(),
                                                                      controller.getCurrentSession().getSession());

        ObservableList<String> eventOL = FXCollections.observableArrayList();
        ObservableList<String> testOL = FXCollections.observableArrayList();

        ObservableList<String> instructorOL = FXCollections.observableArrayList();

        for(Event event : editEventVector)
            eventOL.add(event.getName());
        for(Test test : editTestVector)
            testOL.add(test.getName() + " | " + test.getPoints());
        for(Instructor instructor : Objects.requireNonNull(editInstructorVector))
            instructorOL.add(instructor.getName());

        editEventsListView.setItems(eventOL);
        editTestsListView.setItems(testOL);
        editInstructorListView.setItems(instructorOL);
        editEventErrorLabel.setVisible(false);
        editTestErrorLabel.setVisible(false);
        editEventsListView.getSelectionModel().clearSelection();
        editTestsListView.getSelectionModel().clearSelection();
        editInstructorListView.getSelectionModel().clearSelection();
        editEventNameTextField.clear();
        editEventNameTextField.setPromptText("");
        editEventNotesTextField.clear();
        editEventNotesTextField.setPromptText("");
        editTestNameTextField.clear();
        editTestNameTextField.setPromptText("");
        editTestPointsTextField.clear();
        editTestPointsTextField.setPromptText("");
        editInstructorNameTextField.clear();
        editInstructorNameTextField.setPromptText("");

    }

    /**
     * Populates the text fields with the selected event's data.
     */
    public void onEditEventListViewClicked(){

        int selectedIndex = editEventsListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1)
            return;

        newEventCheckBox.setSelected(false);

        Event tmp = editEventVector.get(selectedIndex);
        editEventNameTextField.setPromptText(tmp.getName());
        editEventNotesTextField.setPromptText(tmp.getNotes());

    }

    /**
     * Adds or updates the event
     */
    public void onAddEventClicked(){

        int selectedIndex = editEventsListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1 && !newEventCheckBox.isSelected())
            return;

        Event tmp = new Event();
        tmp.setYear(controller.getCurrentSession().getYear());
        tmp.setSession(controller.getCurrentSession().getSession());
        tmp.setEventID(0);

        //New Event
        if(newEventCheckBox.isSelected()){

            if(editEventNameTextField.getText().isEmpty()){
                editEventErrorLabel.setVisible(true);
                return;
            }

            boolean isFound = false;
            for(Event event : editEventVector){
                if(event.getName().equals(editEventNameTextField.getText())){
                    isFound = true;
                    break;
                }
            }
            if(isFound){
                editEventErrorLabel.setVisible(true);
                return;
            }

            if(editEventNotesTextField.getText().isEmpty())
                editEventNotesTextField.setText("");

            tmp.setName(editEventNameTextField.getText());
            tmp.setNotes(editEventNotesTextField.getText());


            DBManager.addEvent(tmp);
            controller.updateCurrentEvents();
            eventInstructorTestTabRefresh();

            //Update Event
        }else{

            tmp = editEventVector.get(selectedIndex);

            if(!editEventNameTextField.getText().isEmpty()){

                boolean isFound = false;
                for(Event event : editEventVector){
                    if(event.getName().equals(editEventNameTextField.getText())){
                        isFound = true;
                        break;
                    }
                }
                if(isFound){
                    editEventErrorLabel.setVisible(true);
                    return;
                }

                tmp.setName(editEventNameTextField.getText());

            }

            if(!editEventNotesTextField.getText().isEmpty())
                tmp.setNotes(editEventNotesTextField.getText());

            DBManager.updateEvent(tmp);
            eventInstructorTestTabRefresh();
            editEventsListView.getSelectionModel().select(selectedIndex);
            onEditEventListViewClicked();

        }

    }

    /**
     * Deletes the selected event
     */
    public void onDeleteEventClicked(){

        Event tmp = editEventVector.get(editEventsListView.getSelectionModel().getSelectedIndex());

        String str = "Deleting " + tmp.getName()
                    + " will remove ALL event scores associated with this event.\nDo you want to continue?";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, str, ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.CANCEL)
            return;

        DBManager.deleteAllEventScoresOfAnEvent(tmp.getEventID());
        DBManager.deleteEvent(tmp);
        controller.updateCurrentEvents();
        eventInstructorTestTabRefresh();

    }

    /**
     * Populates the textfields with the selected test's data
     */
    public void onEditTestListViewClicked(){

        int selectedIndex = editTestsListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1)
            return;

        newTestCheckBox.setSelected(false);

        Test tmp = editTestVector.get(selectedIndex);
        editTestNameTextField.setPromptText(tmp.getName());
        editTestPointsTextField.setPromptText(String.valueOf(tmp.getPoints()));

    }

    /**
     * Adds or updates the test
     */
    public void onAddTestClicked(){

        int selectedIndex = editTestsListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1 && !newTestCheckBox.isSelected())
            return;

        Test tmp = new Test();
        tmp.setYear(controller.getCurrentSession().getYear());
        tmp.setSession(controller.getCurrentSession().getSession());
        tmp.setTestID(0);

        //New Test
        if(newTestCheckBox.isSelected()){

            if(editTestNameTextField.getText().isEmpty() || editTestPointsTextField.getText().isEmpty()){

                editTestErrorLabel.setVisible(true);
                return;

            }
            boolean isFound = false;
            for(Test test : editTestVector){
                if(test.getName().equals(editTestNameTextField.getText())){
                    isFound = true;
                    break;
                }
            }
            if(isFound){
                editTestErrorLabel.setVisible(true);
                return;
            }
            tmp.setName(editTestNameTextField.getText());

            if(isInteger(editTestPointsTextField.getText()) && Integer.parseInt(editTestPointsTextField.getText()) > 0)
                tmp.setPoints(Integer.parseInt(editTestPointsTextField.getText()));
            else{

                editTestErrorLabel.setVisible(true);
                return;

            }

            DBManager.addTest(tmp);
            controller.updateCurrentTests();
            eventInstructorTestTabRefresh();

            //Update Test
        }else{

            tmp.setTestID(editTestVector.get(selectedIndex).getTestID());
            tmp.setName(editTestVector.get(selectedIndex).getName());
            tmp.setPoints(editTestVector.get(selectedIndex).getPoints());

            if(!editTestNameTextField.getText().isEmpty()){

                boolean isFound = false;
                for(Test test : editTestVector){
                    if(test.getName().equals(editTestNameTextField.getText())){
                        isFound = true;
                        break;
                    }
                }
                if(isFound){
                    System.out.println("FOUND");
                    editTestErrorLabel.setVisible(true);
                    return;
                }

                tmp.setName(editTestNameTextField.getText());

            }

            if(!editTestPointsTextField.getText().isEmpty() && isInteger(editTestPointsTextField.getText()) &&
                Integer.parseInt(editTestPointsTextField.getText()) > 0)
                tmp.setPoints(Integer.parseInt(editTestPointsTextField.getText()));

            if(!editTestPointsTextField.getText().isEmpty() && (!isInteger(editTestPointsTextField.getText())
                    || Integer.parseInt(editTestPointsTextField.getText()) < 1)){
                System.out.println("HERE");
                editTestErrorLabel.setVisible(true);
                return;
            }

            String str = "Are you sure you want to set the point value of " + tmp.getName() + " to " + tmp.getPoints() + "?\n"
                        + "It will change all trainee test scores to a value of the same ratio as before.";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, str, ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();

            if(alert.getResult() == ButtonType.CANCEL)
                return;

            //Updates all Trainee Test Scores with the new ratio given.
            if(editTestVector.get(selectedIndex).getPoints() != tmp.getPoints()){

                Vector<TestScore> testScoresToChange = DBManager.getAllTestScoresFromTestID(tmp.getTestID());
                double ratio = editTestVector.get(selectedIndex).getPoints() * 1.0 / tmp.getPoints();
                for(TestScore score : testScoresToChange){

                    score.setScore((int)Math.ceil(score.getScore() / ratio));
                    DBManager.updateTestScore(score);

                }

            }

            DBManager.updateTest(tmp);
            controller.updateCurrentTests();
            eventInstructorTestTabRefresh();
            editTestsListView.getSelectionModel().select(selectedIndex);
            onEditTestListViewClicked();

        }

    }

    /**
     * Deletes the selected test
     */
    public void onDeleteTestClicked(){

        Test tmp = editTestVector.get(editTestsListView.getSelectionModel().getSelectedIndex());

        String str = "Deleting " + tmp.getName()
                + " will remove ALL test scores associated with this test.\nDo you want to continue?";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, str, ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.CANCEL)
            return;

        DBManager.deleteAllTestScoresOfATest(tmp.getTestID());
        DBManager.deleteTest(tmp);
        controller.updateCurrentTests();
        eventInstructorTestTabRefresh();

    }

    /*******************************************************************************************************************
     *                                        Trainee Tab Methods
     ******************************************************************************************************************/

    /**
     * Refreshes the page's data with the most current data from Controller.
     */
    private void traineeTabRefresh() {

        addTraineeButton.setText("Add New Trainee");
        holdsEditQuestionnaireData = new Trainee();
        try {
            traineePFPImageView.setPreserveRatio(true);
            traineePFPImageView.setImage(new Image(getClass().getClassLoader().getResource("org/openjfx/images/blankpfp.png").toURI().toString(),
                    0, 250, true, true));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        };
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
        eventScoreNameLabel.setText("Event Place: ");
        testScoreNameLabel.setText("Test Score: ");
        eventPlaceTextField.setPromptText("");
        eventPlaceTextField.clear();
        testScoreTextField.setPromptText("");
        testScoreTextField.clear();
        eventScoreErrorLabel.setVisible(false);
        testScoreErrorLabel.setVisible(false);

        traineeCommentsListView.getItems().clear();
        tCIncidentComboBox.getSelectionModel().clearSelection();
        tCIncidentDescriptionTextField.clear();
        tCIncidentDescriptionTextField.setPromptText("");
        tCActionsTextField.clear();
        tCActionsTextField.setPromptText("");
        tCInstructorTextField.clear();
        tCInstructorTextField.setPromptText("");
        tCRotationComboBox.getSelectionModel().clearSelection();
        tCNextStepsComboBox.getSelectionModel().clearSelection();
        tCDateTextField.clear();
        tCDateTextField.setPromptText("mm/dd/yyyy");

    }

    /**
     * Shows the user a table of all inactive trainees which can be reactivated, and prompts them to enter in the needed
     * data.
     */
    public void onViewInactiveTraineesClicked(){

        //Initialize Dialog box contents for session inputs.
        final Stage dialog = new Stage();
        dialog.setTitle("Re-activate Trainees");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(LifeguardTrainingApplication.getCoordinator().getStage());
        VBox dialogVBox = new VBox();
        Label label = new Label("          These are the inactive trainees,\nselect one, enter their info, and reactivate.");
        label.setFont(new Font("System", 14));
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: #efb748;");
        label.setAlignment(Pos.CENTER);

        ListView<Trainee> inactiveTraineesListView = new ListView<>();
        inactiveTraineesListView.setCellFactory(stringListView -> new CenteredTraineeListViewCell());
        inactiveTraineesListView.setMaxWidth(200);
        inactiveTraineesListView.setMaxHeight(250);
        Vector<Trainee> inactiveTrainees = DBManager.getAllTraineesFromSession(controller.getCurrentSession().getYear(),
                                                                               controller.getCurrentSession().getSession());
        Objects.requireNonNull(inactiveTrainees).removeIf(Trainee::isActive);
        if(inactiveTrainees.size() == 0)
            return;

        ObservableList<Trainee> inactiveTraineesOL = FXCollections.observableArrayList();
        inactiveTraineesOL.setAll(inactiveTrainees);
        inactiveTraineesListView.setItems(inactiveTraineesOL);


        Label tableViewInstructions = new Label("Catch up the missed test scores!");
        tableViewInstructions.setFont(new Font("System", 14));
        tableViewInstructions.setStyle("-fx-font-weight: bold; -fx-text-fill: #efb748;");
        tableViewInstructions.setAlignment(Pos.CENTER);

        //Creates and fills the Test Table View
        TableView<AddExamsData> testTableView = new TableView<>();
        testTableView.requestFocus();
        testTableView.layout();
        testTableView.setEditable(true);
        testTableView.setMaxHeight(200);

        TableColumn<AddExamsData, String> examColumn = new TableColumn<>("Name");
        examColumn.setEditable(false);
        examColumn.setSortable(false);
        examColumn.setCellValueFactory(new PropertyValueFactory<>("examName"));
        examColumn.setMinWidth(140);
        TableColumn<AddExamsData, String> scoreColumn = new TableColumn<>("Enter Score:");
        scoreColumn.setEditable(true);
        scoreColumn.setSortable(false);
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("enteredScore"));
        scoreColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        TableColumn<AddExamsData, String> maxScoreColumn = new TableColumn<>("Max Score");
        maxScoreColumn.setEditable(false);
        maxScoreColumn.setSortable(false);
        maxScoreColumn.setCellValueFactory(new PropertyValueFactory<>("maxScore"));
        testTableView.getColumns().addAll(examColumn, scoreColumn, maxScoreColumn);

        Label hoursLabel = new Label();
        hoursLabel.setFont(new Font("System", 14));
        hoursLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #efb748;");
        hoursLabel.setAlignment(Pos.CENTER);
        TextField hoursTextField = new TextField();
        hoursTextField.setPromptText("New cumulative value");
        hoursTextField.setAlignment(Pos.CENTER);
        HBox hoursHBox = new HBox();
        hoursHBox.setAlignment(Pos.CENTER);
        hoursHBox.setSpacing(5);
        hoursHBox.getChildren().addAll(hoursLabel, hoursTextField);
        hoursHBox.setVisible(false);

        //Populates the table with the selected trainee's relevant data
        inactiveTraineesListView.setOnMouseClicked(event -> {

            int selectedIndex = inactiveTraineesListView.getSelectionModel().getSelectedIndex();
            if(selectedIndex == -1)
                return;

            Vector<Test> completedTests = new Vector<>(Objects.requireNonNull(
                    DBManager.getAllTestsFromSession(controller.getCurrentSession().getYear(),
                                                     controller.getCurrentSession().getSession())));
            //Remove non scored tests from the list
            completedTests.removeIf(e -> !e.isScored());
            //Remove trainee completed tests from list
            Trainee selectedTrainee = inactiveTrainees.get(selectedIndex);
            Vector<TestScore> traineeScores = DBManager.getAllTestScoresFromTraineeID(selectedTrainee.getId());
            Vector<Test> toBeRemoved = new Vector<>();
            for(TestScore ts : traineeScores){

                for (Test completedTest : completedTests) {

                    if (ts.getTestID() == completedTest.getTestID()) {

                        toBeRemoved.add(completedTest);
                        break;

                    }
                }
            }

            completedTests.removeAll(toBeRemoved);

            ObservableList<AddExamsData> data = FXCollections.observableArrayList();
            for(Test test : completedTests)
                data.add(new AddExamsData(test.getName(), "0", String.valueOf(test.getPoints())));

            testTableView.setItems(data);
            testTableView.setPlaceholder(new Label("No missing test scores"));

            hoursLabel.setText("Current Hours: " + selectedTrainee.getHoursAttended());
            hoursHBox.setVisible(true);


        });

        //Action event where the user presses enter to enter that cell, which then increments the edit focus to the
        //next cell.
        scoreColumn.setOnEditCommit((TableColumn.CellEditEvent<AddExamsData, String> t) -> {

            t.getTableView().getItems().get(t.getTablePosition().getRow()).setEnteredScore(t.getNewValue());
            int focusedIndex = testTableView.getSelectionModel().getSelectedIndex();
            if(focusedIndex + 1 < t.getTableView().getItems().size()){

                Platform.runLater(() -> {
                    testTableView.edit(focusedIndex + 1, scoreColumn);
                    testTableView.getSelectionModel().select(focusedIndex + 1);
                });
            }

        });

        Label missedEventsLabel = new Label("All missed events by this trainee\nwill be set to last place by" +
                                            " default.\nThey can manually be changed later.");
        missedEventsLabel.setFont(new Font("System", 14));
        missedEventsLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #efb748;");
        missedEventsLabel.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Reactivate");
        saveButton.setStyle("-fx-font-weight: bold");
        Button cancelButton = new Button("Cancel");
        HBox buttonHBox = new HBox(saveButton, cancelButton);

        Label activateErrorLabel = new Label("*Error - enter valid info*");
        activateErrorLabel.setAlignment(Pos.CENTER);
        activateErrorLabel.setVisible(false);
        activateErrorLabel.setStyle("-fx-text-fill: #57ff8c");

        //Saves the entered data, and reactivates the trainee.
        saveButton.setOnMouseClicked(event -> {

            Trainee selectedTrainee = inactiveTrainees.get(inactiveTraineesListView.getSelectionModel().getSelectedIndex());

            //Verify Entered Data
            for(int i = 0; i < testTableView.getItems().size(); i++){
                String tmp = scoreColumn.getCellObservableValue(i).getValue();
                if(tmp.isEmpty() || !isInteger(tmp) ||
                        Integer.parseInt(maxScoreColumn.getCellObservableValue(i).getValue()) < Integer.parseInt(tmp) ||
                        Integer.parseInt(tmp) < 0){

                    activateErrorLabel.setVisible(true);
                    return;

                }

            }
            if(hoursTextField.getText().isEmpty() || !isInteger(hoursTextField.getText()) ||
                    Integer.parseInt(hoursTextField.getText()) < selectedTrainee.getHoursAttended() ||
                    Integer.parseInt(hoursTextField.getText()) > 100){

                activateErrorLabel.setVisible(true);
                return;

            }

            Vector<Test> tests = new Vector<>(Objects.requireNonNull(
                            DBManager.getAllTestsFromSession(controller.getCurrentSession().getYear(),
                            controller.getCurrentSession().getSession())));

            //Save New Data
            for(int i = 0; i < testTableView.getItems().size(); i++){

                int testID = -1;
                for(Test test : tests)
                    if(test.getName().equals(examColumn.getCellObservableValue(i).getValue())) {
                        testID = test.getTestID();
                        break;
                    }

                DBManager.addTestScore(new TestScore(testID, selectedTrainee.getId(),
                                                     Integer.parseInt(scoreColumn.getCellObservableValue(i).getValue())));

            }

            selectedTrainee.setHoursAttended(Integer.parseInt(hoursTextField.getText()));
            selectedTrainee.setActive(true);
            DBManager.updateTraineeHours(selectedTrainee);
            DBManager.setTraineeActive(selectedTrainee);

            dialog.close();
            controller.updateCurrentTrainees();
            traineeTabRefresh();

        });

        cancelButton.setOnMouseClicked(event -> dialog.close());

        dialogVBox.getChildren().addAll(label, inactiveTraineesListView, tableViewInstructions, testTableView, hoursHBox,
                                        missedEventsLabel, activateErrorLabel, buttonHBox);

        //Set dialog box style
        buttonHBox.setSpacing(10);
        buttonHBox.setAlignment(Pos.CENTER);
        dialogVBox.setAlignment(Pos.CENTER);
        dialogVBox.setSpacing(10);
        dialogVBox.setStyle("-fx-background-color: #3476f7;");
        Scene dialogScene = new Scene(dialogVBox, 300, 700);
        dialog.setScene(dialogScene);
        dialog.showAndWait();

    }

    /**
     * Allows the user to delete a selected trainee. This is only advisable if the session is effectively yet to have
     * started.
     */
    public void onDeleteSelectedTraineeClicked(){

        int selectedIndex = traineeListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1)
            return;

        Alert alert = new Alert(Alert.AlertType.WARNING, "Please do not delete a trainee once there has been" +
                " scores and attendance recorded. Unforeseen errors MAY occur after.\n\n" +
                "ONLY CONTINUE IF THIS IS A PRELIMINARY DELETION. CANNOT BE UNDONE", ButtonType.OK, ButtonType.CANCEL);

        alert.showAndWait();

        if(alert.getResult() == ButtonType.CANCEL)
            return;

        if(!DBManager.deleteTrainee(controller.getCurrentTrainees().get(selectedIndex))){

            Alert newAlert = new Alert(Alert.AlertType.ERROR, "Something went wrong deleting the trainee!" +
                    "\nI warned you lmao", ButtonType.OK);
            newAlert.showAndWait();

        }

        controller.updateCurrentSession(controller.getCurrentSession());
        traineeTabRefresh();

    }

    /**
     * Sets the selected trainee to inactive, removing them from runnings but keeping their data for reports.
     */
    public void onSetTraineeInactiveClicked(){

        int selectedIndex = traineeListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1)
            return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are about to set " +
                controller.getCurrentTrainees().get(selectedIndex).getFullName() + " as INACTIVE.\nThis means they will "
                + "be removed from all rankings and lists, however their info will persist on reports.\nMAKE SURE TO GIVE TODAY'S ATTENDANCE BEFORE INACTIVATING ANYONE!",
                ButtonType.APPLY, ButtonType.CANCEL);

        alert.showAndWait();
        //If this was a mistake, leave, if not, continue
        if (alert.getResult() == ButtonType.CANCEL)
            return;

        DBManager.setTraineeInactive(controller.getCurrentTrainees().get(selectedIndex));
        controller.updateCurrentTrainees();
        traineeTabRefresh();

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
        traineeCommentsListView.getItems().clear();
        tCErrorLabel.setVisible(false);

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

        tECNameTextField.clear();
        tECRelationshipTextField.clear();
        tECPhoneNumberTextField.clear();
        tECAddressTextField.clear();
        tECCityTextField.clear();
        tECStateTextField.clear();
        tECZipcodeTextField.clear();

        //Populates Emergency Contact data fields.
        if(tmp.getEmergencyContact() != null){


            tECNameTextField.setPromptText(tmp.getEmergencyContact().getFullName());
            tECRelationshipTextField.setPromptText(tmp.getEmergencyContact().getRelationship());
            tECPhoneNumberTextField.setPromptText(tmp.getEmergencyContact().getPhoneNumber());
            tECAddressTextField.setPromptText(tmp.getEmergencyContact().getAddress());
            tECCityTextField.setPromptText(tmp.getEmergencyContact().getCity());
            tECStateTextField.setPromptText(tmp.getEmergencyContact().getState());
            tECZipcodeTextField.setPromptText(tmp.getEmergencyContact().getZipcode());

        }else{

            tECNameTextField.setPromptText("");
            tECRelationshipTextField.setPromptText("");
            tECAddressTextField.setPromptText("");
            tECCityTextField.setPromptText("");
            tECStateTextField.setPromptText("");
            tECPhoneNumberTextField.setPromptText("xxx-xxx-xxxx");
            tECZipcodeTextField.setPromptText("xxxxx");

        }

        //Populates trainee event scores info.
        traineeEventScores = DBManager.getAllEventScoresFromTraineeID(tmp.getId());
        if(traineeEventScores != null){

            Vector<Integer> eventIDs = new Vector<>();
            for(EventScore score : traineeEventScores)
                eventIDs.add(score.getEventID());
            associatedTraineeEvents = DBManager.getAllEventsFromEventIDs(eventIDs);

            ObservableList<String> traineeEventScoresList = FXCollections.observableArrayList();
            for(EventScore score : traineeEventScores){

                for(Event event : Objects.requireNonNull(associatedTraineeEvents)){

                    if(event.getEventID() == score.getEventID()) {

                        traineeEventScoresList.add(event.getName() + " | " + getPlaceSuffix(score.getPlace()));
                        break;

                    }
                }
            }

            traineeEventScoresListView.setItems(traineeEventScoresList);

        }

        //Populates trainee test scores info.
        traineeTestScores = DBManager.getAllTestScoresFromTraineeID(tmp.getId());
        if(traineeTestScores != null){

            Vector<Integer> testIDs = new Vector<>();
            for(TestScore score :  traineeTestScores)
                testIDs.add(score.getTestID());
            associatedTraineeTests = DBManager.getAllTestFromTestIDs(testIDs);

            ObservableList<String> traineeTestScoresList = FXCollections.observableArrayList();
            for(TestScore score : traineeTestScores){

                for(Test test : Objects.requireNonNull(associatedTraineeTests)){

                    if(test.getTestID() == score.getTestID()){

                        traineeTestScoresList.add(test.getName() + " | " + score.getScore() + "/" + test.getPoints());
                        break;

                    }
                }
            }

            traineeTestScoresListView.setItems(traineeTestScoresList);

        }

        tCIncidentComboBox.getSelectionModel().clearSelection();
        tCNewCheckBox.setSelected(true);
        tCIncidentDescriptionTextField.clear();
        tCIncidentDescriptionTextField.setPromptText("");
        tCActionsTextField.clear();
        tCActionsTextField.setPromptText("");
        tCInstructorTextField.clear();
        tCInstructorTextField.setPromptText("");
        tCRotationComboBox.getSelectionModel().clearSelection();
        tCNextStepsComboBox.getSelectionModel().clearSelection();
        tCDateTextField.clear();
        tCDateTextField.setPromptText("mm/dd/yyyy");

        //Populates the comment info.
        traineeComments = DBManager.getAllCommentsFromTID(tmp.getId());
        if(traineeComments != null){

            ObservableList<Comment> tCItems = FXCollections.observableArrayList();
            tCItems.addAll(traineeComments);
            traineeCommentsListView.setItems(tCItems);

        }

    }

    /**
     * Populates the comment data fields with the selected comment's data.
     */
    public void onTCListViewClicked(){

        int selectedIndex = traineeCommentsListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1)
            return;

        tCNewCheckBox.setSelected(false);
        Comment tmpComment = traineeComments.get(selectedIndex);
        tCIncidentComboBox.getSelectionModel().select(tmpComment.getIncidentType());
        tCIncidentDescriptionTextField.setPromptText(tmpComment.getIncidentDescription());
        tCActionsTextField.setPromptText(tmpComment.getInstructorActions());
        tCInstructorTextField.setPromptText(tmpComment.getInstructorName());
        tCRotationComboBox.getSelectionModel().select(tmpComment.getRotation());
        tCNextStepsComboBox.getSelectionModel().select(tmpComment.getNextSteps());
        tCDateTextField.setPromptText(tmpComment.getDate());

    }

    /**
     * Validates and saves the added or updated comment for the selected trainee.
     */
    public void onSaveCommentClicked(){
    System.out.println("START");
        int traineeIndex = traineeListView.getSelectionModel().getSelectedIndex();
        int tCommentIndex = traineeCommentsListView.getSelectionModel().getSelectedIndex();
        if(traineeIndex == -1 || (tCommentIndex == -1 && !tCNewCheckBox.isSelected()))
            return;
        System.out.println("HERE");
        Trainee tmpTrainee = controller.getCurrentTrainees().get(traineeIndex);
        Comment tmpComment = new Comment();
        tmpComment.setYear(tmpTrainee.getYear());
        tmpComment.setSession(tmpTrainee.getSession());
        tmpComment.setTraineeID(tmpTrainee.getId());
        tmpComment.setTraineeName(tmpTrainee.getFullName());

        //Validate and add new comment if valid
        if(tCNewCheckBox.isSelected()){

            int validator = 0;
            if(tCIncidentComboBox.getSelectionModel().getSelectedIndex() != -1)
                validator++;
            if(!tCIncidentDescriptionTextField.getText().isEmpty())
                validator++;
            if(!tCActionsTextField.getText().isEmpty())
                validator++;
            if(!tCInstructorTextField.getText().isEmpty())
                validator++;
            if(tCRotationComboBox.getSelectionModel().getSelectedIndex() != -1)
                validator++;
            if(tCNextStepsComboBox.getSelectionModel().getSelectedIndex() != -1)
                validator++;
            if(!tCDateTextField.getText().isEmpty() && isGoodDate(tCDateTextField.getText()))
                validator++;

            if(validator == 0)
                return;

            if(validator != 7){

                tCErrorLabel.setVisible(true);
                return;

            }

            tmpComment.setIncidentType(tCIncidentComboBox.getSelectionModel().getSelectedItem());
            tmpComment.setIncidentDescription(tCIncidentDescriptionTextField.getText());
            tmpComment.setInstructorActions(tCActionsTextField.getText());
            tmpComment.setInstructorName(tCInstructorTextField.getText());
            tmpComment.setRotation(tCRotationComboBox.getSelectionModel().getSelectedItem());
            tmpComment.setNextSteps(tCNextStepsComboBox.getSelectionModel().getSelectedItem());
            tmpComment.setDate(tCDateTextField.getText());
            tmpComment.setCurrentDay(controller.getCurrentSession().getCurrentDay());

            DBManager.addComment(tmpComment);

            //Perform Update
        }else{

            tmpComment = traineeCommentsListView.getSelectionModel().getSelectedItem();

            tmpComment.setIncidentType(tCIncidentComboBox.getSelectionModel().getSelectedItem());
            if(!tCIncidentDescriptionTextField.getText().isEmpty())
                tmpComment.setIncidentDescription(tCIncidentDescriptionTextField.getText());
            if(!tCActionsTextField.getText().isEmpty())
                tmpComment.setInstructorActions(tCActionsTextField.getText());
            if(!tCInstructorTextField.getText().isEmpty())
                tmpComment.setInstructorName(tCInstructorTextField.getText());
            tmpComment.setRotation(tCRotationComboBox.getSelectionModel().getSelectedItem());
            tmpComment.setNextSteps(tCNextStepsComboBox.getSelectionModel().getSelectedItem());
            if(!tCDateTextField.getText().isEmpty())
                tmpComment.setDate(tCDateTextField.getText());

            DBManager.updateComment(tmpComment);

        }

        traineeTabRefresh();
        traineeListView.getSelectionModel().select(traineeIndex);
        onTraineeListViewClicked();

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
            if(!tECZipcodeTextField.getText().isEmpty() && isInteger(tECZipcodeTextField.getText()) &&
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
            else if(isInteger(tECZipcodeTextField.getText()) && tECZipcodeTextField.getText().length() == 5)
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
        traineeTabRefresh();
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
        eventPlaceTextField.setPromptText(strParts[strParts.length - 1].substring(0, strParts[strParts.length - 1].length() - 2));

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
        if(selectedIndex == -1 || eventPlaceTextField.getText().isEmpty())
            return;

        String[] strParts = traineeEventScoresListView.getSelectionModel().getSelectedItem().split(" ");
        if(!isInteger(eventPlaceTextField.getText()) || Integer.parseInt(eventPlaceTextField.getText()) < 0) {
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
                Integer.parseInt(eventPlaceTextField.getText())));
        int index = traineeListView.getSelectionModel().getSelectedIndex();
        traineeTabRefresh();
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
        if(!isInteger(testScoreTextField.getText()) || Integer.parseInt(testScoreTextField.getText()) < 0 ||
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
        traineeTabRefresh();
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

        traineePFPImageView.setPreserveRatio(true);
        traineePFPImageView.setImage(new Image(selectedFile.toURI().toString(),0, 250, true, true));
        VBox.setMargin(traineePFPImageView, new Insets(20, 0,
                187 - Math.ceil(traineePFPImageView.getBoundsInLocal().getHeight()), 0));

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
        prepInfoTextField.setPromptText("What have they done to prep?");
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

            boolean check = false;
            if(trainingCountTextField.getText().isEmpty())
                check = true;
            else if(isInteger(trainingCountTextField.getText()))
                check = true;

            //Test Validation
            if(!((q1Validator == 0 || q1Validator == 3) && (q2Validator == 0 || q2Validator == 10)) ||
                    !check){
                errorLabel.setVisible(true);
                return;
            }

            ///Fill tmp trainee with recorded data.
            if(finalIsUpdate){
                if(q1Validator == 3){

                    try{
                        holdsEditQuestionnaireData.setQuestionnaire1Complete(true);
                        holdsEditQuestionnaireData.setShirtSize(shirtSizeComboBox.getSelectionModel().getSelectedItem());
                        holdsEditQuestionnaireData.setShortSize(shortSizeComboBox.getSelectionModel().getSelectedItem());
                        holdsEditQuestionnaireData.setSwimSuitSize(swimSuitSizeComboBox.getSelectionModel().getSelectedItem());
                        holdsEditQuestionnaireData.setReturningTrainee(isReturningCheckBox.isSelected());
                        if(isReturningCheckBox.isSelected()) {
                            if (whyReturningTextField.getText().isEmpty())
                                if(whyReturningTextField.getPromptText().equals("What are they returning?"))
                                    throw new Exception("");
                                else
                                    holdsEditQuestionnaireData.setWhyReturning(whyReturningTextField.getPromptText());
                            else
                                holdsEditQuestionnaireData.setWhyReturning(whyReturningTextField.getText());
                        }
                        if(whyBeStateLGTextField.getText().isEmpty())
                            if(whyBeStateLGTextField.getPromptText().equals("Why be State LG?"))
                                throw new Exception("");
                            else
                                holdsEditQuestionnaireData.setWhyBeStateLG(whyBeStateLGTextField.getPromptText());
                        else
                            holdsEditQuestionnaireData.setWhyBeStateLG(whyBeStateLGTextField.getText());
                        if(whatLearnInTrainingTextField.getText().isEmpty())
                            if(whatLearnInTrainingTextField.getPromptText().equals("What they want to learn?"))
                                throw new Exception("");
                            else
                                holdsEditQuestionnaireData.setWhatWantLearnTraining(whatLearnInTrainingTextField.getPromptText());
                        else
                            holdsEditQuestionnaireData.setWhatWantLearnTraining(whatLearnInTrainingTextField.getText());
                        holdsEditQuestionnaireData.setJG(isJGCheckBox.isSelected());
                        if(isJGCheckBox.isSelected()){
                            if(jgInfoTextField.getText().isEmpty())
                                if(jgInfoTextField.getPromptText().equals("JG Info"))
                                    throw new Exception("");
                                else
                                    holdsEditQuestionnaireData.setJgInfo(jgInfoTextField.getPromptText());
                            else
                                holdsEditQuestionnaireData.setJgInfo(jgInfoTextField.getText());
                        }
                        holdsEditQuestionnaireData.setOpenWaterLG(isOpenWaterLGCheckBox.isSelected());
                        if(isOpenWaterLGCheckBox.isSelected()){
                            if(openWaterLGInfoTextField.getText().isEmpty())
                                if(openWaterLGInfoTextField.getPromptText().equals("Open Water LG Info"))
                                    throw new Exception("");
                                else
                                    holdsEditQuestionnaireData.setOpenWaterLGInfo(openWaterLGInfoTextField.getPromptText());
                            else
                                holdsEditQuestionnaireData.setOpenWaterLGInfo(openWaterLGInfoTextField.getText());
                        }
                        holdsEditQuestionnaireData.setPoolLG(isPoolLGCheckBox.isSelected());
                        if(isPoolLGCheckBox.isSelected()){
                            if(poolLGInfoTextField.getText().isEmpty())
                                if(poolLGInfoTextField.getPromptText().equals("Pool LG Info"))
                                    throw new Exception("");
                                else
                                    holdsEditQuestionnaireData.setPoolLGInfo(poolLGInfoTextField.getPromptText());
                            else
                                holdsEditQuestionnaireData.setPoolLGInfo(poolLGInfoTextField.getText());
                        }
                        holdsEditQuestionnaireData.setEMT(isEMTCheckBox.isSelected());
                        if(isEMTCheckBox.isSelected()){
                            if(emtInfoTextField.getText().isEmpty())
                                if(emtInfoTextField.getPromptText().equals("EMT Info"))
                                    throw new Exception("");
                                else
                                    holdsEditQuestionnaireData.setEmtInfo(emtInfoTextField.getPromptText());
                            else
                                holdsEditQuestionnaireData.setEmtInfo(emtInfoTextField.getText());
                        }
                        holdsEditQuestionnaireData.setOtherAdvancedMedicalTraining(isOtherMedicalCheckBox.isSelected());
                        if(isOtherMedicalCheckBox.isSelected()){
                            if(otherMedicalInfoTextField.getText().isEmpty())
                                if(otherMedicalInfoTextField.getPromptText().equals("Other Medical Training Info"))
                                    throw new Exception("");
                                else
                                    holdsEditQuestionnaireData.setAdvancedMedicalTrainingInfo(otherMedicalInfoTextField.getPromptText());
                            else
                                holdsEditQuestionnaireData.setAdvancedMedicalTrainingInfo(otherMedicalInfoTextField.getText());
                        }
                        holdsEditQuestionnaireData.setFirstJob(isFirstJobCheckBox.isSelected());
                        if(!isFirstJobCheckBox.isSelected()){
                            if(jobInfoTextField.getText().isEmpty())
                                if(jobInfoTextField.getPromptText().equals("Previous Employment Info"))
                                    throw new Exception("");
                                else
                                    holdsEditQuestionnaireData.setJobExperienceInfo(jobInfoTextField.getPromptText());
                            else
                                holdsEditQuestionnaireData.setJobExperienceInfo(jobInfoTextField.getText());
                        }
                        if(anyOtherInfoTextField.getText().isEmpty())
                            if(anyOtherInfoTextField.getPromptText().equals("Any other extra info?"))
                                throw new Exception("");
                            else
                                holdsEditQuestionnaireData.setAnyExtraInfo(anyOtherInfoTextField.getPromptText());
                        else
                            holdsEditQuestionnaireData.setAnyExtraInfo(anyOtherInfoTextField.getText());

                    }catch (Exception ex){
                        ex.printStackTrace();
                        holdsEditQuestionnaireData.setQuestionnaire1Complete(false);
                        errorLabel.setVisible(true);
                        return;
                    }

                }
                if(q2Validator == 10){

                    try{
                        holdsEditQuestionnaireData.setQuestionnaire2Complete(true);
                        if(expectedChallengeTextField.getText().isEmpty())
                            if(expectedChallengeTextField.getPromptText().equals("Expected challenges?"))
                                throw new Exception("");
                            else
                                holdsEditQuestionnaireData.setExpectedBiggestTrainingChallengeInfo(expectedChallengeTextField.getPromptText());
                        else
                            holdsEditQuestionnaireData.setExpectedBiggestTrainingChallengeInfo(expectedChallengeTextField.getText());
                        if(prepInfoTextField.getText().isEmpty())
                            if(prepInfoTextField.getPromptText().equals("What have they done to prep?"))
                                throw new Exception("");
                            else
                                holdsEditQuestionnaireData.setPreparationInfo(prepInfoTextField.getPromptText());
                        else
                            holdsEditQuestionnaireData.setPreparationInfo(prepInfoTextField.getText());
                        holdsEditQuestionnaireData.setMedicalConfidence(firstAidComboBox.getSelectionModel().getSelectedItem());
                        holdsEditQuestionnaireData.setCprConfidence(cprComboBox.getSelectionModel().getSelectedItem());
                        holdsEditQuestionnaireData.setPhysicalConfidence(physicalComboBox.getSelectionModel().getSelectedItem());
                        holdsEditQuestionnaireData.setMentalConfidence(mentalComboBox.getSelectionModel().getSelectedItem());
                        if(trainingCountTextField.getText().isEmpty())
                            if(trainingCountTextField.getPromptText().equals("# of Trainings Attended"))
                                throw new Exception("");
                            else
                                holdsEditQuestionnaireData.setPreTrainingSeminarsAttended(Integer.parseInt(trainingCountTextField.getPromptText()));
                        else if(isInteger(trainingCountTextField.getText()))
                            holdsEditQuestionnaireData.setPreTrainingSeminarsAttended(Integer.parseInt(trainingCountTextField.getText()));
                        holdsEditQuestionnaireData.setOrganizedSwimPoloFreq(getRealString(orgSwimFreqComboBox.getSelectionModel().getSelectedItem()));
                        holdsEditQuestionnaireData.setPersonalSwimFreq(getRealString(personalSwimFreqComboBox.getSelectionModel().getSelectedItem()));
                        holdsEditQuestionnaireData.setGymFreq(getRealString(gymFreqComboBox.getSelectionModel().getSelectedItem()));
                        holdsEditQuestionnaireData.setOceanSwimFreq(getRealString(oceanFreqComboBox.getSelectionModel().getSelectedItem()));
                        holdsEditQuestionnaireData.setRunningFreq(getRealString(runningFreqComboBox.getSelectionModel().getSelectedItem()));
                        holdsEditQuestionnaireData.setSurfingFreq(getRealString(surfFreqComboBox.getSelectionModel().getSelectedItem()));
                        holdsEditQuestionnaireData.setDisabled(isDisabledCheckBox.isSelected());

                    }catch(Exception ex){
                        ex.printStackTrace();
                        holdsEditQuestionnaireData.setQuestionnaire2Complete(false);
                        errorLabel.setVisible(true);
                        return;
                    }

                }

                dialog.close();

            }else{

                //Fill tmp trainee with recorded q1 data.
                if(q1Validator == 3) {

                    try{
                        holdsEditQuestionnaireData.setQuestionnaire1Complete(true);
                        holdsEditQuestionnaireData.setShirtSize(shirtSizeComboBox.getSelectionModel().getSelectedItem());
                        holdsEditQuestionnaireData.setShortSize(shortSizeComboBox.getSelectionModel().getSelectedItem());
                        holdsEditQuestionnaireData.setSwimSuitSize(swimSuitSizeComboBox.getSelectionModel().getSelectedItem());
                        holdsEditQuestionnaireData.setReturningTrainee(isReturningCheckBox.isSelected());
                        if(isReturningCheckBox.isSelected()) {
                            if (whyReturningTextField.getText().isEmpty())
                                throw new Exception("");
                            else
                                holdsEditQuestionnaireData.setWhyReturning(whyReturningTextField.getText());
                        }
                        if(whyBeStateLGTextField.getText().isEmpty())
                            throw new Exception("");
                        else
                            holdsEditQuestionnaireData.setWhyBeStateLG(whyBeStateLGTextField.getText());
                        if(whatLearnInTrainingTextField.getText().isEmpty())
                            throw new Exception("");
                        else
                            holdsEditQuestionnaireData.setWhatWantLearnTraining(whatLearnInTrainingTextField.getText());
                        holdsEditQuestionnaireData.setJG(isJGCheckBox.isSelected());
                        if(isJGCheckBox.isSelected()){
                            if(jgInfoTextField.getText().isEmpty())
                                throw new Exception("");
                            else
                                holdsEditQuestionnaireData.setJgInfo(jgInfoTextField.getText());
                        }
                        holdsEditQuestionnaireData.setOpenWaterLG(isOpenWaterLGCheckBox.isSelected());
                        if(isOpenWaterLGCheckBox.isSelected()){
                            if(openWaterLGInfoTextField.getText().isEmpty())
                                throw new Exception("");
                            else
                                holdsEditQuestionnaireData.setOpenWaterLGInfo(openWaterLGInfoTextField.getText());
                        }
                        holdsEditQuestionnaireData.setPoolLG(isPoolLGCheckBox.isSelected());
                        if(isPoolLGCheckBox.isSelected()){
                            if(poolLGInfoTextField.getText().isEmpty())
                                throw new Exception("");
                            else
                                holdsEditQuestionnaireData.setPoolLGInfo(poolLGInfoTextField.getText());
                        }
                        holdsEditQuestionnaireData.setEMT(isEMTCheckBox.isSelected());
                        if(isEMTCheckBox.isSelected()){
                            if(emtInfoTextField.getText().isEmpty())
                                throw new Exception("");
                            else
                                holdsEditQuestionnaireData.setEmtInfo(emtInfoTextField.getText());
                        }
                        holdsEditQuestionnaireData.setOtherAdvancedMedicalTraining(isOtherMedicalCheckBox.isSelected());
                        if(isOtherMedicalCheckBox.isSelected()){
                            if(otherMedicalInfoTextField.getText().isEmpty())
                                throw new Exception("");
                            else
                                holdsEditQuestionnaireData.setAdvancedMedicalTrainingInfo(otherMedicalInfoTextField.getText());
                        }
                        holdsEditQuestionnaireData.setFirstJob(isFirstJobCheckBox.isSelected());
                        if(!isFirstJobCheckBox.isSelected()){
                            if(jobInfoTextField.getText().isEmpty())
                                throw new Exception("");
                            else
                                holdsEditQuestionnaireData.setJobExperienceInfo(jobInfoTextField.getText());
                        }
                        holdsEditQuestionnaireData.setAnyExtraInfo(anyOtherInfoTextField.getText());

                    }catch(Exception ex){
                        ex.printStackTrace();
                        holdsEditQuestionnaireData.setQuestionnaire1Complete(false);
                        errorLabel.setVisible(true);
                        return;
                    }

                }else
                    holdsEditQuestionnaireData.setQuestionnaire1Complete(false);
                //Fill tmp trainee with recorded q2 data.
                if(q2Validator == 10){

                    try{
                        holdsEditQuestionnaireData.setQuestionnaire2Complete(true);
                        if(expectedChallengeTextField.getText().isEmpty())
                            throw new Exception("");
                        else
                            holdsEditQuestionnaireData.setExpectedBiggestTrainingChallengeInfo(expectedChallengeTextField.getText());
                        if(prepInfoTextField.getText().isEmpty())
                            throw new Exception("");
                        else
                            holdsEditQuestionnaireData.setPreparationInfo(prepInfoTextField.getText());

                        holdsEditQuestionnaireData.setMedicalConfidence(firstAidComboBox.getSelectionModel().getSelectedItem());
                        holdsEditQuestionnaireData.setCprConfidence(cprComboBox.getSelectionModel().getSelectedItem());
                        holdsEditQuestionnaireData.setPhysicalConfidence(physicalComboBox.getSelectionModel().getSelectedItem());
                        holdsEditQuestionnaireData.setMentalConfidence(mentalComboBox.getSelectionModel().getSelectedItem());
                        if(trainingCountTextField.getText().isEmpty() || !isInteger(trainingCountTextField.getText()))
                            throw new Exception("");
                        else
                            holdsEditQuestionnaireData.setPreTrainingSeminarsAttended(Integer.parseInt(trainingCountTextField.getText()));
                        holdsEditQuestionnaireData.setOrganizedSwimPoloFreq(getRealString(orgSwimFreqComboBox.getSelectionModel().getSelectedItem()));
                        holdsEditQuestionnaireData.setPersonalSwimFreq(getRealString(personalSwimFreqComboBox.getSelectionModel().getSelectedItem()));
                        holdsEditQuestionnaireData.setGymFreq(getRealString(gymFreqComboBox.getSelectionModel().getSelectedItem()));
                        holdsEditQuestionnaireData.setOceanSwimFreq(getRealString(oceanFreqComboBox.getSelectionModel().getSelectedItem()));
                        holdsEditQuestionnaireData.setRunningFreq(getRealString(runningFreqComboBox.getSelectionModel().getSelectedItem()));
                        holdsEditQuestionnaireData.setSurfingFreq(getRealString(surfFreqComboBox.getSelectionModel().getSelectedItem()));
                        holdsEditQuestionnaireData.setDisabled(isDisabledCheckBox.isSelected());

                    }catch (Exception ex){
                        ex.printStackTrace();
                        holdsEditQuestionnaireData.setQuestionnaire2Complete(false);
                        errorLabel.setVisible(true);
                        return;
                    }

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

        System.out.println("START Qs");
        //Updates Q1 Data if needed
        if(holdsEditQuestionnaireData.isQuestionnaire1Complete()){
            System.out.println("ADD Q1");
            DBManager.addExistingTraineeQuestionnaire1Data(holdsEditQuestionnaireData);
        }


        //Updates Q2 Data if needed
        if(holdsEditQuestionnaireData.isQuestionnaire2Complete()){
            System.out.println("ADD Q2");
            DBManager.addExistingTraineeQuestionnaire2Data(holdsEditQuestionnaireData);
        }


        //Updates PFP if needed
        if(!traineePFPImageView.getImage().equals(defaultImage)) {
            holdsEditQuestionnaireData.setImage(traineePFPImageView.getImage());
            DBManager.addTraineeProfileImage(holdsEditQuestionnaireData);
        }

        controller.updateCurrentTrainees();
        int index = traineeListView.getSelectionModel().getSelectedIndex();
        traineeTabRefresh();
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
                                selectedFile.getName() + " into the Comments database for Session " +
                                controller.getCurrentSession().getSession() + "?", ButtonType.YES, ButtonType.CANCEL);
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
                                            controller.getCurrentSession().getSession(),
                                            controller.getCurrentSession().getCurrentDay()));

            //Adds to db if does not exist
            for(Comment comment : tmpComments){

                boolean isFound = false;
                for(Comment storedComment : controller.getCurrentComments()){

                    if(comment.getDate().equals(storedComment.getDate()) &&
                        comment.getTraineeName().toLowerCase().equals(storedComment.getTraineeName().toLowerCase()) &&
                        comment.getInstructorName().toLowerCase().equals(storedComment.getInstructorName().toLowerCase()) &&
                        comment.getIncidentDescription().toLowerCase().equals(storedComment.getIncidentDescription().toLowerCase())) {

                        isFound = true;
                        break;

                    }

                }

                if(!isFound)
                    DBManager.addComment(comment);

            }

            controller.updateCurrentComments();
            importComboBox.getSelectionModel().selectFirst();
            traineeTabRefresh();

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
            if (records.get(0).size() != 21)
                throw new Exception("Wrong File");
            for (CSVRecord record : records)
                tmpTrainees.add(new Trainee(record.get(3).trim(), record.get(4).trim(), record.get(5).trim(),
                        record.get(6), record.get(7), record.get(8), record.get(9), record.get(10), record.get(11),
                        record.get(12).charAt(0) == 'Y',
                        new EmergencyContact(record.get(13), record.get(14), record.get(15),
                                record.get(16), record.get(17), record.get(18),
                                record.get(19)),
                        0, false, false, true, 2222, Integer.parseInt(record.get(2))));

            parser.close();

            //Remove early duplicates
            Vector<Pair<String, String>> currentNames = new Vector<>();
            Iterator<Trainee> it = tmpTrainees.iterator();
            Vector<Trainee> removeList = new Vector<>();
            Vector<Integer> removedIndecies = new Vector<>();
            while (it.hasNext()){
                Trainee tmp = it.next();
                for(int index = 0; index < currentNames.size(); index++){
                    if(currentNames.get(index).getKey().toLowerCase().equals(tmp.getFirstName().toLowerCase())
                            && currentNames.get(index).getValue().toLowerCase().equals(tmp.getLastName().toLowerCase())
                            && !removedIndecies.contains(index)){

                        removedIndecies.add(index);
                        removeList.add(tmpTrainees.get(index));
                        break;

                    }
                }
                currentNames.add(new Pair<>(tmp.getFirstName(), tmp.getLastName()));

            }
            tmpTrainees.removeAll(removeList);

            List<Integer> sessionChoices = new ArrayList<>();
            for(Trainee trainee : tmpTrainees) {
                if (!sessionChoices.contains(trainee.getSession()))
                    sessionChoices.add(trainee.getSession());
            }
            Collections.sort(sessionChoices);

            //Initialize Dialog box contents for session inputs.
            final Stage dialog = new Stage();
            dialog.setTitle("Import - Create Needed Sessions");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(LifeguardTrainingApplication.getCoordinator().getStage());
            VBox dialogVBox = new VBox();
            Label label = new Label("These are the sessions found in the .csv file,\nall sessions which do not " +
                                    "exist already will\nbe added with the below information.");
            label.setFont(new Font("System", 14));
            label.setStyle("-fx-font-weight: bold; -fx-text-fill: #efb748;");

            TextField yearTextField = new TextField();
            yearTextField.setPromptText("year");
            yearTextField.setMaxWidth(50);
            yearTextField.setFocusTraversable(false);

            //Creates and fills teh TableView
            TableView<AddSessionsData> newSessionsTableView = new TableView<>();
            newSessionsTableView.requestFocus();
            newSessionsTableView.layout();
            newSessionsTableView.setEditable(true);
            TableColumn<AddSessionsData, String> sessionColumn = new TableColumn<>("Session");
            sessionColumn.setEditable(false);
            sessionColumn.setSortable(false);
            sessionColumn.setCellValueFactory(new PropertyValueFactory<>("session"));
            TableColumn<AddSessionsData, String> startDateColumn = new TableColumn<>("Start Date");
            startDateColumn.setEditable(true);
            startDateColumn.setSortable(false);
            startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            startDateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            TableColumn<AddSessionsData, String> endDateColumn = new TableColumn<>("End Date");
            endDateColumn.setEditable(true);
            endDateColumn.setSortable(false);
            endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            endDateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            newSessionsTableView.getColumns().addAll(sessionColumn, startDateColumn, endDateColumn);
            ObservableList<AddSessionsData> data = FXCollections.observableArrayList();
            for(Integer sessionNum : sessionChoices)
                data.add(new AddSessionsData(String.valueOf(sessionNum), "mm/dd", "mm/dd"));
            newSessionsTableView.setItems(data);

            //Action event where the user presses enter to enter that cell, which then increments the edit focus to the
            //next cell.
            startDateColumn.setOnEditCommit((TableColumn.CellEditEvent<AddSessionsData, String> t) -> {

                t.getTableView().getItems().get(t.getTablePosition().getRow()).setStartDate(t.getNewValue());
                int index = newSessionsTableView.getSelectionModel().getSelectedIndex();

                Platform.runLater(() -> {
                    newSessionsTableView.edit(index, endDateColumn);
                });
            });

            //Action event where the user presses enter to enter that cell, which then increments the edit focus to the
            //next row, this time at the startDateColumn, not the currently selected endDateColumn.
            endDateColumn.setOnEditCommit((TableColumn.CellEditEvent<AddSessionsData, String> t) -> {

                t.getTableView().getItems().get(t.getTablePosition().getRow()).setEndDate(t.getNewValue());
                int index = newSessionsTableView.getSelectionModel().getSelectedIndex();
                if(index + 1 < sessionChoices.size()){

                    Platform.runLater(() -> {

                        newSessionsTableView.getSelectionModel().select(index + 1);
                        newSessionsTableView.edit(index + 1, startDateColumn);

                    });
                }
            });

            Label addSessionsErrorLabel = new Label("*ERROR* enter valid year and dates");
            addSessionsErrorLabel.setStyle("-fx-text-fill: #57ff8c;");
            addSessionsErrorLabel.setVisible(false);

            Button sessionButton = new Button("Save and Import");
            sessionButton.setStyle("-fx-font-weight: bold");
            Button cancelButton = new Button("Cancel");
            HBox buttonHBox = new HBox(sessionButton, cancelButton);

            Label loadingLabel = new Label(" If you think it's frozen, it's not! Be patient here.");
            loadingLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #efb748;");

            dialogVBox.getChildren().addAll(label, yearTextField, newSessionsTableView, addSessionsErrorLabel,
                                            loadingLabel, buttonHBox);

            //Set dialog box style
            buttonHBox.setSpacing(10);
            buttonHBox.setAlignment(Pos.CENTER);
            dialogVBox.setAlignment(Pos.CENTER);
            dialogVBox.setSpacing(10);
            dialogVBox.setStyle("-fx-background-color: #3476f7;");

            newSessionsTableView.setMaxWidth(205);
            newSessionsTableView.setMaxHeight(300);
            VBox.setMargin(label, new Insets(10, 0, 0, 10));
            VBox.setMargin(buttonHBox, new Insets(10));

            Scene dialogScene = new Scene(dialogVBox, 300, 550);
            dialog.setScene(dialogScene);

            //Button Events
            sessionButton.setOnMouseClicked(event -> {
                sessionButton.setDisable(true);
                cancelButton.setDisable(true);

                //Verify needed data entries
                if(yearTextField.getText().isEmpty() || !isGoodYear(yearTextField.getText())){
                    addSessionsErrorLabel.setVisible(true);
                    return;
                }
                Vector<Session> pendingSessions = new Vector<>();
                for(int i = 0; i < sessionChoices.size(); i++){

                    if(!isGoodHalfDate(startDateColumn.getCellObservableValue(i).getValue()) ||
                       !isGoodHalfDate(endDateColumn.getCellObservableValue(i).getValue())){
                        addSessionsErrorLabel.setVisible(true);
                        return;
                    }else
                        pendingSessions.add(new Session(Integer.parseInt(yearTextField.getText()),
                                            Integer.parseInt(sessionColumn.getCellObservableValue(i).getValue()),
                                            startDateColumn.getCellObservableValue(i).getValue() + "/" + yearTextField.getText(),
                                            endDateColumn.getCellObservableValue(i).getValue() + "/" + yearTextField.getText(),
                                            1));

                }

                //Verified, save data now
                //Adds the session if it is not found
                Vector<Session> existingSessions = DBManager.getAllSessions();
                for(Session newSes : pendingSessions){

                    Set<District> foundDistricts = new HashSet<>();
                    for(Trainee trainee : tmpTrainees){
                        if(trainee.getSession() == newSes.getSession())
                            foundDistricts.add(new District(newSes.getYear(), newSes.getSession(),
                                    trainee.getDistrictChoice().split(" - ")[0], "", 1));
                    }

                    boolean isFound = false;
                    for(Session oldSes : Objects.requireNonNull(existingSessions))
                        if (oldSes.getYear() == newSes.getYear() && oldSes.getSession() == newSes.getSession()) {
                            isFound = true;
                            for(District district : Objects.requireNonNull(foundDistricts))
                                DBManager.addDistrict(district);
                            break;
                        }

                    //Add Session and copy over Event, Test, and District data from current session.
                    if(!isFound) {
                        DBManager.addNewSession(newSes.getYear(), newSes.getSession(), newSes.getStartDate(),
                                newSes.getEndDate());
                        for(Event copyEvent : controller.getCurrentEvents())
                            DBManager.addEvent(new Event(copyEvent.getName(), "", false, newSes.getYear(),
                                    newSes.getSession()));

                        for(Test copyTest : controller.getCurrentTests())
                            DBManager.addTest(new Test(copyTest.getName(), copyTest.getPoints(), false,
                                    newSes.getYear(), newSes.getSession()));

                        for(District district : Objects.requireNonNull(foundDistricts))
                            DBManager.addDistrict(district);

                        for(Sector sector : Objects.requireNonNull(DBManager.getAllSectorsFromSession(controller.getCurrentSession().getYear(),
                                controller.getCurrentSession().getSession())))
                            DBManager.addSector(new Sector(newSes.getYear(), newSes.getSession(), sector.getName()));

                    }

                }
                Vector<Trainee> allRelevantTrainees = new Vector<>();
                for(Session ses : pendingSessions)
                    allRelevantTrainees.addAll(Objects.requireNonNull(DBManager.getAllTraineesFromSession(ses.getYear(), ses.getSession())));

                //Save Trainees to their respective sessions
                for(Trainee trainee : tmpTrainees) {

                    boolean found = false;
                    for(Trainee currentTrainees : allRelevantTrainees){
                        if(trainee.getFullName().toLowerCase().equals(currentTrainees.getFullName().toLowerCase())){
                            found = true;
                            System.out.println("Duplicate: " + trainee.getFullName());
                            break;
                        }
                    }

                    if(!found){
                        trainee.setYear(Integer.parseInt(yearTextField.getText()));
                        DBManager.addInitialTrainee(trainee);
                    }
                }

                controller.updateCurrentTrainees();
                sessionButton.setDisable(false);
                cancelButton.setDisable(false);
                dialog.close();
                traineeTabRefresh();

            });
            cancelButton.setOnMouseClicked(event -> dialog.close());

            dialog.showAndWait();

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
                tmpTraineeQ1.add(new Trainee(record.get(3).trim(), record.get(2).trim(), record.get(1).trim(),
                        record.get(4), record.get(5), record.get(6),
                        !(Character.toUpperCase(record.get(7).charAt(0)) == 'N' && record.get(7).charAt(1) == '/'),
                        (Character.toUpperCase(record.get(7).charAt(0)) == 'N' && record.get(7).charAt(1) == '/') ? null : record.get(7),
                        record.get(8), record.get(9), record.get(10).charAt(0) == 'Y',
                        record.get(11), record.get(12).charAt(0) == 'Y',
                        record.get(13), record.get(14).charAt(0) == 'Y',
                        record.get(15), record.get(16).charAt(0) == 'Y', record.get(17),
                        record.get(18).charAt(0) == 'Y',
                        record.get(19), record.get(20).charAt(0) == 'Y',
                        record.get(21), record.get(22)));

            parser.close();

            //Remove early duplicates
            Vector<Pair<String, String>> currentNames = new Vector<>();
            Iterator<Trainee> it = tmpTraineeQ1.iterator();
            Vector<Trainee> removeList = new Vector<>();
            Vector<Integer> removedIndecies = new Vector<>();
            while (it.hasNext()){
                Trainee tmp = it.next();
                for(int index = 0; index < currentNames.size(); index++){
                    if(currentNames.get(index).getKey().toLowerCase().equals(tmp.getFirstName().toLowerCase())
                            && currentNames.get(index).getValue().toLowerCase().equals(tmp.getLastName().toLowerCase())
                            && !removedIndecies.contains(index)){

                        removedIndecies.add(index);
                        removeList.add(tmpTraineeQ1.get(index));
                        break;

                    }
                }
                currentNames.add(new Pair<>(tmp.getFirstName(), tmp.getLastName()));

            }
            tmpTraineeQ1.removeAll(removeList);

            //Adds to db if does not exist
            Vector<Trainee> allTrainees = DBManager.getAllTrainees();
            Collections.reverse(Objects.requireNonNull(allTrainees));
            for (int i = 0; i < tmpTraineeQ1.size(); i++) {

                boolean isFound = false;
                boolean isFilled = false;
                for (int j = 0; j < allTrainees.size() && !isFound; j++) {

                    if (tmpTraineeQ1.get(i).getFirstName().toLowerCase().equals(allTrainees.get(j).getFirstName().toLowerCase()) &&
                            tmpTraineeQ1.get(i).getLastName().toLowerCase().equals(allTrainees.get(j).getLastName().toLowerCase())) {

                        tmpTraineeQ1.get(i).setId(allTrainees.get(j).getId());
                        isFound = true;

                        if (allTrainees.get(j).isQuestionnaire1Complete())
                            isFilled = true;

                    }

                }

                if (isFound && !isFilled) {
                    DBManager.addExistingTraineeQuestionnaire1Data(tmpTraineeQ1.get(i));
                    System.out.println(tmpTraineeQ1.get(i).getFirstName() + " ADDED");
                }

            }

            controller.updateCurrentTrainees();
            importComboBox.getSelectionModel().selectFirst();
            traineeTabRefresh();

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
                tmpTraineeQ2.add(new Trainee(record.get(1).trim(), record.get(2).trim(), record.get(3), record.get(4),
                        Integer.parseInt(record.get(5)), Integer.parseInt(record.get(6)),
                        Integer.parseInt(record.get(7)), Integer.parseInt(record.get(8)),
                        Integer.parseInt(record.get(9)), record.get(10), record.get(11),
                        record.get(12), record.get(13), record.get(14), record.get(15),
                        record.get(16).charAt(0) == 'Y' || record.get(16).charAt(0) == 'y'));

            parser.close();

            //Remove early duplicates
            Vector<Pair<String, String>> currentNames = new Vector<>();
            Iterator<Trainee> it = tmpTraineeQ2.iterator();
            Vector<Trainee> removeList = new Vector<>();
            Vector<Integer> removedIndecies = new Vector<>();
            while (it.hasNext()){
                Trainee tmp = it.next();
                for(int index = 0; index < currentNames.size(); index++){
                    if(currentNames.get(index).getKey().toLowerCase().equals(tmp.getFirstName().toLowerCase())
                            && currentNames.get(index).getValue().toLowerCase().equals(tmp.getLastName().toLowerCase())
                            && !removedIndecies.contains(index)){

                        removedIndecies.add(index);
                        removeList.add(tmpTraineeQ2.get(index));
                        break;

                    }
                }
                currentNames.add(new Pair<>(tmp.getFirstName(), tmp.getLastName()));

            }
            tmpTraineeQ2.removeAll(removeList);

            //Adds to db if does not exist
            Vector<Trainee> allTrainees = DBManager.getAllTrainees();
            Collections.reverse(Objects.requireNonNull(allTrainees));
            for (int i = 0; i < tmpTraineeQ2.size(); i++) {
                boolean isFound = false;
                boolean isFilled = false;
                for (int j = 0; j < allTrainees.size() && !isFound; j++) {

                    if (tmpTraineeQ2.get(i).getFirstName().toLowerCase().equals(allTrainees.get(j).getFirstName().toLowerCase()) &&
                            tmpTraineeQ2.get(i).getLastName().toLowerCase().equals(allTrainees.get(j).getLastName().toLowerCase())) {

                        tmpTraineeQ2.get(i).setId(allTrainees.get(j).getId());
                        isFound = true;

                        if (allTrainees.get(j).isQuestionnaire2Complete())
                            isFilled = true;

                    }

                }

                if (isFound && !isFilled) {
                    DBManager.addExistingTraineeQuestionnaire2Data(tmpTraineeQ2.get(i));
                    System.out.println(tmpTraineeQ2.get(i).getFirstName() + " ADDED");
                }

            }

            controller.updateCurrentTrainees();
            importComboBox.getSelectionModel().selectFirst();
            traineeTabRefresh();

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
     * Attempts to move the selected trainee to a new session.
     */
    public void onMoveTraineeClicked(){

        int selectedIndex = traineeListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1)
            return;


        //Initialize Dialog box contents
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(LifeguardTrainingApplication.getCoordinator().getStage());
        VBox dialogVBox = new VBox();

        Label label = new Label(" Select the session to transfer " + controller.getCurrentTrainees().get(selectedIndex).getFirstName()
                                + " " + controller.getCurrentTrainees().get(selectedIndex).getLastName() + " to.");
        Label label2 = new Label("NOTICE: All data in this session associated with the");
        Label label3 = new Label(" trainee will be deleted.");

        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);
        label2.setTextAlignment(TextAlignment.CENTER);
        label2.setAlignment(Pos.CENTER);
        label3.setTextAlignment(TextAlignment.CENTER);
        label3.setAlignment(Pos.CENTER);
        ListView<String> sessionListView = new ListView<>();
        ObservableList<String> sessionObList = FXCollections.observableArrayList();
        Button sessionButton = new Button("Transfer Trainee");
        Button cancelButton = new Button("Cancel");
        HBox buttonHBox = new HBox(sessionButton, cancelButton);

        //Make and sort session list
        Vector<Session> sessions = DBManager.getAllSessions();
        if(sessions == null)
            return;

        //Removes Current Session From List
        sessions.removeIf(b -> b.getStartDate().equals(controller.getCurrentSession().getStartDate()) &&
                                b.getEndDate().equals(controller.getCurrentSession().getEndDate()));

        //Sorts out the sessions which already have tests or events scored.
        Iterator<Session> it = sessions.iterator();
        while(it.hasNext()){

            Session tmp = it.next();
            boolean isRemovable = false;
            for(Test test : Objects.requireNonNull(DBManager.getAllTestsFromSession(tmp.getYear(), tmp.getSession())))
                if(test.isScored()){
                    isRemovable = true;
                    break;
                }

            for(Event event : Objects.requireNonNull(DBManager.getAllEventsFromSession(tmp.getYear(), tmp.getSession())))
                if(event.isScored()){
                    isRemovable = true;
                    break;
                }

            if(isRemovable)
                it.remove();

        }

        sessions.sort(new SortByNewestSession());
        for(Session session: sessions)
            sessionObList.add("Year: " + session.getYear() + " Session: " + session.getSession() + " | " +
                    session.getStartDate() + " - " + session.getEndDate());

        sessionListView.setItems(sessionObList);
        sessionListView.getSelectionModel().select(0);
        dialogVBox.getChildren().addAll(label, label2, label3, sessionListView, buttonHBox);

        //Button Events
        sessionButton.setOnMouseClicked(event -> {

            if(sessionListView.getSelectionModel().isEmpty())
                return;

            int index = sessionListView.getSelectionModel().getSelectedIndex();
            Session destinationSession = sessions.get(index);

            Trainee tmpTrainee = controller.getCurrentTrainees().get(selectedIndex);
            tmpTrainee.setYear(destinationSession.getYear());
            tmpTrainee.setSession(destinationSession.getSession());

            DBManager.updateTrainee(tmpTrainee);
            DBManager.deleteAllTestScoresOfATrainee(tmpTrainee.getId());
            DBManager.deleteAllEventScoresOfATrainee(tmpTrainee.getId());
            DBManager.deleteAllOfATraineeComments(tmpTrainee.getId());

            dialog.close();
            controller.updateCurrentTrainees();
            controller.updateCurrentComments();
            traineeTabRefresh();

        });
        cancelButton.setOnMouseClicked(event -> dialog.close());

        //Style
        buttonHBox.setSpacing(10);
        buttonHBox.setAlignment(Pos.CENTER);
        dialogVBox.setAlignment(Pos.CENTER);
        dialogVBox.setSpacing(10);
        VBox.setMargin(label, new Insets(10, 0, 0, 0));
        VBox.setMargin(label2, new Insets(-5, 0, 0, 0));
        VBox.setMargin(label3, new Insets(-5, 0, 0, 0));
        VBox.setMargin(buttonHBox, new Insets(10));
        VBox.setMargin(sessionListView, new Insets(0, 5, 0, 5));

        Scene dialogScene = new Scene(dialogVBox, 300, 450);
        dialog.setScene(dialogScene);
        dialog.showAndWait();

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
            traineeTabRefresh();
            eventInstructorTestTabRefresh();
            allOtherTabRefresh();

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
     * @return true if successful, false if not.
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
     * Checks if the given string is in the 'mm/dd' format
     * @param date
     * @return true if successful, false if not.
     */
    private boolean isGoodHalfDate(String date){

        if(date == null || date.equals("") || date.equals("mm/dd"))
            return false;

        date = date.trim();

        //Checks if correct format
        return date.length() == 5 && isDigit(date.charAt(0)) && isDigit(date.charAt(1)) && date.charAt(2) == '/'
                && isDigit(date.charAt(3)) && isDigit(date.charAt(4));

    }

    /**
     * Checks if the given phone number is in the correct format "xxx-xxx-xxxx"
     * @param num
     * @return true if successful, false if not.
     */
    private boolean isGoodPhoneNumber(String num){

        String[] stringSplit = num.split("-");
        return stringSplit[0].length() == 3 && stringSplit[1].length() == 3 && stringSplit[2].length() == 4;

    }

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
     * Checks if the given string is an integer
     * @param s
     * @return true if successful, false if not.
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

    /**
     * Returns the given placement as a string with the correct suffix behind it.
     * @param place
     * @return true if successful, false if not.
     */
    private String getPlaceSuffix(int place){

        int j = place % 10;
        int k = place % 100;
        if (j == 1 && k != 11) {
            return place + "st";
        }
        if (j == 2 && k != 12) {
            return place + "nd";
        }
        if (j == 3 && k != 13) {
            return place + "rd";
        }
        return place + "th";

    }

    static final class CenteredListViewCell extends ListCell<String> { { setAlignment(Pos.BASELINE_CENTER); }

        @Override protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(item);
        }
    }

    static final class CenteredTraineeListViewCell extends ListCell<Trainee> { { setAlignment(Pos.BASELINE_CENTER); }

        @Override protected void updateItem(Trainee item, boolean empty) {
            super.updateItem(item, empty);
            if(item != null)
                setText(item.getFullName());
            else
                setText(null);
        }
    }

    /**
     * Used to hold the table view data for the add trainees from .csv dialog box.
     */
    public class AddSessionsData{

        SimpleStringProperty session;
        SimpleStringProperty startDate;
        SimpleStringProperty endDate;

        AddSessionsData(String session, String startDate, String endDate){

            this.session = new SimpleStringProperty(session);
            this.startDate = new SimpleStringProperty(startDate);
            this.endDate = new SimpleStringProperty(endDate);

        }

        //Getters
        public String getSession(){return session.get();}
        public String getStartDate(){return startDate.get();}
        public String getEndDate(){return endDate.get();}
        //Setters
        public void setSession(String s){session.set(s);}
        public void setStartDate(String s){startDate.set(s);}
        public void setEndDate(String s){endDate.set(s);}

    }

    /**
     * Used to hold the table view data for the reactivate trainees.
     */
    public class AddExamsData{

        SimpleStringProperty examName;
        SimpleStringProperty enteredScore;
        SimpleStringProperty maxScore;

        AddExamsData(String examName, String enteredScore, String maxScore){

            this.examName = new SimpleStringProperty(examName);
            this.enteredScore = new SimpleStringProperty(enteredScore);
            this.maxScore = new SimpleStringProperty(maxScore);

        }

        //Getters
        public String getExamName(){return examName.get();}
        public String getEnteredScore(){return enteredScore.get();}
        public String getMaxScore(){return maxScore.get();}

        //Setters
        public void setExamName(String s){examName.set(s);}
        public void setEnteredScore(String s){enteredScore.set(s);}
        public void setMaxScore(String s){maxScore.set(s);}

    }

}

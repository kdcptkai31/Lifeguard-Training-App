package org.openjfx.view;

import java.io.IOException;

import java.util.Comparator;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.openjfx.controller.Controller;
import org.openjfx.controller.DBManager;
import org.openjfx.controller.DocumentGenerator;
import org.openjfx.controller.LifeguardTrainingApplication;
import org.openjfx.model.Session;

public class ReportsView {

    private Controller controller;
    private DocumentGenerator documentGenerator;

    @FXML
    private Label yearLabel;
    @FXML
    private Label sessionLabel;
    @FXML
    private Label datesLabel;

    //Middle VBox Loading Objects
    @FXML
    private Label loadingLabel;
    @FXML
    private ImageView loadingGif;
    @FXML
    private Label extraTimeLabel;

    //Printouts
    @FXML
    private Button emailListButton;
    @FXML
    private Button lodgingListButton;
    @FXML
    private Button profilesButton;
    @FXML
    private Button averyListButton;
    @FXML
    private Button uniformInfoButton;
    @FXML
    private Button currentRankingsButton;
    @FXML
    private Button certificatesButton;

    //Deliverables
    @FXML
    private Button individualSumButton;
    @FXML
    private Button districtSumButton;
    @FXML
    private Button testAnalysisButton;
    @FXML
    private Button attendanceButton;


    @FXML
    protected void initialize(){

        controller = LifeguardTrainingApplication.getController();
        documentGenerator = new DocumentGenerator();
        refresh();

    }

    /**
     * Refreshes the page with all current data.
     */
    private void refresh(){

        yearLabel.setText(String.valueOf(controller.getCurrentSession().getYear()));
        sessionLabel.setText("Session " + controller.getCurrentSession().getSession());
        datesLabel.setText(controller.getCurrentSession().getStartDate() + " - " + controller.getCurrentSession().getEndDate());
        documentGenerator.createDirectoriesIfNeeded();
        enableButtons();

    }

    public void onProfilesClicked(){

        disableButtons();
        loadingLabel.setVisible(true);
        loadingGif.setVisible(true);
        extraTimeLabel.setVisible(true);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            documentGenerator.generateTraineeProfiles();
            ScheduledExecutorService executor1 = Executors.newSingleThreadScheduledExecutor();
            Runnable task1 = () -> {
                enableButtons();
                loadingLabel.setVisible(false);
                loadingGif.setVisible(false);
                extraTimeLabel.setVisible(false);
            };
            executor1.schedule(task1, 1, TimeUnit.MILLISECONDS);
            executor1.shutdown();

        };
        executor.schedule(task, 1, TimeUnit.MILLISECONDS);
        executor.shutdown();

    }

    public void onAveryListClicked(){

        disableButtons();
        loadingLabel.setVisible(true);
        loadingGif.setVisible(true);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            documentGenerator.generateAveryList();
            ScheduledExecutorService executor1 = Executors.newSingleThreadScheduledExecutor();
            Runnable task1 = () -> {
                enableButtons();
                loadingLabel.setVisible(false);
                loadingGif.setVisible(false);
            };
            executor1.schedule(task1, 1, TimeUnit.MILLISECONDS);
            executor1.shutdown();

        };
        executor.schedule(task, 1, TimeUnit.MILLISECONDS);
        executor.shutdown();

    }

    public void onUniformClicked(){

        disableButtons();
        loadingLabel.setVisible(true);
        loadingGif.setVisible(true);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            documentGenerator.generateUniformForm();
            ScheduledExecutorService executor1 = Executors.newSingleThreadScheduledExecutor();
            Runnable task1 = () -> {
                enableButtons();
                loadingLabel.setVisible(false);
                loadingGif.setVisible(false);
            };
            executor1.schedule(task1, 1, TimeUnit.MILLISECONDS);
            executor1.shutdown();

        };
        executor.schedule(task, 1, TimeUnit.MILLISECONDS);
        executor.shutdown();

    }

    public void onCurrentRankingsClicked(){

        disableButtons();
        loadingLabel.setVisible(true);
        loadingGif.setVisible(true);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            documentGenerator.generateCurrentRankings();
            ScheduledExecutorService executor1 = Executors.newSingleThreadScheduledExecutor();
            Runnable task1 = () -> {
                enableButtons();
                loadingLabel.setVisible(false);
                loadingGif.setVisible(false);
            };
            executor1.schedule(task1, 1, TimeUnit.MILLISECONDS);
            executor1.shutdown();

        };
        executor.schedule(task, 1, TimeUnit.MILLISECONDS);
        executor.shutdown();

    }

    public void onCertificatesClicked(){

        disableButtons();
        loadingLabel.setVisible(true);
        loadingGif.setVisible(true);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            documentGenerator.generateCertificates();
            ScheduledExecutorService executor1 = Executors.newSingleThreadScheduledExecutor();
            Runnable task1 = () -> {
                enableButtons();
                loadingLabel.setVisible(false);
                loadingGif.setVisible(false);
            };
            executor1.schedule(task1, 1, TimeUnit.MILLISECONDS);
            executor1.shutdown();

        };
        executor.schedule(task, 1, TimeUnit.MILLISECONDS);
        executor.shutdown();

    }

    public void onIndividualSumClicked(){

        disableButtons();
        loadingLabel.setVisible(true);
        loadingGif.setVisible(true);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            //Makes them all with the given null value
            documentGenerator.preProcessIndividualSummaries(null);
            ScheduledExecutorService executor1 = Executors.newSingleThreadScheduledExecutor();
            Runnable task1 = () -> {
                enableButtons();
                loadingLabel.setVisible(false);
                loadingGif.setVisible(false);
            };
            executor1.schedule(task1, 1, TimeUnit.MILLISECONDS);
            executor1.shutdown();

        };
        executor.schedule(task, 1, TimeUnit.MILLISECONDS);
        executor.shutdown();

    }

    public void onDistrictSumClicked(){

        disableButtons();
        loadingLabel.setVisible(true);
        loadingGif.setVisible(true);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            //Makes them all with the given null value
            documentGenerator.preProcessDistrictSummaries(null);
            ScheduledExecutorService executor1 = Executors.newSingleThreadScheduledExecutor();
            Runnable task1 = () -> {
                enableButtons();
                loadingLabel.setVisible(false);
                loadingGif.setVisible(false);
            };
            executor1.schedule(task1, 1, TimeUnit.MILLISECONDS);
            executor1.shutdown();

        };
        executor.schedule(task, 1, TimeUnit.MILLISECONDS);
        executor.shutdown();

    }

    public void onTestAnalysisClicked(){

        disableButtons();
        loadingLabel.setVisible(true);
        loadingGif.setVisible(true);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {

            documentGenerator.generateTestAnalysis();
            ScheduledExecutorService executor1 = Executors.newSingleThreadScheduledExecutor();
            Runnable task1 = () -> {
                enableButtons();
                loadingLabel.setVisible(false);
                loadingGif.setVisible(false);
            };
            executor1.schedule(task1, 1, TimeUnit.MILLISECONDS);
            executor1.shutdown();

        };
        executor.schedule(task, 1, TimeUnit.MILLISECONDS);
        executor.shutdown();

    }

    public void onAttendanceClicked(){

        disableButtons();
        loadingLabel.setVisible(true);
        loadingGif.setVisible(true);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {

            documentGenerator.generateAttendanceList();
            ScheduledExecutorService executor1 = Executors.newSingleThreadScheduledExecutor();
            Runnable task1 = () -> {
                enableButtons();
                loadingLabel.setVisible(false);
                loadingGif.setVisible(false);
            };
            executor1.schedule(task1, 1, TimeUnit.MILLISECONDS);
            executor1.shutdown();

        };
        executor.schedule(task, 1, TimeUnit.MILLISECONDS);
        executor.shutdown();

    }

    public void onEmailListClicked(){

        disableButtons();
        loadingLabel.setVisible(true);
        loadingGif.setVisible(true);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {

            documentGenerator.generateEmailList();
            ScheduledExecutorService executor1 = Executors.newSingleThreadScheduledExecutor();
            Runnable task1 = () -> {
                enableButtons();
                loadingLabel.setVisible(false);
                loadingGif.setVisible(false);
            };
            executor1.schedule(task1, 1, TimeUnit.MILLISECONDS);
            executor1.shutdown();

        };
        executor.schedule(task, 1, TimeUnit.MILLISECONDS);
        executor.shutdown();

    }

    public void onLodgingListClicked(){

        disableButtons();
        loadingLabel.setVisible(true);
        loadingGif.setVisible(true);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {

            documentGenerator.generateLodgingList();
            ScheduledExecutorService executor1 = Executors.newSingleThreadScheduledExecutor();
            Runnable task1 = () -> {
                enableButtons();
                loadingLabel.setVisible(false);
                loadingGif.setVisible(false);
            };
            executor1.schedule(task1, 1, TimeUnit.MILLISECONDS);
            executor1.shutdown();

        };
        executor.schedule(task, 1, TimeUnit.MILLISECONDS);
        executor.shutdown();
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
    public void onReportsClicked(){/*Nothing, already on the edit/import page*/}

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
     * Disables all report buttons
     */
    private void disableButtons(){

        emailListButton.setDisable(true);
        lodgingListButton.setDisable(true);
        attendanceButton.setDisable(true);
        testAnalysisButton.setDisable(true);
        districtSumButton.setDisable(true);
        individualSumButton.setDisable(true);
        certificatesButton.setDisable(true);
        currentRankingsButton.setDisable(true);
        uniformInfoButton.setDisable(true);
        averyListButton.setDisable(true);
        profilesButton.setDisable(true);

    }

    /**
     * Enables all report buttons
     */
    private void enableButtons(){

        emailListButton.setDisable(false);
        lodgingListButton.setDisable(false);
        attendanceButton.setDisable(false);
        testAnalysisButton.setDisable(false);
        districtSumButton.setDisable(false);
        individualSumButton.setDisable(false);
        certificatesButton.setDisable(false);
        currentRankingsButton.setDisable(false);
        uniformInfoButton.setDisable(false);
        averyListButton.setDisable(false);
        profilesButton.setDisable(false);

    }

}

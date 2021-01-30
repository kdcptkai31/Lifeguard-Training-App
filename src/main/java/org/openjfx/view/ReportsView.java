package org.openjfx.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import org.openjfx.controller.Controller;
import org.openjfx.controller.LifeguardTrainingApplication;

import java.io.IOException;

public class ReportsView {

    Controller controller;

    @FXML
    private Label yearLabel;
    @FXML
    private Label sessionLabel;

    @FXML
    protected void initialize(){

        controller = LifeguardTrainingApplication.getController();
        yearLabel.setText(String.valueOf(controller.getCurrentYear()));
        sessionLabel.setText("Session " + controller.getCurrentSession());

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

}
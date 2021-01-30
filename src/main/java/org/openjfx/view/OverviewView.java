package org.openjfx.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Pair;
import org.openjfx.controller.Controller;
import org.openjfx.controller.LifeguardTrainingApplication;
import org.openjfx.model.EmergencyContact;
import org.openjfx.model.Trainee;

import java.io.IOException;

public class OverviewView {

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

//        if(controller.getDBManager().addInitialTrainee(new Trainee("bob", "m", "smith", "12313",
//                "san clemente", "CA", "434-534-1231", "ddd@gmail.com", "San CLemente", true,
//                new EmergencyContact(0, "Veronika", "MOM",
//                        "434-645-7676", "14312312", "SAN CLEMENTE",
//                        "CA", "23424"), 45, false, false, true, 2021,
//                1)))
//            System.out.println("WORKED");
//
//        System.out.println("DONE");

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

}

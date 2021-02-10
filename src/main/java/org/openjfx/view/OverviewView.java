package org.openjfx.view;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import org.openjfx.controller.Controller;
import org.openjfx.controller.LifeguardTrainingApplication;
import org.openjfx.model.EmergencyContact;
import org.openjfx.model.Instructor;
import org.openjfx.model.Trainee;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;

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

        Vector<Instructor> tmp = controller.getDBManager().getAllInstructorsFromSession(controller.getCurrentYear(), controller.getCurrentSession());


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

package org.openjfx.view;

import javafx.fxml.FXML;
import org.openjfx.controller.Controller;
import org.openjfx.controller.LifeguardTrainingApplication;

public class SetUpView {

    Controller controller;

    @FXML
    protected void initialize(){

        controller = LifeguardTrainingApplication.getController();

    }

}

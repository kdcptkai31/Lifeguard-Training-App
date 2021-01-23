package org.openjfx.view;

import javafx.fxml.FXML;
import org.openjfx.controller.Controller;
import org.openjfx.controller.LifeguardTrainingApplication;
import org.openjfx.model.EmergencyContact;
import org.openjfx.model.Trainee;

public class HomePageView {

    Controller controller;

    @FXML
    protected void initialize(){

        controller = LifeguardTrainingApplication.getController();
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

}

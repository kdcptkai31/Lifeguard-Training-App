package org.openjfx.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SceneCoordinator {

    private Controller controller;
    private Stage window;

    /**
     * Initializes the game controller into the scene coordinator for use throughout the application.
     * @param window
     */
    public SceneCoordinator(Stage window){

        this.window = window;
        this.controller = new Controller();

    }

    /**
     * Fetches the login scene.
     * @throws Exception
     */
    public void showHomeScene() throws IOException {
        URL url = new File("src/main/resources/org/openjfx/layout/homepage.fxml").toURL();
        Parent layout = FXMLLoader.load(url);
        this.window.setScene(new Scene(layout));

    }

    /**
     * Closes the application.
     */
    public void onExitRequested(){System.exit(0);}

    //Getters
    /**
     *
     * @return the controller
     */
    public Controller getController(){return controller;}

}

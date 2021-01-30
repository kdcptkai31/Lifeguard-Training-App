package org.openjfx.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SceneCoordinator {

    private Stage window;
    private Controller controller;

    /**
     * Initializes the game controller into the scene coordinator for use throughout the application.
     * @param window
     */
    public SceneCoordinator(Stage window){

        this.window = window;
        this.controller = new Controller();

    }

    public void showSetUpScene() throws IOException {

        URL url = new File("src/main/resources/org/openjfx/layout/setup.fxml").toURI().toURL();
        this.window.setScene(new Scene(FXMLLoader.load(url)));

    }

    /**
     * Fetches the overview scene.
     * @throws Exception
     */
    public void showOverviewScene() throws IOException {

        URL url = new File("src/main/resources/org/openjfx/layout/overview.fxml").toURI().toURL();
        this.window.setScene(new Scene(FXMLLoader.load(url)));

    }

    /**
     * Fetches the edit import scene.
     * @throws IOException
     */
    public void showEditImportScene() throws IOException {

        URL url = new File("src/main/resources/org/openjfx/layout/editImport.fxml").toURI().toURL();
        this.window.setScene(new Scene(FXMLLoader.load(url)));

    }

    /**
     * Fetches the reports scene.
     * @throws IOException
     */
    public void showReportsScene() throws IOException {

        URL url = new File("src/main/resources/org/openjfx/layout/reports.fxml").toURI().toURL();
        this.window.setScene(new Scene(FXMLLoader.load(url)));

    }

    /**
     * Closes the application.
     */
    public void onExitRequested(){System.exit(0);}

    //Getters

    /**
     *
     * @return the controller.
     */
    public Controller getController(){return controller;}

}

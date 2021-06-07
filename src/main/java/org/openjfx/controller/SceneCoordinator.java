package org.openjfx.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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

        this.window.setScene(new Scene(FXMLLoader.load(
                                getClass().getClassLoader().getResource("org/openjfx/layout/setup.fxml"))));
    }

    /**
     * Fetches the overview scene.
     * @throws Exception
     */
    public void showOverviewScene() throws IOException {

        this.window.setScene(new Scene(FXMLLoader.load(
                                getClass().getClassLoader().getResource("org/openjfx/layout/overview.fxml"))));

    }

    /**
     * Fetches the edit import scene.
     * @throws IOException
     */
    public void showEditImportScene() throws IOException {

        this.window.setScene(new Scene(FXMLLoader.load(
                                getClass().getClassLoader().getResource("org/openjfx/layout/editImport.fxml"))));

    }

    /**
     * Fetches the reports scene.
     * @throws IOException
     */
    public void showReportsScene() throws IOException {

        this.window.setScene(new Scene(FXMLLoader.load(
                getClass().getClassLoader().getResource("org/openjfx/layout/reports.fxml"))));

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

    public Stage getStage(){return window;}

}

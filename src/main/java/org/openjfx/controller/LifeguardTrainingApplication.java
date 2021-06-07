package org.openjfx.controller;

import javafx.application.Application;
import javafx.stage.Stage;

public class LifeguardTrainingApplication extends Application {

    private static SceneCoordinator coordinator;
    private static Stage Window;

    //Constants
    final private static int defaultWidth = 1200;
    final private static int defaultHeight = 900;

    @Override
    public void start(Stage window) {

        Window = window;
        Window.setTitle("Lifeguard Training");
        setToDefaultWindowSize();
        Window.show();
        Window.setResizable(false);

        //Initializes the SceneCoordinator which also initializes the DBManager, which will initialize a DB if needed.
        coordinator = new SceneCoordinator(Window);
        try {
            if (coordinator.getController().getCurrentSession().getYear() == 0)
                coordinator.showSetUpScene();
            else
                coordinator.showOverviewScene();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Closes the application on window closure.
     */
    @Override
    public void stop(){coordinator.onExitRequested();}

    //Getters
    /**
     * Allows the controller to be accessed by each scene.
     * @return
     */
    public static Controller getController(){return coordinator.getController();}

    /**
     * Allows the scene coordinator to be called from each scene view.
     * @return
     */
    public static SceneCoordinator getCoordinator(){return coordinator;}

    //Setters
    /**
     * Changes the window dimensions.
     * @param width
     * @param height
     */
    public static void setWindowSize(int width, int height){

        Window.setWidth(width);
        Window.setHeight(height);

    }

    /**
     * Changes the window dimensions to the default size.
     */
    public static void setToDefaultWindowSize(){ setWindowSize(defaultWidth, defaultHeight);}

}

package org.openjfx.view;

import javafx.application.Application;
import javafx.stage.Stage;
import org.openjfx.controller.SceneCoordinator;

public class LifeguardTrainingApplication extends Application {

    private static SceneCoordinator coordinator;
    private static Stage Window;

    @Override
    public void start(Stage window) throws Exception {
        Window = window;
        Window.setTitle("Lifeguard Training");
        Window.setWidth(600);
        Window.setHeight(475);
        Window.show();
        Window.setResizable(false);

        coordinator = new SceneCoordinator(Window);
        coordinator.showLoginScene();

    }
}

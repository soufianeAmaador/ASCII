package Purpleye;

import Purpleye.controller.ASCIIcontroller;
import Purpleye.controller.Controller;
import Purpleye.views.ASCIIview;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URI;
import java.net.URL;

public class MainApplication extends Application {

    private final String TITLE = "ASCII: convert and generate";
    private final int WIDTH = 1000;
    private final int HEIGHT = 500;
    private static Stage stage;
    private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        MainApplication.stage = stage;

        stage.setTitle(TITLE);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setX(50);
        stage.setY(50);
//        stage.setMaxWidth(1500);
        stage.initStyle(StageStyle.DECORATED);
        switchController(new ASCIIcontroller());
    }

    private static void switchController(Controller controller){
        scene = new Scene(controller.getView().getRoot());
        scene.getRoot().setStyle("-fx-background-image:url(Background-ASCII.jpg);");
        stage.setScene(scene);
        stage.show();
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        MainApplication.stage = stage;
    }
}

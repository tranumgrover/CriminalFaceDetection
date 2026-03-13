package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gui.HomePage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        HomePage home = new HomePage();

        Scene scene = new Scene(home.getView(), 1000, 650);

        stage.setTitle("Criminal Face Detection System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
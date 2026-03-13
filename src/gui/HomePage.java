package gui;

import javafx.scene.Scene;
import javafx.stage.Stage;
import gui.Dashboard;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gui.Dashboard;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class HomePage {

    private StackPane root;

    public HomePage() {

        root = new StackPane();

        // Dark gradient background
        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #141E30, #243B55);"
        );

        // Title
        Text title = new Text("Criminal Face Detection System");

        title.setStyle(
                "-fx-font-size:42;" +
                "-fx-fill:white;" +
                "-fx-font-weight:bold;"
        );

        // Subtitle
        Text subtitle = new Text("AI Powered Surveillance");

        subtitle.setStyle(
                "-fx-font-size:18;" +
                "-fx-fill:#cfd8dc;"
        );

        // Start Button
        Button startButton = new Button("Start Detection");

        startButton.setStyle(
                "-fx-font-size:20;" +
                "-fx-background-color:#ff4b4b;" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:30;" +
                "-fx-padding:12 40 12 40;"
        );

        // Hover effect
        startButton.setOnMouseEntered(e ->
                startButton.setStyle(
                        "-fx-font-size:20;" +
                        "-fx-background-color:#ff1e1e;" +
                        "-fx-text-fill:white;" +
                        "-fx-background-radius:30;" +
                        "-fx-padding:12 40 12 40;"
                )
        );
        startButton.setOnAction(e -> {

    Stage stage = (Stage) root.getScene().getWindow();

    Dashboard dashboard = new Dashboard();

    Scene scene = new Scene(dashboard.getView(), 1000, 650);

    stage.setScene(scene);

});

        startButton.setOnMouseExited(e ->
                startButton.setStyle(
                        "-fx-font-size:20;" +
                        "-fx-background-color:#ff4b4b;" +
                        "-fx-text-fill:white;" +
                        "-fx-background-radius:30;" +
                        "-fx-padding:12 40 12 40;"
                )
        );

        startButton.setOnAction(e -> {

    Stage stage = (Stage) root.getScene().getWindow();

    Dashboard dashboard = new Dashboard();

    Scene scene = new Scene(dashboard.getView(), 1000, 650);

    stage.setScene(scene);

});

        VBox box = new VBox(20, title, subtitle, startButton);

        box.setAlignment(Pos.CENTER);

        root.getChildren().add(box);
    }

    public StackPane getView() {
        return root;
    }
}
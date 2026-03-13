package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Dashboard {

    private BorderPane root;

    public static Text alertText;
    public static Text nameText;
    public static Text crimeText;
    public static VBox alertBox;
    public static ImageView matchedImageView;

    public Dashboard() {

        root = new BorderPane();

        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #08131f, #10273a, #17384d);"
        );

        Text title = new Text("Criminal Detection Dashboard");
        title.setStyle(
                "-fx-font-size:30;" +
                "-fx-fill:white;" +
                "-fx-font-weight:bold;"
        );

        HBox topBar = new HBox(title);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(20));

        VBox cameraBox = new VBox();
        cameraBox.setPrefSize(520, 360);
        cameraBox.setAlignment(Pos.CENTER);
        cameraBox.setStyle(
                "-fx-background-color:#111827;" +
                "-fx-background-radius:16;" +
                "-fx-border-color:#2d89ef;" +
                "-fx-border-width:2;" +
                "-fx-border-radius:16;"
        );

        ImageView cameraView = new ImageView();
        cameraView.setFitWidth(500);
        cameraView.setFitHeight(340);
        cameraView.setPreserveRatio(true);

        cameraBox.getChildren().add(cameraView);

        VBox infoBox = new VBox(20);
        infoBox.setPrefWidth(320);
        infoBox.setPadding(new Insets(20));
        infoBox.setStyle(
                "-fx-background-color:#111827;" +
                "-fx-border-color:#2d89ef;" +
                "-fx-border-width:2;" +
                "-fx-border-radius:16;"
        );

        Text infoTitle = new Text("Criminal Details");
        infoTitle.setStyle(
                "-fx-fill:white;" +
                "-fx-font-size:22;" +
                "-fx-font-weight:bold;"
        );

        alertText = new Text("Status: Waiting for scan...");
        alertText.setFill(Color.LIGHTGRAY);

        nameText = new Text("Name: ---");
        crimeText = new Text("Crime: ---");

        nameText.setFill(Color.WHITE);
        crimeText.setFill(Color.WHITE);

        matchedImageView = new ImageView();
        matchedImageView.setFitWidth(220);
        matchedImageView.setFitHeight(180);
        matchedImageView.setPreserveRatio(true);

        alertBox = new VBox(12, alertText, nameText, crimeText, matchedImageView);
        alertBox.setPadding(new Insets(15));
        alertBox.setStyle(
                "-fx-background-color:#1f2937;" +
                "-fx-background-radius:14;" +
                "-fx-border-color:#4b5563;" +
                "-fx-border-radius:14;"
        );

        infoBox.getChildren().addAll(infoTitle, alertBox);

        Button detectButton = new Button("Start Camera");
        detectButton.setStyle(
                "-fx-font-size:16;" +
                "-fx-background-color:#ef4444;" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:25;" +
                "-fx-padding:12 28 12 28;"
        );

        Button stopButton = new Button("Stop Camera");
        stopButton.setStyle(
                "-fx-font-size:16;" +
                "-fx-background-color:#6b7280;" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:25;" +
                "-fx-padding:12 28 12 28;"
        );

        Button addCriminalButton = new Button("Add Criminal");
        addCriminalButton.setStyle(
                "-fx-font-size:16;" +
                "-fx-background-color:#2563eb;" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:25;" +
                "-fx-padding:12 28 12 28;"
        );

        Button viewButton = new Button("View Criminals");
        viewButton.setStyle(
                "-fx-font-size:16;" +
                "-fx-background-color:#10b981;" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:25;" +
                "-fx-padding:12 28 12 28;"
        );

        detectButton.setOnMouseEntered(e -> detectButton.setStyle(
                "-fx-font-size:16;" +
                "-fx-background-color:#dc2626;" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:25;" +
                "-fx-padding:12 28 12 28;"
        ));

        detectButton.setOnMouseExited(e -> detectButton.setStyle(
                "-fx-font-size:16;" +
                "-fx-background-color:#ef4444;" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:25;" +
                "-fx-padding:12 28 12 28;"
        ));

        CameraService cameraService = new CameraService();

        detectButton.setOnAction(e -> cameraService.startCamera(cameraView));
        stopButton.setOnAction(e -> cameraService.stopCamera());

        addCriminalButton.setOnAction(e -> {
            AddCriminalPage page = new AddCriminalPage();
            page.show();
        });

        viewButton.setOnAction(e -> {
            ViewCriminalsPage page = new ViewCriminalsPage();
            page.show();
        });

        HBox buttonRow1 = new HBox(15, detectButton, stopButton);
        buttonRow1.setAlignment(Pos.CENTER);

        HBox buttonRow2 = new HBox(15, addCriminalButton, viewButton);
        buttonRow2.setAlignment(Pos.CENTER);

        VBox centerBox = new VBox(20, cameraBox, buttonRow1, buttonRow2);
        centerBox.setAlignment(Pos.CENTER);

        root.setTop(topBar);
        root.setCenter(centerBox);
        root.setRight(infoBox);
    }

    public BorderPane getView() {
        return root;
    }
}
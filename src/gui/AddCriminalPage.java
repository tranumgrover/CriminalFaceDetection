package gui;

import database.DBConnection;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddCriminalPage {

    private String imagePath = "";

    public void show() {

        Stage stage = new Stage();

        TextField nameField = new TextField();
        nameField.setPromptText("Enter Criminal Name");

        TextField crimeField = new TextField();
        crimeField.setPromptText("Enter Crime");

        Button uploadButton = new Button("Upload Image");
        Button captureButton = new Button("Capture From Camera");
        Button saveButton = new Button("Save Criminal");

        Label imageLabel = new Label("No image selected");
        Label statusLabel = new Label("");

        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Criminal Image");

            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                imagePath = file.getAbsolutePath();
                imageLabel.setText(file.getName());
            }
        });

        captureButton.setOnAction(e -> {
            String path = CriminalCameraCapture.captureImage();

            if (path != null) {
                imagePath = path;
                imageLabel.setText("Captured: " + path);
            }
        });

        saveButton.setOnAction(e -> {
            try {
                if (nameField.getText().trim().isEmpty()) {
                    statusLabel.setText("Enter criminal name");
                    return;
                }

                if (crimeField.getText().trim().isEmpty()) {
                    statusLabel.setText("Enter crime");
                    return;
                }

                if (imagePath.trim().isEmpty()) {
                    statusLabel.setText("Select or capture an image");
                    return;
                }

                Connection conn = DBConnection.connect();

                if (conn == null) {
                    statusLabel.setText("Database connection failed");
                    return;
                }

                String sql = "INSERT INTO criminals(name, crime, image_path) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, nameField.getText().trim());
                ps.setString(2, crimeField.getText().trim());
                ps.setString(3, imagePath);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    statusLabel.setText("Criminal saved successfully");
                    System.out.println("Criminal saved successfully");
                } else {
                    statusLabel.setText("Failed to save criminal");
                }

                ps.close();
                conn.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                statusLabel.setText("Error: " + ex.getMessage());
            }
        });

        VBox root = new VBox();
        root.setSpacing(15);
        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(
                nameField,
                crimeField,
                uploadButton,
                captureButton,
                imageLabel,
                saveButton,
                statusLabel
        );

        Scene scene = new Scene(root, 420, 360);

        stage.setTitle("Add Criminal");
        stage.setScene(scene);
        stage.show();
    }
}
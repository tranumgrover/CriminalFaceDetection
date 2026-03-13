package gui;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;

public class CameraService {

    private VideoCapture camera;
    private boolean running = false;
    private boolean alarmPlaying = false;

    public void startCamera(ImageView imageView) {

        if (running) {
            return;
        }

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        camera = new VideoCapture(0);

        CascadeClassifier faceDetector =
                new CascadeClassifier("haarcascade_frontalface_default.xml");

        running = true;

        new Thread(() -> {

            Mat frame = new Mat();

            while (running) {

                camera.read(frame);

                if (!frame.empty()) {

                    MatOfRect faces = new MatOfRect();
                    faceDetector.detectMultiScale(frame, faces);

                    boolean anyMatch = false;

                    for (Rect rect : faces.toArray()) {

                        Mat face = new Mat(frame, rect);
                        MatchResult match = FaceMatcher.matchFace(face);

                        Scalar color;
                        String label;

                        if (match.isMatched()) {
                            anyMatch = true;
                            color = new Scalar(0, 0, 255);
                            label = match.getName() + " - " + match.getCrime();

                            Platform.runLater(() -> {
                                Dashboard.alertText.setText("⚠ ALERT: Criminal Detected");
                                Dashboard.alertText.setFill(javafx.scene.paint.Color.RED);

                                Dashboard.nameText.setText("Name: " + match.getName());
                                Dashboard.crimeText.setText("Crime: " + match.getCrime());

                                Dashboard.alertBox.setStyle(
                                        "-fx-background-color:#3a1f1f;" +
                                        "-fx-background-radius:12;" +
                                        "-fx-border-color:red;" +
                                        "-fx-border-width:2;" +
                                        "-fx-border-radius:12;"
                                );

                                try {
                                    Dashboard.matchedImageView.setImage(
                                            new Image(new FileInputStream(match.getImagePath()))
                                    );
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            });

                            if (!alarmPlaying) {
                                alarmPlaying = true;
                                new Thread(() -> {
                                    AlarmPlayer.playAlarm();
                                    alarmPlaying = false;
                                }).start();
                            }

                        } else {
                            color = new Scalar(0, 255, 0);
                            label = "Unknown";
                        }

                        Imgproc.rectangle(
                                frame,
                                new Point(rect.x, rect.y),
                                new Point(rect.x + rect.width, rect.y + rect.height),
                                color,
                                3
                        );

                        Imgproc.putText(
                                frame,
                                label,
                                new Point(rect.x, rect.y - 10),
                                Imgproc.FONT_HERSHEY_SIMPLEX,
                                0.7,
                                color,
                                2
                        );
                    }

                    if (!anyMatch) {
                        Platform.runLater(() -> {
                            Dashboard.alertText.setText("✅ No criminal found");
                            Dashboard.alertText.setFill(javafx.scene.paint.Color.LIGHTGREEN);

                            Dashboard.nameText.setText("Name: ---");
                            Dashboard.crimeText.setText("Crime: ---");
                            Dashboard.matchedImageView.setImage(null);

                            Dashboard.alertBox.setStyle(
                                    "-fx-background-color:#1f3a28;" +
                                    "-fx-background-radius:12;" +
                                    "-fx-border-color:limegreen;" +
                                    "-fx-border-width:2;" +
                                    "-fx-border-radius:12;"
                            );
                        });
                    }

                    Image img = mat2Image(frame);
                    Platform.runLater(() -> imageView.setImage(img));
                }
            }

            if (camera != null) {
                camera.release();
            }

        }).start();
    }

    public void stopCamera() {
        running = false;
        if (camera != null) {
            camera.release();
        }
    }

    private Image mat2Image(Mat frame) {

        Mat resized = new Mat();
        Imgproc.resize(frame, resized, new Size(640, 480));

        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", resized, buffer);

        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }
}
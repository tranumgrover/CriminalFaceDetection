package gui;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.highgui.HighGui;

public class CriminalCameraCapture {

    public static String captureImage() {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        VideoCapture camera = new VideoCapture(0);

        if (!camera.isOpened()) {
            System.out.println("Camera not detected");
            return null;
        }

        CascadeClassifier faceDetector =
                new CascadeClassifier("haarcascade_frontalface_default.xml");

        Mat frame = new Mat();
        String filePath = null;

        while (true) {

            camera.read(frame);

            if (frame.empty()) {
                continue;
            }

            HighGui.imshow("Capture Criminal Image - Press SPACE", frame);

            int key = HighGui.waitKey(30);

            if (key == 32) {
                MatOfRect faces = new MatOfRect();
                faceDetector.detectMultiScale(frame, faces);

                if (faces.toArray().length > 0) {
                    Rect rect = faces.toArray()[0];
                    Mat capturedFace = new Mat(frame, rect);

                    String filename = "criminal_" + System.currentTimeMillis() + ".png";
                    filePath = "criminal_images/" + filename;

                    Imgcodecs.imwrite(filePath, capturedFace);
                    System.out.println("Face image saved: " + filePath);
                } else {
                    System.out.println("No face detected while capturing.");
                }

                break;
            }

            if (key == 27) {
                break;
            }
        }

        camera.release();
        HighGui.destroyAllWindows();

        return filePath;
    }
}
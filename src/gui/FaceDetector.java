package gui;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.MatOfRect;

import org.opencv.videoio.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;

public class FaceDetector {

    public static void startCamera() {

        // Load OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Start camera
        VideoCapture camera = new VideoCapture(0 , 700);

        if (!camera.isOpened()) {
            System.out.println("Camera not detected");
            return;
        }

        // Load face detection model
        CascadeClassifier faceDetector =
                new CascadeClassifier("haarcascade_frontalface_default.xml");

        Mat frame = new Mat();

        while (true) {

            camera.read(frame);

            if (frame.empty()) {
                break;
            }

            MatOfRect faceDetections = new MatOfRect();

            faceDetector.detectMultiScale(frame, faceDetections);

            // DRAW RECTANGLE AROUND FACE
            for (Rect rect : faceDetections.toArray()) {

                Imgproc.rectangle(
                        frame,
                        new Point(rect.x, rect.y),
                        new Point(rect.x + rect.width, rect.y + rect.height),
                        new Scalar(0, 255, 0),
                        3
                );
            }

            HighGui.imshow("Face Detection Camera", frame);

            // Press ESC to exit
            if (HighGui.waitKey(30) == 27) {
                break;
            }
        }

        camera.release();
        HighGui.destroyAllWindows();
    }
}
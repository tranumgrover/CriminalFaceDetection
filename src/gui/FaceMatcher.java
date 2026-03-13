package gui;

import database.DBConnection;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FaceMatcher {

    public static MatchResult matchFace(Mat detectedFace) {

        try {
            CascadeClassifier faceDetector =
                    new CascadeClassifier("haarcascade_frontalface_default.xml");

            Mat detectedGray = new Mat();
            Imgproc.cvtColor(detectedFace, detectedGray, Imgproc.COLOR_BGR2GRAY);
            Imgproc.resize(detectedGray, detectedGray, new Size(200, 200));
            Imgproc.equalizeHist(detectedGray, detectedGray);

            Connection conn = DBConnection.connect();
            String sql = "SELECT name, crime, image_path FROM criminals";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            double bestScore = -1;
            String bestName = "Unknown";
            String bestCrime = "No Record";
            String bestImagePath = "";

            while (rs.next()) {
                String name = rs.getString("name");
                String crime = rs.getString("crime");
                String imagePath = rs.getString("image_path");

                Mat criminalImage = Imgcodecs.imread(imagePath);

                if (criminalImage.empty()) {
                    continue;
                }

                Mat criminalGray = new Mat();
                Imgproc.cvtColor(criminalImage, criminalGray, Imgproc.COLOR_BGR2GRAY);

                MatOfRect faces = new MatOfRect();
                faceDetector.detectMultiScale(criminalGray, faces);

                if (faces.toArray().length == 0) {
                    continue;
                }

                Rect faceRect = faces.toArray()[0];
                Mat criminalFace = new Mat(criminalGray, faceRect);

                Imgproc.resize(criminalFace, criminalFace, new Size(200, 200));
                Imgproc.equalizeHist(criminalFace, criminalFace);

                Mat result = new Mat();
                Imgproc.matchTemplate(criminalFace, detectedGray, result, Imgproc.TM_CCOEFF_NORMED);

                Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

                if (mmr.maxVal > bestScore) {
                    bestScore = mmr.maxVal;
                    bestName = name;
                    bestCrime = crime;
                    bestImagePath = imagePath;
                }
            }

            rs.close();
            ps.close();
            conn.close();

            if (bestScore > 0.45) {
                return new MatchResult(true, bestName, bestCrime, bestImagePath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new MatchResult(false, "Unknown", "No Record", "");
    }
}
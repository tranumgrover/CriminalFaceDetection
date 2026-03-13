package gui;

import java.io.File;
import javax.sound.sampled.*;

public class AlarmPlayer {

    public static void playAlarm() {

        try {

            File soundFile = new File("sounds/alarm.wav");

            AudioInputStream audioStream =
                    AudioSystem.getAudioInputStream(soundFile);

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package com.example.chessfx.UI;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class SoundSetup {

    private AudioInputStream audioInputStream;
    private Clip moveClip;

    public SoundSetup() {
        try {
            // Load sound file
            String filePath = "src/main/resources/com/example/chessfx/Sound/movingPiece(trimmed).wav";
            File soundFile = new File(filePath);

            // Initialize AudioInputStream and Clip
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            moveClip = AudioSystem.getClip();
            moveClip.open(audioInputStream);

            // Reset audioInputStream for reuse
            audioInputStream.close();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            System.out.println("Exception message: " + e.getMessage());
        }
    }

    public void startMusic() {
        if (moveClip == null) {
            System.out.println("Clip is not initialized, cannot play sound.");
            return;
        }

        try {
            // Reset the clip to the beginning
            moveClip.setFramePosition(0);
            moveClip.start();
            //logic.delay(100);
            moveClip.stop();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception message: " + e.getMessage());
        }
    }

}

package com.example.chessfx.UI;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class SoundSetup {

    private AudioInputStream audioInputStream1,audioInputStream2,audioInputStream3,audioInputStream4;
    private AudioInputStream audioInputStream5,audioInputStream6;

    private Clip moveClip1,moveClip2,captureClip1,captureClip2,castleClip1,castleClip2;

    public SoundSetup() {
        try {
            // Load sound file
            String filePathMovePiece = "src/main/resources/com/example/chessfx/Sound/move-self.wav";
            String filePathCapture = "src/main/resources/com/example/chessfx/Sound/capture.wav";
            String filePathCastling = "src/main/resources/com/example/chessfx/Sound/castle.wav";
            File soundFileMovePiece = new File(filePathMovePiece);
            File soundFileCapture = new File(filePathCapture);
            File soundFileCastle = new File(filePathCastling);

            // Initialize AudioInputStream and Clip for move piece
            audioInputStream1 = AudioSystem.getAudioInputStream(soundFileMovePiece);
            audioInputStream2 = AudioSystem.getAudioInputStream(soundFileMovePiece);
            moveClip1 = AudioSystem.getClip();
            moveClip2 = AudioSystem.getClip();
            moveClip1.open(audioInputStream1);
            moveClip2.open(audioInputStream2);
            // Initialize AudioInputStream and Clip for capture
            audioInputStream3 = AudioSystem.getAudioInputStream(soundFileCapture);
            audioInputStream4 = AudioSystem.getAudioInputStream(soundFileCapture);
            captureClip1 = AudioSystem.getClip();
            captureClip2 = AudioSystem.getClip();
            captureClip1.open(audioInputStream3);
            captureClip2.open(audioInputStream4);
            // Initialize AudioInputStream and Clip for castling
            audioInputStream5 = AudioSystem.getAudioInputStream(soundFileCastle);
            audioInputStream6 = AudioSystem.getAudioInputStream(soundFileCastle);
            castleClip1 = AudioSystem.getClip();
            castleClip2 = AudioSystem.getClip();
            castleClip1.open(audioInputStream5);
            castleClip2.open(audioInputStream6);

            // Reset audioInputStream for reuse
            audioInputStream1.close();
            audioInputStream2.close();
            audioInputStream3.close();
            audioInputStream4.close();
            audioInputStream5.close();
            audioInputStream6.close();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            System.out.println("Exception message: " + e.getMessage());
        }
    }

    public void startMusic(boolean isPlayer, boolean isCapture, boolean isCastle) {
        Clip clip;
        if (isPlayer) {
            clip = isCapture ? captureClip1 : (isCastle ? castleClip1 : moveClip1);
        } else {
            clip = isCapture ? captureClip2 : (isCastle ? castleClip2 : moveClip2);
        }
        playClip(clip);
    }

    private void playClip(Clip clip) {
        if (clip == null) {
            System.out.println("Clip is not initialized, cannot play sound.");
            return;
        }
        try {
            clip.setFramePosition(0);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception message: " + e.getMessage());
        }
    }
}

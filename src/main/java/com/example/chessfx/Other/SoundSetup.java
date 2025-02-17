package com.example.chessfx.Other;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class SoundSetup {

    private AudioInputStream audioInputStream1,audioInputStream2,audioInputStream3,audioInputStream4;
    private AudioInputStream audioInputStream5,audioInputStream6,audioInputStream7,audioInputStream8;
    private AudioInputStream audioInputStream9,audioInputStream10,audioInputStream11,audioInputStream12;
    private AudioInputStream audioInputStream13,audioInputStream14;
    private Clip moveClip1,moveClip2,captureClip1,captureClip2,castleClip1,castleClip2;
    private Clip startGameClip,endGameClip,moveCheckClip1,moveCheckClip2,illegalClip1,illegalClip2;
    private Clip promotionClip1,promotionClip2;

    public SoundSetup() {
        try {
            // Load sound file
            String filePathMovePiece = "src/main/resources/com/example/chessfx/Sound/move-self.wav";
            String filePathCapture = "src/main/resources/com/example/chessfx/Sound/capture.wav";
            String filePathCastling = "src/main/resources/com/example/chessfx/Sound/castle.wav";
            String fileGameStart = "src/main/resources/com/example/chessfx/Sound/game-start.wav";
            String fileGameEnd = "src/main/resources/com/example/chessfx/Sound/game-end.wav";
            String fileMoveCheck = "src/main/resources/com/example/chessfx/Sound/move-check.wav";
            String fileIllegal = "src/main/resources/com/example/chessfx/Sound/illegal.wav";
            String filePromote = "src/main/resources/com/example/chessfx/Sound/promote.wav";
            File soundFileMovePiece = new File(filePathMovePiece);
            File soundFileCapture = new File(filePathCapture);
            File soundFileCastle = new File(filePathCastling);
            File soundFileGameStart = new File(fileGameStart);
            File soundFileGameEnd = new File(fileGameEnd);
            File soundFileMoveCheck = new File(fileMoveCheck);
            File soundFileIllegal = new File(fileIllegal);
            File soundFilePromote = new File(filePromote);

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
            // Initialize AudioInputStream and Clip for game start and end
            audioInputStream7 = AudioSystem.getAudioInputStream(soundFileGameStart);
            audioInputStream8 = AudioSystem.getAudioInputStream(soundFileGameEnd);
            startGameClip = AudioSystem.getClip();
            endGameClip = AudioSystem.getClip();
            startGameClip.open(audioInputStream7);
            endGameClip.open(audioInputStream8);
            // Initialize AudioInputStream and Clip for check and illegal move
            audioInputStream9 = AudioSystem.getAudioInputStream(soundFileMoveCheck);
            audioInputStream10 = AudioSystem.getAudioInputStream(soundFileMoveCheck);
            audioInputStream11 = AudioSystem.getAudioInputStream(soundFileIllegal);
            audioInputStream12 = AudioSystem.getAudioInputStream(soundFileIllegal);
            moveCheckClip1 = AudioSystem.getClip();
            moveCheckClip2 = AudioSystem.getClip();
            illegalClip1 = AudioSystem.getClip();
            illegalClip2 = AudioSystem.getClip();
            moveCheckClip1.open(audioInputStream9);
            moveCheckClip2.open(audioInputStream10);
            illegalClip1.open(audioInputStream11);
            illegalClip2.open(audioInputStream12);
            // Initialize AudioInputStream and Clip for pawm promotion
            audioInputStream13 = AudioSystem.getAudioInputStream(soundFilePromote);
            audioInputStream14 = AudioSystem.getAudioInputStream(soundFilePromote);
            promotionClip1 = AudioSystem.getClip();
            promotionClip2 = AudioSystem.getClip();
            promotionClip1.open(audioInputStream13);
            promotionClip2.open(audioInputStream14);
            // Reset audioInputStream for reuse
            audioInputStream1.close();
            audioInputStream2.close();
            audioInputStream3.close();
            audioInputStream4.close();
            audioInputStream5.close();
            audioInputStream6.close();
            audioInputStream7.close();
            audioInputStream8.close();
            audioInputStream9.close();
            audioInputStream10.close();
            audioInputStream11.close();
            audioInputStream12.close();
            audioInputStream13.close();
            audioInputStream14.close();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            System.out.println("Exception message: " + e.getMessage());
        }
    }

    public void startMusic(boolean notPlayer, boolean isCapture, boolean isCastle,boolean isCheck) {
        Clip clip;
        if (notPlayer) {
            clip = isCapture ? captureClip1 : (isCastle ? castleClip1 : isCheck ? moveCheckClip1 : moveClip1);
        } else {
            clip = isCapture ? captureClip2 : (isCastle ? castleClip2 : isCheck ? moveCheckClip2 : moveClip2);
        }
        playClip(clip);
    }
    public void illegalMusic(boolean notPlayer){
        Clip clip;
        if(notPlayer) clip = illegalClip1;
        else         clip = illegalClip2;

        playClip(clip);

    }
    public void pawnPromotionMusic(boolean notPlayer){
        Clip clip;
        if(notPlayer) clip = promotionClip1;
        else         clip = promotionClip2;

        playClip(clip);
    }
    public void gameMusic(boolean isStart){
        Clip clip;
        if(isStart){
            clip = startGameClip;
        }
        else
            clip = endGameClip;

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

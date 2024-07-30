package com.example.chessfx.Controller;
import com.example.chessfx.Logic.*;

import com.example.chessfx.UI.GameOverUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Board implements Initializable {

    @FXML
    public AnchorPane anchorPane;
    @FXML
    public GridPane boardPane;

    @FXML
    public Label opponentTimerLabel,ownTimerLabel;
    public StackPane[] squares;

    private GamePlay gamePlay;
    private GameOverUI gameOverUI;
    private Time time;   // Time only work when time is > 0 meaning the game has time odds
    private Timeline timeline;

    // This fields will be set by the parent
    private Settings settings;
    private String defaultColor;
    private boolean isSound;

    @Override
    public void initialize(URL url,ResourceBundle resourceBundle) {

        squares = boardPane.getChildren().toArray(StackPane[]::new);
        boardPane.setOnMouseDragEntered(this::mouseDraggedEnter);
        boardPane.setOnMouseDragged(this::mouseDragged);
        boardPane.setOnMouseReleased(mouseEvent -> mouseReleased());

        int index = 0;
        for (StackPane square : squares) {
            square.setId(String.valueOf(index));
            square.setOnMouseClicked(mouseEvent -> mouseClick(square));
            index++;
        }

    }
    public void setSettings(Settings settings){
        this.settings = settings;
        isSound = settings.isSound;
        if(settings.boardType == logic.GREEN_BOARD) defaultColor = "white "+logic.FOREST_GREEN;
        else if(settings.boardType == logic.BROWN_BOARD) defaultColor = "white "+logic.BROWN;
        else if(settings.boardType == logic.BLACK_BOARD) defaultColor = "white "+logic.GRAY;

        init_gamePlay();
    }
    private void init_gamePlay(){
        setBackground();
        if(settings.duration > 0) {
            time = new Time(settings.duration);
        }
        setTimeLine();
        gamePlay = new GamePlay(boardPane,squares, time,defaultColor, settings.player, settings.gameType);
        System.out.println("Time : "+settings.duration);
        System.out.println("Player : "+settings.player);
        System.out.println("GameType : "+settings.gameType);
        System.out.println("sound : "+settings.isSound);
    }

    private void mouseClick(StackPane square) {

        // According to game
        // 2v2 or computer
        // Time constrains
        gamePlay.play(square, settings.gameType);
    }
    private void mouseDraggedEnter(MouseEvent mouseEvent){

    }
    private void mouseDragged(MouseEvent mouseEvent){

    }
    private void mouseReleased(){

    }


    private void setTimeLine() {
        // Initialize the timeline
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            // Update the timer labels
            if (settings.duration > 0) {
                if (settings.player == logic.WHITE) {
                    ownTimerLabel.setText(String.valueOf(time.getWhiteTime()));
                    opponentTimerLabel.setText(String.valueOf(time.getBlackTime()));
                } else {
                    ownTimerLabel.setText(String.valueOf(time.getBlackTime()));
                    opponentTimerLabel.setText(String.valueOf(time.getWhiteTime()));
                }
            }

            // Check if the game is over
            boolean gameOver = gamePlay.checkGameOver();
            if (gameOver) {
                // Stop the timeline
                timeline.stop();
                // Show the game over UI
                String text = gamePlay.getGameOverText();
                gameOverUI = new GameOverUI(anchorPane,settings,text);
            }
        }));

        // Set the timeline to repeat indefinitely
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void setBackground(){

        String css = logic.BACKGROUND_CSS + logic.DIM_GRAY;
        anchorPane.setStyle(css);

        if(settings.duration > 0) {
            if (settings.player == logic.WHITE) {
                opponentTimerLabel.setStyle(logic.BACKGROUND_CSS + logic.BLACK_COLOR);
                opponentTimerLabel.setTextFill(new Color(1.0,1.0,1.0,1));
                ownTimerLabel.setStyle(logic.BACKGROUND_CSS + logic.WHITE_COLOR);
                ownTimerLabel.setTextFill(new Color(0,0,0,1));
            } else {
                opponentTimerLabel.setStyle(logic.BACKGROUND_CSS + logic.WHITE_COLOR);
                opponentTimerLabel.setTextFill(new Color(0,0,0,1));
                ownTimerLabel.setStyle(logic.BACKGROUND_CSS + logic.BLACK_COLOR);
                ownTimerLabel.setTextFill(new Color(1.0,1.0,1.0,1));
            }
        }
    }

}

package com.example.chessfx.Controller;
import com.example.chessfx.Logic.*;

import com.example.chessfx.Logic.Abstract.loadFXML;
import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Other.Time;
import com.example.chessfx.UI.GameOverUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class Game_Controller implements Initializable {

    @FXML
    public AnchorPane anchorPane;
    @FXML
    public GridPane boardPane;
    @FXML
    public Label opponentTimerLabel,ownTimerLabel;
    @FXML
    public Button restartButton,resignButton;
    public StackPane[] squares;

    private GamePlay gamePlay;
    private GameOverUI gameOverUI;
    private Time time;   // Time only work when time is > 0 meaning the game has time odds
    private Timeline timeline;

    // This fields will be set by the parent
    private Settings settings;

    @Override
    public void initialize(URL url,ResourceBundle resourceBundle) {

        squares = boardPane.getChildren().toArray(StackPane[]::new);

        int index = 0;
        for (StackPane square : squares) {
            square.setId(String.valueOf(index));
            square.setOnMouseClicked(mouseEvent -> mouseClick(square));
            index++;
        }

    }
    public void setSettings(Settings settings){
        this.settings = settings;

        init_gamePlay();
    }
    private void init_gamePlay(){
        setBackground();
        setButtons();
        if(settings.duration > 0) {
            time = new Time(settings.duration);
        }
        setTimeLine();
        gamePlay = new GamePlay(boardPane,squares,time,settings);
        System.out.println("Time : "+settings.duration);
        System.out.println("Player : "+settings.player);
        System.out.println("GameType : "+settings.gameType);
        System.out.println("sound : "+settings.isSound);
    }

    private void mouseClick(StackPane square) {

        // According to game
        // 2v2 or computer
        // Time control
        gamePlay.play(square, settings.gameType);
    }
    private void restartOnAction(ActionEvent event){
        loadFXML.loadGame(loadFXML.GAME_FXML,event,settings);
    }
    private void resignButtonOnAction(ActionEvent event){
        gamePlay.getResign();
    }

    private void setTimeLine() {
        // Initialize the timeline
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            // Update the timer labels
            if (settings.duration > 0) {

                String whiteTime = String.format("%.2f",time.getRemainingTime(time.getWhiteTime()));
                String blackTime = String.format("%.2f",time.getRemainingTime(time.getBlackTime()));
                if (settings.player == logic.WHITE) {
                    ownTimerLabel.setText(whiteTime);
                    opponentTimerLabel.setText(blackTime);
                } else {
                    ownTimerLabel.setText(blackTime);
                    opponentTimerLabel.setText(whiteTime);
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
    private void setButtons(){
        restartButton.setOnAction(this::restartOnAction);
        restartButton.getStyleClass().add("button-19");

        resignButton.setOnAction(this::resignButtonOnAction);
        resignButton.getStyleClass().add("button-19");

    }
}

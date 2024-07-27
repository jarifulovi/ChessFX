package com.example.chessfx.Controller;
import com.example.chessfx.Logic.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
    private Time time;   // Time only work when time is > 0 meaning the game has time odds


    // This fields will be set by the parent
    private String defaultColor;
    private int gameType;
    private int player;
    private int duration;

    @Override
    public void initialize(URL url,ResourceBundle resourceBundle) {

        squares = boardPane.getChildren().toArray(StackPane[]::new);


        // Provided from parent fxml
        defaultColor = "white "+ logic.FOREST_GREEN;
        player = logic.WHITE;
        duration = 0;
        gameType = logic.ONE_PLAYER;

        setBackground();
        if(duration > 0) {
            time = new Time(duration);
            setTimer();
        }




        int index = 0;
        for (StackPane square : squares) {
            square.setId(String.valueOf(index));
            square.setOnMouseClicked(mouseEvent -> mouseClick(square));
            index++;
        }

        gamePlay = new GamePlay(boardPane,squares, time,defaultColor,player);

    }


    private void mouseClick(StackPane square) {

        // According to game
        // 2v2 or computer
        // Time constrains
        gamePlay.play(square,gameType);
    }

    private void setTimer(){

        Timeline timeline;

        if(player == logic.WHITE) {
            timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
                ownTimerLabel.setText(String.valueOf(time.getWhiteTime()));
                opponentTimerLabel.setText(String.valueOf(time.getBlackTime()));
            }));
        }
        else {
            timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
                ownTimerLabel.setText(String.valueOf(time.getBlackTime()));
                opponentTimerLabel.setText(String.valueOf(time.getWhiteTime()));
            }));
        }

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void setBackground(){

        String css = logic.BACKGROUND_CSS + logic.DIM_GRAY;
        anchorPane.setStyle(css);

        if(duration > 0) {
            if (player == logic.WHITE) {
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

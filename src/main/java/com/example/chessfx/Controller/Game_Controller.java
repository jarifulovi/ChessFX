package com.example.chessfx.Controller;
import com.example.chessfx.Logic.*;

import com.example.chessfx.Logic.Abstract.loadFXML;
import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Other.Time;
import com.example.chessfx.UI.GameOverUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
    private ImageView movingPieceImage;
    private double startX,startY;
    private final double DRAGGED_THREESHOLD = 10.0;
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
            square.setOnMousePressed(mouseEvent -> mousePressed(mouseEvent,square));
            index++;
        }
        boardPane.setOnMouseEntered(mouseEvent -> boardPane.setCursor(Cursor.HAND));
        boardPane.setOnMouseExited(mouseEvent -> boardPane.setCursor(Cursor.DEFAULT));

        boardPane.setOnMousePressed(this::mouseDragPressed);
        // When the drag is in progress in anchorPane
        boardPane.setOnMouseDragged(this::mouseDragged);
        // When drag or click released
        boardPane.setOnMouseReleased(this::mouseDragRelease);

    }
    // This method will invoke when dragging initiated
    // Has no functionality for gamePlay
    // Mouse pressed will be also invoked along it
    private void mouseDragPressed(MouseEvent event){

        double posX = event.getX();
        double posY = event.getY();
        int row = (int)posY/60;
        int col = (int)posX/60;

        movingPieceImage = gamePlay.getCurrentPieceImage(row,col);

        if(movingPieceImage != null){
            movingPieceImage.setFitWidth(60.0);
            movingPieceImage.setFitHeight(60.0);
            // Initial position of the image
            movingPieceImage.setX(posX - (movingPieceImage.getFitWidth() / 2));
            movingPieceImage.setY(posY + (movingPieceImage.getFitHeight() / 2));
            startX = posX;
            startY = posY;
            movingPieceImage.setMouseTransparent(true);
            anchorPane.getChildren().add(movingPieceImage);
        }
    }
    // This method will invoke when dragging piece
    // Has no functionality for gamePlay
    private void mouseDragged(MouseEvent event){

        // Change image position
        double posX = event.getX();
        double posY = event.getY();

        if(movingPieceImage != null){
            // change moving piece position
            movingPieceImage.setX(posX - (movingPieceImage.getFitWidth() / 2));
            movingPieceImage.setY(posY + (movingPieceImage.getFitHeight() / 2));
        }

    }

    // This method will invoke when a press is released
    // Works both for click and drag using DRAG_THREESHOLD
    private void mouseDragRelease(MouseEvent event){

        if(gamePlay.getGameOver()) return;

        // Check if a valid move is made or not
        if(movingPieceImage != null){
            anchorPane.getChildren().remove(movingPieceImage);
            movingPieceImage = null;
            double posX = event.getX();
            double posY = event.getY();
            int row = (int)posY/60;
            int col = (int)posX/60;
            int index = row * 8 + col;


            double distance = Math.abs(posX-startX) + Math.abs(posY-startY);

            if(logic.isWithinBoard(row,col) && distance > DRAGGED_THREESHOLD) {
                gamePlay.play(squares[index], settings.gameType,true);
            }
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

    private void mousePressed(MouseEvent event, StackPane square) {

        if(event.getButton() != MouseButton.SECONDARY)
            gamePlay.play(square, settings.gameType,false);
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

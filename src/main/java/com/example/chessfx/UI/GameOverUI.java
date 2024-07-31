package com.example.chessfx.UI;

import com.example.chessfx.Controller.Settings;
import com.example.chessfx.Logic.Abstract.loadFXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import javafx.event.ActionEvent;

public class GameOverUI {
    private Settings settings;
    private final AnchorPane anchorPane;
    private final VBox vBox;
    private final Label gameOverLabel;
    private  Button rematchButton;
    private  Button exitButton;
    private double vBoxWidth,vBoxHeight;

    public GameOverUI(AnchorPane anchorPane,Settings settings, String gameOverText) {
        this.settings = settings;
        this.anchorPane = anchorPane;
        vBoxWidth = anchorPane.getWidth() * 0.6;
        vBoxHeight = anchorPane.getHeight() * 0.4;

        // Initialize VBox and set alignment
        vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);

        // Initialize and configure the gameOverLabel
        gameOverLabel = new Label(gameOverText);
        gameOverLabel.setFont(new Font(48)); // Larger font size
        gameOverLabel.setTextAlignment(TextAlignment.CENTER);
        gameOverLabel.setWrapText(true); // Allows the text to wrap within the label

        setButtons();


        // Add VBox to AnchorPane
        anchorPane.getChildren().add(vBox);

        vBox.setPrefWidth(vBoxWidth);
        vBox.setPrefHeight(vBoxHeight);
        // Set background color of VBox
        vBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        // Center the VBox within the AnchorPane
        AnchorPane.setLeftAnchor(vBox, (anchorPane.getWidth() - vBoxWidth) / 2);
        AnchorPane.setRightAnchor(vBox, (anchorPane.getWidth() - vBoxWidth) / 2);
        AnchorPane.setTopAnchor(vBox, (anchorPane.getHeight() - vBoxHeight) / 2);
        AnchorPane.setBottomAnchor(vBox, (anchorPane.getHeight() - vBoxHeight) / 2);
    }

    private void setButtons(){
        // Initialize buttons and set actions
        rematchButton = new Button("Rematch");
        rematchButton.setPrefWidth(vBoxWidth/4);
        rematchButton.setPrefHeight(vBoxHeight/5);
        rematchButton.setOnAction(this::rematchOnAction);
        rematchButton.getStyleClass().add("button-19");
        exitButton = new Button("Exit");
        exitButton.setPrefWidth(vBoxWidth/4);
        exitButton.setPrefHeight(vBoxHeight/5);
        exitButton.setOnAction(this::exitOnAction);
        exitButton.getStyleClass().add("button-19");

        // Create and configure buttonBox
        HBox buttonBox = new HBox(20);
        buttonBox.setBackground(new Background(new BackgroundFill(Color.FORESTGREEN,CornerRadii.EMPTY,Insets.EMPTY)));
        buttonBox.setPrefWidth(vBoxWidth);
        buttonBox.setPrefHeight(vBoxHeight/2);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(rematchButton, exitButton);

        // Add elements to VBox
        vBox.getChildren().addAll(gameOverLabel, buttonBox);
    }

    private void rematchOnAction(ActionEvent event) {
        System.out.println("rematch");
        loadFXML.loadGame(loadFXML.GAME_FXML,event,settings);
    }

    private void exitOnAction(ActionEvent event) {
        System.out.println("exited");
        loadFXML.loadMenu(loadFXML.MENU_FXML,event,settings);
    }
}

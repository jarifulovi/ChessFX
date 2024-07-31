package com.example.chessfx.Controller;

import com.example.chessfx.Logic.Abstract.loadFXML;
import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Abstract.AssetLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Options_Controller implements Initializable {

    private Settings settings;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public AnchorPane containPane;
    @FXML
    public ToggleButton soundToggleButton;
    @FXML
    public Button greenTile,brownTile,blackTile,backButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setBackground();
        setButtons();
    }
    // Must call when loading this fxml
    public void setSettings(Settings settings){
        this.settings = settings;
        setTileSelected();
        setSoundStatus();
    }
    private void setTileSelected(){
        if(settings.boardType == logic.GREEN_BOARD){
            greenTile.setStyle(logic.BACKGROUND_CSS+logic.FOREST_GREEN);
            brownTile.setStyle(logic.BACKGROUND_CSS+"transparent");
            blackTile.setStyle(logic.BACKGROUND_CSS+"transparent");
        }
        else if(settings.boardType == logic.BROWN_BOARD){
            greenTile.setStyle(logic.BACKGROUND_CSS+"transparent");
            brownTile.setStyle(logic.BACKGROUND_CSS+logic.BROWN);
            blackTile.setStyle(logic.BACKGROUND_CSS+"transparent");
        }
        else {
            greenTile.setStyle(logic.BACKGROUND_CSS+"transparent");
            brownTile.setStyle(logic.BACKGROUND_CSS+"transparent");
            blackTile.setStyle(logic.BACKGROUND_CSS+logic.GRAY);
        }
    }
    private void setSoundStatus(){
        if(settings.isSound){
            soundToggleButton.setText("On");
            soundToggleButton.setSelected(true);
        }
        else {
            soundToggleButton.setText("Off");
            soundToggleButton.setSelected(false);
        }
    }
    private void setBackground(){

        anchorPane.setBackground(AssetLoader.getBackgroundWithImage(AssetLoader.menuBackgroundPath));
        containPane.setStyle(" -fx-background-color: rgba(0, 0, 0, 0.5);");
    }
    private void setButtons(){

        // toggle button
        soundToggleButton.setOnAction(event -> soundToggleOnAction());
        soundToggleButton.getStyleClass().add("toggle-button");
        // back button
        backButton.getStyleClass().add("button-29");
        backButton.setOnAction(this::backButtonOnAction);

        Image image = AssetLoader.getImage(AssetLoader.tileBasePath+"greenTile.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(greenTile.getPrefWidth());
        imageView.setFitHeight(greenTile.getPrefHeight());

        greenTile.setOnAction(event -> greenTileSelected());
        greenTile.setGraphic(imageView);


        image = AssetLoader.getImage(AssetLoader.tileBasePath+"brownTile.png");
        imageView = new ImageView(image);
        imageView.setFitWidth(brownTile.getPrefWidth());
        imageView.setFitHeight(brownTile.getPrefHeight());


        brownTile.setOnAction(event -> brownTileSelected());
        brownTile.setGraphic(imageView);


        image = AssetLoader.getImage(AssetLoader.tileBasePath+"blackTile.png");
        imageView = new ImageView(image);
        imageView.setFitWidth(blackTile.getPrefWidth());
        imageView.setFitHeight(blackTile.getPrefHeight());


        blackTile.setOnAction(event -> blackTileSelected());
        blackTile.setGraphic(imageView);

    }
    private void backButtonOnAction(ActionEvent event){
        loadFXML.loadMenu(loadFXML.MENU_FXML,event,settings);
    }
    private void soundToggleOnAction(){

        settings.isSound = soundToggleButton.isSelected();
        String text = (soundToggleButton.isSelected()) ? ("On") : ("Off");
        soundToggleButton.setText(text);
    }
    private void greenTileSelected(){
        settings.boardType = logic.GREEN_BOARD;
        greenTile.setStyle(logic.BACKGROUND_CSS+logic.FOREST_GREEN);
        brownTile.setStyle(logic.BACKGROUND_CSS+"transparent");
        blackTile.setStyle(logic.BACKGROUND_CSS+"transparent");
    }
    private void brownTileSelected(){
        settings.boardType = logic.BROWN_BOARD;
        greenTile.setStyle(logic.BACKGROUND_CSS+"transparent");
        brownTile.setStyle(logic.BACKGROUND_CSS+logic.BROWN);
        blackTile.setStyle(logic.BACKGROUND_CSS+"transparent");
    }
    private void blackTileSelected(){
        settings.boardType = logic.BLACK_BOARD;
        greenTile.setStyle(logic.BACKGROUND_CSS+"transparent");
        brownTile.setStyle(logic.BACKGROUND_CSS+"transparent");
        blackTile.setStyle(logic.BACKGROUND_CSS+logic.GRAY);
    }
}

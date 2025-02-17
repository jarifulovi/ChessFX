package com.example.chessfx.Controller;

import com.example.chessfx.Logic.Abstract.loadFXML;
import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Abstract.AssetLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Menu_Controller implements Initializable {

    private Settings settings;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public VBox buttonContainer;
    @FXML
    public Button playButton,computerButton,optionsButton,aboutButton,exitButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.settings = new Settings();

        setBackground();
        setButtons();

    }
    // This method will invok after loading fxml in options
    // So it will first create a new Settings obj then initialize with the options one
    public void setSettings(Settings settings){
        this.settings = settings;
    }
    private void setBackground(){

        anchorPane.setBackground(AssetLoader.getBackgroundWithImage(AssetLoader.menuBackgroundPath));
    }
    private void setButtons(){


        playButton.getStyleClass().add("button-20");

        computerButton.getStyleClass().add("button-20");

        optionsButton.getStyleClass().add("button-20");

        aboutButton.getStyleClass().add("button-20");

        exitButton.getStyleClass().add("button-20");


        playButton.setOnAction(this::playOnAction);
        computerButton.setOnAction(this::computerOnAction);
        optionsButton.setOnAction(this::optionsOnAction);
        aboutButton.setOnAction(this::aboutOnAction);
        exitButton.setOnAction(event -> exitOnAction());
    }
    private void playOnAction(ActionEvent event){
        settings.gameType = logic.TWO_PLAYER;
        loadFXML.loadPlay(loadFXML.PLAY_FXML,event,settings);
    }
    private void computerOnAction(ActionEvent event){
        settings.gameType = logic.ONE_PLAYER;
        loadFXML.loadPlay(loadFXML.PLAY_FXML,event,settings);
    }
    private void optionsOnAction(ActionEvent event){
        loadFXML.loadOptions(loadFXML.OPTIONS_FXML,event,settings);
    }
    private void aboutOnAction(ActionEvent event){
        loadFXML.loadAbout(loadFXML.ABOUT_FXML,event,settings);
    }
    private void exitOnAction(){
        Platform.exit();
    }

}

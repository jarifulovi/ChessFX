package com.example.chessfx.Controller;

import com.example.chessfx.Logic.Abstract.loadFXML;
import com.example.chessfx.Logic.Abstract.AssetLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class About_Controller implements Initializable {

    private Settings settings;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public Label textLabel;
    @FXML
    public Button backButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String text = "ChessFX is a chess game application built with JavaFX. " +
                "It supports both two-player mode and single-player mode against " +
                "a computer opponent. Key features include customizable board colors, " +
                "time control options, and a user-friendly interface for an engaging chess experience.";

        textLabel.setText(text);
        anchorPane.setBackground(AssetLoader.getBackgroundWithImage(AssetLoader.menuBackgroundPath));
        setButton();
    }
    public void setSettings(Settings settings){
        this.settings = settings;
    }
    private void setButton(){

        backButton.getStyleClass().add("button-29");
        backButton.setOnAction(this::backButtonOnAction);
    }
    private void backButtonOnAction(ActionEvent event){
        loadFXML.loadMenu(loadFXML.MENU_FXML,event,settings);
    }
}

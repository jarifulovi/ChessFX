package com.example.chessfx.Controller;

import com.example.chessfx.Logic.Abstract.loadFXML;
import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Abstract.AssetLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class Play_Controller implements Initializable {
    private Settings settings;
    @FXML
    public AnchorPane anchorPane,imagePane;
    @FXML
    public ToggleButton toggleButtonBlack;
    @FXML
    public ToggleButton toggleButtonWhite;
    public ToggleGroup toggleGroup;
    @FXML
    public ComboBox<String> comboBoxTime;
    @FXML
    public Button playButton,backButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        setBackground();
        setImagePane();
        setToggleGroup();
        setComboBoxTime();
        setButton();

    }
    public void setSettings(Settings settings){
        this.settings = settings;
        // some default settings
        settings.player = logic.WHITE;
        settings.duration = 0;
    }
    private void setBackground(){
        anchorPane.setStyle(logic.BACKGROUND_CSS+logic.GRAY);
    }
    private void setImagePane(){
        imagePane.setBackground(AssetLoader.getBackgroundWithImage(AssetLoader.playBackgroundPath));
    }
    private void setToggleGroup(){
        // Create a new ToggleGroup
        toggleGroup = new ToggleGroup();
        toggleButtonWhite.getStyleClass().add("toggle-button");
        toggleButtonBlack.getStyleClass().add("toggle-button");
        toggleButtonWhite.setText("W");
        toggleButtonBlack.setText("B");

        // Set the ToggleGroup for each ToggleButton
        toggleButtonBlack.setToggleGroup(toggleGroup);
        toggleButtonWhite.setToggleGroup(toggleGroup);

        // Optionally, set a default selection
        toggleButtonWhite.setSelected(true);

        // Add listeners if you need to handle selection changes
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ToggleButton selectedButton = (ToggleButton) newValue;
                String selectedText = selectedButton.getText();

                System.out.println("Selected: " + selectedText);

                // Update settings based on selection
                if ("B".equals(selectedText)) {

                    settings.player = logic.BLACK;
                } else if ("W".equals(selectedText)) {

                    settings.player = logic.WHITE;
                }
            }
        });
    }
    private void setComboBoxTime(){
        // Create a list of time options
        String[] timeOptions = {"No Time", "1 Min", "5 Min", "10 Min", "15 Min", "30 Min", "60 Min"};

        comboBoxTime.getItems().addAll(timeOptions);

        comboBoxTime.setValue("No Time");
        comboBoxTime.getStyleClass().add("combo-box");
        comboBoxTime.setOnAction(event -> timeControl());

    }
    private void timeControl(){

        String time = comboBoxTime.getValue();
        if(Objects.equals(time, "No Time")) settings.duration = -1;
        else if(Objects.equals(time, "1 Min")) settings.duration = 1;
        else if(Objects.equals(time, "5 Min")) settings.duration = 5;
        else if(Objects.equals(time, "10 Min")) settings.duration = 10;
        else if(Objects.equals(time, "15 Min")) settings.duration = 15;
        else if(Objects.equals(time, "30 Min")) settings.duration = 30;
        else if(Objects.equals(time, "60 Min")) settings.duration = 60;

    }
    private void setButton(){
        playButton.getStyleClass().add("button-20");
        playButton.setOnAction(this::playOnAction);
        backButton.getStyleClass().add("button-29");
        backButton.setOnAction(this::backButtonOnAction);
    }

    private void playOnAction(ActionEvent event) {
        System.out.println("play");
        loadFXML.loadGame(loadFXML.GAME_FXML,event,settings);
    }
    private void backButtonOnAction(ActionEvent event){
        loadFXML.loadMenu(loadFXML.MENU_FXML,event,settings);
    }

}

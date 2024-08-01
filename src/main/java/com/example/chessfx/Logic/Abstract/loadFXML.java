package com.example.chessfx.Logic.Abstract;

import com.example.chessfx.Controller.*;
import com.example.chessfx.application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public abstract class loadFXML {

    public static String MENU_FXML = "menu.fxml";
    public static String OPTIONS_FXML = "options.fxml";
    public static String PLAY_FXML = "play.fxml";
    public static String GAME_FXML = "game.fxml";
    public static String ABOUT_FXML = "about.fxml";
    public static String CSS_FILE = "buttonUI.css";

    public static void addCSSInScene(Scene scene){
        String cssPath = loadFXML.CSS_FILE;
        scene.getStylesheets().add(Objects.requireNonNull(application.class.getResource(cssPath)).toExternalForm());
    }
    public static void loadAbout(String fxmlFile, ActionEvent event,Settings settings) {
        try {
            FXMLLoader loader = new FXMLLoader(application.class.getResource(fxmlFile));
            Parent root = loader.load();

            // Obtain the controller and set the settings
            About_Controller controller = loader.getController();
            if (controller != null) {
                controller.setSettings(settings);
            }
            // take the obj and use obj.setSettings(settings)
            Scene scene = new Scene(root);
            addCSSInScene(scene);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Chess FX");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadOptions(String fxmlFile, ActionEvent event,Settings settings) {
        try {
            FXMLLoader loader = new FXMLLoader(application.class.getResource(fxmlFile));
            Parent root = loader.load();

            // Obtain the controller and set the settings
            Options_Controller controller = loader.getController();
            if (controller != null) {
                controller.setSettings(settings);
            }
            // take the obj and use obj.setSettings(settings)
            Scene scene = new Scene(root);
            addCSSInScene(scene);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Chess FX");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadMenu(String fxmlFile, ActionEvent event,Settings settings){
        try {
            FXMLLoader loader = new FXMLLoader(application.class.getResource(fxmlFile));
            Parent root = loader.load();


            Menu_Controller controller = loader.getController();
            if (controller != null) {
                controller.setSettings(settings);
            }

            Scene scene = new Scene(root);
            addCSSInScene(scene);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Chess FX");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadPlay(String fxmlFile, ActionEvent event,Settings settings){
        try {
            FXMLLoader loader = new FXMLLoader(application.class.getResource(fxmlFile));
            Parent root = loader.load();


            Play_Controller controller = loader.getController();
            if (controller != null) {
                controller.setSettings(settings);
            }

            Scene scene = new Scene(root);
            addCSSInScene(scene);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Chess FX");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadGame(String fxmlFile, ActionEvent event,Settings settings){
        try {
            FXMLLoader loader = new FXMLLoader(application.class.getResource(fxmlFile));
            Parent root = loader.load();


            Game_Controller controller = loader.getController();
            if (controller != null) {
                controller.setSettings(settings);
            }

            Scene scene = new Scene(root);
            addCSSInScene(scene);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Chess FX");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

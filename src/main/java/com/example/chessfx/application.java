package com.example.chessfx;

import com.example.chessfx.Logic.Abstract.loadFXML;
import com.example.chessfx.Logic.Abstract.AssetLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class application extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(application.class.getResource(loadFXML.MENU_FXML));
        Scene scene = new Scene(fxmlLoader.load());

        loadFXML.addCSSInScene(scene);

        stage.setTitle("ChessFX");
        Image icon = AssetLoader.getImage(AssetLoader.appIconPath);
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}


// (a-b pruning + move ordering) lines : 459404,538372 , avb time : 532ms
// (a-b + mo + transposition)    lines : 447537,521130 , avg time : 272ms
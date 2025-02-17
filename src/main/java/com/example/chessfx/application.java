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


//   benchmark
//   nodes   lines    t lines  time
//   depth == 4
//   26.6k   23.6k     9.2k   102ms
//   depth == 5
//   37.8k   24.5k     9.5k   170ms



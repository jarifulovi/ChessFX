package com.example.chessfx.Logic.Abstract;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.Objects;

public abstract class AssetLoader {

    // For loading assets ( image, music )
    public static String menuBackgroundPath = "/com/example/chessfx/Images/Background/menuBackground.png";
    public static String playBackgroundPath = "/com/example/chessfx/Images/Background/playBackground.png";
    public static String tileBasePath = "/com/example/chessfx/Images/Tiles/";
    public static String appIconPath = "/com/example/chessfx/Images/Icons/app_icon.png";
    public static Image[] loadPieceImage(){

        String pieceBasePath = "/com/example/chessfx/Images/Pieces/";
        Image[] images = new Image[13];
        String[] imagePaths = {
                "", // Empty square
                pieceBasePath + "white-pawn.png",
                pieceBasePath + "white-knight.png",
                pieceBasePath + "white-bishop.png",
                pieceBasePath + "white-rook.png",
                pieceBasePath + "white-queen.png",
                pieceBasePath + "white-king.png",
                pieceBasePath + "black-pawn.png",
                pieceBasePath + "black-knight.png",
                pieceBasePath + "black-bishop.png",
                pieceBasePath + "black-rook.png",
                pieceBasePath + "black-queen.png",
                pieceBasePath + "black-king.png"
        };

        int index = 0;
        for(String path : imagePaths){
            if(!path.isEmpty()){
                images[index] = getImage(path);
            }
            index++;
        }
        return images;
    }
    public static Image getImage(String path){
        return new Image(Objects.requireNonNull(AssetLoader.class.getResourceAsStream(path)));
    }
    public static Background getBackgroundWithImage(String path){
        Image image = AssetLoader.getImage(path);

        // Create a BackgroundImage object
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true) // Size to cover entire pane
        );
        return new Background(backgroundImage);
    }
    public static ImageView[] getPawnPromotionImage(String color){

        String pieceBasePath = "/com/example/chessfx/Images/Pieces/";
        Image[] images = new Image[4];
        ImageView[] imageViews = new ImageView[4];

        String[] stringPaths = {
                pieceBasePath + color + "-knight.png",
                pieceBasePath + color + "-bishop.png",
                pieceBasePath + color + "-queen.png",
                pieceBasePath + color + "-rook.png"
        };

        int index = 0;
        for(String path : stringPaths){
            images[index] = getImage(path);
            imageViews[index] = new ImageView(images[index]);
            imageViews[index].setFitWidth(60.0);
            imageViews[index].setFitHeight(60.0);
            index++;
        }

        return imageViews;
    }
}

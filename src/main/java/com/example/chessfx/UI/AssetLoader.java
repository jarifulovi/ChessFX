package com.example.chessfx.UI;

import javafx.scene.image.Image;

import java.util.Objects;

public abstract class AssetLoader {

    // For loading assets ( image, music )

    public static Image[] loadPieceImage(){

        String pieceBasePath = "/com/example/chessfx/Pieces/";
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
                images[index] = new Image(Objects.requireNonNull(AssetLoader.class.getResourceAsStream(path)));
            }
            index++;
        }
        return images;
    }
    public static Image[] getPawnPromotionImage(String color){

        String pieceBasePath = "/com/example/chessfx/Pieces/";
        Image[] images = new Image[4];

        String[] stringPaths = {
                pieceBasePath + color + "-knight.png",
                pieceBasePath + color + "-bishop.png",
                pieceBasePath + color + "-queen.png",
                pieceBasePath + color + "-rook.png"
        };

        int index = 0;
        for(String path : stringPaths){
            images[index] = new Image(Objects.requireNonNull(AssetLoader.class.getResourceAsStream(path)));
            index++;
        }
        return images;
    }
}

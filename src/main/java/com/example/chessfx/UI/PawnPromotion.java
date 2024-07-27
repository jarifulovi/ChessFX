package com.example.chessfx.UI;

import com.example.chessfx.Logic.logic;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;


public class PawnPromotion {

    private GridPane boardPane;
    private StackPane[] promotedSquares;    // knight bishop queen rook

    private Image[] whitePieceImages;
    private Image[] blackPieceImages;
    public PawnPromotion(GridPane boardPane){
        this.boardPane = boardPane;
        this.promotedSquares = new StackPane[4];

        whitePieceImages = AssetLoader.getPawnPromotionImage("white");
        blackPieceImages = AssetLoader.getPawnPromotionImage("black");
        setSquares();
    }
    public StackPane[] getPromotedSquares(){
        return promotedSquares;
    }
    private void setSquares(){

        for(int i=0;i<4;i++) promotedSquares[i] = new StackPane();

        int index = 0;
        for(StackPane square : promotedSquares){
            square.setPrefWidth(60.0);
            square.setPrefHeight(60.0);
            square.setVisible(true);
            // set on action
            square.setId(String.valueOf(index));
            index++;
        }
    }
    public void setPawnPromotion(int turn,int player){

        // Disable pre board stackPanes
        changePreSquares(true);
        int rank = (turn==player) ? 0 : 7;
        // Set promote stackPanes
        setImage(turn);
        setStackPane(rank);

    }
    public void unSetPawnPromotion(){

        removeStackPane();
        changePreSquares(false);

    }

    // This method will be invoked after setPawnPromotion

    private void changePreSquares(boolean isDisable){

        StackPane[] preSquares = boardPane.getChildren().toArray(StackPane[]::new);
        for(StackPane square : preSquares){
            if(isDisable) {
                square.setOpacity(0.5);
                square.setDisable(true);
            }
            else {
                square.setDisable(false);
                square.setOpacity(1.0);
            }
        }
    }
    private void setStackPane(int rank){

        int col = 0;
        for(StackPane square : promotedSquares){
            boardPane.add(square,col,rank);
            col++;
        }
    }
    private void removeStackPane(){

        for(StackPane square : promotedSquares){
            boardPane.getChildren().remove(square);
        }
    }

    // Whose promoting pawn
    private void setImage(int turn){

        Image[] images;

        if(turn == logic.WHITE){
            images = whitePieceImages;
        }
        else {
            images = blackPieceImages;
        }

        int index = 0;
        for(StackPane square : promotedSquares){

            ImageView imageView = new ImageView(images[index]);
            imageView.setFitWidth(square.prefWidthProperty().doubleValue());
            imageView.setFitHeight(square.prefHeightProperty().doubleValue());

            if(!square.getChildren().isEmpty())
                square.getChildren().clear();

            square.getChildren().add(imageView);
            index++;
        }
    }
}

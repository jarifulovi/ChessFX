package com.example.chessfx.UI;

import com.example.chessfx.Logic.logic;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class Board_UI {

     // Improvement : after same piece click the highlight remains
    private int[][] grid;
    private boolean[][] highLights;
    private final String highLightColor = "#7FFFD4";
    private final String highLightCaptures = "#8B0000";

    private String defaultColor;

    private Image[] images;
    public Board_UI(int[][] grid, StackPane[] squares,String defaultColor){

        this.grid = logic.copyGrid(grid);
        this.highLights = new boolean[8][8];
        this.defaultColor = defaultColor;

        setColor(squares,defaultColor);
        images = AssetLoader.loadPieceImage();
        setPiecesOnBoard(squares);
    }

    public void updateUI(StackPane[] squares,int[][] newGrid,int preRow,int preCol,int row,int col){


        int[][] preGrid = logic.copyGrid(this.grid);
        this.grid = logic.copyGrid(newGrid);

        changePiecesOnBoard(squares,preGrid);
        // Change pre-position and new position color
    }


    private void changePiecesOnBoard(StackPane[] squares,int[][] preGrid){
        // Update ui
        int index = 0;
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(preGrid[i][j] != grid[i][j]){
                    setImage(squares[index],grid[i][j]);
                }
                index++;
            }
        }
    }

    private void setPiecesOnBoard(StackPane[] squares){
        // set piece image on board
        int index = 0;
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                setImage(squares[index],grid[i][j]);
                index++;
            }
        }
    }
    public void highLightSquare(int[][] positions,int turn,StackPane[] squares){

        if(positions == null) {
            System.out.println("Position is null");
            return;
        }

        for(int[] pos : positions){
            int row = pos[0];
            int col = pos[1];
            highLights[row][col] = true;
            int index = row*8+col;
            changeSquareColor(squares[index],index,turn,true);
        }

    }
    public void resetHighLight(StackPane[] squares,int turn){
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(highLights[i][j]) {
                    highLights[i][j] = false;
                    int index = i*8 + j;
                    changeSquareColor(squares[index],index,turn,false);
                }
            }
        }

    }

    private void changeSquareColor(StackPane square,int index,int turn,boolean isChange){


        // For resetting
        if(!isChange){
            String[] colors = defaultColor.split(" ");
            int row = index/8;
            int col = index%8;
            String color = (row + col) % 2 == 0 ? colors[0] : colors[1];
            square.setStyle(logic.BACKGROUND_CSS + color);

        }
        else{

            if(!logic.isOwnPiece(grid[index/8][index%8],turn)) {
                if(logic.isOpponentPiece(grid[index/8][index%8],turn)){
                    square.setStyle(logic.BACKGROUND_CSS + highLightCaptures);
                }
                else {
                    square.setStyle(logic.BACKGROUND_CSS + highLightColor);
                }
            }

        }

    }
    public void setColor(StackPane[] squares,String color) {
        String[] colors = color.split(" ");

        for (int i = 0; i < squares.length; i++) {

            // Calculate row and column based on the square index
            int row = i / 8;
            int col = i % 8;

            // Set color based on a checkerboard pattern
            String squareColor = (row + col) % 2 == 0 ? colors[0] : colors[1];

            // Set the background color using CSS style
            squares[i].setStyle(logic.BACKGROUND_CSS + squareColor);
        }
    }
    public void setImage(StackPane square,int piece){

        if(piece > 12) return;
        if(piece == logic.NO_PIECE){
            square.getChildren().clear();
            return;
        }

        ImageView imageView = new ImageView(images[piece]);

        imageView.setFitWidth(square.prefWidthProperty().doubleValue());
        imageView.setFitHeight(square.prefHeightProperty().doubleValue());

        if(!square.getChildren().isEmpty())
            square.getChildren().clear();

        square.getChildren().add(imageView);
    }
}
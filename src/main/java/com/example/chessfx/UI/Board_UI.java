package com.example.chessfx.UI;

import com.example.chessfx.Logic.Abstract.AssetLoader;
import com.example.chessfx.Logic.Abstract.logic;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class Board_UI {

     // Improvement : after same piece click the highlight remains
    private int[][] grid;
    private boolean[][] highLights;
    private double squareWidth,squareHeight;
    private final Color HIGHLIGHT_NORMAL = Color.rgb(25, 155, 0, 0.6);
    private final Color HIGHTLIGHT_CAPTURE = Color.rgb(155,50,0);
    private final Color HIGHTLIGHT_MOVE = Color.rgb(150,255,100,0.6);

    private final RadialGradient radialGradient = new RadialGradient(
            0, // Focus distance (0 means center of circle)
            0, // Focus color (0 means fully opaque)
            0.5, // Center X
            0.5, // Center Y
            0.5, // Radius of gradient
            true, // Proportional
            javafx.scene.paint.CycleMethod.NO_CYCLE, // No cycling of colors
            new Stop(0, Color.RED), // Color at the center (dense)
            new Stop(1, Color.TRANSPARENT) // Color at the edge (transparent)
    );
    private String defaultColor;

    private Image[] images;
    public Board_UI(int[][] grid, StackPane[] squares,String defaultColor){

        this.squareWidth = squares[0].getPrefWidth();
        this.squareHeight = squares[0].getPrefHeight();
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
        changeMovedPieceColor(squares,preRow,preCol,row,col);
    }
    public void checkedKingColor(StackPane[] squares,int turn){

        // Turn is opponent
        // after update grid
        int[] kingPos = logic.getKingPosition(grid,turn);
        int index = kingPos[0] * 8 + kingPos[1];


        // Create the Circle with the radial gradient
        Circle checkCircle = new Circle(squareWidth / 2, squareHeight / 2, squareWidth/2);
        checkCircle.setFill(radialGradient);


        checkCircle.radiusProperty().bind(squares[index].widthProperty().multiply(1).divide(2));

        // Make sure the circle does not interfere with layout or mouse events
        checkCircle.setManaged(false);
        checkCircle.setMouseTransparent(true);

        // Add the circle to the pane, ensuring it's on top of other content
        squares[index].getChildren().addFirst(checkCircle);
    }
    private void changeMovedPieceColor(StackPane[] squares,int preRow,int preCol,int row,int col){
        // Remove the previous move color and checked color
        for(StackPane square : squares){
            square.getChildren().removeIf(node -> node instanceof Rectangle &&
                     ((Rectangle) node).getFill().equals(HIGHTLIGHT_MOVE));

            square.getChildren().removeIf(node -> node instanceof Circle &&
                    ((Circle) node).getFill() instanceof RadialGradient);
        }

        int indexPre = preRow * 8 + preCol;
        int index = row * 8 + col;

        hightLightSquare(squares[indexPre],HIGHTLIGHT_MOVE);
        hightLightSquare(squares[index],HIGHTLIGHT_MOVE);
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
    public void highLightSquares(int[][] positions, int turn, StackPane[] squares){

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
            if(logic.isOpponentPiece(grid[index/8][index%8],turn)){
                removeHighlightSquare(square);
            }
            else {
                removeHighlightCircle(square);
            }
        }
        else{

            if(logic.isOpponentPiece(grid[index/8][index%8],turn)){
                hightLightSquare(square,HIGHTLIGHT_CAPTURE);
            }
            else {
                hightLightCircle(square,HIGHLIGHT_NORMAL);
            }

        }

    }
    public void hightLightCircle(StackPane pane, Color HIGHLIGHT_COLOR){

        Circle highlightCircle = new Circle(squareWidth / 2, squareHeight / 2, squareWidth/ 3);
        highlightCircle.setFill(HIGHLIGHT_COLOR);
        highlightCircle.setStroke(Color.BLACK);

        // Make sure the circle does not interfere with layout or mouse events
        highlightCircle.setManaged(false);
        highlightCircle.setMouseTransparent(true);

        // Bind the circle's size to the pane's size to ensure it scales correctly
        highlightCircle.radiusProperty().bind(pane.widthProperty().multiply(0.3).divide(2)); // 30% of width or height

        // For circle(normal) and move highlight
        // Circle highlight gets the priority
        boolean hasRectangle = pane.getChildren().stream()
                .anyMatch(node -> node instanceof Rectangle);

        if(hasRectangle){
            pane.getChildren().add(1,highlightCircle);
        }
        else
            pane.getChildren().addFirst(highlightCircle);
    }
    public void removeHighlightCircle(StackPane pane){
        pane.getChildren().removeIf(node -> node instanceof Circle);
    }
    public void hightLightSquare(StackPane pane, Color HIGHLIGHT_COLOR) {

        Rectangle highLightRect = new Rectangle(squareWidth, squareHeight, HIGHLIGHT_COLOR);
        highLightRect.setManaged(false); // Ensure it does not affect layout
        highLightRect.setMouseTransparent(true); // Ensure it does not capture mouse events


        highLightRect.widthProperty().bind(pane.widthProperty());
        highLightRect.heightProperty().bind(pane.heightProperty());

        // For capture and move hightlight
        // Capture highlight gets the priority
        if(HIGHLIGHT_COLOR.equals(HIGHTLIGHT_CAPTURE)){

            boolean hasMoveHighlight = pane.getChildren().stream()
                    .anyMatch(node -> node instanceof Rectangle &&
                            ((Rectangle) node).getFill().equals(HIGHTLIGHT_MOVE));

            if(hasMoveHighlight){
                pane.getChildren().add(1,highLightRect);
            }
            else {
                pane.getChildren().addFirst(highLightRect);
            }
        }
        else
            pane.getChildren().addFirst(highLightRect);
    }
    public void removeHighlightSquare(StackPane pane) {
        pane.getChildren().removeIf(node -> node instanceof Rectangle &&
                 ((Rectangle) node).getFill().equals(HIGHTLIGHT_CAPTURE));
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

        imageView.setFitWidth(60.0);
        imageView.setFitHeight(60.0);

        if(!square.getChildren().isEmpty())
            square.getChildren().clear();

        square.getChildren().add(imageView);
    }
}

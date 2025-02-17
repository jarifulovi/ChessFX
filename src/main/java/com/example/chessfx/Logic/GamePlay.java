package com.example.chessfx.Logic;

import com.example.chessfx.Controller.Settings;
import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Engine.BitBoard.*;
import com.example.chessfx.Logic.Engine.Engine;
import com.example.chessfx.Logic.Object.Move;
import com.example.chessfx.Other.Time;
import com.example.chessfx.UI.Board_UI;
import com.example.chessfx.UI.PawnPromotion;
import com.example.chessfx.Other.SoundSetup;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.List;


public class GamePlay {

    private GridLogic gridLogic;
    private Board_UI board_ui;
    private PawnPromotion pawnPromotion;
    private Engine engine;
    private SoundSetup soundSetup;
    private StackPane[] squares;
    private Time time;      // Time can be null
    private boolean hasTime;
    private boolean hasSound;
    private int player;
    private int gameType;
    private int turn = logic.WHITE;
    private boolean firstClick;
    private int engineMoveDelay = 100;

    int[][] positions = new int[0][0];
    Move currentMove;
    boolean check = false;
    boolean gameOver = false;
    String gameOverText;

    public GamePlay(GridPane boardPane, StackPane[] squares, Time time,Settings settings){
        // set board grid logic and ui
        this.squares = squares;
        this.time = time;
        this.gameOverText = "GameOver";
        init_timer();

        this.player = settings.player;
        this.gameType = settings.gameType;
        this.hasSound = settings.isSound;
        String defaultColor = "";
        if(settings.boardType == logic.GREEN_BOARD) defaultColor = "white "+logic.FOREST_GREEN;
        else if(settings.boardType == logic.BROWN_BOARD) defaultColor = "white "+logic.BROWN;
        else if(settings.boardType == logic.BLACK_BOARD) defaultColor = "white "+logic.GRAY;

        currentMove = new Move();
        this.firstClick = false;
        gridLogic = new GridLogic(player);
        turn = gridLogic.getBoard().currentTurn;
        board_ui = new Board_UI(gridLogic.getGrid(),squares,defaultColor);
        pawnPromotion = new PawnPromotion(boardPane);
        soundSetup = new SoundSetup();
        if(hasSound)
            soundSetup.gameMusic(true);

        if(gameType == logic.ONE_PLAYER) {
            engine = new Engine();
            if(player == logic.BLACK) logic.delay(engineMoveDelay,this::computerTurn);
        }

    }
    private void init_timer(){

        if(time != null){
            // game has time control
            hasTime = true;
            time.startWhite();
        }
    }
    public String getGameOverText(){
        return gameOverText;
    }
    public boolean getGameOver(){
        return gameOver;
    }
    public boolean checkGameOver(){

        boolean timeEnd = false;
        if(hasTime){
            timeEnd = (turn==logic.WHITE) ? (time.isTimeOver(time.getWhiteTime())) :
                    (time.isTimeOver(time.getBlackTime()));

            if(timeEnd) {
                gameOverText = (turn==logic.WHITE) ? ("Black Wins") : ("White Wins");
            }
        }

        gameOver = gameOver || timeEnd;
        if(gameOver){

            if(hasTime){
                time.stopWhite();
                time.stopBlack();

            }
        }
        return gameOver;
    }
    public void getResign(){
        if(turn == logic.WHITE)
            gameOverText = "Black wins";
        else
            gameOverText = "White wins";

        gameOver = true;
    }
    public ImageView getCurrentPieceImage(int row,int col){

        if(gameOver) return null;


        if(gridLogic.isOwnPieceClick(row,col,turn)){
            int index = row * 8 + col;

            if(squares[index].isDisable()) return null;
            List<Node> childrens = squares[index].getChildren();
            for(Node node : childrens){
                if(node instanceof ImageView originalImageView){
                    Image copiedImage = originalImageView.getImage();
                    return new ImageView(copiedImage);
                }
            }
        }
        return null;
    }

    private void myTurn(int row,int col,boolean isDragged){

        if(gameOver) return;
        if(gameType == logic.ONE_PLAYER && turn != player) return;
        if(isDragged && logic.isOwnPiece(gridLogic.getGrid()[row][col],turn)) return;

        long startTime = System.currentTimeMillis();
        currentMove.newRow = row;
        currentMove.newCol = col;
        board_ui.resetHighLight(squares,turn);

        // Move is choose to play
        if(gridLogic.isOwnPieceClick(row,col,turn)){

            currentMove.piece = gridLogic.clickPiece(row,col);
            currentMove.preRow = row;
            currentMove.preCol = col;
            positions = gridLogic.getValidPositions(row,col);

            //List<Move> moves = bitBoardLogic.getSinglePieceMoves(bitboard, currentMove.piece, turn);    /////////
            //positions = logic.convertMoveToPosition(moves);                               // Debug purpose
            //System.out.println("Size : "+moves.size());                               /////////

            if(positions.length > 0){
                firstClick = true;
                board_ui.highLightSquares(positions,turn,squares);
            }

            logic.displayTime(startTime);
            return;
        }


        if(firstClick){
            // A valid move is played
            if(logic.validSquareClick(positions,row,col)) {
                if (logic.isPawnPromoting(currentMove.piece, row)) {
                    handlePawnPromotion(row, col);
                    return;
                }

                update(currentMove);

                // Turn becomes opponent
            }
            else {
                if(hasSound && isDragged)
                    soundSetup.illegalMusic(turn==player);
            }
        }

        checkMoveAndReset();
        logic.displayTime(startTime);

    }
    private void computerTurn(){

        if(gameOver) return;
        if(turn == player) return;

        long startTime = System.currentTimeMillis();

        // Get row,col,piece
        Move engineMove = engine.bestMove(gridLogic.getBoard(),turn,player);

        if(logic.isPawnPromoting(engineMove.piece,engineMove.newRow)){

            engineMove.promotedPiece = engine.getPawnPromotedPiece(turn);
            engineMove.isPromotingPiece = true;
        }

        update(engineMove);
        // Turn becomes opponent

        checkMoveAndReset();
        logic.displayTime(startTime);

    }

    private void handlePawnPromotion(int newRow,int newCol){
        pawnPromotion.setPawnPromotion(turn,player);
        StackPane[] promotedSquares = pawnPromotion.getPromotedSquares();

        for(StackPane square : promotedSquares){
            square.setOnMouseClicked((mouseEvent -> mouseClickPP(square,newRow,newCol)));
        }

    }
    private void mouseClickPP(StackPane square,int newRow,int newCol){


        int newPiece = logic.getNewPiecePP(square,turn);
        pawnPromotion.unSetPawnPromotion();
        // update
        currentMove.newRow = newRow;
        currentMove.newCol = newCol;
        currentMove.isPromotingPiece = true;
        currentMove.promotedPiece = newPiece;
        update(currentMove);
        // Turn becomes opponent
        if(gridLogic.clickPiece(newRow,newCol) == logic.NO_PIECE && hasSound) {
            soundSetup.pawnPromotionMusic(turn == player);
        }

        checkMoveAndReset();
        if(gameType == logic.ONE_PLAYER){
            logic.delay(engineMoveDelay,this::computerTurn);

        }
    }

    public void play(StackPane square,int gameType,boolean isDragged){

        if(gameOver) return;

        int index = Integer.parseInt(square.getId());
        int row = index/8;
        int col = index%8;

        if(gameType == logic.TWO_PLAYER)
            myTurn(row,col,isDragged);
        else {

            myTurn(row,col,isDragged);
            logic.delay(engineMoveDelay,this::computerTurn);
        }
    }
    private void update(Move move){

        boolean isCapture = (gridLogic.clickPiece(move.newRow,move.newCol) != logic.NO_PIECE);
        boolean isCastle = logic.isCastle(move.piece,move.preCol,move.newCol);
        gridLogic.updateGrid(player,move);

        board_ui.updateUI(squares,gridLogic.getGrid(),move.preRow,move.preCol,move.newRow,move.newCol);

        // Turn changes
        turn = (turn == logic.WHITE) ? logic.BLACK : logic.WHITE;

        check = logic.isKingInCheck(gridLogic.getGrid(),turn,player);

        if(hasSound) {
            soundSetup.startMusic(turn == player, isCapture, isCastle, check);
        }

        if(hasTime) {
            updateTimer();
        }

    }

    private void checkMoveAndReset(){

        // Turn is opponent
        // Draw positions checking
        if(logic.drawByInsufficientMaterial(gridLogic.getGrid())){
            System.out.println("Draw by insufficient material");
            gameOverText = "Draw";
            gameOver = true;
        }
        if(gridLogic.isThreeFoldRepitionHappens()){
            System.out.println("Threefold repition");
            gameOverText = "Threefold Repition";
            gameOver = true;
        }

        // If provide a check then update check ui
        if(check){
            board_ui.checkedKingColor(squares,turn);
        }
        // Check-mate or stale-mate checking
        if(gridLogic.noValidSquareForOpponent(gridLogic.getGrid(),turn)){


            if(check){
                System.out.println("check-mate");
                if(hasSound)
                    soundSetup.gameMusic(false);
                gameOverText = (turn==logic.WHITE) ? ("Black wins") : ("White wins");
            }
            else {
                System.out.println("stale-mate");
                gameOverText = "Stale-mate";

            }
            gameOver = true;
        }

        firstClick = false;
        positions = new int[0][0];
        currentMove.piece = logic.NO_PIECE;
        currentMove.promotedPiece = logic.NO_PIECE;
        currentMove.isPromotingPiece = false;
        currentMove.preRow = currentMove.preCol = -1;
    }
    private void updateTimer(){

        // At first black condition will be true
        if(turn == logic.WHITE){
            time.startWhite();
            time.stopBlack();
            if(time.isTimeOver(time.getWhiteTime())){
                gameOver = true;
            }
        }
        else {
            time.startBlack();
            time.stopWhite();
            if(time.isTimeOver(time.getBlackTime())){
                gameOver = true;
            }
        }

    }
}

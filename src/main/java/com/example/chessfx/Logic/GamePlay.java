package com.example.chessfx.Logic;

import com.example.chessfx.Logic.Engine.Engine;
import com.example.chessfx.Logic.Engine.Move;
import com.example.chessfx.UI.Board_UI;
import com.example.chessfx.UI.PawnPromotion;
import com.example.chessfx.UI.SoundSetup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;



public class GamePlay {

    private GridLogic gridLogic;
    private Board_UI board_ui;
    private PawnPromotion pawnPromotion;
    private Engine engine;
    private SoundSetup soundSetup;
    private StackPane[] squares;
    private Time time;      // Time can be null
    private boolean hasTime;
    private int player;
    private int gameType;
    private int turn = logic.WHITE;
    private boolean firstClick;

    int[][] positions = new int[0][0];
    int piece = logic.NO_PIECE;
    int preRow = -1,preCol = -1;
    boolean check = false;
    boolean gameOver = false;
    String gameOverText;


    public GamePlay(GridPane boardPane, StackPane[] squares, Time time, String defaultColor, int player, int gameType){
        // set board grid logic and ui
        this.squares = squares;
        this.time = time;
        this.gameOverText = "GameOver";
        init_timer();

        this.player = player;
        this.gameType = gameType;
        this.firstClick = false;
        gridLogic = new GridLogic(player);
        board_ui = new Board_UI(gridLogic.getGrid(),squares,defaultColor);
        pawnPromotion = new PawnPromotion(boardPane);
        soundSetup = new SoundSetup();

        if(gameType == logic.ONE_PLAYER) {
            engine = new Engine(gridLogic,logic.getOpponentTurn(player));
            if(player == logic.BLACK) computerTurn();
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
    public boolean checkGameOver(){

        boolean timeEnd = false;
        if(hasTime) timeEnd = (turn==logic.WHITE) ? (time.isTimeOver(time.getWhiteTime())) :
                (time.isTimeOver(time.getBlackTime()));

        gameOver = gameOver || timeEnd;
        if(gameOver){

            if(hasTime){
                time.stopWhite();
                time.stopBlack();
                gameOverText = (turn==logic.WHITE) ? ("Black Wins") : ("White Wins");
            }
            System.out.println("Game over!");
        }
        return gameOver;
    }
    private void myTurn(int row,int col){

        if(gameOver) return;
        if(gameType == logic.ONE_PLAYER && turn != player) return;

        long startTime = System.currentTimeMillis();

        board_ui.resetHighLight(squares,turn);
        // Move is choose to play
        if(gridLogic.isOwnPieceClick(row,col,turn)){

            piece = gridLogic.clickPiece(row,col);
            preRow = row;
            preCol = col;
            positions = gridLogic.getValidPositions(row,col,turn);

            if(positions.length > 0){
                firstClick = true;
                board_ui.highLightSquare(positions,turn,squares);
            }

            logic.displayTime(startTime);
            return;
        }

        // A valid move is played
        if(firstClick && logic.validSquareClick(positions,row,col)){

            if(logic.isPawnPromoting(piece,turn,player,row)) {
                handlePawnPromotion(row,col);
                return;
            }

            // piece = new piece
            update(piece,preRow,preCol,row,col);
            // Turn becomes opponent
            check = logic.isKingInCheck(gridLogic.getGrid(),turn,player);
        }

        checkMoveAndReset();
        logic.displayTime(startTime);

    }
    private void computerTurn(){

        if(gameOver) return;
        if(turn == player) return;

        long startTime = System.currentTimeMillis();

        // Get row,col,piece
        Move engineMove = engine.randomMove(turn);

        if(logic.isPawnPromoting(engineMove.piece,turn,player,engineMove.newRow)){

            engineMove.piece = engine.getPawnPromotedPiece(turn);
        }

        update(engineMove.piece,engineMove.preRow,engineMove.preCol,engineMove.newRow,engineMove.newCol);
        // Turn becomes opponent
        check = logic.isKingInCheck(gridLogic.getGrid(),turn,player);

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


        int index = Integer.parseInt(square.getId());
        int newPiece = logic.NO_PIECE;

        if(turn == logic.WHITE){
            if     (index == 0) newPiece = logic.W_KNIGHT;
            else if(index == 1) newPiece = logic.W_BISHOP;
            else if(index == 2) newPiece = logic.W_QUEEN;
            else if(index == 3) newPiece = logic.W_ROOK;
        }
        else{
            if     (index == 0) newPiece = logic.B_KNIGHT;
            else if(index == 1) newPiece = logic.B_BISHOP;
            else if(index == 2) newPiece = logic.B_QUEEN;
            else if(index == 3) newPiece = logic.B_ROOK;
        }
        pawnPromotion.unSetPawnPromotion();
        // update
        piece = newPiece;
        update(piece,preRow,preCol,newRow,newCol);
        // Turn becomes opponent
        check = logic.isKingInCheck(gridLogic.getGrid(),turn,player);


        checkMoveAndReset();
        computerTurn();
    }
    public void play(StackPane square,int gameType){

        if(gameOver) return;

        int index = Integer.parseInt(square.getId());
        int row = index/8;
        int col = index%8;

        if(gameType == logic.TWO_PLAYER)
            myTurn(row,col);
        else {

            myTurn(row,col);
            computerTurn();
        }
    }
    private void update(int piece,int preRow,int preCol,int row,int col){

        gridLogic.updateGrid(player,piece,preRow,preCol,row,col);
        board_ui.updateUI(squares,gridLogic.getGrid(),preRow,preCol,row,col);

        turn = (turn == logic.WHITE) ? logic.BLACK : logic.WHITE;
        System.out.println(logic.isKingInCheck(gridLogic.getGrid(),turn,player));

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

        // Check-mate or stale-mate checking
        if(gridLogic.noValidSquareForOpponent(gridLogic.getGrid(),turn)){


            if(check){
                System.out.println("check-mate");
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
        piece = logic.NO_PIECE;
        preRow = preCol = -1;
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

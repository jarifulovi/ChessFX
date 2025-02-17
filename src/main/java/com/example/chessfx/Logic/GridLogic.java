package com.example.chessfx.Logic;


import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Board;
import com.example.chessfx.Logic.Object.Move;
import com.example.chessfx.Logic.Object.ZobristHash;

import java.util.HashMap;
import java.util.Map;

public class GridLogic {

    private Board board;
    private PieceLogic pieceLogic;
    private ZobristHash zobristHash;
    private Map<Long,Integer> boardHash;
    private int player;
    private int enPassantTurn;

    public GridLogic(int player){
        this.board = new Board();

        String FEN;
        if(player == logic.WHITE)
            FEN = logic.debugFEN;
        else
            FEN = logic.defaultBlackPlayerFEN;

        this.board = logic.convertFENIntoBoard(FEN);

        this.pieceLogic = new PieceLogic(player);
        this.zobristHash = new ZobristHash();
        this.boardHash = new HashMap<>();
        this.player = player;
        this.enPassantTurn = logic.NO_EN_PASSANT;
    }
    public int[][] getGrid(){
        return this.board.grid;
    }
    public Board getBoard(){
        return board;
    }
    public boolean isOwnPieceClick(int row,int col,int turn){

        return logic.isOwnPiece(board.grid[row][col],turn);
    }
    public int clickPiece(int row,int col){
        return board.grid[row][col];
    }
    public boolean isThreeFoldRepitionHappens(){
        long currentHash = zobristHash.calculateHashBoard(board);
        return boardHash.getOrDefault(currentHash,0) >= 3;
    }
    public int[][] getValidPositions(int row,int col){

        return pieceLogic.allValidMoves(board,board.grid[row][col],row,col);
    }

    // For computer moves
    public int[][][][] getAllValidPositionsOnBoard(int turn) {

        int[][][][] allPositionGrid = new int[8][8][0][0];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int piece = board.grid[i][j];
                if (logic.isOwnPiece(piece, turn)) {
                    // Get valid moves for current piece
                    allPositionGrid[i][j] = pieceLogic.allValidMoves(board,piece, i, j);
                }
            }
        }
        return allPositionGrid;
    }

    public boolean noValidSquareForOpponent(int[][] grid, int opponent){

        int[][][][] allPositionGrid;

        allPositionGrid = getAllValidPositionsOnBoard(opponent);

        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                int piece = grid[i][j];
                if(logic.isOwnPiece(piece,opponent)){
                    if(allPositionGrid[i][j].length > 0) return false;
                }
            }
        }
        return true;
    }

    public void updateGrid(int player, Move move){
        // when a valid move is made by player or engine
        int turn = logic.getPieceColor(move.piece);
        board.update(move);

        // Set and reset en passant ( must call before update )
        updateEnPassant(board,move.piece,move.preRow,move.newRow,move.preCol,move.newCol,turn);


        // Updating grid
        if(move.isPromotingPiece){
            board.grid[move.preRow][move.preCol] = logic.NO_PIECE;
            board.grid[move.newRow][move.newCol] = move.promotedPiece;
        }
        else {
            board.grid[move.preRow][move.preCol] = logic.NO_PIECE;
            board.grid[move.newRow][move.newCol] = move.piece;
        }


        // Update castling
        updateCastling(board,player,move.piece,move.preRow,move.preCol,move.newRow,move.newCol);
        // Add the hash code
        long currentHash = zobristHash.calculateHashBoard(board);
        boardHash.put(currentHash,boardHash.getOrDefault(currentHash,0)+1);
    }

    public void setEnPassant(Board board,int row,int col,int turn){
        int enPassantRow = (turn==player) ? row + 1 : row - 1;
        board.enPassantIndex = enPassantRow * 8 + col;
        enPassantTurn = turn;
    }
    // Position is where the capture happend
    public void removeEnPassantPiece(int[][] grid,int turn,int row,int col){

        int enPassantCaptureRow = (turn==player) ? row + 1 : row - 1;
        grid[enPassantCaptureRow][col] = logic.NO_PIECE;
    }

    // This method has to call before updating grid
    public void updateEnPassant(Board board,int piece,int preRow,int newRow,int preCol,int newCol,int turn){


        if(piece == logic.W_PAWN || piece == logic.B_PAWN){
            if(Math.abs(preRow - newRow) == 2){

                setEnPassant(board,newRow,newCol,turn);
            }
            // If en passant capture happens
            // When moves diagonally and new position doesn't have any opponent piece
            if(Math.abs(preRow-newRow)==Math.abs(preCol-newCol)){
                if(!logic.isOpponentPiece(board.grid[newRow][newCol],turn)){
                    removeEnPassantPiece(board.grid,turn,newRow,newCol);
                }
            }
        }
        if(enPassantTurn != turn)
            board.enPassantIndex = logic.NO_EN_PASSANT;
    }


    public void updateCastling(Board board,int player,int piece,int preRow,int preCol,int newRow,int newCol){

        if(piece == logic.W_KING){
            board.canWhiteLeftCastled = false;
            board.canWhiteRightCastled = false;
        }
        if(piece == logic.B_KING){
            board.canBlackLeftCastled = false;
            board.canBlackRightCastled = false;
        }
        if(piece == logic.W_ROOK){
            if(preCol == 0) board.canWhiteLeftCastled = false;
            else if(preCol == 7) board.canWhiteRightCastled = false;
        }
        if(piece == logic.B_ROOK){
            if(preCol == 0) board.canBlackLeftCastled = false;
            else if(preCol == 7) board.canBlackRightCastled = false;
        }
        // If captured
        if(player == logic.WHITE) {
            if (board.grid[7][0] != logic.W_ROOK) board.canWhiteLeftCastled = false;
            if (board.grid[7][7] != logic.W_ROOK) board.canWhiteRightCastled = false;
            if (board.grid[0][0] != logic.B_ROOK) board.canBlackLeftCastled = false;
            if (board.grid[0][7] != logic.B_ROOK) board.canBlackRightCastled = false;
        }
        else {
            if (board.grid[7][0] != logic.B_ROOK) board.canBlackLeftCastled = false;
            if (board.grid[7][7] != logic.B_ROOK) board.canBlackRightCastled = false;
            if (board.grid[0][0] != logic.W_ROOK) board.canWhiteLeftCastled = false;
            if (board.grid[0][7] != logic.W_ROOK) board.canWhiteRightCastled = false;
        }

        if(piece == logic.W_KING || piece == logic.B_KING){

            if(Math.abs(preCol - newCol) == 2 && preRow == newRow)
                updateRook(board.grid,piece,preRow,preCol,newCol);
        }
    }
    private void updateRook(int[][] grid,int piece,int preRow,int preCol,int newCol){
        // Only updates rook position as king is updated in gridLogic

        int color = logic.getPieceColor(piece);
        // Right side castle
        if(newCol > preCol){
            grid[preRow][preCol+1] = (color==logic.WHITE)?(logic.W_ROOK):(logic.B_ROOK);
            grid[preRow][7] = logic.NO_PIECE;
        }
        else {
            grid[preRow][preCol-1] = (color==logic.WHITE)?(logic.W_ROOK):(logic.B_ROOK);
            grid[preRow][0] = logic.NO_PIECE;
        }
    }
}

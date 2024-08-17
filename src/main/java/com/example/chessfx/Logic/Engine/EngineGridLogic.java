package com.example.chessfx.Logic.Engine;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Board;
import com.example.chessfx.Logic.Object.Move;
import com.example.chessfx.Logic.PieceLogic;

import java.util.ArrayList;
import java.util.List;

public class EngineGridLogic {

    private Board board;
    private PieceLogic pieceLogic;
    private int player;
    private int enPassantTurn;

    // This board is the actual object
    public  EngineGridLogic(Board board,int enginePlayer){
        this.board = board;
        this.pieceLogic = new PieceLogic(logic.getOpponentTurn(enginePlayer));
        this.player = logic.getOpponentTurn(enginePlayer);
        this.enPassantTurn = logic.NO_EN_PASSANT;
    }
    public Board getBoard(){
        return board;
    }
    public int[][] getGrid(){
        return board.grid;
    }
    public int[][] getValidPositions(int row,int col){
        return pieceLogic.allValidMoves(board,board.grid[row][col],row,col);
    }
    public List<Move> getAllPossibleMove(Board board, int turn){

        List<Move> moves = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(logic.isOwnPiece(board.grid[i][j],turn)){
                    int[][] allPosition = pieceLogic.allValidMoves(board,board.grid[i][j],i,j);
                    for(int[] pos : allPosition){
                        Move move = new Move(i,j,pos[0],pos[1],board.grid[i][j]);
                        if(logic.isPawnPromoting(board.grid[i][j],pos[0])){
                            move.isPromotingPiece = true;
                            move.promotedPiece = (turn==logic.WHITE) ? logic.W_QUEEN : logic.B_QUEEN;
                        }
                        moves.add(move);
                    }
                }
            }
        }
        return moves;
    }

    public Board simulateBoard(Board board,Move move,int player){

        Board simulatedBoard = board.deepCopy();
        int turn = logic.getPieceColor(move.piece);

        simulatedBoard.update(move);

        // Set and reset en passant
        updateEnPassant(simulatedBoard,move.piece,move.preRow,move.newRow,move.preCol,move.newCol,turn);

        // Updating grid
        simulatedBoard.grid[move.preRow][move.preCol] = logic.NO_PIECE;
        simulatedBoard.grid[move.newRow][move.newCol] = move.piece;

        // Update castling
        updateCastling(simulatedBoard,player,move.piece,move.preRow,move.preCol,move.newRow,move.newCol);

        return simulatedBoard;
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

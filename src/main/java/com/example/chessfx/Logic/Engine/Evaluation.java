package com.example.chessfx.Logic.Engine;

import com.example.chessfx.Logic.Abstract.logic;

public class Evaluation {

    private int turn;
    public Evaluation(int turn){
        this.turn = turn;
    }
    public Move getSafeMove(int[][] moves,int preRow,int preCol,int[][] tempGrid){

        for(int[] pos : moves){
            int row = pos[0];
            int col = pos[1];

            if(!logic.isCheckSquare(tempGrid,row,col,turn,logic.getOpponentTurn(turn))){
                return new Move(preRow,preCol,row,col,tempGrid[preRow][preCol]);
            }
        }

        return null;
    }
    public Move getCaptureMove(int[][] moves,int preRow,int preCol,int[][] tempGrid){

        for(int[] pos : moves){
            int row = pos[0];
            int col = pos[1];
            if(getPieceValue(tempGrid[preRow][preCol]) < getPieceValue(tempGrid[row][col])){
                return new Move(preRow,preCol,row,col,tempGrid[preRow][preCol]);
            }
        }
        return null;
    }
    public Move getCaptureUndefendedMove(int[][] moves, int preRow, int preCol, int[][] tempGrid){

        for(int[] pos : moves){
            int row = pos[0];
            int col = pos[1];
            if(!logic.isCheckSquare(tempGrid,row,col,turn,logic.getOpponentTurn(turn))){
                if(logic.isOpponentPiece(tempGrid[row][col],turn)){
                    return new Move(preRow,preCol,row,col,tempGrid[preRow][preCol]);
                }
            }
        }
        return null;
    }

    public Move getUndefendedPieceDefendMove(int[][] moves, int preRow,int preCol,int[][] tempGrid){

        boolean isUndefendedPieceExist = false;
        for(int[] pos : moves){
            int row = pos[0];
            int col = pos[1];
            if(logic.isCheckSquare(tempGrid,preRow,preCol,turn,logic.getOpponentTurn(turn))){
                if(!logic.isCheckSquare(tempGrid,preRow,preCol,logic.getOpponentTurn(turn),turn)){
                    isUndefendedPieceExist = true;
                }
            }
        }
        if(isUndefendedPieceExist) {
            Move bestMove = getCaptureUndefendedMove(moves, preRow, preCol, tempGrid);
            if (bestMove == null) {
                bestMove = getCaptureMove(moves, preRow, preCol, tempGrid);
                if (bestMove == null) {
                    return getSafeMove(moves, preRow, preCol, tempGrid);
                }
            }
        }
        return null;
    }
    public Move getPawnPromotionMove(int[][] moves,int preRow,int preCol,int[][] tempGrid){

        int piece = tempGrid[preRow][preCol];
        if(piece == logic.W_PAWN || piece == logic.B_PAWN){
            for(int[] pos : moves){
                if(pos[0] == 7 || pos[0] == 0) return new Move(preRow,preCol,pos[0],pos[1],tempGrid[preRow][preCol]);
            }
        }
        return null;
    }
    private int getPieceValue(int piece) {
        if (piece == logic.W_PAWN || piece == logic.B_PAWN) return 1;
        if (piece == logic.W_KNIGHT || piece == logic.B_KNIGHT) return 3;
        if (piece == logic.W_BISHOP || piece == logic.B_BISHOP) return 3;
        if (piece == logic.W_ROOK || piece == logic.B_ROOK) return 5;
        if (piece == logic.W_QUEEN || piece == logic.B_QUEEN) return 9;
        return 0; // Empty square
    }
}

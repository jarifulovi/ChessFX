package com.example.chessfx.Logic.Engine;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Board;
import com.example.chessfx.Logic.Move;

public class Evaluation {

    private PieceValueTable pieceValueTable;
    private int enginePlayer;
    public Evaluation(int enginePlayer){
        this.enginePlayer = enginePlayer;
        // EnginePlayer is the opponent for the player
        pieceValueTable = new PieceValueTable(logic.getOpponentTurn(enginePlayer));
    }

   public int getEvaluation(Board board,int turn){

        int pieceValue = (board.whitePieceValues-board.blackPieceValues);
        int piecePositioanlValue = pieceValueTable.getPiecePositionalValue(true,board.grid,turn);

        return (turn==logic.WHITE) ? pieceValue + piecePositioanlValue:
                   -pieceValue + piecePositioanlValue;
   }
    public Move getSafeMove(int[][] moves, int preRow, int preCol, int[][] tempGrid){

        for(int[] pos : moves){
            int row = pos[0];
            int col = pos[1];

            if(!logic.isCheckSquare(tempGrid,row,col,enginePlayer,logic.getOpponentTurn(enginePlayer))){
                return new Move(preRow,preCol,row,col,tempGrid[preRow][preCol]);
            }
        }

        return null;
    }
    public Move getCaptureMove(int[][] moves,int preRow,int preCol,int[][] tempGrid){

        for(int[] pos : moves){
            int row = pos[0];
            int col = pos[1];
            if(logic.getPieceValue(tempGrid[preRow][preCol]) < logic.getPieceValue(tempGrid[row][col])){
                return new Move(preRow,preCol,row,col,tempGrid[preRow][preCol]);
            }
        }
        return null;
    }
    public Move getCaptureUndefendedMove(int[][] moves, int preRow, int preCol, int[][] tempGrid){

        for(int[] pos : moves){
            int row = pos[0];
            int col = pos[1];
            if(!logic.isCheckSquare(tempGrid,row,col,enginePlayer,logic.getOpponentTurn(enginePlayer))){
                if(logic.isOpponentPiece(tempGrid[row][col],enginePlayer)){
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
            if(logic.isCheckSquare(tempGrid,preRow,preCol,enginePlayer,logic.getOpponentTurn(enginePlayer))){
                if(!logic.isCheckSquare(tempGrid,preRow,preCol,logic.getOpponentTurn(enginePlayer),enginePlayer)){
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
}

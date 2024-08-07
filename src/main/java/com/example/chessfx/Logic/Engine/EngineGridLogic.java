package com.example.chessfx.Logic.Engine;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Board;
import com.example.chessfx.Logic.Move;
import com.example.chessfx.Logic.PieceLogic;

import java.util.ArrayList;
import java.util.List;

public class EngineGridLogic {

    private Board board;
    private PieceLogic pieceLogic;

    // This board is the actual object
    public  EngineGridLogic(Board board,int enginePlayer){
        this.board = board;
        pieceLogic = new PieceLogic(logic.getOpponentTurn(enginePlayer));
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
        boolean isPawn = (move.piece == logic.W_PAWN || move.piece == logic.B_PAWN);
        logic.updatePieceValues(simulatedBoard,move);

        // Set and reset en passant
        pieceLogic.updateEnPassant(simulatedBoard,move.piece,move.preRow,move.newRow,move.preCol,move.newCol,turn);

        // Updating grid
        simulatedBoard.grid[move.preRow][move.preCol] = logic.NO_PIECE;
//        if(isPawn){
//            boolean isBackRank = (move.newRow == 0 || move.newRow == 7);
//            if(isBackRank)
//                simulatedBoard.grid[move.newRow][move.newCol] = (turn==logic.WHITE) ? logic.W_QUEEN : logic.B_QUEEN;
//        }
//        else
            simulatedBoard.grid[move.newRow][move.newCol] = move.piece;

        // Update castling
        pieceLogic.updateCastling(simulatedBoard,player,move.piece,move.preRow,move.preCol,move.newRow,move.newCol);

        return simulatedBoard;
    }

}

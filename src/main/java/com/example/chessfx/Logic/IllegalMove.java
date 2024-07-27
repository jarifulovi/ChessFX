package com.example.chessfx.Logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IllegalMove {

    private int[][] tempGrid;


    // Here grid is the original grid, modifying grid will result modifying actual grid
    public boolean isSelfCheckAfterMove(int[][] grid,int piece,int player,int preRow,int preCol,int newRow,int newCol){


        int turn = logic.getPieceColor(piece);
        // If the position is occupied by own king
        int[] kingPos = logic.getKingPosition(grid,turn);
        if(kingPos[0] == newRow && kingPos[1] == newCol) return true;

        tempGrid = logic.copyGrid(grid);


        // Castling check
        if(piece == logic.W_KING || piece == logic.B_KING){
            if(Math.abs(preCol-newCol) == 2){

                return checkCastleIllegal(grid,turn,player,preRow,preCol,newRow,newCol);
            }
        }
        // En passant
        if(piece == logic.W_PAWN || piece == logic.B_PAWN){
            if(Math.abs(preRow-newRow)==Math.abs(preCol-newCol)){
                if(grid[newRow][newCol] == logic.NO_PIECE){
                    int direction = (turn==player) ? 1 : -1;
                    tempGrid[newRow+direction][newCol] = logic.NO_PIECE;
                }
            }
        }

        tempGrid[preRow][preCol] = logic.NO_PIECE;
        tempGrid[newRow][newCol] = piece;

        int[][] allAttackedPositions = getAllAttackSquare(tempGrid,turn,player,false);
        int[] kingPosition = logic.getKingPosition(tempGrid,turn);

        return logic.isWithinPosition(allAttackedPositions,kingPosition);
    }


    // Turn indicate who is playing the move
    // Not works for castling and en passant
    private int[][] getAllAttackSquare(int[][] grid,int turn,int player,boolean isCastling){

        List<int[]> validPositions = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int piece = grid[i][j];
                if (logic.isOpponentPiece(piece, turn)) {
                    // Get valid moves for opponent piece
                    int[][] potentialMoves = new int[0][0];

                    if(piece == logic.B_KNIGHT || piece == logic.W_KNIGHT){
                        potentialMoves = logic.validKnightSquares(i,j);
                    }
                    else if(piece == logic.B_ROOK || piece == logic.W_ROOK){
                        potentialMoves = logic.validRookSquares(i,j,grid);
                    }
                    else if(piece == logic.B_BISHOP || piece == logic.W_BISHOP){
                        potentialMoves = logic.validBishopSquares(i,j,grid);
                    }
                    else if(piece == logic.B_QUEEN || piece == logic.W_QUEEN){
                        potentialMoves = logic.validQueenSquares(i,j,grid);
                    }

                    // Only check when not checking castling right
                    if(!isCastling){
                        if(piece == logic.W_KING || piece == logic.B_KING){
                            potentialMoves = logic.validKingAttackSquares(i,j);
                        }
                        else if(piece == logic.W_PAWN || piece == logic.B_PAWN){
                            potentialMoves = logic.validPawnAttackSquares(turn,player,i,j);

                        }
                    }

                    validPositions.addAll(Arrays.asList(potentialMoves));
                }
            }
        }

        // Convert list to 2D array if needed
        int[][] validPositionsArray = new int[validPositions.size()][2];
        int index = 0;
        for (int[] position : validPositions) {
            validPositionsArray[index++] = position;
        }

        return validPositionsArray;
    }

    private boolean checkCastleIllegal(int[][] grid,int turn,int player,int preRow,int preCol,int newRow,int newCol){

        // First find the positions where castle involved
        int[][] castlePositions;

        // Right side or king side castling
        if(newCol > preCol){
            castlePositions = new int[4][2];
            for(int i=0;i<4;i++){
                castlePositions[i][0] = preRow;
                castlePositions[i][1] = i+4;
            }
        }
        else {
            castlePositions = new int[5][2];
            for(int i=0;i<=4;i++){
                castlePositions[i][0] = preRow;
                castlePositions[i][1] = i;

            }
        }

        return checkAllCastleAttackSquare(grid,castlePositions,turn,player);
    }

    private boolean checkAllCastleAttackSquare(int[][] grid,int[][] castlePositions,int turn,int player){

        // Without pawn and king
        int[][] validSquare = getAllAttackSquare(grid,turn,player,true);

        // Check if attacks exist in castle positions
        for(int[] pos : validSquare){
            for(int[] pos2 : castlePositions){
                if(pos[0] == pos2[0] && pos[1] == pos2[1]) {
                    System.out.println("castling attacks");
                    return true;
                }
            }
        }
        // Check pawns and kings
        int secondRank = (turn == player) ? (6) : (1);
        int pawn = (turn == logic.WHITE) ? (logic.B_PAWN) : (logic.W_PAWN);
        int king = (turn == logic.WHITE) ? (logic.B_KING) : (logic.W_KING);

        // No opponent pawn and  king should be there
        for(int[] pos : castlePositions){
            int row = secondRank;
            int col = pos[1];
            return grid[row][col] == pawn || grid[row][col] == king;
        }
        return false;
    }
}

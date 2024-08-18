package com.example.chessfx.Logic;

import com.example.chessfx.Logic.Abstract.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IllegalMove {

    private int[][] tempGrid;


    // Here grid is the original grid, modifying grid will result modifying actual grid
    public boolean isSelfCheckAfterMove(int[][] grid,int piece,int player,int preRow,int preCol,int newRow,int newCol){


        int turn = logic.getPieceColor(piece);

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


        return logic.isKingInCheck(tempGrid,turn,player);
    }


    private boolean checkCastleIllegal(int[][] grid,int turn,int player,int preRow,int preCol,int newRow,int newCol){

        // First find the positions where castle involved
        int[][] castlePositions;

        // Right side or king side castling
        if(newCol > preCol){
            castlePositions = new int[3][2];
            for(int i=0;i<3;i++){
                castlePositions[i][0] = preRow;
                castlePositions[i][1] = i+4;
            }
        }
        else {
            castlePositions = new int[3][2];
            for(int i=0;i<3;i++){
                castlePositions[i][0] = preRow;
                castlePositions[i][1] = i+2;
            }
        }

        return checkAllCastleAttackSquare(grid,castlePositions,turn,player);
    }

    private boolean checkAllCastleAttackSquare(int[][] grid,int[][] castlePositions,int turn,int player){

        // Check if attacks exist in castle positions
        for(int[] pos : castlePositions){
            if(logic.isCheckSquare(grid,pos[0],pos[1],turn,player)) return true;
        }
        return false;
    }
}

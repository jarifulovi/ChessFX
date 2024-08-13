package com.example.chessfx.Logic.Engine;

import com.example.chessfx.Logic.Abstract.logic;

public class PieceValueTable{

    // This map is based on player
    // For non-player turn the values will be negative
    public int[][][] openingPiecePositionalValues;
    public int[][][] endGamePiecePositionalValues;

    public PieceValueTable(int player) {
        openingPiecePositionalValues = new int[13][8][8];
        int[][][] piecePositionalValues = {
                pawnOpeningPositionalValues, knightPositionalValues, bishopOpeningPositionalValues,
                rookOpeningPositionalValues, queenOpeningPositionalValues, kingOppeningPositionalValues
        };

        // Piece type constants for easy access
        int[] whitePieceTypes = {logic.W_PAWN, logic.W_KNIGHT, logic.W_BISHOP, logic.W_ROOK, logic.W_QUEEN, logic.W_KING};
        int[] blackPieceTypes = {logic.B_PAWN, logic.B_KNIGHT, logic.B_BISHOP, logic.B_ROOK, logic.B_QUEEN, logic.B_KING};

        // Initialize white and black piece positional values
        for (int i = 0; i < 6; i++) {
            if (player == logic.WHITE) {
                openingPiecePositionalValues[whitePieceTypes[i]] = logic.copyGrid(piecePositionalValues[i]);
                openingPiecePositionalValues[blackPieceTypes[i]] = logic.copyOpponentGrid(piecePositionalValues[i]);
            } else {
                openingPiecePositionalValues[whitePieceTypes[i]] = logic.copyOpponentGrid(piecePositionalValues[i]);
                openingPiecePositionalValues[blackPieceTypes[i]] = logic.copyGrid(piecePositionalValues[i]);
            }
        }
        endGamePiecePositionalValues = new int[13][8][8];
        int[][][] piecePositionalValues2 = {
                pawnEndgamePositionalValues, knightPositionalValues, bishopEndgamePositionalValues,
                rookEndgamePositionalValues, queenOpeningPositionalValues, kingEndgamePositionalValues
        };
        for(int i=0;i<6;i++){
            if(player == logic.WHITE){
                endGamePiecePositionalValues[whitePieceTypes[i]] = logic.copyGrid(piecePositionalValues2[i]);
                endGamePiecePositionalValues[blackPieceTypes[i]] = logic.copyOpponentGrid(piecePositionalValues2[i]);
            }
            else {
                endGamePiecePositionalValues[whitePieceTypes[i]] = logic.copyOpponentGrid(piecePositionalValues2[i]);
                endGamePiecePositionalValues[blackPieceTypes[i]] = logic.copyGrid(piecePositionalValues2[i]);
            }
        }
    }

    public int getPiecePositionalValue(boolean hasAdvantage,boolean isEndgame,int[][] grid,int turn){
        int point = 0;
        if(!isEndgame){
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    if(logic.isOwnPiece(grid[i][j],turn)){
                        point +=  openingPiecePositionalValues[grid[i][j]][i][j];
                    }
                }
            }
        }
        else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (logic.isOwnPiece(grid[i][j], turn)) {
                        point += endGamePiecePositionalValues[grid[i][j]][i][j];
                    }
                }
            }
        }
        return point;
    }
    public  int[][] kingOppeningPositionalValues = {
            {-50, -50, -50, -50, -50, -50, -50, -50},
            {-50, -50, -50, -50, -50, -50, -50, -50},
            {-50, -50, -50, -50, -50, -50, -50, -50},
            {-50, -50, -50, -50, -50, -50, -50, -50},
            {-30, -30, -30, -50, -50, -30, -30, -30},
            {-30, -30, -30, -40, -40, -30, -30, -30},
            {-20, -20, -20, -20, -20, -20, -20, -20},
            { 20,  30,  30,   0, (0),  20,  30,  20}
    };
    // When no queens also at most one major piece for each side
    int[][] kingEndgamePositionalValues = {
            {-50, -40, -30, -30, -30, -30, -40, -50},
            {-40, -20,   0,   0,   0,   0, -20, -40},
            {-30,  0,   10,  15,  15,  10,   0, -30},
            {-30,  5,   15,  20,  20,  15,   5, -30},
            {-30,  0,   15,  20,  20,  15,   0, -30},
            {-30,  5,   10,  15,  15,  10,   5, -30},
            {-40, -20,   0,   0,   0,   0, -20, -40},
            {-50, -40, -30, -30, -30, -30, -40, -50}
    };
    int[][] knightPositionalValues = {
            {-50, -40, -30, -30, -30, -30, -40, -50},
            {-40, -20,   0,   0,   0,   0, -20, -40},
            {-30,   0,  15,  15,  15,  15,   0, -30},
            {-30,   5,  15,  30,  30,  15,   5, -30},
            {-30,   5,  15,  30,  30,  15,   5, -30},
            {-30,  15,  20,  15,  15,  20,  15, -30},
            {-40, -20,   0,   5,   5,   0, -20, -40},
            {-50,(-40),-30, -30, -30,-30,(-40), -50}
    };
    int[][] bishopOpeningPositionalValues = {
            {-20, -10, -10, -10, -10, -10, -10, -20},
            {-10,   0,   0,   0,   0,   0,   0, -10},
            {-10,   0,   5,   5,   5,   5,   0, -10},
            {-10,  10,   5,  10,  10,   5,  10, -10},
            {-10,   5,  30,  10,  10,  30,   5, -10},
            {-10,  10,  10,  30,  30,  10,  10, -10},
            {  0,  30,   0,   0,   0,   0,  30,   0},
            {  0, -10,  10, -10, -10, -10, -10,   0}
    };
    int[][] bishopEndgamePositionalValues = {
            {  0,   0,   0,   0,   0,   0,   0,   0},
            {  0,  10,  10,  10,  10,  10,  10,   0},
            {  0,  10,  20,  20,  20,  20,  10,   0},
            {  0,  10,  20,  30,  30,  20,  10,   0},
            {  0,  10,  20,  30,  30,  20,  10,   0},
            {  0,  10,  20,  20,  20,  20,  10,   0},
            {  0,  10,  10,  10,  10,  10,  10,   0},
            {  0,   0,   0,   0,   0,   0,   0,   0}
    };
    int[][] rookOpeningPositionalValues = {
            {-50, -20, -10, -10, -10, -10, -20, -50},
            {-20, -10, -10, -10, -10, -10, -10, -20},
            {-10,   0,   5,  10,  10,   5,   0, -10},
            {-10,   0,   5,  15,  15,   5,   0, -10},
            {-10,   0,   5,  10,  10,   5,   0, -10},
            {-10,   0,   5,  10,  10,   5,   0, -10},
            {-10, -20, -20,   5,   5, -20, -20, -10},
            {-10, -10,  30,  30,  30,  30, -10, -10}
    };
    int[][] rookEndgamePositionalValues = {
            {-10,   0,   0,   0,   0,   0,   0, -10},
            {  0,  10,  20,  20,  20,  20,  10,   0},
            {-10,   0,  10,  10,  10,  10,   0, -10},
            {-10,   0,  10,  15,  15,  10,   0, -10},
            {-10,   0,  10,  15,  15,  10,   0, -10},
            {-10,   0,  10,  10,  10,  10,   0, -10},
            {-10,   0,  10,  10,  10,  10,   0, -10},
            {-10, -10,   0,   0,   0,   0, -10, -10}
    };
    int[][] queenOpeningPositionalValues = {
            {-20, -10, -10,  -5,  -5, -10, -10, -20},
            {-10,   0,   0,   5,   5,   0,   0, -10},
            {-10,   0,   5,   5,   5,   5,   0, -10},
            {-5,    0,   5,   5,   5,   5,   0,  -5},
            {-5,    0,   5,   5,   5,   5,   0,  -5},
            {-10,   5,   5,   5,   5,   5,   0, -10},
            {-10,   0,   5,   5,   5,   5,   0, -10},
            {-20, -10, -10,  -5,  -5, -10, -10, -20}
    };
    int[][] pawnOpeningPositionalValues = {
            {-20, -10, -10,  -5,  -5, -10, -10, -20},
            {-10,   0,   0,   5,   5,   0,   0, -10},
            {-10,   0,   5,   5,   5,   5,   0, -10},
            {-5,    0,   5,   5,   5,   5,   0,  -5},
            {-5,    0,  10,  20,  20,  10,   0,  -5},
            { 10,  10,  10,  20,  20,  10,  10,  10},
            {  5,   5,   5,   5,   5,   5,   5,   5},
            {  0,   0,   0,   0,   0,   0,   0,   0}
    };
    int[][] pawnEndgamePositionalValues = {
            { 60,  60,  60,  60,  60,  60,  60,  60},
            { 50,  50,  50,  50,  50,  50,  50,  50},
            { 40,  40,  40,  40,  40,  40,  40,  40},
            { 30,  30,  30,  30,  30,  30,  30,  30},
            { 20,  20,  20,  20,  20,  20,  20,  20},
            { 10,  10,  10,  20,  20,  10,  10,  10},
            {  5,   5,   5,   5,   5,   5,   5,   5},
            {  0,   0,   0,   0,   0,   0,   0,   0}
    };
}

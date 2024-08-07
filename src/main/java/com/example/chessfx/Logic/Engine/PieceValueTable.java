package com.example.chessfx.Logic.Engine;

import com.example.chessfx.Logic.Abstract.logic;

public class PieceValueTable{

    // This map is based on player
    // For non-player turn the values will be negative
    public int[][][] openingPiecePositionalValues;

    public PieceValueTable(int player){

        openingPiecePositionalValues = new int[13][8][8];

        if(player == logic.WHITE) {
            // Init white piece positional values
            openingPiecePositionalValues[logic.W_PAWN] = logic.copyGrid(pawnOpeningPositionalValues);
            openingPiecePositionalValues[logic.W_KNIGHT] = logic.copyGrid(knightPositionalValues);
            openingPiecePositionalValues[logic.W_BISHOP] = logic.copyGrid(bishopOpeningPositionalValues);
            openingPiecePositionalValues[logic.W_ROOK] = logic.copyGrid(rookOpeningPositionalValues);
            openingPiecePositionalValues[logic.W_QUEEN] = logic.copyGrid(queenOpeningPositionalValues);
            openingPiecePositionalValues[logic.W_KING] = logic.copyGrid(kingOppeningPositionalValues);
            // Init black piece positional values
            openingPiecePositionalValues[logic.B_PAWN] = logic.copyOpponentGrid(pawnOpeningPositionalValues);
            openingPiecePositionalValues[logic.B_KNIGHT] = logic.copyOpponentGrid(knightPositionalValues);
            openingPiecePositionalValues[logic.B_BISHOP] = logic.copyOpponentGrid(bishopOpeningPositionalValues);
            openingPiecePositionalValues[logic.B_ROOK] = logic.copyOpponentGrid(rookOpeningPositionalValues);
            openingPiecePositionalValues[logic.B_QUEEN] = logic.copyOpponentGrid(queenOpeningPositionalValues);
            openingPiecePositionalValues[logic.B_KING] = logic.copyOpponentGrid(knightPositionalValues);
        }
        else {
            // Init white piece positional values
            openingPiecePositionalValues[logic.W_PAWN] = logic.copyOpponentGrid(pawnOpeningPositionalValues);
            openingPiecePositionalValues[logic.W_KNIGHT] = logic.copyOpponentGrid(knightPositionalValues);
            openingPiecePositionalValues[logic.W_BISHOP] = logic.copyOpponentGrid(bishopOpeningPositionalValues);
            openingPiecePositionalValues[logic.W_ROOK] = logic.copyOpponentGrid(rookOpeningPositionalValues);
            openingPiecePositionalValues[logic.W_QUEEN] = logic.copyOpponentGrid(queenOpeningPositionalValues);
            openingPiecePositionalValues[logic.W_KING] = logic.copyOpponentGrid(knightPositionalValues);
            // Init black piece positional values
            openingPiecePositionalValues[logic.B_PAWN] = logic.copyGrid(pawnOpeningPositionalValues);
            openingPiecePositionalValues[logic.B_KNIGHT] = logic.copyGrid(knightPositionalValues);
            openingPiecePositionalValues[logic.B_BISHOP] = logic.copyGrid(bishopOpeningPositionalValues);
            openingPiecePositionalValues[logic.B_ROOK] = logic.copyGrid(rookOpeningPositionalValues);
            openingPiecePositionalValues[logic.B_QUEEN] = logic.copyGrid(queenOpeningPositionalValues);
            openingPiecePositionalValues[logic.B_KING] = logic.copyGrid(kingOppeningPositionalValues);
        }
    }
    public int getPiecePositionalValue(boolean isOpening,int[][] grid,int turn){
        int point = 0;
        if(isOpening){
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    if(logic.isOwnPiece(grid[i][j],turn)){
                        point +=  openingPiecePositionalValues[grid[i][j]][i][j];
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

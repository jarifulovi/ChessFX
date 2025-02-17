package com.example.chessfx.Logic.Engine;

import com.example.chessfx.Logic.Engine.BitBoard.Bitboard;

public class PieceValueTable{

    // This map is based on white player turn
    public int[][] openingPositionalValueBB;
    public int[][] endGamePositionalValueBB;

    public PieceValueTable() {
        openingPositionalValueBB = new int[13][64];
        endGamePositionalValueBB = new int[13][64];
        int[][][] piecePositionalValues = { new int[8][8],
                pawnOpeningPositionalValues, knightPositionalValues, bishopOpeningPositionalValues,
                rookOpeningPositionalValues, queenOpeningPositionalValues, kingOppeningPositionalValues,
                pawnOpeningPositionalValuesB, knightPositionalValuesB, bishopOpeningPositionalValuesB,
                rookOpeningPositionalValuesB, queenOpeningPositionalValues, kingOppeningPositionalValuesB
        };

        int[][][] piecePositionalValues2 = { new int[8][8],
                pawnEndgamePositionalValues, knightPositionalValues, bishopEndgamePositionalValues,
                rookEndgamePositionalValues, queenOpeningPositionalValues, kingEndgamePositionalValues,
                pawnEndgamePositionalValuesB, knightPositionalValuesB, bishopEndgamePositionalValues,
                rookEndgamePositionalValuesB, queenOpeningPositionalValues, kingEndgamePositionalValues
        };


        initBBTable(piecePositionalValues,piecePositionalValues2);
    }
    private void initBBTable(int[][][] ppV,int[][][] ppV2){
        // Retrieve the tables for each piece
        for(int piece = 1; piece < Bitboard.NUM_PIECE_TYPES; piece++){
            openingPositionalValueBB[piece] = convertToBBTAble(ppV[piece]);
            endGamePositionalValueBB[piece] = convertToBBTAble(ppV2[piece]);
        }
    }
    public int[] convertToBBTAble(int[][] posTable) {
        int[] posDict = new int[64];

        for (int row = 0; row < posTable.length; row++) {
            for (int col = 0; col < posTable[row].length; col++) {
                posDict[row * 8 + col] = posTable[row][col];
            }
        }
        return posDict;
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
    public  int[][] kingOppeningPositionalValuesB = {
            { 20, 30, 30,  0,(0), 20, 30, 20},
            {-20,-20,-20,-20,-20,-20,-20,-20},
            {-30,-30,-30,-40,-40,-30,-30,-30},
            {-30,-30,-30,-50,-50,-30,-30,-30},
            {-50,-50,-50,-50,-50,-50,-50,-50},
            {-50,-50,-50,-50,-50,-50,-50,-50},
            {-50,-50,-50,-50,-50,-50,-50,-50},
            {-50,-50,-50,-50,-50,-50,-50,-50}
    };
    // When no queens also at most one major piece for each side
    int[][] kingEndgamePositionalValues = {
            {-50, -40, -30, -30, -30, -30, -40, -50},
            {-40, -20,   0,   0,   0,   0, -20, -40},
            {-30,  0,   10,  15,  15,  10,   0, -30},
            {-30,  0,   15,  20,  20,  15,   0, -30},
            {-30,  0,   15,  20,  20,  15,   0, -30},
            {-30,  0,   10,  15,  15,  10,   0, -30},
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
    int[][] knightPositionalValuesB = {
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,0,5,5,0,-20,-40},
            {-30,15,20,15,15,20,15,-30},
            {-30,5,15,30,30,15,5,-30},
            {-30,5,15,30,30,15,5,-30},
            {-30,0,15,15,15,15,0,-30},
            {-40,-20,0,0,0,0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50},
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
    int[][] bishopOpeningPositionalValuesB = {
            {0,-10,10,-10,-10,-10,-10,0},
            {0,30,0,0,0,0,30,0},
            {-10,10,10,30,30,10,10,-10},
            {-10,5,30,10,10,30,5,-10},
            {-10,10,5,10,10,5,10,-10},
            {-10,0,5,5,5,5,0,-10},
            {-10,0,0,0,0,0,0,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20},
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
    int[][] rookOpeningPositionalValuesB = {
            {-10,-10,30,30,30,30,-10,-10},
            {-10,-20,-20,5,5,-20,-20,-10},
            {-10,0,5,10,10,5,0,-10},
            {-10,0,5,10,10,5,0,-10},
            {-10,0,5,15,15,5,0,-10},
            {-10,0,5,10,10,5,0,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-50,-20,-10,-10,-10,-10,-20,-50},
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
    int[][] rookEndgamePositionalValuesB = {
            {-10,-10,0,0,0,0,-10,-10},
            {-10,0,10,10,10,10,0,-10},
            {-10,0,10,10,10,10,0,-10},
            {-10,0,10,15,15,10,0,-10},
            {-10,0,10,15,15,10,0,-10},
            {-10,0,10,10,10,10,0,-10},
            {0,10,20,20,20,20,10,0},
            {-10,0,0,0,0,0,0,-10},
    };
    int[][] queenOpeningPositionalValues = {
            {-20, -10, -10,  -5,  -5, -10, -10, -20},
            {-10,   0,   0,   5,   5,   0,   0, -10},
            {-10,   0,   5,   5,   5,   5,   0, -10},
            {-5,    0,   5,   5,   5,   5,   0,  -5},
            {-5,    0,   5,   5,   5,   5,   0,  -5},
            {-10,   0,   5,   5,   5,   5,   0, -10},
            {-10,   0,   0,   5,   5,   0,   0, -10},
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
    int[][] pawnOpeningPositionalValuesB = {
            {0,0,0,0,0,0,0,0},
            {5,5,5,5,5,5,5,5},
            {10,10,10,20,20,10,10,10},
            {-5,0,10,20,20,10,0,-5},
            {-5,0,5,5,5,5,0,-5},
            {-10,0,5,5,5,5,0,-10},
            {-10,0,0,5,5,0,0,-10},
            {-20,-10,-10,-5,-5,-10,-10,-20},
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
    int[][] pawnEndgamePositionalValuesB = {
            {0,0,0,0,0,0,0,0},
            {5,5,5,5,5,5,5,5},
            {10,10,10,20,20,10,10,10},
            {20,20,20,20,20,20,20,20},
            {30,30,30,30,30,30,30,30},
            {40,40,40,40,40,40,40,40},
            {50,50,50,50,50,50,50,50},
            {60,60,60,60,60,60,60,60},
    };
}

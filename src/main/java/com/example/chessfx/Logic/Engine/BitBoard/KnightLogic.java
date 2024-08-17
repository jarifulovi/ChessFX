package com.example.chessfx.Logic.Engine.BitBoard;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Move;

import java.util.ArrayList;
import java.util.List;

public class KnightLogic {

    private final List<Integer>[] knightMovesTable = new ArrayList[64];

    public KnightLogic(){
        initKnightMoveTable();
    }

    public void initKnightMoveTable(){
        for(int i = 0; i < 64 ; i++){
            knightMovesTable[i] = new ArrayList<>();

            int[] rowOffsets = {-2, -2, -1, 1, 2, 2, -1, 1};
            int[] colOffsets = {-1, 1, 2, 2, 1, -1, -2, -2};

            int row = i / 8;
            int col = i % 8;
            for(int j = 0; j < 8; j++){
                int newRow = row + rowOffsets[j];
                int newCol = col + colOffsets[j];

                if(logic.isWithinBoard(newRow,newCol)){
                    int newIndex = newRow * 8 + newCol;
                    knightMovesTable[i].add(newIndex);
                }
            }
        }
    }
    public List<Move> getKnightMoves(Bitboard bitboard,int turn){

        List<Move> moves = new ArrayList<>();
        int piece;
        long bitBoardForKnights;
        long allOwnPiece;
        if(turn == logic.WHITE){
            piece = logic.W_KNIGHT;
            bitBoardForKnights = bitboard.bitboards[logic.W_KNIGHT];
            allOwnPiece = bitboard.allWhitePieces;
        }
        else {
            piece = logic.B_KNIGHT;
            bitBoardForKnights = bitboard.bitboards[logic.B_KNIGHT];
            allOwnPiece = bitboard.allBlackPieces;
        }

        while (bitBoardForKnights != 0) {
            int knightPos = Long.numberOfTrailingZeros(bitBoardForKnights);
            bitBoardForKnights &= ~(1L << knightPos);

            List<Integer> potentialMoves = knightMovesTable[knightPos];


            for (int newIndex : potentialMoves) {
                //System.out.println("Row : "+newIndex/8 + " col : "+newIndex%8);
                if (!logic.isSquareOccupied(allOwnPiece, newIndex)) {
                    moves.add(new Move(knightPos, newIndex, piece));
                }
            }
        }
        return moves;
    }
    
}

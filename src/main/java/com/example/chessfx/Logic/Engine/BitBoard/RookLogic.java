package com.example.chessfx.Logic.Engine.BitBoard;

import com.example.chessfx.Logic.Abstract.MagicBB;
import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Move;

import java.util.ArrayList;
import java.util.List;

public class RookLogic {

    public List<Move> getRookMoves(Bitboard bitboard, int turn){
        List<Move> moves = new ArrayList<>();
        long bitBoardForRook;
        long allOwnPiece;
        int piece;
        if(turn == logic.WHITE){
            bitBoardForRook = bitboard.bitboards[logic.W_ROOK];
            allOwnPiece = bitboard.allWhitePieces;
            piece = logic.W_ROOK;
        }
        else {
            bitBoardForRook = bitboard.bitboards[logic.B_ROOK];
            allOwnPiece = bitboard.allBlackPieces;
            piece = logic.B_ROOK;
        }

        long attackPositions;
        while(bitBoardForRook != 0){
            int rookPosition = Long.numberOfTrailingZeros(bitBoardForRook);
            bitBoardForRook &= ~(1L << rookPosition);

            attackPositions = MagicBB.RookAttacks(rookPosition,bitboard.allPieces);
            moves.addAll(retrieveMoves(attackPositions,rookPosition,piece,allOwnPiece));
        }
        return moves;
    }
    private List<Move> retrieveMoves(long attackPositions,int preIndex,int piece,long allOwnPiece){
        List<Move> moves = new ArrayList<>();
        while(attackPositions != 0){
            int newIndex = Long.numberOfTrailingZeros(attackPositions);
            // Clears the i'th bit
            attackPositions &= ~(1L << newIndex);

            if(!logic.isSquareOccupied(allOwnPiece,newIndex)) {
                moves.add(new Move(preIndex, newIndex, piece));
            }
        }
        return moves;
    }
}

package com.example.chessfx.Logic.Engine.BitBoard;

import com.example.chessfx.Logic.Abstract.MagicBB;
import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Move;

import java.util.ArrayList;
import java.util.List;

public class BishopLogic {

    public List<Move> getBishopMoves(Bitboard bitboard, int turn){
        List<Move> moves = new ArrayList<>();
        long bitBoardForBishop;
        long allOwnPiece;
        int piece;
        if(turn == logic.WHITE){
            bitBoardForBishop = bitboard.bitboards[logic.W_BISHOP];
            allOwnPiece = bitboard.allWhitePieces;
            piece = logic.W_BISHOP;
        }
        else {
            bitBoardForBishop = bitboard.bitboards[logic.B_BISHOP];
            allOwnPiece = bitboard.allBlackPieces;
            piece = logic.B_BISHOP;
        }

        long attackPositions;
        while(bitBoardForBishop != 0){
            int bishopPosition = Long.numberOfTrailingZeros(bitBoardForBishop);
            bitBoardForBishop &= ~(1L << bishopPosition);

            attackPositions = MagicBB.BishopAttacks(bishopPosition,bitboard.allPieces);
            moves.addAll(retrieveMoves(attackPositions,bishopPosition,piece,allOwnPiece));
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

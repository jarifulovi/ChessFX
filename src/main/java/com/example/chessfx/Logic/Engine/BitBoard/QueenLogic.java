package com.example.chessfx.Logic.Engine.BitBoard;

import com.example.chessfx.Logic.Abstract.MagicBB;
import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Move;

import java.util.ArrayList;
import java.util.List;

public class QueenLogic {

    public List<Move> getQueenMoves(Bitboard bitboard, int turn){
        List<Move> moves = new ArrayList<>();
        long bitBoardForQueen;
        long allOwnPiece;
        int piece;
        if(turn == logic.WHITE){
            bitBoardForQueen = bitboard.bitboards[logic.W_QUEEN];
            allOwnPiece = bitboard.allWhitePieces;
            piece = logic.W_QUEEN;
        }
        else {
            bitBoardForQueen = bitboard.bitboards[logic.B_QUEEN];
            allOwnPiece = bitboard.allBlackPieces;
            piece = logic.B_QUEEN;
        }

        long attackPositions;
        while(bitBoardForQueen != 0){
            int queenPosition = Long.numberOfTrailingZeros(bitBoardForQueen);
            bitBoardForQueen &= ~(1L << queenPosition);

            attackPositions = MagicBB.QueenAttacks(queenPosition,bitboard.allPieces);
            moves.addAll(retrieveMoves(attackPositions,queenPosition,piece,allOwnPiece));
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

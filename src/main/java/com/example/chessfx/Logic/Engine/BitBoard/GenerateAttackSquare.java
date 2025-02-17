package com.example.chessfx.Logic.Engine.BitBoard;

import com.example.chessfx.Logic.Abstract.MagicBB;
import com.example.chessfx.Logic.Abstract.logic;

public abstract class GenerateAttackSquare {

    public static long getAllAttackSquare(Bitboard bitboard,int turn){

        long attackSquare = 0L;
        attackSquare |= getPawnAttackSquare(bitboard,turn);
        attackSquare |= getKnightAttackSquare(bitboard,turn);
        attackSquare |= getSlidingPieceAttackSquare(bitboard,turn);
        attackSquare |= getKingAttackSquare(bitboard,turn);

        return attackSquare;
    }
    public static boolean isSquareAttacked(Bitboard bitboard,int turn,int square){

        long allAttackSquares = getAllAttackSquare(bitboard,logic.getOpponentTurn(turn));
        return logic.isSquareOccupied(allAttackSquares,square);
    }
    public static boolean isOwnKingAttacked(Bitboard bitboard,int turn){

        long bitboardForKing = (turn==logic.WHITE) ? bitboard.bitboards[logic.W_KING] :
                                        bitboard.bitboards[logic.B_KING];

        int index = Long.numberOfTrailingZeros(bitboardForKing);
        return isSquareAttacked(bitboard,turn,index);
    }
    private static long getPawnAttackSquare(Bitboard bitboard,int turn){

        long attack = 0L;
        long bitBoardForPawn = (turn==logic.WHITE) ? bitboard.bitboards[logic.W_PAWN]
                : bitboard.bitboards[logic.B_PAWN];

        while(bitBoardForPawn != 0){
            int pawnPos = Long.numberOfTrailingZeros(bitBoardForPawn);
            bitBoardForPawn &= ~(1L << pawnPos);
            attack |= (turn==logic.WHITE) ? PawnLogic.whitePawnAttackTable[pawnPos] :
                            PawnLogic.blackPawnAttackTable[pawnPos];
        }
        return attack;
    }
    private static long getKingAttackSquare(Bitboard bitboard,int turn){

        long attack = 0L;
        long bitBoardForKing = (turn==logic.WHITE) ? bitboard.bitboards[logic.W_KING]
                : bitboard.bitboards[logic.B_KING];


        int kingPos = Long.numberOfTrailingZeros(bitBoardForKing);
        attack |= KingLogic.kingAttackTable[kingPos];

        return attack;
    }
    private static long getKnightAttackSquare(Bitboard bitboard,int turn){

        long attack = 0L;
        long bitBoardForKnights = (turn== logic.WHITE) ? bitboard.bitboards[logic.W_KNIGHT]
                : bitboard.bitboards[logic.B_KNIGHT];


        while(bitBoardForKnights != 0){
            int knightPos = Long.numberOfTrailingZeros(bitBoardForKnights);
            bitBoardForKnights &= ~(1L << knightPos);
            // knightAttackTable is initialized
            attack |= KnightLogic.knightAttackTable[knightPos];
        }
        return attack;
    }
    private static long getSlidingPieceAttackSquare(Bitboard bitboard,int turn){

        long attack = 0L;

        long bitBoardForBishop = (turn== logic.WHITE) ? bitboard.bitboards[logic.W_BISHOP]
                : bitboard.bitboards[logic.B_BISHOP];

        while(bitBoardForBishop != 0){
            int bishopPos = Long.numberOfTrailingZeros(bitBoardForBishop);
            bitBoardForBishop &= ~(1L << bishopPos);
            attack |= MagicBB.BishopAttacks(bishopPos,bitboard.allPieces);
        }
        long bitBoardForRook = (turn== logic.WHITE) ? bitboard.bitboards[logic.W_ROOK]
                : bitboard.bitboards[logic.B_ROOK];

        while(bitBoardForRook != 0){
            int rookPos = Long.numberOfTrailingZeros(bitBoardForRook);
            bitBoardForRook &= ~(1L << rookPos);
            attack |= MagicBB.RookAttacks(rookPos,bitboard.allPieces);
        }
        long bitBoardForQueen = (turn== logic.WHITE) ? bitboard.bitboards[logic.W_QUEEN]
                : bitboard.bitboards[logic.B_QUEEN];

        while(bitBoardForQueen != 0){
            int queenPos = Long.numberOfTrailingZeros(bitBoardForQueen);
            bitBoardForQueen &= ~(1L << queenPos);
            attack |= MagicBB.QueenAttacks(queenPos,bitboard.allPieces);
        }
        return attack;
    }

}

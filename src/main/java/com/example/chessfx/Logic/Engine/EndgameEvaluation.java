package com.example.chessfx.Logic.Engine;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Engine.BitBoard.Bitboard;

public class EndgameEvaluation {

    private int[][] opponentKingTable = {
            {60, 30, 30, 20, 20, 30, 30, 60},
            {30, 20, 10,  5,  5, 10, 10, 30},
            {30, 20, 10,  5,  5, 10, 10, 30},
            {20,  5,  5,  0,  0,  5,  5, 20},
            {20,  5,  5,  0,  0,  5,  5, 20},
            {30, 20, 10,  5,  5, 10, 10, 30},
            {30, 20, 10,  5,  5, 10, 10, 30},
            {60, 30, 30, 20, 20, 30, 30, 60}
    };

    // Rook and no other pieces so check-mate is inevitable
    public int rookOnlyEndgame(Bitboard bitboard, int point, int turn){
        int rookHolder = (bitboard.bitboards[logic.W_ROOK]!=0) ? logic.WHITE : logic.BLACK;
        long bitboardForKing1 = (rookHolder==logic.WHITE) ? bitboard.bitboards[logic.W_KING] :
                                    bitboard.bitboards[logic.B_KING];
        // King who doesn't have rook
        long bitboardForKing2 = (rookHolder==logic.WHITE) ? bitboard.bitboards[logic.B_KING] :
                            bitboard.bitboards[logic.W_KING];

        int king1 = Long.numberOfTrailingZeros(bitboardForKing1);
        int king2 = Long.numberOfTrailingZeros(bitboardForKing2);

        return (rookHolder==turn) ? forceKingToMoveOnEdge(king1,king2) + point:
                -forceKingToMoveOnEdge(king1,king2) + point;
    }
    // Queen and no other pieces so cm is inevitable
    public int queenOnlyEndgame(Bitboard bitboard, int point, int turn){

        int queenHolder = (bitboard.bitboards[logic.W_QUEEN]!=0) ? logic.WHITE : logic.BLACK;

        long bitboardForKing1 = (queenHolder==logic.WHITE) ? bitboard.bitboards[logic.W_KING] :
                bitboard.bitboards[logic.B_KING];
        // King who doesn't have queen
        long bitboardForKing2 = (queenHolder==logic.WHITE) ? bitboard.bitboards[logic.B_KING] :
                bitboard.bitboards[logic.W_KING];

        int king1 = Long.numberOfTrailingZeros(bitboardForKing1);
        int king2 = Long.numberOfTrailingZeros(bitboardForKing2);

        return (queenHolder==turn) ? forceKingToMoveOnEdge(king1,king2) + point:
                -forceKingToMoveOnEdge(king1,king2) + point;
    }
    private int forceKingToMoveOnEdge(int ownKingPos,int opponentKingPos){

        // Force opponent king to go to edges
        // Force ownking to cut of opponent king by getting closer
        int evaluation = 0;
        int opponentKingRow = opponentKingPos / 8;
        int opponentKingCol = opponentKingPos % 8;

        evaluation += opponentKingTable[opponentKingRow][opponentKingCol];

        // Position king closer to the opponent king
        int ownKingRow = ownKingPos / 8;
        int ownKingCol = ownKingPos % 8;

        int distanceToOpponentKing = Math.abs(ownKingRow - (opponentKingRow)) +
                Math.abs(ownKingCol - (opponentKingCol));

        // Reward for being closer to the opponent king
        evaluation += (7 - distanceToOpponentKing)*20;

        return  evaluation;
    }
}

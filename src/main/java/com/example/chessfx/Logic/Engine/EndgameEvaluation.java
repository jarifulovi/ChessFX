package com.example.chessfx.Logic.Engine;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Engine.BitBoard.Bitboard;

public class EndgameEvaluation {


    // Two rook and no other pieces so check-mate is inevitable
    public int twoRookOnlyEval(Bitboard bitboard,int point,int turn){
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
    private int forceKingToMoveOnEdge(int ownKingPos,int opponentKingPos){

        // Force opponent king to go to edges
        // Force ownking to cut of opponent king by getting closer
        int evaluation = 0;
        int opponentKingRow = opponentKingPos / 8;
        int opponentKingCol = opponentKingPos % 8;

        // Define the edges of the board
        boolean opponentKingOnEdge = (opponentKingRow == 0 || opponentKingRow == 7 ||
                opponentKingCol == 0 || opponentKingCol == 7);

        // Reward if the opponent's king is already on the edge
        if (opponentKingOnEdge) {
            evaluation += 1000;
        } else {
            // Encourage opponent king to move towards the edge
            int distanceToEdge = Math.min(opponentKingRow, 7 - opponentKingRow) + Math.min(opponentKingCol, 7 - opponentKingCol);

            // Penalize for being further from the edge
            evaluation -= distanceToEdge * 100;
        }

        // Position your king closer to the opponent king
        int ownKingRow = ownKingPos / 8;
        int ownKingCol = ownKingPos % 8;

        // Calculate the Manhattan distance between your king and the opponent king
        int distanceToOpponentKing = Math.abs(ownKingRow - (opponentKingRow)) +
                Math.abs(ownKingCol - (opponentKingCol));

        // Reward for being closer to the opponent king
        evaluation += (7 - distanceToOpponentKing)*50; // Adjust the scale as needed

        return  evaluation;
    }
}

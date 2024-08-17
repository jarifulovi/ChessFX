package com.example.chessfx.Logic.Engine.BitBoard;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Move;

import java.util.ArrayList;
import java.util.List;

public class KingLogic {

    private final List<Integer>[] kingMovesTable = new ArrayList[64];

    public KingLogic() {
        initKingMoveTable();
    }

    private void initKingMoveTable() {
        for (int i = 0; i < 64; i++) {
            kingMovesTable[i] = new ArrayList<>();

            int[] rowOffsets = {0, 0, 1, 1, 1, -1, -1, -1};
            int[] colOffsets = {1, -1, 1, -1, 0, 1, -1, 0};

            int row = i / 8;
            int col = i % 8;
            for (int j = 0; j < 8; j++) {
                int newRow = row + rowOffsets[j];
                int newCol = col + colOffsets[j];

                if (logic.isWithinBoard(newRow, newCol)) {
                    int newIndex = newRow * 8 + newCol;
                    kingMovesTable[i].add(newIndex);
                }
            }
        }
    }

    public List<Move> getKingMoves(Bitboard bitboard, int turn) {

        List<Move> moves = new ArrayList<>();
        int piece;
        long bitBoardForKing;
        long allOwnPiece;
        if (turn == logic.WHITE) {
            piece = logic.W_KING;
            bitBoardForKing = bitboard.bitboards[logic.W_KING];
            allOwnPiece = bitboard.allWhitePieces;
        } else {
            piece = logic.B_KING;
            bitBoardForKing = bitboard.bitboards[logic.B_KING];
            allOwnPiece = bitboard.allBlackPieces;
        }


        int kingPosition = Long.numberOfTrailingZeros(bitBoardForKing);
        List<Integer> potentialMoves = kingMovesTable[kingPosition];

        for (int newIndex : potentialMoves) {
            //System.out.println("Row : "+newIndex/8 + " col : "+newIndex%8);
            if (!logic.isSquareOccupied(allOwnPiece, newIndex)) {
                moves.add(new Move(kingPosition, newIndex, piece));
            }
        }

        int rightCastleIndex = kingPosition + 2;
        int leftCastleIndex = kingPosition - 2;
        // No need to check boundary
        // Because the boolean will reset to false when a king moves
        // Thus the king will be always at the initial position
        if (canCastle(bitboard, true, kingPosition / 8, turn)) {
            moves.add(new Move(kingPosition, rightCastleIndex, piece));
        }
        if (canCastle(bitboard, false, kingPosition / 8, turn)) {
            moves.add(new Move(kingPosition, leftCastleIndex, piece));
        }
        return moves;
    }

    private boolean canCastle(Bitboard bitboard,boolean isRight,int row,int turn){

        boolean noPiece;

        if (isRight) {
            // Check squares at (row, 5) and (row, 6) for kingside castling
            noPiece = !logic.isSquareOccupied(bitboard.allPieces, row * 8 + 5) &&
                    !logic.isSquareOccupied(bitboard.allPieces, row * 8 + 6);
        } else {
            // Check squares at (row, 1), (row, 2), and (row, 3) for queenside castling
            noPiece = !logic.isSquareOccupied(bitboard.allPieces, row * 8 + 1) &&
                    !logic.isSquareOccupied(bitboard.allPieces, row * 8 + 2) &&
                    !logic.isSquareOccupied(bitboard.allPieces, row * 8 + 3);
        }

        if(turn==logic.WHITE){
            if(isRight) return bitboard.whiteKingSideCastle && noPiece;
            else        return bitboard.whiteQueenSideCastle && noPiece;
        }
        else {
            if(isRight) return bitboard.blackKingSideCastle && noPiece;
            else        return bitboard.blackQueenSideCastle && noPiece;
        }
    }

}

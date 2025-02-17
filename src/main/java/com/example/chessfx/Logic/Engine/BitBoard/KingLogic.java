package com.example.chessfx.Logic.Engine.BitBoard;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Move;

import java.util.ArrayList;
import java.util.List;

public class KingLogic {

    @SuppressWarnings("unchecked")
    private final List<Integer>[] kingMovesTable = new ArrayList[64];
    public static final long[] kingAttackTable = {0x0000000000000302L, 0x0000000000000705L, 0x0000000000000E0AL, 0x0000000000001C14L, 0x0000000000003828L, 0x0000000000007050L, 0x000000000000E0A0L, 0x000000000000C040L,
            0x0000000000030203L, 0x0000000000070507L, 0x00000000000E0A0EL, 0x00000000001C141CL, 0x0000000000382838L, 0x0000000000705070L, 0x0000000000E0A0E0L, 0x0000000000C040C0L,
            0x0000000003020300L, 0x0000000007050700L, 0x000000000E0A0E00L, 0x000000001C141C00L, 0x0000000038283800L, 0x0000000070507000L, 0x00000000E0A0E000L, 0x00000000C040C000L,
            0x0000000302030000L, 0x0000000705070000L, 0x0000000E0A0E0000L, 0x0000001C141C0000L, 0x0000003828380000L, 0x0000007050700000L, 0x000000E0A0E00000L, 0x000000C040C00000L,
            0x0000030203000000L, 0x0000070507000000L, 0x00000E0A0E000000L, 0x00001C141C000000L, 0x0000382838000000L, 0x0000705070000000L, 0x0000E0A0E0000000L, 0x0000C040C0000000L,
            0x0003020300000000L, 0x0007050700000000L, 0x000E0A0E00000000L, 0x001C141C00000000L, 0x0038283800000000L, 0x0070507000000000L, 0x00E0A0E000000000L, 0x00C040C000000000L,
            0x0302030000000000L, 0x0705070000000000L, 0x0E0A0E0000000000L, 0x1C141C0000000000L, 0x3828380000000000L, 0x7050700000000000L, 0xE0A0E00000000000L, 0xC040C00000000000L,
            0x0203000000000000L, 0x0507000000000000L, 0x0A0E000000000000L, 0x141C000000000000L, 0x2838000000000000L, 0x5070000000000000L, 0xA0E0000000000000L, 0x40C0000000000000L,
    };

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

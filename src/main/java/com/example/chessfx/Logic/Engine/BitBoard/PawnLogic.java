package com.example.chessfx.Logic.Engine.BitBoard;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PawnLogic {

    public static final long[] whitePawnAttackTable = {0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L,
            0x0000000000000002L, 0x0000000000000005L, 0x000000000000000AL, 0x0000000000000014L, 0x0000000000000028L, 0x0000000000000050L, 0x00000000000000A0L, 0x0000000000000040L,
            0x0000000000000200L, 0x0000000000000500L, 0x0000000000000A00L, 0x0000000000001400L, 0x0000000000002800L, 0x0000000000005000L, 0x000000000000A000L, 0x0000000000004000L,
            0x0000000000020000L, 0x0000000000050000L, 0x00000000000A0000L, 0x0000000000140000L, 0x0000000000280000L, 0x0000000000500000L, 0x0000000000A00000L, 0x0000000000400000L,
            0x0000000002000000L, 0x0000000005000000L, 0x000000000A000000L, 0x0000000014000000L, 0x0000000028000000L, 0x0000000050000000L, 0x00000000A0000000L, 0x0000000040000000L,
            0x0000000200000000L, 0x0000000500000000L, 0x0000000A00000000L, 0x0000001400000000L, 0x0000002800000000L, 0x0000005000000000L, 0x000000A000000000L, 0x0000004000000000L,
            0x0000020000000000L, 0x0000050000000000L, 0x00000A0000000000L, 0x0000140000000000L, 0x0000280000000000L, 0x0000500000000000L, 0x0000A00000000000L, 0x0000400000000000L,
            0x0002000000000000L, 0x0005000000000000L, 0x000A000000000000L, 0x0014000000000000L, 0x0028000000000000L, 0x0050000000000000L, 0x00A0000000000000L, 0x0040000000000000L,
    };
    public static final long[] blackPawnAttackTable = {0x0000000000000200L, 0x0000000000000500L, 0x0000000000000A00L, 0x0000000000001400L, 0x0000000000002800L, 0x0000000000005000L, 0x000000000000A000L, 0x0000000000004000L,
            0x0000000000020000L, 0x0000000000050000L, 0x00000000000A0000L, 0x0000000000140000L, 0x0000000000280000L, 0x0000000000500000L, 0x0000000000A00000L, 0x0000000000400000L,
            0x0000000002000000L, 0x0000000005000000L, 0x000000000A000000L, 0x0000000014000000L, 0x0000000028000000L, 0x0000000050000000L, 0x00000000A0000000L, 0x0000000040000000L,
            0x0000000200000000L, 0x0000000500000000L, 0x0000000A00000000L, 0x0000001400000000L, 0x0000002800000000L, 0x0000005000000000L, 0x000000A000000000L, 0x0000004000000000L,
            0x0000020000000000L, 0x0000050000000000L, 0x00000A0000000000L, 0x0000140000000000L, 0x0000280000000000L, 0x0000500000000000L, 0x0000A00000000000L, 0x0000400000000000L,
            0x0002000000000000L, 0x0005000000000000L, 0x000A000000000000L, 0x0014000000000000L, 0x0028000000000000L, 0x0050000000000000L, 0x00A0000000000000L, 0x0040000000000000L,
            0x0200000000000000L, 0x0500000000000000L, 0x0A00000000000000L, 0x1400000000000000L, 0x2800000000000000L, 0x5000000000000000L, 0xA000000000000000L, 0x4000000000000000L,
            0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L,
    };

    public PawnLogic(){

    }
    public static void main(String[] args) {

        PawnLogic pawnLogic = new PawnLogic();
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Enter an index : ");
            int i = scanner.nextInt();
            long attack = whitePawnAttackTable[i];
            while(attack != 0) {
                int index = Long.numberOfTrailingZeros(attack);
                System.out.println("Row : " + index / 8 + " Col : " + index % 8);
                attack &= ~(1L << index);
            }
        }
    }

    public List<Move> getPawnMoves(Bitboard bitboard, int turn){
        List<Move> moves = new ArrayList<>();
        int piece;
        long bitboardForPawns;
        int direction;
        if(turn == logic.WHITE){
            piece = logic.W_PAWN;
            bitboardForPawns = bitboard.bitboards[logic.W_PAWN];
            direction = -8;  // White moves up
        }
        else   {
            piece = logic.B_PAWN;
            bitboardForPawns = bitboard.bitboards[logic.B_PAWN];
            direction = 8; // Black moves down
        }

        while(bitboardForPawns != 0){
            int i = Long.numberOfTrailingZeros(bitboardForPawns);
            bitboardForPawns &= ~(1L << i);

            int row = i / 8;
            int col = i % 8;
            // Generate forward move ( 1 square )
            int newIndex = i + direction;
            if(logic.isWithinBoard(newIndex)){
                // Empty square
                if(logic.isSquareEmpty(bitboard.bitboards,newIndex)){
                    // Pawn promotion
                    if(newIndex/8 == 0 || newIndex/8 == 7){
                        int promotedPiece = (turn==logic.WHITE)?logic.W_QUEEN:logic.B_QUEEN;
                        moves.add(new Move(i,newIndex,piece,promotedPiece));
                    }
                    else
                        moves.add(new Move(i,newIndex,piece));

                    // Check for initial two-square move
                    if((turn==logic.WHITE && (i/8) == 6) || (turn==logic.BLACK && (i/8) == 1)){
                        newIndex = i + 2 * direction;
                        if(logic.isWithinBoard(newIndex) && logic.isSquareEmpty(bitboard.bitboards,newIndex)){
                            // Emtpy square
                            moves.add(new Move(i,newIndex,piece));
                        }
                    }
                }
            }
            // Generate capture moves
            for(int dc = -1; dc <= 1; dc+= 2){
                newIndex = i + dc + direction;
                // Capture boundary checking needs row,col
                if(logic.isWithinBoard(row + dc,col + dc)){
                    long targetBitboard = (turn==logic.WHITE) ? bitboard.allBlackPieces : bitboard.allWhitePieces;
                    if(logic.isSquareOccupied(targetBitboard,newIndex) || isEnPassant(bitboard,newIndex)){
                        // Capture move
                        if(newIndex/8 == 0 || newIndex/8 == 7){
                            int promotedPiece = (turn==logic.WHITE)?logic.W_QUEEN:logic.B_QUEEN;
                            moves.add(new Move(i,newIndex,piece,promotedPiece));
                        }
                        else
                            moves.add(new Move(i,newIndex,piece));
                    }
                }
            }
        }
        return moves;
    }

    // En passant position is where the capture will occur
    private boolean isEnPassant(Bitboard bitboard, int index) {
        return bitboard.enPassantIndex == index;
    }

}

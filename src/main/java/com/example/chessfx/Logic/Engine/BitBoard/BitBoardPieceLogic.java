package com.example.chessfx.Logic.Engine.BitBoard;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Move;

import java.util.ArrayList;
import java.util.List;

public class BitBoardPieceLogic {

    private KnightLogic knightLogic;
    private KingLogic kingLogic;
    private RookLogic rookLogic;
    private BishopLogic bishopLogic;
    private QueenLogic queenLogic;

    public BitBoardPieceLogic(){
        this.knightLogic = new KnightLogic();
        this.kingLogic = new KingLogic();
        this.rookLogic = new RookLogic();
        this.bishopLogic = new BishopLogic();
        this.queenLogic = new QueenLogic();
    }
    public List<Move> generateValidMove(Bitboard bitboard,int piece){

        List<Move> validMoves = new ArrayList<>();
        int turn = logic.getPieceColor(piece);

        if(piece == logic.W_PAWN || piece == logic.B_PAWN){
            return validPawnMoves(bitboard,turn);
        }
        else if(piece == logic.W_KNIGHT || piece == logic.B_KNIGHT){
            return knightLogic.getKnightMoves(bitboard,turn);
        }
        else if(piece == logic.W_BISHOP || piece == logic.B_BISHOP){
            return bishopLogic.getBishopMoves(bitboard,turn);
        }
        else if(piece == logic.W_ROOK || piece == logic.B_ROOK){
            return rookLogic.getRookMoves(bitboard,turn);
        }
        else if(piece == logic.W_QUEEN || piece == logic.B_QUEEN){
            return queenLogic.getQueenMoves(bitboard,turn);
        }
        else if(piece == logic.W_KING || piece == logic.B_KING){
            return kingLogic.getKingMoves(bitboard,turn);
        }

        return validMoves;
    }
    private List<Move> validPawnMoves(Bitboard bitboard,int turn){
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

            // Generate forward move ( 1 square )
            int newIndex = i + direction;
            if(logic.isWithinBoard(newIndex)){
                if(logic.isSquareEmpty(bitboard.bitboards,newIndex)){
                    // Empty square
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
                if(logic.isWithinBoard(newIndex/8,newIndex%8)){
                    long targetBitboard = (turn==logic.WHITE) ? bitboard.allBlackPieces : bitboard.allWhitePieces;
                    if(logic.isSquareOccupied(targetBitboard,newIndex) || isEnPassant(bitboard,newIndex)){
                        // Capture move
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

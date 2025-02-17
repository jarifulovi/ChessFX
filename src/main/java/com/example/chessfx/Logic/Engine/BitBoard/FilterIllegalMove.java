package com.example.chessfx.Logic.Engine.BitBoard;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Move;

import java.util.ArrayList;
import java.util.List;

public class FilterIllegalMove {

    public List<Move> filterIllegalMove(Bitboard bitboard,List<Move> moves,int turn){
        List<Move> filteredMoves = new ArrayList<>();
        for(Move move : moves){
            if(!isSelfCheckAfterMove(bitboard,move,turn)){
                filteredMoves.add(move);
            }
        }
        return filteredMoves;
    }
    public boolean isSelfCheckAfterMove(Bitboard bitboard, Move move,int turn){

        Bitboard tempBitBoard = new Bitboard(bitboard);
        // Castling check
        if(move.piece == logic.W_KING || move.piece == logic.B_KING){
            // This works because king's only 2 square move is castling
            if(Math.abs(move.preIndex-move.newIndex) == 2){
                return checkCastleIllegal(tempBitBoard,move,turn);
            }
        }
        // En passant capture
        if(move.piece == logic.W_PAWN || move.piece == logic.B_PAWN){
            // This works because move is valid pawn move
            if(Math.abs(move.preIndex-move.newIndex)==7 || Math.abs(move.preIndex-move.newIndex)==9){
                if(logic.isSquareOccupied(tempBitBoard.bitboards[logic.NO_PIECE],move.newIndex)){
                    int direction = (turn == logic.WHITE) ? 8 : -8;
                    int capturePiece = (turn == logic.WHITE) ? logic.B_PAWN : logic.W_PAWN;
                    int index = move.newIndex + direction;
                    // remove pawn from index
                    long mask = ~(1L << index);
                    tempBitBoard.bitboards[capturePiece] &= mask;
                }
            }
        }
        // No need to check pawn promotion because of pin
        // Update bitboard
        int oldIndex = move.preIndex;
        int newIndex = move.newIndex;

        // Change the piece position
        tempBitBoard.bitboards[move.piece]  &= ~(1L << oldIndex);
        tempBitBoard.bitboards[move.piece]  |=  (1L << newIndex);
        // Check for a capture and remove the captured piece
        int startIndex,endIndex;
        if(turn==logic.WHITE){
            startIndex = 7;     // Black piece is capture piece
            endIndex = Bitboard.NUM_PIECE_TYPES;
        }
        else {
            startIndex = 1;    // White piece is capture piece
            endIndex = 7;      // Exclusive
        }
        for (int i = startIndex; i < endIndex; i++) {
            if (logic.isSquareOccupied(tempBitBoard.bitboards[i],newIndex)) {
                tempBitBoard.bitboards[i] &= ~(1L << newIndex);
                break; // Exit after removing the captured piece
            }
        }
        // Check is king in check
        tempBitBoard.updateAllPieceBoards();
        return isKingInCheck(tempBitBoard,turn);
    }
    private boolean isKingInCheck(Bitboard bitboard,int turn){
        long bitBoardForKing;
        if(turn==logic.WHITE) bitBoardForKing = bitboard.bitboards[logic.W_KING];
        else                  bitBoardForKing = bitboard.bitboards[logic.B_KING];

        int kingPos = Long.numberOfTrailingZeros(bitBoardForKing);
        return GenerateAttackSquare.isSquareAttacked(bitboard,turn,kingPos);
    }
    private boolean checkCastleIllegal(Bitboard bitboard,Move move,int turn){

        int[] castlePositions = new int[3];
        // White king side castling
        if(move.newIndex == 62){
            castlePositions[0] = 60;
            castlePositions[1] = 61;
            castlePositions[2] = 62;
        }
        // White queen side castling
        else if(move.newIndex == 58){
            castlePositions[0] = 58;
            castlePositions[1] = 59;
            castlePositions[2] = 60;
        }
        // Black king side castling
        else if(move.newIndex == 6){
            castlePositions[0] = 4;
            castlePositions[1] = 5;
            castlePositions[2] = 6;
        }
        // Black queen side castling
        else {
            castlePositions[0] = 2;
            castlePositions[1] = 3;
            castlePositions[2] = 4;
        }

        long allAttackSquares = GenerateAttackSquare.getAllAttackSquare(bitboard,logic.getOpponentTurn(turn));
        // check king in check
        for(int index : castlePositions){
            if(logic.isSquareOccupied(allAttackSquares,index)) return true;
        }
        return false;
    }
}

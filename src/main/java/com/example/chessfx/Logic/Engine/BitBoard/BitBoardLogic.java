package com.example.chessfx.Logic.Engine.BitBoard;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Board;
import com.example.chessfx.Logic.Object.Move;

import java.util.ArrayList;
import java.util.List;

public class BitBoardLogic {
    private BitboardConverter bitboardConverter;
    private BitBoardPieceLogic bitBoardPieceLogic;
    private FilterIllegalMove filterIllegalMove;
    private int enPassantTurn;
    public BitBoardLogic(){
        bitBoardPieceLogic = new BitBoardPieceLogic();
        bitboardConverter = new BitboardConverter();
        filterIllegalMove = new FilterIllegalMove();
        this.enPassantTurn = logic.NO_EN_PASSANT;
    }
    public Bitboard getBitBoard(Board board,int player) {

        Board tempBoard = board.deepCopy();

        if (player == logic.WHITE) tempBoard.grid = logic.copyGrid(tempBoard.grid);
        else tempBoard.grid = logic.copyOpponentGrid(tempBoard.grid);

        return bitboardConverter.convert(tempBoard);
    }

    public List<Move> getSinglePieceMoves(Bitboard bitboard,int piece,int turn){
        List<Move> validMoves = new ArrayList<>();
        if(logic.isOwnPiece(piece,turn)){
            validMoves = bitBoardPieceLogic.generateValidMove(bitboard,piece);
            return filterIllegalMove.filterIllegalMove(bitboard,validMoves,turn);
        }
        return validMoves;
    }
    public List<Move> getAllPossibleMove(Bitboard bitboard,int turn){

        List<Move> allPossibleMoves = new ArrayList<>();

        int startIndex,endIndex;
        if(turn==logic.WHITE){
            startIndex = 1;     // White piece is ownPiece piece
            endIndex = 7;       // Exclusive
        }
        else {
            startIndex = 7;    // Black piece is ownPiece piece
            endIndex = Bitboard.NUM_PIECE_TYPES;
        }
        for(int piece = startIndex;piece < endIndex; piece++){
            if(bitboard.bitboards[piece] != 0){
                allPossibleMoves.addAll(bitBoardPieceLogic.generateValidMove(bitboard,piece));
            }
        }
        return filterIllegalMove.filterIllegalMove(bitboard,allPossibleMoves,turn);
    }

    public Bitboard simulateBitBoard(Bitboard bitboard,Move move){

        int turn = logic.getPieceColor(move.piece);
        Bitboard simulatedBitBoard = new Bitboard(bitboard);

        // Set and reset en passant ( also remove en passant capture piece )
        updateEnPassant(simulatedBitBoard,move,turn);

        int oldIndex = move.preIndex;
        int newIndex = move.newIndex;

        if(move.isPromotingPiece){
            simulatedBitBoard.bitboards[move.piece]  &= ~(1L << oldIndex);
            // Promoted piece will be according to engine
            // For now engine will always promote to a queen
            simulatedBitBoard.bitboards[move.piece]  &= ~(1L << oldIndex);
            simulatedBitBoard.bitboards[move.promotedPiece] |= (1L << newIndex);
        }
        else {
            simulatedBitBoard.bitboards[move.piece] &= ~(1L << oldIndex);
            simulatedBitBoard.bitboards[move.piece] |= (1L << newIndex);
        }

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
            if (logic.isSquareOccupied(simulatedBitBoard.bitboards[i],newIndex)) {
                simulatedBitBoard.bitboards[i] &= ~(1L << newIndex);
                break; // Exit after removing the captured piece
            }
        }

        // Update castling
        updateCastling(simulatedBitBoard,move,turn);
        simulatedBitBoard.updateAllPieceBoards();

        return simulatedBitBoard;
    }

    public void updateCastling(Bitboard bitboard,Move move,int turn){

        if(move.piece == logic.W_KING){
            bitboard.whiteQueenSideCastle = false;
            bitboard.whiteKingSideCastle = false;
        }
        else if(move.piece == logic.B_KING){
            bitboard.blackQueenSideCastle = false;
            bitboard.blackKingSideCastle = false;
        }
        else if(move.piece == logic.W_ROOK){
            if(move.preIndex%8 == 0) bitboard.whiteQueenSideCastle = false;
            else if(move.preIndex%8 == 7) bitboard.whiteKingSideCastle = false;
        }
        else if(move.piece == logic.B_ROOK){
            if(move.preIndex%8 == 0) bitboard.blackQueenSideCastle = false;
            else if(move.preIndex%8 == 7) bitboard.blackKingSideCastle = false;
        }
        // If captured
        if ((bitboard.bitboards[logic.W_ROOK] & (1L << 56)) == 0) {
            bitboard.whiteQueenSideCastle = false;
        }
        if ((bitboard.bitboards[logic.W_ROOK] & (1L << 63)) == 0) {
            bitboard.whiteKingSideCastle = false;
        }
        if ((bitboard.bitboards[logic.B_ROOK] & (1L)) == 0) {
            bitboard.blackQueenSideCastle = false;
        }
        if ((bitboard.bitboards[logic.B_ROOK] & (1L << 7)) == 0) {
            bitboard.blackKingSideCastle = false;
        }

        if(move.piece == logic.W_KING || move.piece == logic.B_KING){

            // This works because the king can only move twice at initial move
            if(Math.abs(move.preIndex - move.newIndex) == 2)
                updateRook(bitboard,move,turn);
        }
    }
    private void updateRook(Bitboard bitboard,Move move,int turn){
        // Only updates the rook position as the king position will be updated
        // in the bitboardLogic.update

        // Right side castle
        long rookBitboard = (turn == logic.WHITE) ? bitboard.bitboards[logic.W_ROOK] : bitboard.bitboards[logic.B_ROOK];
        int rookOldIndex, rookNewIndex;

        // White king side castling
        if(move.newIndex == 62){
            rookOldIndex = 63;
            rookNewIndex = 61;
        }
        // White queen side castling
        else if(move.newIndex == 58){
            rookOldIndex = 56;
            rookNewIndex = 59;
        }
        // Black king side castling
        else if(move.newIndex == 6){
            rookOldIndex = 7;
            rookNewIndex = 5;
        }
        // Black queen side castling
        else{
            rookOldIndex = 0;
            rookNewIndex = 3;
        }
        // Update the bitboard for the rook
        rookBitboard &= ~(1L << rookOldIndex); // Remove the rook from the old square
        rookBitboard |= (1L << rookNewIndex);  // Place the rook on the new square

        // Assign the updated rook bitboard back
        if (turn == logic.WHITE) {
            bitboard.bitboards[logic.W_ROOK] = rookBitboard;
        } else {
            bitboard.bitboards[logic.B_ROOK] = rookBitboard;
        }
        bitboard.updateAllPieceBoards();
    }
    // En passant position is where the capture will happen
    private void setEnpassant(Bitboard bitboard,int index,int turn){
        // If turn == white
        // then en passant pos is downward as pawn goes upward
        bitboard.enPassantIndex = (turn==logic.WHITE) ? index + 8 : index - 8;
        enPassantTurn = turn;
    }
    private void removeEnPassantPiece(Bitboard bitboard,int index,int turn){

        int capturedIndex = (turn == logic.WHITE) ? index + 8 : index - 8;

        // Clear the bit at the captured index
        long mask = ~(1L << capturedIndex); // Mask to clear the bit at capturedIndex

        if (turn == logic.WHITE) {
            // Remove the captured black pawn from the bitboard
            bitboard.bitboards[logic.B_PAWN] &= mask;
        } else {
            // Remove the captured white pawn from the bitboard
            bitboard.bitboards[logic.W_PAWN] &= mask;
        }
    }
    public void updateEnPassant(Bitboard bitboard,Move move,int turn){

        if(move.piece == logic.W_PAWN || move.piece == logic.B_PAWN){

            if(Math.abs(move.newIndex-move.preIndex) == 16){
                setEnpassant(bitboard,move.newIndex,turn);
            }
            // If en passant capture happens
            // When moves diagonally and new position doesn't have any  piece
            int preRow = move.preIndex / 8;
            int newRow = move.newIndex / 8;
            int preCol = move.preIndex % 8;
            int newCol = move.newIndex % 8;
            if(Math.abs(preRow-newRow)==Math.abs(preCol-newCol)){
                if(logic.isSquareEmpty(bitboard.bitboards,move.newIndex)){
                    removeEnPassantPiece(bitboard,move.newIndex,turn);
                }
            }
        }
        if(enPassantTurn != turn){
            bitboard.enPassantIndex = logic.NO_EN_PASSANT;
        }

    }
}

package com.example.chessfx.Logic.Engine.BitBoard;

import com.example.chessfx.Logic.Abstract.logic;

import java.util.Arrays;

public class Bitboard {
    // Bitboards for each piece type and color
    public static final int NUM_PIECE_TYPES = 13;
    public long[] bitboards;

    // Bitboard for all pieces
    public long allWhitePieces;
    public long allBlackPieces;
    public long allPieces;

    // En passant square bitboard
    public int enPassantIndex;

    // Castling rights
    public boolean whiteKingSideCastle;
    public boolean whiteQueenSideCastle;
    public boolean blackKingSideCastle;
    public boolean blackQueenSideCastle;
    public Bitboard() {
        bitboards = new long[NUM_PIECE_TYPES];
        reset();
    }
    public Bitboard(Bitboard bitboard){
        this.bitboards = new long[NUM_PIECE_TYPES];
        System.arraycopy(bitboard.bitboards, 0, this.bitboards, 0, NUM_PIECE_TYPES);
        this.allPieces = bitboard.allPieces;
        this.allWhitePieces = bitboard.allWhitePieces;
        this.allBlackPieces = bitboard.allBlackPieces;
        this.enPassantIndex = bitboard.enPassantIndex;
        this.whiteKingSideCastle = bitboard.whiteKingSideCastle;
        this.whiteQueenSideCastle = bitboard.whiteQueenSideCastle;
        this.blackKingSideCastle = bitboard.blackKingSideCastle;
        this.blackQueenSideCastle = bitboard.blackQueenSideCastle;
    }
    private void reset(){
        Arrays.fill(bitboards, 0L);

        allWhitePieces = 0L;
        allBlackPieces = 0L;
        allPieces = 0L;
        enPassantIndex = logic.NO_EN_PASSANT;

        whiteKingSideCastle = true;
        whiteQueenSideCastle = true;
        blackKingSideCastle = true;
        blackQueenSideCastle = true;
    }
    public void setBitboard(int pieceType, long bitboard){
        if(pieceType < 0 || pieceType >= NUM_PIECE_TYPES){
            throw  new IllegalArgumentException("Invalid piece type: "+pieceType);
        }
        bitboards[pieceType] = bitboard;
        updateAllPieceBoards();
    }
    public long getBitboard(int pieceType){
        if(pieceType < 0 || pieceType >= NUM_PIECE_TYPES){
            throw  new IllegalArgumentException("Invalid piece type: "+pieceType);
        }
        return bitboards[pieceType];
    }
    public void updateAllPieceBoards(){
        // Update allWhitePieces and allBlackPieces
        allWhitePieces = bitboards[1] | bitboards[2] | bitboards[3] | bitboards[4] | bitboards[5] | bitboards[6];
        allBlackPieces = bitboards[7] | bitboards[8] | bitboards[9] | bitboards[10] | bitboards[11] | bitboards[12];
        allPieces = allWhitePieces | allBlackPieces;
        bitboards[logic.NO_PIECE] = ~allPieces;
    }

}

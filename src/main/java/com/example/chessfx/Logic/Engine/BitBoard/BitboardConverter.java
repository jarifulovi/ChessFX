package com.example.chessfx.Logic.Engine.BitBoard;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Board;

public class BitboardConverter {

    public static final int BOARD_SIZE = 8;

    public Bitboard convert(Board board){
        Bitboard bitboard = new Bitboard();

        for(int row = 0;row < BOARD_SIZE ; row++){
            for(int col = 0;col < BOARD_SIZE ; col++){
                int piece = board.grid[row][col];
                if(piece != logic.NO_PIECE){
                    long bitPosition = 1L << (row * BOARD_SIZE + col);
                    bitboard.setBitboard(piece, bitboard.bitboards[piece] | bitPosition);
                }
            }
        }
        // Set castling info
        bitboard.whiteQueenSideCastle = board.canWhiteLeftCastled;
        bitboard.whiteKingSideCastle = board.canWhiteRightCastled;
        bitboard.blackQueenSideCastle = board.canBlackLeftCastled;
        bitboard.blackKingSideCastle = board.canBlackRightCastled;

        bitboard.enPassantIndex =  board.enPassantIndex;
        return bitboard;
    }
}

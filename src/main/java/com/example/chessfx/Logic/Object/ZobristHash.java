package com.example.chessfx.Logic.Object;
import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Engine.BitBoard.Bitboard;

import java.util.Random;

public class ZobristHash {

    private long[][][] zobristTable; // [Piece Type][Row][Column]
    private long[][] zobristTableBB; // [Piece Type][Index]
    private long zobristEnPassant;
    private long[] zobristCastlingRights;
    private long zobristTurnWhite,zobristTurnBlack;

    public ZobristHash() {
        initializeZobristTable();
    }

    // Initialize the Zobrist table with random bitstrings
    private void initializeZobristTable() {
        Random random = new Random();
        zobristTable = new long[13][8][8];
        zobristTableBB = new long[13][64];

        for (int piece = 0; piece <= 12; piece++) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    long value = random.nextLong();
                    zobristTable[piece][row][col] = value;
                    zobristTableBB[piece][row*8+col] = value;
                }
            }
        }

        // Random values for en passant (one for each column)
        zobristEnPassant = random.nextLong();

        // Random values for castling rights
        zobristCastlingRights = new long[4]; // White kingside, White queenside, Black kingside, Black queenside
        for (int i = 0; i < 4; i++) {
            zobristCastlingRights[i] = random.nextLong();
        }

        // Random value for the turn
        zobristTurnWhite = random.nextLong();
        zobristTurnBlack = random.nextLong();
    }

    // Calculate the initial Zobrist hash for the given board state
    public long calculateHashBoard(Board board) {
        long hash = 0L;

        // Hash the pieces on the board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int piece = board.grid[row][col];
                if (piece != logic.NO_PIECE) {
                    hash ^= zobristTable[piece][row][col];
                }
            }
        }

        // Hash the castling rights
        if (board.canWhiteRightCastled) hash ^= zobristCastlingRights[0];
        if (board.canWhiteLeftCastled) hash ^= zobristCastlingRights[1];
        if (board.canBlackRightCastled) hash ^= zobristCastlingRights[2];
        if (board.canBlackLeftCastled) hash ^= zobristCastlingRights[3];

        // Hash the en passant square if any
        if (board.enPassantIndex != logic.NO_EN_PASSANT) {
            hash ^= zobristEnPassant;
        }

        // Hash the turn
        if (board.currentTurn == logic.WHITE) {
            hash ^= zobristTurnWhite;
        } else {
            hash ^= zobristTurnBlack;
        }

        return hash;
    }

    public long calculateHashBitboard(Bitboard bitboard, int turn) {
        long hash = 0L;

        // Hash all pieces on the board
        for (int piece = 1; piece <= 12; piece++) {
            long pieceBitboard = bitboard.bitboards[piece];
            if (pieceBitboard != 0) {
                // Iterate over each bit in the bitboard
                while (pieceBitboard != 0) {
                    int i = Long.numberOfTrailingZeros(pieceBitboard);
                    pieceBitboard &= ~(1L << i);
                    hash ^= zobristTableBB[piece][i];
                }
            }
        }

        // Hash castling rights
        if (bitboard.whiteKingSideCastle) hash ^= zobristCastlingRights[0];
        if (bitboard.whiteQueenSideCastle) hash ^= zobristCastlingRights[1];
        if (bitboard.blackKingSideCastle) hash ^= zobristCastlingRights[2];
        if (bitboard.blackQueenSideCastle) hash ^= zobristCastlingRights[3];

        // Hash en passant
        if (bitboard.enPassantIndex != logic.NO_EN_PASSANT) {
            hash ^= zobristEnPassant;
        }

        // Hash turn
        if (turn == logic.WHITE) {
            hash ^= zobristTurnWhite;
        } else {
            hash ^= zobristTurnBlack;
        }

        return hash;
    }

}

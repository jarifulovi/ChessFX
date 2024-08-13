package com.example.chessfx.Logic.Object;
import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Board;
import com.example.chessfx.Logic.Object.Move;

import java.util.Random;

public class ZobristHash {

    private long[][][] zobristTable; // [Piece Type][Row][Column]
    private long zobristEnPassant;
    private long[] zobristCastlingRights;
    private long zobristTurnWhite,zobristTurnBlack;
    private long currentHash;

    public ZobristHash() {
        initializeZobristTable();
    }

    // Initialize the Zobrist table with random bitstrings
    private void initializeZobristTable() {
        Random random = new Random();
        zobristTable = new long[13][8][8]; // 13 because 12 pieces + NO_PIECE

        for (int piece = 0; piece <= 12; piece++) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    zobristTable[piece][row][col] = random.nextLong();
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
    public long calculateInitialHash(Board board) {
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
        if (board.enPassantIndex != -1) {
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


    // Method to update the Zobrist hash when a move is made
    public void updateHash(Move move, boolean isWhiteTurn,boolean isCapture,boolean isCastle) {
        // XOR out the piece from the old position
        currentHash ^= zobristTable[move.piece][move.preRow][move.preCol];

        // XOR in the piece at the new position
        currentHash ^= zobristTable[move.piece][move.newRow][move.newCol];


        // XOR the turn
        if(isWhiteTurn) currentHash ^= zobristTurnWhite;
        else            currentHash ^= zobristTurnBlack;
    }
}

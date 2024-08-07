package com.example.chessfx.Logic;

import com.example.chessfx.Logic.Abstract.logic;

public class Board {

    public int[][] grid;
    public int enPassantIndex;
    public boolean canWhiteLeftCastled, canWhiteRightCastled, canBlackLeftCastled, canBlackRightCastled;

    // This fields will be used in engine
    public int whitePieceValues,blackPieceValues;
    public Board(){
        this.grid = new int[8][8];
        enPassantIndex = logic.NO_EN_PASSANT;
        canWhiteLeftCastled = false;
        canWhiteRightCastled = false;
        canBlackLeftCastled = false;
        canBlackRightCastled = false;
        whitePieceValues = 3900;
        blackPieceValues = 3900;
    }
    // Method to create a deep copy of the Board object
    public Board deepCopy() {
        Board copy = new Board();

        // Copy the grid
        for (int i = 0; i < 8; i++) {
            System.arraycopy(this.grid[i], 0, copy.grid[i], 0, 8);
        }

        // Copy the rest of the attributes
        copy.enPassantIndex = this.enPassantIndex;
        copy.canWhiteLeftCastled = this.canWhiteLeftCastled;
        copy.canWhiteRightCastled = this.canWhiteRightCastled;
        copy.canBlackLeftCastled = this.canBlackLeftCastled;
        copy.canBlackRightCastled = this.canBlackRightCastled;
        copy.whitePieceValues = this.whitePieceValues;
        copy.blackPieceValues = this.blackPieceValues;

        return copy;
    }
}

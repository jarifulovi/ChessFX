package com.example.chessfx.Logic.Object;

import com.example.chessfx.Logic.Abstract.logic;

import java.util.Arrays;

public class Board {

    public int[][] grid;
    public int enPassantIndex;
    public boolean canWhiteLeftCastled, canWhiteRightCastled, canBlackLeftCastled, canBlackRightCastled;

    public int currentTurn;
    // This fields will be used in engine
    public int whitePieceValues,blackPieceValues;
    public int[] pieceCount = new int[13];
    public int gamePhase;
    public Board(){
        this.grid = new int[8][8];
        enPassantIndex = logic.NO_EN_PASSANT;
        canWhiteLeftCastled = false;
        canWhiteRightCastled = false;
        canBlackLeftCastled = false;
        canBlackRightCastled = false;
        currentTurn = logic.WHITE;
        whitePieceValues = 3900;
        blackPieceValues = 3900;

        gamePhase = logic.OPENING_PHASE;
        initPieceCount();
    }
    // This method will invoke before updating states
    public void update(Move move){
        currentTurn = logic.getPieceColor(move.piece);
        logic.updatePieceValues(this,move);
        updatePieceCount(move);
        if(this.gamePhase != logic.ENDGAME_PHASE && isEndGame()) this.gamePhase = logic.ENDGAME_PHASE;
    }
    public boolean isEndGame(){

        if(pieceCount[logic.W_QUEEN] > 0 || pieceCount[logic.B_QUEEN] > 0) return false;

        int totalWhiteMajorPiece = pieceCount[logic.W_KNIGHT]+pieceCount[logic.W_BISHOP]+
                pieceCount[logic.W_ROOK];
        int totalBlackMajorPiece = pieceCount[logic.B_KNIGHT]+pieceCount[logic.B_BISHOP]+
                pieceCount[logic.B_ROOK];

        return totalWhiteMajorPiece <= 1 && totalBlackMajorPiece <= 1;
    }
    public boolean hasGreatPieceMaterialAdvantage(int turn){
        int totalWhiteMajorPiece = pieceCount[logic.W_KNIGHT]+pieceCount[logic.W_BISHOP]+
                pieceCount[logic.W_ROOK] + pieceCount[logic.W_QUEEN];
        int totalBlackMajorPiece = pieceCount[logic.B_KNIGHT]+pieceCount[logic.B_BISHOP]+
                pieceCount[logic.B_ROOK] + pieceCount[logic.B_QUEEN];

        int pieceDiff = (totalWhiteMajorPiece-totalBlackMajorPiece);

        if(turn==logic.WHITE) return (pieceDiff >= 2) || ((pieceDiff == 1)&&(pieceCount[logic.W_QUEEN]>pieceCount[logic.B_QUEEN]));
        else
            return (-pieceDiff >= 2) || ((-pieceDiff == 1)&&(pieceCount[logic.B_QUEEN]>pieceCount[logic.W_QUEEN]));

    }
    private void updatePieceCount(Move move){

        if(move.isPromotingPiece){
            pieceCount[move.promotedPiece]++;
            pieceCount[move.piece]--;
        }
        else if(grid[move.newRow][move.newCol] != logic.NO_PIECE)
            pieceCount[grid[move.newRow][move.newCol]]--;
    }

    // Method to create a deep copy of the Board object
    public Board deepCopy() {
        Board copy = new Board();

        // Copy the grid
        for (int i = 0; i < 8; i++) {
            System.arraycopy(this.grid[i], 0, copy.grid[i], 0, 8);
        }
        System.arraycopy(this.pieceCount,0,copy.pieceCount,0,13);

        // Copy the rest of the attributes
        copy.enPassantIndex = this.enPassantIndex;
        copy.canWhiteLeftCastled = this.canWhiteLeftCastled;
        copy.canWhiteRightCastled = this.canWhiteRightCastled;
        copy.canBlackLeftCastled = this.canBlackLeftCastled;
        copy.canBlackRightCastled = this.canBlackRightCastled;
        copy.currentTurn = this.currentTurn;
        copy.whitePieceValues = this.whitePieceValues;
        copy.blackPieceValues = this.blackPieceValues;

        copy.gamePhase = this.gamePhase;

        return copy;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Board board = (Board) obj;
        return Arrays.deepEquals(grid, board.grid) &&
                enPassantIndex == board.enPassantIndex &&
                canWhiteLeftCastled == board.canWhiteLeftCastled &&
                canWhiteRightCastled == board.canWhiteRightCastled &&
                canBlackLeftCastled == board.canBlackLeftCastled &&
                canBlackRightCastled == board.canBlackRightCastled &&
                currentTurn == board.currentTurn &&
                whitePieceValues == board.whitePieceValues &&
                blackPieceValues == board.blackPieceValues &&
                gamePhase == board.gamePhase;
    }

    private void initPieceCount(){
        pieceCount[0] = -1;
        pieceCount[logic.W_PAWN] = 8;
        pieceCount[logic.W_KNIGHT] = 2;
        pieceCount[logic.W_BISHOP] = 2;
        pieceCount[logic.W_ROOK] = 2;
        pieceCount[logic.W_QUEEN] = 1;
        pieceCount[logic.W_KING] = 1;

        pieceCount[logic.B_PAWN] = 8;
        pieceCount[logic.B_KNIGHT] = 2;
        pieceCount[logic.B_BISHOP] = 2;
        pieceCount[logic.B_ROOK] = 2;
        pieceCount[logic.B_QUEEN] = 1;
        pieceCount[logic.B_KING] = 1;
    }
}

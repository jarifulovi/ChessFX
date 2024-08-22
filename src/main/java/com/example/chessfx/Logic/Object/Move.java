package com.example.chessfx.Logic.Object;

import com.example.chessfx.Logic.Abstract.logic;

public class Move {

    public int preRow;
    public int preCol;
    public int newRow;
    public int newCol;

    public int preIndex;
    public int newIndex;

    public int piece;
    public int capturePiece;
    public boolean isPromotingPiece;
    public int promotedPiece;

    public Move(){
        this.preRow = -1;
        this.preCol = -1;
        this.newRow = -1;
        this.newCol = -1;
        this.preIndex = -1;
        this.newIndex = -1;
        this.piece = logic.NO_PIECE;
        this.isPromotingPiece = false;
        this.promotedPiece = logic.NO_PIECE;
    }
    // Normal construstor for engine
    public Move(int preIndex,int newIndex,int piece){
        this.preIndex = preIndex;
        this.newIndex = newIndex;
        this.piece = piece;
    }
    // Pawn promotion constructor
    public Move(int preIndex,int newIndex,int piece,int promotedPiece){
        this.preIndex = preIndex;
        this.newIndex = newIndex;
        this.piece = piece;
        this.isPromotingPiece = true;
        this.promotedPiece = promotedPiece;
    }
    public Move(int preRow,int preCol,int newRow,int newCol,int piece){
        this.preRow = preRow;
        this.preCol = preCol;
        this.newRow = newRow;
        this.newCol = newCol;
        this.piece = piece;
    }
    public Move(Move move){
        this.preRow = move.preRow;
        this.preCol = move.preCol;
        this.newRow = move.newRow;
        this.newCol = move.newCol;
        this.piece = move.piece;
    }

    public boolean equals(Move move){
        if(move == this) return true;

        return this.preRow==move.preRow && this.preCol==move.preCol && this.newRow==move.newRow
                && this.newCol==move.newCol && this.piece==move.piece;

    }
}

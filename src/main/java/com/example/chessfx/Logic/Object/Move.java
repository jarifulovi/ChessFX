package com.example.chessfx.Logic.Object;

import com.example.chessfx.Logic.Abstract.logic;

public class Move {

    public int preRow;
    public int preCol;
    public int newRow;
    public int newCol;

    public int piece;
    public int capturePiece;
    public boolean isPromotingPiece;
    public int promotedPiece;

    public Move(){
        this.preRow = -1;
        this.preCol = -1;
        this.newRow = -1;
        this.newCol = -1;
        this.piece = logic.NO_PIECE;
        this.isPromotingPiece = false;
        this.promotedPiece = logic.NO_PIECE;
    }
    public Move(int preRow,int preCol,int newRow,int newCol){
        this.preRow = preRow;
        this.preCol = preCol;
        this.newRow = newRow;
        this.newCol = newCol;
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
    public Move(int preRow,int preCol,int newRow,int newCol,int piece,int capturePiece){
        this.preRow = preRow;
        this.preCol = preCol;
        this.newRow = newRow;
        this.newCol = newCol;
        this.piece = piece;
        this.capturePiece = capturePiece;
    }

    public boolean equals(Move move){
        if(move == this) return true;

        return this.preRow==move.preRow && this.preCol==move.preCol && this.newRow==move.newRow
                && this.newCol==move.newCol && this.piece==move.piece;

    }
}
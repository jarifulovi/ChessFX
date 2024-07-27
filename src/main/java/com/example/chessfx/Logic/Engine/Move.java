package com.example.chessfx.Logic.Engine;

public class Move {

    public int preRow;
    public int preCol;
    public int newRow;
    public int newCol;
    public int piece;
    public int capturePiece;
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
    public Move(int preRow,int preCol,int newRow,int newCol,int piece,int capturePiece){
        this.preRow = preRow;
        this.preCol = preCol;
        this.newRow = newRow;
        this.newCol = newCol;
        this.piece = piece;
        this.capturePiece = capturePiece;
    }
}

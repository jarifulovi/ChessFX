package com.example.chessfx.Logic.Engine.BitBoard;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Move;

import java.util.ArrayList;
import java.util.List;

public class BitBoardPieceLogic {

    private PawnLogic pawnLogic;
    private KnightLogic knightLogic;
    private KingLogic kingLogic;
    private RookLogic rookLogic;
    private BishopLogic bishopLogic;
    private QueenLogic queenLogic;

    public BitBoardPieceLogic(){
        this.pawnLogic = new PawnLogic();
        this.knightLogic = new KnightLogic();
        this.kingLogic = new KingLogic();
        this.rookLogic = new RookLogic();
        this.bishopLogic = new BishopLogic();
        this.queenLogic = new QueenLogic();
    }
    public List<Move> generateValidMove(Bitboard bitboard,int piece){

        List<Move> validMoves = new ArrayList<>();
        int turn = logic.getPieceColor(piece);

        if(piece == logic.W_PAWN || piece == logic.B_PAWN){
            return pawnLogic.getPawnMoves(bitboard,turn);
        }
        else if(piece == logic.W_KNIGHT || piece == logic.B_KNIGHT){
            return knightLogic.getKnightMoves(bitboard,turn);
        }
        else if(piece == logic.W_BISHOP || piece == logic.B_BISHOP){
            return bishopLogic.getBishopMoves(bitboard,turn);
        }
        else if(piece == logic.W_ROOK || piece == logic.B_ROOK){
            return rookLogic.getRookMoves(bitboard,turn);
        }
        else if(piece == logic.W_QUEEN || piece == logic.B_QUEEN){
            return queenLogic.getQueenMoves(bitboard,turn);
        }
        else if(piece == logic.W_KING || piece == logic.B_KING){
            return kingLogic.getKingMoves(bitboard,turn);
        }

        return validMoves;
    }

}

package com.example.chessfx.Logic.Engine;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Engine.BitBoard.Bitboard;

public class Evaluation {

    private PieceValueTable pieceValueTable;
    private EndgameEvaluation endgameEvaluation;
    public Evaluation(){
        pieceValueTable = new PieceValueTable();
        endgameEvaluation = new EndgameEvaluation();
    }

    public int getEvaluationBB(Bitboard bitboard,int turn){

        int point = 0;
        int ownPieceValue = 0,opponentPieceValue = 0;
        int[] pieceCount = getPieceCount(bitboard);
        boolean isEndgame = isEndGame(pieceCount);


        for(int piece = 1; piece < Bitboard.NUM_PIECE_TYPES; piece++){
            long position = bitboard.bitboards[piece];
            int pieceValue = logic.getPieceValue(piece);
            while(position != 0){
                int i = Long.numberOfTrailingZeros(position);
                position &= ~(1L << i);

                if(logic.isOwnPiece(piece,turn)){
                    ownPieceValue += pieceValue;
                    if(!isEndgame)
                        point  += (pieceValueTable.openingPositionalValueBB[piece][i] + pieceValue);
                    else
                        point  += (pieceValueTable.endGamePositionalValueBB[piece][i] + pieceValue);
                }
                else {
                    opponentPieceValue += pieceValue;
                    if(!isEndgame)
                        point -= (pieceValueTable.openingPositionalValueBB[piece][i] + pieceValue);
                    else
                        point -= (pieceValueTable.endGamePositionalValueBB[piece][i] + pieceValue);
                }
            }
        }

        // If endgame specific eval needs
        if(isEndgame){
            return getEndGameEval(bitboard,pieceCount,point,turn);
        }
        return point + getRatioValue(ownPieceValue,opponentPieceValue);
    }

    private int getRatioValue(int ownPieceValue,int opponentPieceValue){
        double totalValue = ownPieceValue + opponentPieceValue;
        double ratio = Math.abs(ownPieceValue - opponentPieceValue) / totalValue;
        return (int)(ratio * 100);
    }
    private int[] getPieceCount(Bitboard bitboard){
        int[] pieceCount = new int[13];
        for(int piece = 1; piece < Bitboard.NUM_PIECE_TYPES; piece++){
            long bitboardForPiece = bitboard.bitboards[piece];
            while(bitboardForPiece != 0){
                int index = Long.numberOfTrailingZeros(bitboardForPiece);
                pieceCount[piece]++;
                bitboardForPiece &= ~(1L << index);
            }
        }
        return pieceCount;
    }
    private boolean isEndGame(int[] pc){

        boolean noQueen = (pc[logic.W_QUEEN] == 0 && pc[logic.B_QUEEN] == 0);
        int majorWhitePiece = pc[logic.W_KNIGHT] + pc[logic.W_BISHOP] + pc[logic.W_ROOK];
        int majorBlackPiece = pc[logic.B_KNIGHT] + pc[logic.B_BISHOP] + pc[logic.B_ROOK];
        return noQueen && (majorWhitePiece <= 2 && majorBlackPiece <= 2) ||
                (!noQueen && (majorWhitePiece+majorBlackPiece)==0);
    }
    private int getEndGameEval(Bitboard bitboard,int[] pc,int point,int turn){

        // Efficiency is not bottleneck as the total piece is less
        int totalMajorWhitePiece = pc[logic.W_KNIGHT]+pc[logic.W_BISHOP]+pc[logic.W_ROOK];
        int totalMajorBlackPiece = pc[logic.B_KNIGHT]+pc[logic.B_BISHOP]+pc[logic.B_ROOK];
        int totalMajorPiece = totalMajorWhitePiece+totalMajorBlackPiece;
        int totalKnightAndBishop = totalMajorPiece - pc[logic.W_ROOK] - pc[logic.B_ROOK];
        int totalPawns = pc[logic.W_PAWN] + pc[logic.B_PAWN];

        boolean noPawn = (totalPawns) == 0;
        boolean noQueen = (pc[logic.W_QUEEN]+pc[logic.B_QUEEN]==0);
        boolean sameQueenCount = pc[logic.W_QUEEN] == pc[logic.B_QUEEN];
        boolean rookAdvantage = (pc[logic.W_ROOK]>0 && pc[logic.B_ROOK]==0) || (pc[logic.B_ROOK]>0 && pc[logic.W_ROOK]==0);
        // rook only endgame
        if(noPawn && noQueen && rookAdvantage && totalKnightAndBishop==0){
            return endgameEvaluation.rookOnlyEndgame(bitboard,point,turn);
        }
        // Has issue with queen advantage
        // king closer functionality not working
        else if(noPawn && !noQueen && totalMajorPiece==0){
            if(sameQueenCount) return point;
            else               return endgameEvaluation.queenOnlyEndgame(bitboard,point,turn);
        }
        // will implement letter
        return point;
    }

}

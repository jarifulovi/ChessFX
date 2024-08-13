package com.example.chessfx.Logic.Engine;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Board;

public class Evaluation {

    private PieceValueTable pieceValueTable;
    private int enginePlayer;
    public Evaluation(int enginePlayer){
        this.enginePlayer = enginePlayer;
        // EnginePlayer is the opponent for the player
        pieceValueTable = new PieceValueTable(logic.getOpponentTurn(enginePlayer));
    }

    public int getEvaluation(Board board,int turn){

        int pieceValue = (board.whitePieceValues-board.blackPieceValues);
        boolean hasGreatPieceMaterialAdvantage = board.hasGreatPieceMaterialAdvantage(turn);
        int piecePositionalValue = pieceValueTable.getPiecePositionalValue(hasGreatPieceMaterialAdvantage, board.isEndGame(), board.grid,turn);

        return (turn==logic.WHITE) ? pieceValue + piecePositionalValue:
                   -pieceValue + piecePositionalValue;
    }
}

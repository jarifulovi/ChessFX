package com.example.chessfx.Logic.Engine;

import com.example.chessfx.Logic.Engine.BitBoard.BitBoardLogic;
import com.example.chessfx.Logic.Engine.BitBoard.Bitboard;
import com.example.chessfx.Logic.Engine.BitBoard.GenerateAttackSquare;
import com.example.chessfx.Logic.Object.Board;
import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Move;
import com.example.chessfx.Logic.Object.ZobristHash;

import java.util.*;


public class Engine {

    private Evaluation evaluation;
    private BitBoardLogic bitBoardLogic;
    private ZobristHash zobristHash;
    private Map<Long,Integer> bitboardHash;
    boolean isDebugged = true;
    int maxDepth = 4;
    int lines = 0,tpLines = 0,qsLines = 0,qtpLines = 0,possibleMoveCount = 0;
    public Engine(){
        this.bitBoardLogic = new BitBoardLogic();
        this.evaluation = new Evaluation();
        this.zobristHash = new ZobristHash();
        this.bitboardHash = new HashMap<>();
    }
    public Move bestMove(Board board,int turn, int player){
        Bitboard tempBitboard = bitBoardLogic.getBitBoard(board,player);
        long hash = zobristHash.calculateHashBitboard(tempBitboard,turn);
        bitboardHash.put(hash,bitboardHash.getOrDefault(hash,0)+1);
        int bestValue = Integer.MIN_VALUE;
        lines = 0;
        tpLines = 0;
        Move bestMove = null;

        List<Move> possibleMoves = bitBoardLogic.getAllPossibleMove(tempBitboard,turn);
        possibleMoveCount++;
        possibleMoves = orderMovesByEvaluation(possibleMoves,tempBitboard,turn);

        for(Move move : possibleMoves){
            Bitboard simulatedBitboard = bitBoardLogic.simulateBitBoard(tempBitboard,move);
            int moveValue = alphaBeta(simulatedBitboard, maxDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false, logic.getOpponentTurn(turn),player);
            System.out.println(move.preIndex+" "+move.newIndex+" value : "+moveValue);
            if (moveValue >= bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
        }
//        System.out.println("Number of nodes : "+lines);
//        System.out.println("Number of tp nodes : "+tpLines);
//        System.out.println("Number of qs nodes : "+qsLines);
//        System.out.println("Number of q tp nodes : "+qtpLines);
//        System.out.println("Number of p mc : "+possibleMoveCount);
        if(bestMove != null) {
            if(player == logic.WHITE) {
                bestMove.preRow = bestMove.preIndex / 8;
                bestMove.preCol = bestMove.preIndex % 8;
                bestMove.newRow = bestMove.newIndex / 8;
                bestMove.newCol = bestMove.newIndex % 8;
            }
            else {
                bestMove.preRow = (63 - bestMove.preIndex) / 8;
                bestMove.preCol = (63 - bestMove.preIndex) % 8;
                bestMove.newRow = (63 - bestMove.newIndex) / 8;
                bestMove.newCol = (63 - bestMove.newIndex) % 8;
            }
        }
        return bestMove;
    }

    private TranspositionTable transpositionTable = new TranspositionTable();
    public int alphaBeta(Bitboard bitboard, int depth, int alpha, int beta, boolean maximizingPlayer, int turn,int player) {

        // Check the transposition table
        long hash = zobristHash.calculateHashBitboard(bitboard,turn);
        bitboardHash.put(hash,bitboardHash.getOrDefault(hash,0)+1);
        // Check threefold repition
        if (bitboardHash.get(hash) >= 3) {
            bitboardHash.put(hash, bitboardHash.get(hash) - 1);
            return 0;
        }

        TranspositionTable.TTEntry transpositionEntry = transpositionTable.get(hash, depth);
        if (transpositionEntry != null) {
            tpLines++;
            switch (transpositionEntry.getNodeType()) {
                case EXACT:
                    bitboardHash.put(hash,bitboardHash.getOrDefault(hash,1)-1);
                    return transpositionEntry.getValue();
                case ALPHA:
                    if (transpositionEntry.getValue() <= alpha){
                        bitboardHash.put(hash,bitboardHash.getOrDefault(hash,1)-1);
                        return alpha;
                    }
                    break;
                case BETA:
                    if (transpositionEntry.getValue() >= beta){
                        bitboardHash.put(hash,bitboardHash.getOrDefault(hash,1)-1);
                        return beta;
                    }
                    break;
            }
        }
        // Base case: if depth is 0
        lines++;
        if (depth == 0) {
            bitboardHash.put(hash,bitboardHash.getOrDefault(hash,1)-1);
            return evaluation.getEvaluationBB(bitboard,turn);
        }
        List<Move> possibleMoves = bitBoardLogic.getAllPossibleMove(bitboard,turn);
        possibleMoveCount++;

        // If there are no possible moves, check for checkmate or stalemate
        if (possibleMoves.isEmpty()) {
            // Check for checkmate
            bitboardHash.put(hash,bitboardHash.getOrDefault(hash,1)-1);
            if (GenerateAttackSquare.isOwnKingAttacked(bitboard,turn)) {
                return maximizingPlayer ? (Integer.MIN_VALUE/maxDepth)*depth : (Integer.MAX_VALUE/maxDepth)*depth;
            }
            // Check for stalemate
            else {
                return 0; // Draw scenario
            }
        }
        possibleMoves = orderMovesByEvaluation(possibleMoves,bitboard,turn);

        int originalAlpha = alpha;
        int value;
        if (maximizingPlayer) {
            value = Integer.MIN_VALUE;
            for (Move move : possibleMoves) {
                Bitboard simulatedBitboard = bitBoardLogic.simulateBitBoard(bitboard,move);
                int eval = alphaBeta(simulatedBitboard, depth - 1, alpha, beta, false, logic.getOpponentTurn(turn),player);
                value = Math.max(value, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break; // Beta cut-off
                }
            }
        } else {
            value = Integer.MAX_VALUE;
            for (Move move : possibleMoves) {
                Bitboard simulatedBitboard = bitBoardLogic.simulateBitBoard(bitboard,move);
                int eval = alphaBeta(simulatedBitboard, depth - 1, alpha, beta, true, logic.getOpponentTurn(turn),player);
                value = Math.min(value, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break; // Alpha cut-off
                }
            }
        }
        // Store the evaluated value in the transposition table
        TranspositionTable.NodeType nodeType;
        if (value <= originalAlpha) {
            nodeType = TranspositionTable.NodeType.ALPHA;
        } else if (value >= beta) {
            nodeType = TranspositionTable.NodeType.BETA;
        } else {
            nodeType = TranspositionTable.NodeType.EXACT;
        }
        transpositionTable.put(hash, value, depth, nodeType);
        bitboardHash.put(hash,bitboardHash.getOrDefault(hash,1)-1);
        return value;
    }

    public List<Move> orderMovesByEvaluation(List<Move> moves,Bitboard bitboard,int turn) {

        if(moves.size() <= 1) return moves;
        List<Integer> scores = new ArrayList<>();
        List<Move> moveList = new ArrayList<>();

        for (Move move : moves) {
            Bitboard simulatedBitboard = bitBoardLogic.simulateBitBoard(bitboard,move);
            int value = evaluation.getEvaluationBB(simulatedBitboard,turn);
            scores.add(value);
            moveList.add(move);
        }

        // Combine scores and moves into a list of indices
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            indices.add(i);
        }

        // Sort indices based on the corresponding scores
        indices.sort((i1, i2) -> Integer.compare(scores.get(i2), scores.get(i1)));

        // Create a new ordered list of moves based on sorted indices
        List<Move> orderedMoves = new ArrayList<>();
        for (int index : indices) {
            orderedMoves.add(moveList.get(index));
        }

        return orderedMoves;
    }

    public int getPawnPromotedPiece(int turn){


        int[] pieces = new int[4];
        if(turn == logic.WHITE){
            pieces[0] = logic.W_QUEEN;
            pieces[1] = logic.W_ROOK;
            pieces[2] = logic.W_BISHOP;
            pieces[3] = logic.W_KNIGHT;
        }
        else {
            pieces[0] = logic.B_QUEEN;
            pieces[1] = logic.B_ROOK;
            pieces[2] = logic.B_BISHOP;
            pieces[3] = logic.B_KNIGHT;
        }
        return pieces[0];
    }

}

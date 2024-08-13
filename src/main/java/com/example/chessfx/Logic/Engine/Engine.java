package com.example.chessfx.Logic.Engine;

import com.example.chessfx.Logic.Object.Board;
import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Move;
import com.example.chessfx.Logic.Object.ZobristHash;

import java.util.*;


public class Engine {

    private Evaluation evaluation;
    private EngineGridLogic engineGridLogic;
    private ZobristHash zobristHash;
    private Random random;
    boolean isDebugged = true;
    int lines = 0,tpLines = 0;
    public Engine(Board board, int enginePlayer){
        this.engineGridLogic = new EngineGridLogic(board,enginePlayer);
        this.random = new Random();
        this.evaluation = new Evaluation(enginePlayer);
        this.zobristHash = new ZobristHash();
    }
    public Move bestMove(int turn, int player){
        Board tempBoard = engineGridLogic.getBoard().deepCopy();
        int bestValue = Integer.MIN_VALUE;
        int depth = 4;
        Move bestMove = null;

        List<Move> possibleMoves = engineGridLogic.getAllPossibleMove(tempBoard,turn);
        possibleMoves = orderMovesByCapture(possibleMoves,tempBoard,turn);
        //Collections.shuffle(possibleMoves);

        for(Move move : possibleMoves){
            Board simulatedBoard = engineGridLogic.simulateBoard(tempBoard,move,player);
            int moveValue = alphaBeta(simulatedBoard, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false, logic.getOpponentTurn(turn),player);
            if (moveValue >= bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
        }
        System.out.println("Number of lines : "+lines);
        System.out.println("Number of tp lines : "+tpLines);
        return bestMove;
    }

    private TranspositionTable transpositionTable = new TranspositionTable();
    public int alphaBeta(Board board, int depth, int alpha, int beta, boolean maximizingPlayer, int turn,int player) {

        // Check the transposition table
        long hash = zobristHash.calculateInitialHash(board);
        TranspositionTable.TTEntry transpositionEntry = transpositionTable.get(hash, depth);
        if (transpositionEntry != null) {
            tpLines++;
            switch (transpositionEntry.getNodeType()) {
                case EXACT:
                    return transpositionEntry.getValue();
                case ALPHA:
                    if (transpositionEntry.getValue() <= alpha) return alpha;
                    break;
                case BETA:
                    if (transpositionEntry.getValue() >= beta) return beta;
                    break;
            }
        }
        // Base case: if depth is 0
        lines++;
        if (depth == 0) {
            return evaluation.getEvaluation(board, turn);
        }
        List<Move> possibleMoves = engineGridLogic.getAllPossibleMove(board, turn);
        possibleMoves = orderMovesByCapture(possibleMoves,board,turn);
        // If there are no possible moves, check for checkmate or stalemate
        if (possibleMoves.isEmpty()) {
            // Check for checkmate
            if (logic.isKingInCheck(board.grid,turn,player)) {
                return maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            }
            // Check for stalemate
            else {
                return 0; // Draw scenario
            }
        }

        int originalAlpha = alpha;
        int value;
        if (maximizingPlayer) {
            value = Integer.MIN_VALUE;
            for (Move move : possibleMoves) {
                Board simulatedBoard = engineGridLogic.simulateBoard(board, move, player);
                int eval = alphaBeta(simulatedBoard, depth - 1, alpha, beta, false, logic.getOpponentTurn(turn),player);
                value = Math.max(value, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break; // Beta cut-off
                }
            }
        } else {
            value = Integer.MAX_VALUE;
            for (Move move : possibleMoves) {
                Board simulatedBoard = engineGridLogic.simulateBoard(board, move, player);
                int eval = alphaBeta(simulatedBoard, depth - 1, alpha, beta, true, logic.getOpponentTurn(turn),player);
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
        return value;
    }
    public List<Move> orderMovesByCapture(List<Move> moves, Board board, int turn) {
        List<Integer> scores = new ArrayList<>();
        List<Move> moveList = new ArrayList<>();

        for (Move move : moves) {
            Board simulatedBoard = engineGridLogic.simulateBoard(board, move, turn);
            int captureValue = evaluation.getEvaluation(simulatedBoard, turn);
            scores.add(captureValue);
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

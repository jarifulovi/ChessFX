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
    int nodes = 0,line = 0,tpLines = 0, moveGenerationCount = 0;
    public Engine(){
        this.bitBoardLogic = new BitBoardLogic();
        this.evaluation = new Evaluation();
        this.zobristHash = new ZobristHash();
        this.bitboardHash = new HashMap<>();
    }
    public Move bestMove(Board board,int turn, int player){
        Bitboard tempBitboard = bitBoardLogic.getBitBoard(board,player);
        // To make sure that the current state is included
        long hash = zobristHash.calculateHashBitboard(tempBitboard,turn);
        bitboardHash.put(hash,bitboardHash.getOrDefault(hash,0)+1);
        int bestValue = Integer.MIN_VALUE;

        Move bestMove = null;

        List<Move> possibleMoves = bitBoardLogic.getAllPossibleMove(tempBitboard,turn);
        moveGenerationCount++;
        possibleMoves = orderMovesByEvaluation(possibleMoves,tempBitboard,turn);

        for(Move move : possibleMoves){
            Bitboard simulatedBitboard = bitBoardLogic.simulateBitBoard(tempBitboard,move);
            int moveValue = alphaBeta(simulatedBitboard, maxDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false, logic.getOpponentTurn(turn));
            if (moveValue >= bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
        }
        System.out.println("Number of nodes "+ nodes);
        System.out.println("Number of lines "+ line);
        System.out.println("Number of t lines "+tpLines);
        System.out.println("T size : "+transpositionTable.table.size());
        System.out.println("Eval : "+bestValue);

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
    public int alphaBeta(Bitboard bitboard, int depth, int alpha, int beta, boolean maximizingPlayer, int turn) {

        nodes++;
        long hash = zobristHash.calculateHashBitboard(bitboard,turn);
        bitboardHash.put(hash,bitboardHash.getOrDefault(hash,0)+1);
        // Check threefold repition
        if (bitboardHash.get(hash) >= 3) {
            bitboardHash.put(hash, bitboardHash.get(hash) - 1);
            return 0;
        }
        // Check the transposition table
        Integer transpositionEval = transpositionTable.get(hash);
        if(transpositionEval != null){
            tpLines++;
            return transpositionEval;
        }
        // Base case: if depth is 0
        if (depth == 0) {
            line++;
            bitboardHash.put(hash,bitboardHash.getOrDefault(hash,1)-1);
            int exactEval = evaluation.getEvaluationBB(bitboard,turn);
            transpositionTable.put(hash,exactEval);
            return exactEval;
        }
        List<Move> possibleMoves = bitBoardLogic.getAllPossibleMove(bitboard,turn);
        moveGenerationCount++;

        // If there are no possible moves, check for checkmate or stalemate
        if (possibleMoves.isEmpty()) {
            // Check for checkmate
            bitboardHash.put(hash,bitboardHash.getOrDefault(hash,1)-1);
            if (GenerateAttackSquare.isOwnKingAttacked(bitboard,turn)) {
                return maximizingPlayer ? (Integer.MIN_VALUE/maxDepth)*(depth+1) : (Integer.MAX_VALUE/maxDepth)*(depth+1);
            }
            // Check for stalemate
            else {
                return 0;
            }
        }
        possibleMoves = orderMovesByEvaluation(possibleMoves,bitboard,turn);

        int bestEval;
        if (maximizingPlayer) {
            bestEval = Integer.MIN_VALUE;
            for (Move move : possibleMoves) {
                Bitboard simulatedBitboard = bitBoardLogic.simulateBitBoard(bitboard,move);
                int eval = alphaBeta(simulatedBitboard, depth - 1, alpha, beta, false, logic.getOpponentTurn(turn));
                bestEval = Math.max(bestEval, eval);
                alpha = Math.max(alpha, bestEval);
                if (beta <= alpha) {
                    break; // Beta cut-off
                }
            }
        } else {
            bestEval = Integer.MAX_VALUE;
            for (Move move : possibleMoves) {
                Bitboard simulatedBitboard = bitBoardLogic.simulateBitBoard(bitboard,move);
                int eval = alphaBeta(simulatedBitboard, depth - 1, alpha, beta, true, logic.getOpponentTurn(turn));
                bestEval = Math.min(bestEval, eval);
                beta = Math.min(beta, bestEval);
                if (beta <= alpha) {
                    break; // Alpha cut-off
                }
            }
        }

        bitboardHash.put(hash,bitboardHash.getOrDefault(hash,1)-1);
        return bestEval;
    }

    public List<Move> orderMovesByEvaluation(List<Move> moves,Bitboard bitboard,int turn) {

        if(moves.size() <= 1) return moves;
        List<Integer> scores = new ArrayList<>();
        List<Move> moveList = new ArrayList<>();

        // Turn remains same because the eval is current turn perspective
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

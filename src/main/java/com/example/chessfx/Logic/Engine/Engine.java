package com.example.chessfx.Logic.Engine;

import com.example.chessfx.Logic.Board;
import com.example.chessfx.Logic.GridLogic;
import com.example.chessfx.Logic.Abstract.logic;

import java.util.*;


// Plays a random move
// Prioritizes :
//               1 : Promote a pawn to queen
//               2 : Defend undefended piece
//               3 : Capture opponent undefended pieces
//               4 : Capture an advantageous capture
//               5 : Move to a safe square
public class Engine {

    private Evaluation evaluation;
    private EngineGridLogic engineGridLogic;
    private Random random;
    int lines = 0;
    public Engine(Board board, int enginePlayer){
        this.engineGridLogic = new EngineGridLogic(board,enginePlayer);
        this.random = new Random();
        this.evaluation = new Evaluation(enginePlayer);
    }
    public Move bestMove(int turn,int player){
        Board tempBoard = engineGridLogic.getBoard().deepCopy();
        int bestValue = Integer.MIN_VALUE;
        int depth = 3;
        lines = 0;
        Move bestMove = null;

        List<Move> possibleMoves = engineGridLogic.getAllPossibleMove(tempBoard,turn);
        for(Move move : possibleMoves){
            Board simulatedBoard = engineGridLogic.simulateBoard(tempBoard,move,player);
            int moveValue = alphaBeta(simulatedBoard, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false, logic.getOpponentTurn(turn),player);
            if (moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
        }

        return bestMove;
    }
    public int alphaBeta(Board board, int depth, int alpha, int beta, boolean maximizingPlayer, int turn,int player) {
        // Base case: if depth is 0 or the game is over, evaluate the board
        if (depth == 0) {
            return evaluation.getEvaluation(board, turn);
        }
        lines++;
        List<Move> possibleMoves = engineGridLogic.getAllPossibleMove(board, turn);
        System.out.println("moves : "+possibleMoves.size()+" at line "+ lines);

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : possibleMoves) {
                Board simulatedBoard = engineGridLogic.simulateBoard(board, move, player);
                int eval = alphaBeta(simulatedBoard, depth - 1, alpha, beta, false, logic.getOpponentTurn(turn),player);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break; // Beta cut-off
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : possibleMoves) {
                Board simulatedBoard = engineGridLogic.simulateBoard(board, move, player);
                int eval = alphaBeta(simulatedBoard, depth - 1, alpha, beta, true, logic.getOpponentTurn(turn),player);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break; // Alpha cut-off
                }
            }
            return minEval;
        }
    }


    public Move randomMove(int turn){

        // Must have legal moves
        int[][] tempGrid = engineGridLogic.getGrid();

        List<int[]> ownPiecePositions = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (logic.isOwnPiece(tempGrid[i][j], turn)) {
                    ownPiecePositions.add(new int[]{i, j});
                }
            }
        }
        Collections.shuffle(ownPiecePositions);

        Move defaultMove = new Move(-1,-1,-1,-1);
        int hierarchy = 0;
        for (int[] pos : ownPiecePositions) {
            int preRow = pos[0];
            int preCol = pos[1];
            int[][] moves = engineGridLogic.getValidPositions(preRow, preCol);

            if (moves.length > 0) {
                // Set defaultMove
                if(defaultMove.preRow == -1){
                    int index = random.nextInt(moves.length);
                    defaultMove = new Move(preRow,preCol,moves[index][0],moves[index][1],tempGrid[preRow][preCol]);
                }

                // Check if a pawn is at promotion
                Move pawnPromotionMove = evaluation.getPawnPromotionMove(moves,preRow,preCol,tempGrid);
                if(pawnPromotionMove != null) return pawnPromotionMove;


                // Check if there's a hanging piece
                if(hierarchy < 4) {
                    Move undefendedPieceDefendMove = evaluation.getUndefendedPieceDefendMove(moves, preRow, preCol, tempGrid);
                    if (undefendedPieceDefendMove != null) {
                        defaultMove = undefendedPieceDefendMove;
                        hierarchy = 4;
                    }
                }

                // Check if there's an undefended piece to capture
                if(hierarchy < 3) {
                    Move captureUndefendedMove = evaluation.getCaptureUndefendedMove(moves, preRow, preCol, tempGrid);
                    if (captureUndefendedMove != null) {
                        defaultMove = captureUndefendedMove;
                        hierarchy = 3;
                    }
                }

                // Check if there's an advantageous capture
                if(hierarchy < 2) {
                    Move captureMove = evaluation.getCaptureMove(moves, preRow, preCol, tempGrid);
                    if (captureMove != null) {
                        defaultMove = captureMove;
                        hierarchy = 2;
                    }
                }

                // Check for safe moves
                if(hierarchy < 1){
                    Move safeMove = evaluation.getSafeMove(moves,preRow,preCol,tempGrid);
                    if(safeMove != null) defaultMove = safeMove;
                    hierarchy = 1;
                }
            }
        }

        // defaultMove can't be unset as there will be legal move
        return defaultMove;
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

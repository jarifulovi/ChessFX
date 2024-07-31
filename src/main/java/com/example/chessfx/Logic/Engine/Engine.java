package com.example.chessfx.Logic.Engine;

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
    private GridLogic gridLogic;
    private Random random;
    private int turn;
    public Engine(GridLogic gridLogic,int turn){
        this.gridLogic = gridLogic;
        this.random = new Random();
        this.turn = turn;
        this.evaluation = new Evaluation(turn);
    }

    public Move randomMove(int turn){

        //logic.delay(1000);
        // Must have legal moves
        int[][] tempGrid = gridLogic.getGrid();

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
            int[][] moves = gridLogic.getValidPositions(preRow, preCol, turn);

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
                Move undefendedPieceDefendMove = evaluation.getUndefendedPieceDefendMove(moves,preRow,preCol,tempGrid);
                if(undefendedPieceDefendMove != null){
                    defaultMove = undefendedPieceDefendMove;
                    hierarchy = 3;
                }

                // Check if there's an undefended piece to capture
                if(hierarchy < 3) {
                    Move captureUndefendedMove = evaluation.getCaptureUndefendedMove(moves, preRow, preCol, tempGrid);
                    if (captureUndefendedMove != null) {
                        defaultMove = captureUndefendedMove;
                        hierarchy = 2;
                    }
                }

                // Check if there's an advantageous capture
                if(hierarchy < 2) {
                    Move captureMove = evaluation.getCaptureMove(moves, preRow, preCol, tempGrid);
                    if (captureMove != null) {
                        defaultMove = captureMove;
                        hierarchy = 1;
                    }
                }

                // Check for safe moves
                if(hierarchy < 1){
                    Move safeMove = evaluation.getSafeMove(moves,preRow,preCol,tempGrid);
                    if(safeMove != null) defaultMove = safeMove;
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

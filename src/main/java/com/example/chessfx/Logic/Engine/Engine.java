package com.example.chessfx.Logic.Engine;

import com.example.chessfx.Logic.GridLogic;
import com.example.chessfx.Logic.logic;

import java.util.*;

public class Engine {

    private GridLogic gridLogic;
    private Random random;
    private int turn;
    public Engine(GridLogic gridLogic,int turn){
        this.gridLogic = gridLogic;
        this.random = new Random();
        this.turn = turn;
    }

    public Move randomMove(int turn){

        //logic.delay(1000);
        // select a random piece
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


        for (int[] pos : ownPiecePositions) {
            int row = pos[0];
            int col = pos[1];
            int[][] moves = gridLogic.getValidPositions(row, col, turn);

            if (moves.length > 0) {

                int index =  random.nextInt(moves.length);
                return new Move(row, col, moves[index][0], moves[index][1], tempGrid[row][col]);
            }
        }

        // Can't return null as there will be legal move
        return null;
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
        int index = random.nextInt(4);
        return pieces[index];
    }
}

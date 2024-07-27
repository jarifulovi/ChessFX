package com.example.chessfx.Logic.Engine;

import com.example.chessfx.Logic.GridLogic;
import com.example.chessfx.Logic.logic;

import java.util.*;

public class Engine {

    private GridLogic gridLogic;
    private Random random;
    public Engine(GridLogic gridLogic){
        this.gridLogic = gridLogic;
        this.random = new Random();
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
                moves = excludeOwnPiecePosition(tempGrid,moves,turn);
                int index = random.nextInt(moves.length);
                return new Move(row, col, moves[index][0], moves[index][1], tempGrid[row][col]);
            }
        }

        // Can't return null as there will be legal move
        return null;
    }

    private int[][] excludeOwnPiecePosition(int[][] grid,int[][] positions,int turn){

        int[][] pos = new int[64][2];
        int index = 0;
        for(int[] p : positions){
            int row = p[0];
            int col = p[1];
            if(!logic.isOwnPiece(grid[row][col],turn)){
                pos[index][0] = row;
                pos[index][1] = col;
                index++;
            }
        }
        return Arrays.copyOf(pos,index);
    }
    public int getPawnPromotedPiece(){

        return random.nextInt(4);
    }
}

package com.example.chessfx;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Engine.BitBoard.BitBoardLogic;
import com.example.chessfx.Logic.Engine.BitBoard.Bitboard;
import com.example.chessfx.Logic.Engine.BitBoard.GenerateAttackSquare;
import com.example.chessfx.Logic.Engine.EngineGridLogic;
import com.example.chessfx.Logic.GridLogic;
import com.example.chessfx.Logic.Object.Board;
import com.example.chessfx.Logic.Object.Move;


public class Debug {

    BitBoardLogic bitBoardLogic = new BitBoardLogic();
    EngineGridLogic gridLogic;
    Bitboard bitboard;
    Board board;
    String FEN = "r1bqk2r/p1p1bppp/2pp1n2/4p3/4P3/3P1N2/PPP2PPP/RNBQRK2 w kq - 1 9";

    public Debug(){
        board = logic.convertFENIntoBoard(FEN);
        gridLogic = new EngineGridLogic(board,logic.BLACK);
        bitboard = bitBoardLogic.getBitBoard(board,logic.WHITE);
    }
    public static void main(String[] args) {

        Debug debug = new Debug();
        int bitBoardCount = 0,boardCount = 0;
        long startTime = System.currentTimeMillis();

        for(int t = 0; t < 2346; t++) {
            bitBoardCount += debug.bitBoardLogic.getAllPossibleMove(debug.bitboard,logic.WHITE).size();
            debug.bitBoardLogic.simulateBitBoard(debug.bitboard,new Move(48,32,logic.W_PAWN));
        }
        System.out.println("Time for bb : ");
        logic.displayTime(startTime);
        startTime = System.currentTimeMillis();
        for(int t = 0; t < 2346; t++) {
            boardCount += debug.gridLogic.getAllPossibleMove(debug.board,logic.WHITE).size();
            debug.gridLogic.simulateBoard(debug.board,new Move(6,0,4,0,logic.W_PAWN),logic.WHITE);
        }
        System.out.println("Time for b : ");
        logic.displayTime(startTime);
        System.out.println("Result(bb : b) : "+bitBoardCount + " : "+boardCount);
    }
}

// brenchmarks
// :::::  isSquareAttacked :: isKingInCheck
// t == 500,000
// bb : 686ms and b : 2639ms

// t == 50,000
// bb : 165ms and b : 364ms

// :::: generate valid moves
// t == 1000
// bb : 99ms  and b : 71ms

// t == 10,000
// bb : 173ms and b : 346ms

// t == 50,000
// bb : 325ms and b : 1289ms

// t == 100,000
// bb : 486ms and b : 2461ms
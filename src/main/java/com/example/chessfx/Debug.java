package com.example.chessfx;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Engine.BitBoard.BitBoardLogic;
import com.example.chessfx.Logic.Engine.BitBoard.Bitboard;
<<<<<<< HEAD
import com.example.chessfx.Logic.Engine.BitBoard.GenerateAttackSquare;
import com.example.chessfx.Logic.Engine.EngineGridLogic;
import com.example.chessfx.Logic.GridLogic;
=======
import com.example.chessfx.Logic.Engine.Evaluation;
import com.example.chessfx.Logic.Engine.PieceValueTable;
>>>>>>> 885feef (added_endgame_eval)
import com.example.chessfx.Logic.Object.Board;

<<<<<<< HEAD
=======
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

>>>>>>> 885feef (added_endgame_eval)

public class Debug {
    BitBoardLogic bitBoardLogic = new BitBoardLogic();
    PieceValueTable pieceValueTable = new PieceValueTable();
    Bitboard bitboard;
    Board board;
<<<<<<< HEAD
    String FEN = "r1bqk2r/p1p1bppp/2pp1n2/4p3/4P3/3P1N2/PPP2PPP/RNBQRK2 w kq - 1 9";
=======
    public static String FEN = "r1bq2k1/pppp1rpp/2n2n2/2b1p3/4P3/2N2N2/PPPP1PPP/R1BQ1RK1 w - - 0 7";
>>>>>>> 885feef (added_endgame_eval)

    private String filePath = "src/main/resources/com/example/chessfx/lichess_db_puzzle.csv";
    public Debug(){
        board = logic.convertFENIntoBoard(FEN);
        bitboard = bitBoardLogic.getBitBoard(board,logic.WHITE);
    }
    public static void main(String[] args) {

        Debug debug = new Debug();
        int bitBoardCount = 0,boardCount = 0;
<<<<<<< HEAD
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
=======
        int lines = 1000000;


        int turn = logic.BLACK;
        long startTime = System.currentTimeMillis();
        int[] pc = new int[13];
        Arrays.fill(pc,1);
        Evaluation evaluation = new Evaluation();
        System.out.println("Time for bitboard : ");
        logic.displayTime(startTime);
    }

    public static List<String> extractFENStrings(String filePath,int totalLine) {
        List<String> fenList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip the header line
            if ((line = br.readLine()) != null) {
                // Process each line of the CSV
                while ((line = br.readLine()) != null) {
                    // Split the line by commas
                    String[] fields = line.split(",");
                    if (fields.length > 1) {
                        // The FEN is in the second field (index 1)
                        String fen = fields[1];
                        String[] parts = fen.split(" ");
                        //if(parts[1].equals("b"))
                            fenList.add(fen);
                        totalLine--;
                    }
                    if(totalLine == 0) break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fenList;
    }
}

// benchmarks

// :::: generate valid moves + simulate
// t == 1000
// bb : 101ms  and b : 71ms
// bb : 51ms   and b : 134ms ( without initial move g )

// t == 10,000
// bb : 179ms and b : 408ms
// bb : 161ms and b : 670ms ( without initial move g )

// t == 50,000
// bb : 352ms and b : 1289ms
// bb : 386ms and b : 2008ms ( without initial move g )

// t == 100,000
// bb : 541ms  and b : 2844ms
// bb : 573ms  and b : 3810ms ( without initial move g )
>>>>>>> 885feef (added_endgame_eval)

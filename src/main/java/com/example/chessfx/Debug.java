package com.example.chessfx;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Engine.BitBoard.BitBoardLogic;
import com.example.chessfx.Logic.Engine.BitBoard.Bitboard;
import com.example.chessfx.Logic.Engine.Evaluation;
import com.example.chessfx.Logic.Engine.PieceValueTable;
import com.example.chessfx.Logic.Object.Board;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Debug {
    BitBoardLogic bitBoardLogic = new BitBoardLogic();
    PieceValueTable pieceValueTable = new PieceValueTable();
    Bitboard bitboard;
    Board board;
    public static String FEN = "r1bq2k1/pppp1rpp/2n2n2/2b1p3/4P3/2N2N2/PPPP1PPP/R1BQ1RK1 w - - 0 7";

    public Debug(){
        board = logic.convertFENIntoBoard(FEN);
        bitboard = bitBoardLogic.getBitBoard(board,logic.WHITE);
    }
    public static void main(String[] args) {

        Debug debug = new Debug();
        int bitBoardCount = 0,boardCount = 0;
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
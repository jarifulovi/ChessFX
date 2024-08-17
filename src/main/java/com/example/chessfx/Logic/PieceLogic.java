package com.example.chessfx.Logic;

import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PieceLogic {

    private IllegalMove illegalMove;
    private int player;
    private int enPassantTurn;


    public PieceLogic(int player){
        this.illegalMove = new IllegalMove();
        this.enPassantTurn = logic.NO_EN_PASSANT;
        this.player = player;
    }

    public int[][] allValidMoves(Board board, int piece, int row, int col){

        int[][] validSquares;
        int turn = logic.getPieceColor(piece);


        if(piece == logic.W_PAWN || piece == logic.B_PAWN){
            validSquares = validPawnSquares(board,row,col,turn,player);
            validSquares = eliminateIllegalMoves(board.grid,validSquares,row,col);
            return validSquares;
        }
        else if(piece == logic.W_KNIGHT || piece == logic.B_KNIGHT){
            validSquares  = logic.validKnightSquares(row,col,board.grid,turn);
            validSquares = eliminateIllegalMoves(board.grid,validSquares,row,col);
            return validSquares;
        }
        else if(piece == logic.W_ROOK || piece == logic.B_ROOK){
            validSquares = logic.validRookSquares(row,col,turn,board.grid);
            validSquares = eliminateIllegalMoves(board.grid,validSquares,row,col);
            return validSquares;
        }
        else if(piece == logic.W_BISHOP || piece == logic.B_BISHOP){
            validSquares = logic.validBishopSquares(row,col,turn,board.grid);
            validSquares = eliminateIllegalMoves(board.grid,validSquares,row,col);
            return validSquares;
        }
        else if(piece == logic.W_QUEEN || piece == logic.B_QUEEN){
            validSquares = logic.validQueenSquares(row,col,turn,board.grid);
            validSquares = eliminateIllegalMoves(board.grid,validSquares,row,col);
            return validSquares;
        }
        else if(piece == logic.W_KING || piece == logic.B_KING){
            validSquares = validKingSquares(board,row,col,turn);
            validSquares = eliminateIllegalMoves(board.grid,validSquares,row,col);
            return validSquares;
        }

        return new int[0][0];
    }

    private int[][] eliminateIllegalMoves(int[][] grid, int[][] validSquares, int startRow, int startCol) {

        List<int[]> finalValidMoves = new ArrayList<>();

        for (int[] targetSquare : validSquares) {
            int newRow = targetSquare[0];
            int newCol = targetSquare[1];

            // Check if the move puts own king in check
            if (!illegalMove.isSelfCheckAfterMove(grid, grid[startRow][startCol],player, startRow, startCol, newRow, newCol)) {
                finalValidMoves.add(targetSquare);
            }
        }

        return finalValidMoves.toArray(new int[finalValidMoves.size()][2]); // Convert list to 2D array
    }


    private int[][] validKingSquares(Board board,int row,int col,int turn){

        int[][] validSquares = new int[8][2];
        int validSquareCount = 0;

        int[] rowOffsets = {0, 0, 1, 1, 1,-1, -1,-1};
        int[] colOffsets = {1,-1, 1,-1, 0, 1, -1, 0};

        for (int i = 0; i < rowOffsets.length; i++) {
            int newRow = row + rowOffsets[i];
            int newCol = col + colOffsets[i];

            if(logic.isWithinBoard(newRow,newCol) && !logic.isOwnPiece(board.grid[newRow][newCol],turn)){
                validSquares[validSquareCount][0] = newRow;
                validSquares[validSquareCount][1] = newCol;
                validSquareCount++;
            }
        }
        int rightCastleCol = col + 2;
        int leftCastleCol = col - 2;
        // No need to check boundary as king will be at init position
        if(canCastle(board,true,row,turn)){
            validSquares[validSquareCount][0] = row;
            validSquares[validSquareCount][1] = rightCastleCol;
            validSquareCount++;
        }
        if(canCastle(board,false,row,turn)){
            validSquares[validSquareCount][0] = row;
            validSquares[validSquareCount][1] = leftCastleCol;
            validSquareCount++;
        }


        return Arrays.copyOf(validSquares,validSquareCount);
    }
    private int[][] validPawnSquares(Board board,int row,int col,int turn,int player){

        int[][] validSquares = new int[10][2];
        int validSquareCount = 0;

        int forwardDirection = ( turn == player ) ? -1 : 1;
        int newRow = row + forwardDirection;

        // For one forward push
        if(logic.isWithinBoard(newRow,col)){

            // If no piece on the way
            if(board.grid[newRow][col] == logic.NO_PIECE){
                validSquares[validSquareCount][0] = newRow;
                validSquares[validSquareCount][1] = col;
                validSquareCount++;
            }
            // Check for capture diagonally forward
            int captureColLeft = col - 1;
            int captureColRight = col + 1;


            if(logic.isWithinBoard(newRow,captureColLeft) &&
                    (logic.isOpponentPiece(board.grid[newRow][captureColLeft],turn)  || (isEnPassant(board,newRow,captureColLeft)))){
                validSquares[validSquareCount][0] = newRow;
                validSquares[validSquareCount][1] = captureColLeft;
                validSquareCount++;
            }


            if(logic.isWithinBoard(newRow,captureColRight) &&
                    (logic.isOpponentPiece(board.grid[newRow][captureColRight],turn) || (isEnPassant(board,newRow,captureColRight)))){
                validSquares[validSquareCount][0] = newRow;
                validSquares[validSquareCount][1] = captureColRight;
                validSquareCount++;
            }
        }

        // For two forward push
        boolean canTwoPush = ( turn == player ) ? (row == 6) : (row == 1);

        if(canTwoPush){
            int preRow = newRow;
            newRow = newRow + forwardDirection;
            // If forward two squares are empty
            if(logic.isWithinBoard(newRow,col) &&
                    board.grid[newRow][col] == logic.NO_PIECE && board.grid[preRow][col] == logic.NO_PIECE){
                validSquares[validSquareCount][0] = newRow;
                validSquares[validSquareCount][1] = col;
                validSquareCount++;

            }
        }
        return Arrays.copyOf(validSquares,validSquareCount);
    }

    // Position where the capture can occur
    private boolean isEnPassant(Board board,int row,int col){

        int index = row * 8 + col;
        return board.enPassantIndex == index;
    }

    private boolean canCastle(Board board,boolean isRight,int row,int turn) {

        // If no piece along and castle piece are not moved
        boolean noPiece;

        if (isRight) {
            noPiece = (board.grid[row][5] == logic.NO_PIECE) &&
                    (board.grid[row][6] == logic.NO_PIECE);

        } else
            noPiece = (board.grid[row][1] == logic.NO_PIECE) && (board.grid[row][2] == logic.NO_PIECE)
                    && (board.grid[row][3] == logic.NO_PIECE);


        if (turn == logic.WHITE) {
            if (isRight) return board.canWhiteRightCastled && noPiece;
            else return board.canWhiteLeftCastled && noPiece;
        } else {
            if (isRight) return board.canBlackRightCastled && noPiece;
            else return board.canBlackLeftCastled && noPiece;
        }
    }


}

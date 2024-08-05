package com.example.chessfx.Logic;

import com.example.chessfx.Logic.Abstract.logic;

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

    // Only pawns doesn't take own piece positions
    public int[][] allValidMoves(Board board,int piece,int row,int col){

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
    public void setEnPassant(Board board,int row,int col,int turn){
        int enPassantRow = (turn==player) ? row + 1 : row - 1;
        board.enPassantIndex = enPassantRow * 8 + col;
        enPassantTurn = turn;
    }

    private boolean isEnPassant(Board board,int row,int col){

        int index = row * 8 + col;
        return board.enPassantIndex == index;
    }
    // Position is where the capture happend
    public void removeEnPassantPiece(int[][] grid,int turn,int row,int col){

        int enPassantCaptureRow = (turn==player) ? row + 1 : row - 1;
        grid[enPassantCaptureRow][col] = logic.NO_PIECE;
    }

    // This method has to call before updating grid
    public void updateEnPassant(Board board,int piece,int preRow,int newRow,int preCol,int newCol,int turn){


        if(piece == logic.W_PAWN || piece == logic.B_PAWN){
            if(Math.abs(preRow - newRow) == 2){

                setEnPassant(board,newRow,newCol,turn);
            }
            // If en passant capture happens
            // When moves diagonally and new position doesn't have any opponent piece
            if(Math.abs(preRow-newRow)==Math.abs(preCol-newCol)){
                if(!logic.isOpponentPiece(board.grid[newRow][newCol],turn)){
                    removeEnPassantPiece(board.grid,turn,newRow,newCol);
                }
            }
        }
        if(enPassantTurn != turn)
            board.enPassantIndex = logic.NO_EN_PASSANT;
    }
    private boolean canCastle(Board board,boolean isRight,int row,int turn){

        // If no piece along and castle piece are not moved
        boolean noPiece;

        if(isRight) {
            noPiece = (board.grid[row][5] == logic.NO_PIECE) &&
                    (board.grid[row][6] == logic.NO_PIECE);

        }
        else
            noPiece = (board.grid[row][1] == logic.NO_PIECE) && (board.grid[row][2] == logic.NO_PIECE)
                    && (board.grid[row][3] == logic.NO_PIECE);


        if(turn==logic.WHITE){
            if(isRight) return board.canWhiteRightCastled && noPiece;
            else        return board.canWhiteLeftCastled && noPiece;
        }
        else {
            if(isRight) return board.canBlackRightCastled && noPiece;
            else        return board.canBlackLeftCastled && noPiece;
        }
    }
    public void updateCastling(Board board,int player,int piece,int preRow,int preCol,int newRow,int newCol){

        if(piece == logic.W_KING){
            board.canWhiteLeftCastled = false;
            board.canWhiteRightCastled = false;
        }
        if(piece == logic.B_KING){
            board.canBlackLeftCastled = false;
            board.canBlackRightCastled = false;
        }
        if(piece == logic.W_ROOK){
            if(preCol == 0) board.canWhiteLeftCastled = false;
            else if(preCol == 7) board.canWhiteRightCastled = false;
        }
        if(piece == logic.B_ROOK){
            if(preCol == 0) board.canBlackLeftCastled = false;
            else if(preCol == 7) board.canBlackRightCastled = false;
        }
        // If captured
        if(player == logic.WHITE) {
            if (board.grid[7][0] != logic.W_ROOK) board.canWhiteLeftCastled = false;
            if (board.grid[7][7] != logic.W_ROOK) board.canWhiteRightCastled = false;
            if (board.grid[0][0] != logic.B_ROOK) board.canBlackLeftCastled = false;
            if (board.grid[0][7] != logic.B_ROOK) board.canBlackRightCastled = false;
        }
        else {
            if (board.grid[7][0] != logic.B_ROOK) board.canBlackLeftCastled = false;
            if (board.grid[7][7] != logic.B_ROOK) board.canBlackRightCastled = false;
            if (board.grid[0][0] != logic.W_ROOK) board.canWhiteLeftCastled = false;
            if (board.grid[0][7] != logic.W_ROOK) board.canWhiteRightCastled = false;
        }

        if(piece == logic.W_KING || piece == logic.B_KING){

            if(Math.abs(preCol - newCol) == 2 && preRow == newRow)
                updateRook(board.grid,piece,preRow,preCol,newCol);
        }
    }
    private void updateRook(int[][] grid,int piece,int preRow,int preCol,int newCol){
        // Only updates rook position as king is updated in gridLogic

        int color = logic.getPieceColor(piece);
        // Right side castle
        if(newCol > preCol){
            grid[preRow][preCol+1] = (color==logic.WHITE)?(logic.W_ROOK):(logic.B_ROOK);
            grid[preRow][7] = logic.NO_PIECE;
        }
        else {
            grid[preRow][preCol-1] = (color==logic.WHITE)?(logic.W_ROOK):(logic.B_ROOK);
            grid[preRow][0] = logic.NO_PIECE;
        }
    }


}

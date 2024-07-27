package com.example.chessfx.Logic;

import java.util.Arrays;

public abstract class logic {

    public static int NO_PIECE = 0;
    public static int W_PAWN = 1;
    public static int W_KNIGHT = 2;
    public static int W_BISHOP = 3;
    public static int W_ROOK = 4;
    public static int W_QUEEN = 5;
    public static int W_KING = 6;
    public static int B_PAWN = 7;
    public static int B_KNIGHT = 8;
    public static int B_BISHOP = 9;
    public static int B_ROOK = 10;
    public static int B_QUEEN = 11;
    public static int B_KING = 12;

    // GridPC constants
    public static int WHITE_EN_PASSANT = 15;
    public static int BLACK_EN_PASSANT = 16;
    public static int NO_EN_PASSANT = 0;
    public static int CASTLE = 17;
    public static int NO_CASTLE = 0;

    // For indicating players and turns
    public static int WHITE = 0;
    public static int BLACK = 1;

    // For game type
    public static int TWO_PLAYER = 0;
    public static int ONE_PLAYER = 1;
    public static String BACKGROUND_CSS = "-fx-background-color :";
    public static String FOREST_GREEN = "#69923e";
    public static String LIGHT_GREEN = "#baca44";
    public static String DARK_GREEN = "#4e7837";
    public static String GRAY = "#4b4847";
    public static String DIM_GRAY = "#696969";
    public static String BROWN = "#ab8149";

    public static String BLACK_COLOR = "#000000";
    public static String WHITE_COLOR = "#ffffff";


    public static boolean isWithinBoard(int row,int col){
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
    public static boolean isOwnPiece(int piece,int turn){
        if(piece == NO_PIECE) return false;
        return (turn == WHITE) == (piece <= 6);
    }
    public static boolean isOpponentPiece(int piece,int turn){
        if(piece == NO_PIECE) return false;
        return (turn != WHITE) == (piece <= 6);
    }
    public static boolean isDiagonal(int row1,int col1,int row2,int col2){
        return Math.abs(row1 - row2) == Math.abs(col1 - col2);
    }
    public static boolean isStraight(int row1, int col1, int row2, int col2) {
        return row1 == row2 || col1 == col2;
    }
    public static boolean isWithinPosition(int[][] positions,int[] targetPos){

        for(int[] pos : positions){
            if(pos[0] == targetPos[0] && pos[1] == targetPos[1]) return true;
        }
        return false;
    }
    public static boolean isPawnPromoting(int piece,int turn,int player,int newRow){

        if(piece == logic.W_PAWN || piece == logic.B_PAWN){
            return (turn==player) ? (newRow == 0) : (newRow == 7);
        }
        return false;
    }
    public static boolean drawByInsufficientMaterial(int[][] grid){

        int whiteKnight=0,blackKnight=0;
        int whiteBishop=0,blackBishop=0;
        int totalPiece=0;

        for(int[] row : grid){
            for(int piece : row){
                if(piece != NO_PIECE){
                    if(piece == W_KNIGHT) whiteKnight++;
                    else if(piece == B_KNIGHT) blackKnight++;
                    else if(piece == W_BISHOP) whiteBishop++;
                    else if(piece == B_BISHOP) blackBishop++;
                    totalPiece++;
                }
            }
        }
        int totalKnight = whiteKnight+blackKnight;
        int totalBishop = whiteBishop+blackBishop;

        boolean kingVsKing = (totalPiece==2);
        boolean twoKnight = (totalPiece==4)&&(whiteKnight==1&&blackKnight==1);
        boolean twoBishop = (totalPiece==4)&&(whiteBishop==1&&blackBishop==1);
        boolean oneKnight =  (totalPiece==3)&&(totalKnight==1);
        boolean oneBishop = (totalPiece==3)&&(totalBishop==1);
        boolean knightAndBishop = (totalPiece==4)&&(totalKnight==1&&totalBishop==1)&&(whiteKnight!=whiteBishop);

        return kingVsKing || twoKnight || twoBishop || oneKnight || oneBishop || knightAndBishop;
    }
    public static int getOpponentTurn(int turn){
        return (turn == WHITE) ? (BLACK) : (WHITE);
    }
    public static int getPieceColor(int piece){
        // Piece cannot be a no piece
        return (piece <= 6) ? WHITE : BLACK;
    }
    public static int[][] setPieces(int player) {

        int[][] grid = new int[8][8];

        // Set pawns for both players
        for (int col = 0; col < 8; col++) {
            if(player == logic.WHITE){
                grid[6][col] = W_PAWN;
                grid[1][col] = B_PAWN;
            }
            else {
                grid[6][col] = B_PAWN;
                grid[1][col] = W_PAWN;
            }
        }

        // Set remaining pieces for the given player (white or black)

        // set rooks
        if(player == WHITE){
            grid[0][0] = B_ROOK;
            grid[0][7] = B_ROOK;
            grid[7][0] = W_ROOK;
            grid[7][7] = W_ROOK;
        }
        else {
            grid[0][0] = W_ROOK;
            grid[0][7] = W_ROOK;
            grid[7][0] = B_ROOK;
            grid[7][7] = B_ROOK;
        }

        // set knights
        if(player == WHITE){
            grid[0][1] = B_KNIGHT;
            grid[0][6] = B_KNIGHT;
            grid[7][1] = W_KNIGHT;
            grid[7][6] = W_KNIGHT;
        }
        else {
            grid[0][1] = W_KNIGHT;
            grid[0][6] = W_KNIGHT;
            grid[7][1] = B_KNIGHT;
            grid[7][6] = B_KNIGHT;
        }

        // set bishops
        if(player == WHITE){
            grid[0][2] = B_BISHOP;
            grid[0][5] = B_BISHOP;
            grid[7][2] = W_BISHOP;
            grid[7][5] = W_BISHOP;
        }
        else {
            grid[0][2] = W_BISHOP;
            grid[0][5] = W_BISHOP;
            grid[7][2] = B_BISHOP;
            grid[7][5] = B_BISHOP;
        }

        // set queens and kings
        if(player == WHITE){
            grid[0][3] = B_QUEEN;
            grid[0][4] = B_KING;
            grid[7][3] = W_QUEEN;
            grid[7][4] = W_KING;
        }
        else {
            grid[0][3] = W_QUEEN;
            grid[0][4] = W_KING;
            grid[7][3] = B_QUEEN;
            grid[7][4] = B_KING;
        }

        return grid;
    }
    public static int[][] setGridPC(){

        int[][] gridPC = new int[8][8];
        // Set castling
        gridPC[0][0] = logic.CASTLE;
        gridPC[0][7] = logic.CASTLE;
        gridPC[7][0] = logic.CASTLE;
        gridPC[7][7] = logic.CASTLE;

        return gridPC;
    }
    public static boolean validSquareClick(int[][] positions,int row,int col){

        for(int[] pos : positions){
            if(pos[0] == row && pos[1] == col) return true;
        }
        return false;
    }

    public static int[][] validKnightSquares(int row, int col) {
        int[][] validSquares = new int[8][2];
        int validSquareCount = 0;

        // Define knight's movement offsets (row and column changes)
        int[] rowOffsets = {-2, -2, -1, 1, 2, 2, -1, 1};
        int[] colOffsets = {-1, 1, 2, 2, 1, -1, -2, -2};

        // Iterate through all possible knight moves
        for (int i = 0; i < 8; i++) {
            int newRow = row + rowOffsets[i];
            int newCol = col + colOffsets[i];

            // Check if the new square is within board boundaries
            if (isWithinBoard(newRow,newCol)) {

                validSquares[validSquareCount][0] = newRow;
                validSquares[validSquareCount][1] = newCol;
                validSquareCount++;
            }
        }

        return Arrays.copyOf(validSquares, validSquareCount);
    }
    public static int[][] validSquaresInDirection(int row, int col,int[][] grid, int[] rowOffsets, int[] colOffsets) {
        int[][] validSquares = new int[28][2]; // Maximum 14 valid squares
        int validSquareCount = 0;

        for (int i = 0; i < rowOffsets.length; i++) {
            int newRow = row + rowOffsets[i];
            int newCol = col + colOffsets[i];

            while (logic.isWithinBoard(newRow, newCol) && grid[newRow][newCol] == logic.NO_PIECE) {
                validSquares[validSquareCount][0] = newRow;
                validSquares[validSquareCount][1] = newCol;
                validSquareCount++;

                newRow += rowOffsets[i];
                newCol += colOffsets[i];
            }

            // If an occupied square is encountered, add it for capturing purposes
            if (isWithinBoard(newRow, newCol)) {
                validSquares[validSquareCount][0] = newRow;
                validSquares[validSquareCount][1] = newCol;
                validSquareCount++;
            }
        }

        return Arrays.copyOf(validSquares, validSquareCount);
    }
    public static int[][] validRookSquares(int row,int col,int[][] grid){

        // Define movement directions (row and column changes)
        int[] rowOffsets = {1, -1, 0, 0}; // Up, down, right, left
        int[] colOffsets = {0, 0, 1, -1};

        return logic.validSquaresInDirection(row,col,grid,rowOffsets,colOffsets);
    }

    public static int[][] validBishopSquares(int row,int col,int[][] grid){

        // Define movement directions (row and column changes)
        int[] rowOffsets = {1, 1, -1, -1};
        int[] colOffsets = {1, -1, 1, -1};

        return logic.validSquaresInDirection(row,col,grid,rowOffsets,colOffsets);
    }
    public static int[][] validQueenSquares(int row,int col,int[][] grid){

        // Define movement directions (row and column changes)
        int[] rowOffsets = {1, 1, -1, -1, 1,-1, 0, 0};
        int[] colOffsets = {1, -1, 1, -1, 0, 0, 1,-1};

        return logic.validSquaresInDirection(row,col,grid,rowOffsets,colOffsets);
    }

    public static int[][] validKingAttackSquares(int row,int col){

        int[][] validSquares = new int[8][2];
        int validSquareCount = 0;

        int[] rowOffsets = {0, 0, 1, 1, 1,-1, -1,-1};
        int[] colOffsets = {1,-1, 1,-1, 0, 1, -1, 0};

        for(int i=0;i<rowOffsets.length;i++){
            int newRow = row + rowOffsets[i];
            int newCol = col + colOffsets[i];

            if(isWithinBoard(newRow,newCol)){
                validSquares[validSquareCount][0] = newRow;
                validSquares[validSquareCount][1] = newCol;
                validSquareCount++;
            }
        }
        return Arrays.copyOf(validSquares,validSquareCount);
    }
    public static int[][] validPawnAttackSquares(int turn,int player,int row,int col){

        int[][] validSquares = new int[2][2];
        int validSquareCount = 0;

        int forwardDirection = ( turn != player ) ? -1 : 1;
        int newRow = row + forwardDirection;
        int newColLeft = col - 1;
        int newColRight = col + 1;

        if(isWithinBoard(newRow,newColLeft)){
            validSquares[validSquareCount][0] = newRow;
            validSquares[validSquareCount][1] = newColLeft;
            validSquareCount++;
        }
        if(isWithinBoard(newRow,newColRight)){
            validSquares[validSquareCount][0] = newRow;
            validSquares[validSquareCount][1] = newColRight;
            validSquareCount++;
        }
        return Arrays.copyOf(validSquares,validSquareCount);
    }

    public static int[] getKingPosition(int[][] grid, int turn) {
        int piece = (turn == WHITE) ? W_KING : B_KING;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (grid[i][j] == piece) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{0, 0};
    }
    public static int[][] copyGrid(int[][] grid){

        int[][] copy = new int[8][8];
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++) copy[i][j] = grid[i][j];
        }
        return copy;
    }
    public static void delay(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void display(int[][] grid){
        for (int i = 0; i < 8; i++) {
            for(int j=0;j<8;j++){
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }
    public static void displayPosition(int[][] positions){
        if(positions == null) return;
        for(int[] pos : positions){
            System.out.println("Row : "+pos[0]+" Col : "+pos[1]);
        }
    }
    public static void displayTime(long startTime){
        long endTime = System.currentTimeMillis();
        long exeTime = endTime - startTime;
        System.out.println("Time : "+exeTime);
    }
}

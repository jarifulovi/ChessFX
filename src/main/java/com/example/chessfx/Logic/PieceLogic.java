package com.example.chessfx.Logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PieceLogic {

    private IllegalMove illegalMove;
    public boolean isEnPassantCapture;
    private int player;

    private int[][] gridPC;  // This keeps en passant and castling positions
    public boolean notWhiteLeftCastle,notWhiteRightCastle,notBlackLeftCastle,notBlackRightCastle;

    public PieceLogic(int player,String FEN){
        illegalMove = new IllegalMove();
        gridPC = logic.setGridPC(FEN);
        this.player = player;
        isEnPassantCapture = false;
        notWhiteLeftCastle = true;
        notWhiteRightCastle = true;
        notBlackLeftCastle = true;
        notBlackRightCastle = true;
    }

    // Only pawns doesn't take own piece positions
    public int[][] allValidMoves(int piece,int row,int col,int[][] grid){

        int[][] validSquares;
        int turn = logic.getPieceColor(piece);


        if(piece == logic.W_PAWN || piece == logic.B_PAWN){
            validSquares = validPawnSquares(row,col,turn,player,grid);
            validSquares = eliminateIllegalMoves(grid,validSquares,row,col);
            return validSquares;
        }
        else if(piece == logic.W_KNIGHT || piece == logic.B_KNIGHT){
            validSquares  = logic.validKnightSquares(row,col,grid,turn);
            validSquares = eliminateIllegalMoves(grid,validSquares,row,col);
            return validSquares;
        }
        else if(piece == logic.W_ROOK || piece == logic.B_ROOK){
            validSquares = logic.validRookSquares(row,col,turn,grid);
            validSquares = eliminateIllegalMoves(grid,validSquares,row,col);
            return validSquares;
        }
        else if(piece == logic.W_BISHOP || piece == logic.B_BISHOP){
            validSquares = logic.validBishopSquares(row,col,turn,grid);
            validSquares = eliminateIllegalMoves(grid,validSquares,row,col);
            return validSquares;
        }
        else if(piece == logic.W_QUEEN || piece == logic.B_QUEEN){
            validSquares = logic.validQueenSquares(row,col,turn,grid);
            validSquares = eliminateIllegalMoves(grid,validSquares,row,col);
            return validSquares;
        }
        else if(piece == logic.W_KING || piece == logic.B_KING){
            validSquares = validKingSquares(row,col,turn,player,grid);
            validSquares = eliminateIllegalMoves(grid,validSquares,row,col);
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


    private int[][] validKingSquares(int row,int col,int turn,int player,int[][] grid){

        int[][] validSquares = new int[8][2];
        int validSquareCount = 0;

        int[] rowOffsets = {0, 0, 1, 1, 1,-1, -1,-1};
        int[] colOffsets = {1,-1, 1,-1, 0, 1, -1, 0};

        for (int i = 0; i < rowOffsets.length; i++) {
            int newRow = row + rowOffsets[i];
            int newCol = col + colOffsets[i];

            if(logic.isWithinBoard(newRow,newCol) && !logic.isOwnPiece(grid[newRow][newCol],turn)){
                validSquares[validSquareCount][0] = newRow;
                validSquares[validSquareCount][1] = newCol;
                validSquareCount++;
            }
        }
        int rightCastleCol = col + 2;
        int leftCastleCol = col - 2;
        if(canCastle(true,row,grid)){
            validSquares[validSquareCount][0] = row;
            validSquares[validSquareCount][1] = rightCastleCol;
            validSquareCount++;
        }
        if(canCastle(false,row,grid)){
            validSquares[validSquareCount][0] = row;
            validSquares[validSquareCount][1] = leftCastleCol;
            validSquareCount++;
        }


        return Arrays.copyOf(validSquares,validSquareCount);
    }
    private boolean canCastle(boolean isRight,int row,int[][] grid){


        boolean noPiece;
        if(isRight) {
            noPiece = (grid[row][5] == logic.NO_PIECE) &&
                    (grid[row][6] == logic.NO_PIECE);

        }
        else
            noPiece = (grid[row][1] == logic.NO_PIECE) && (grid[row][2] == logic.NO_PIECE)
                    && (grid[row][3] == logic.NO_PIECE);

        return (isRight) ? (gridPC[row][7]==logic.CASTLE) && noPiece
                : (gridPC[row][0]==logic.CASTLE) && noPiece;
    }
    private int[][] validPawnSquares(int row,int col,int turn,int player,int[][] grid){

        int[][] validSquares = new int[10][2];
        int validSquareCount = 0;

        int forwardDirection = ( turn == player ) ? -1 : 1;
        int newRow = row + forwardDirection;

        // For one forward push
        if(logic.isWithinBoard(newRow,col)){

            // If no piece on the way
            if(grid[newRow][col] == logic.NO_PIECE){
                validSquares[validSquareCount][0] = newRow;
                validSquares[validSquareCount][1] = col;
                validSquareCount++;
            }
            // Check for capture diagonally forward
            int captureColLeft = col - 1;
            int captureColRight = col + 1;


            if(logic.isWithinBoard(newRow,captureColLeft) &&
                    (logic.isOpponentPiece(grid[newRow][captureColLeft],turn)  || (isEnPassant(newRow,captureColLeft,turn,player)))){
                validSquares[validSquareCount][0] = newRow;
                validSquares[validSquareCount][1] = captureColLeft;
                validSquareCount++;
            }


            if(logic.isWithinBoard(newRow,captureColRight) &&
                    (logic.isOpponentPiece(grid[newRow][captureColRight],turn) || (isEnPassant(newRow,captureColRight,turn,player)))){
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
                    grid[newRow][col] == logic.NO_PIECE && grid[preRow][col] == logic.NO_PIECE){
                validSquares[validSquareCount][0] = newRow;
                validSquares[validSquareCount][1] = col;
                validSquareCount++;

            }
        }
        return Arrays.copyOf(validSquares,validSquareCount);
    }
    public void setEnPassant(int row,int col,int turn){
        gridPC[row][col] = ( turn == logic.WHITE ) ? (logic.WHITE_EN_PASSANT):(logic.BLACK_EN_PASSANT);
    }

    public void resetOpponentEnPassant(int turn){


        int enPassant = ( turn == logic.WHITE ) ? (logic.BLACK_EN_PASSANT) : (logic.WHITE_EN_PASSANT);
        // Reset opponent enPassant positions
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(gridPC[i][j] == enPassant) gridPC[i][j] = logic.NO_EN_PASSANT;
            }
        }
    }
    private boolean isEnPassant(int row,int col,int turn,int player){

        // Row,Col means where the pawn will be moving ( diagonal )
        // UpEnPassant means the capture will be upward according to the board
        boolean upEnPassant = (turn == player);
        int enPassant = ( turn == logic.WHITE ) ? (logic.BLACK_EN_PASSANT) : (logic.WHITE_EN_PASSANT);

        if(logic.isWithinBoard(row,col)){

            if(upEnPassant) return gridPC[row+1][col] == enPassant;
            else            return gridPC[row-1][col] == enPassant;
        }
        return false;
    }

    public void removeEnPassantPiece(int[][] grid,int turn){


        int enPassant = (turn == logic.WHITE) ? (logic.BLACK_EN_PASSANT) : (logic.WHITE_EN_PASSANT);
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(gridPC[i][j] == enPassant) grid[i][j] = logic.NO_PIECE;
            }
        }
    }

    // This method has to call before updating grid
    public void updateEnPassant(int[][] grid,int piece,int preRow,int newRow,int preCol,int newCol,int turn){


        if(piece == logic.W_PAWN || piece == logic.B_PAWN){
            if(Math.abs(preRow - newRow) == 2){

                setEnPassant(newRow,newCol,turn);
            }
            // If en passant capture happens
            // When moves diagonally and new position doesn't have any opponent piece
            if(Math.abs(preRow-newRow)==Math.abs(preCol-newCol)){
                if(!logic.isOpponentPiece(grid[newRow][newCol],turn)){
                    removeEnPassantPiece(grid,turn);
                }
            }
        }
        resetOpponentEnPassant(turn);
    }
    public void updateCastling(int[][] grid,int player,int piece,int preRow,int preCol,int newRow,int newCol){

        int turn = logic.getPieceColor(piece);
        int row = (turn==player) ? 7 : 0;
        if(piece == logic.W_KING || piece == logic.B_KING){
            gridPC[row][0] = logic.NO_CASTLE;
            gridPC[row][7] = logic.NO_CASTLE;

            if(Math.abs(preCol - newCol) == 2 && preRow == newRow)
                updateRook(grid,piece,preRow,preCol,newRow,newCol);
        }
        if(piece == logic.W_ROOK || piece == logic.B_ROOK){
            gridPC[preRow][preCol] = logic.NO_CASTLE;
        }
        // If captured
        if(player == logic.WHITE) {
            if (grid[7][0] != logic.W_ROOK) gridPC[7][0] = logic.NO_CASTLE;
            if (grid[7][7] != logic.W_ROOK) gridPC[7][7] = logic.NO_CASTLE;
            if (grid[0][0] != logic.B_ROOK) gridPC[0][0] = logic.NO_CASTLE;
            if (grid[0][7] != logic.B_ROOK) gridPC[0][7] = logic.NO_CASTLE;
        }
        else {
            if (grid[7][0] != logic.B_ROOK) gridPC[7][0] = logic.NO_CASTLE;
            if (grid[7][7] != logic.B_ROOK) gridPC[7][7] = logic.NO_CASTLE;
            if (grid[0][0] != logic.W_ROOK) gridPC[0][0] = logic.NO_CASTLE;
            if (grid[0][7] != logic.W_ROOK) gridPC[0][7] = logic.NO_CASTLE;
        }
    }
    private void updateRook(int[][] grid,int piece,int preRow,int preCol,int newRow,int newCol){
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

package com.example.chessfx.Logic.Abstract;

import com.example.chessfx.Logic.Engine.BitBoard.Bitboard;
import com.example.chessfx.Logic.Object.Board;
import com.example.chessfx.Logic.Object.Move;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

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
    public static int NO_EN_PASSANT = -1;

    // For indicating players and turns

    public static int WHITE = 0;
    public static int BLACK = 1;

    // For game type
    public static int TWO_PLAYER = 0;
    public static int ONE_PLAYER = 1;

    // Game settings
    public static int GREEN_BOARD = 0;
    public static int BROWN_BOARD = 1;
    public static int BLACK_BOARD = 2;
    public static String defaultWhitePlayerFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static String defaultBlackPlayerFEN = "RNBQKBNR/PPPPPPPP/8/8/8/8/pppppppp/rnbqkbnr w KQkq - 0 1";
    public static String debugFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static String BACKGROUND_CSS = "-fx-background-color :";
    public static String FOREST_GREEN = "#69923e";
    public static String LIGHT_GREEN = "#baca44";
    public static String DARK_GREEN = "#4e7837";
    public static String GRAY = "#4b4847";
    public static String DIM_GRAY = "#696969";
    public static String BROWN = "#ab8149";

    public static String BLACK_COLOR = "#000000";
    public static String WHITE_COLOR = "#ffffff";

    // Engine related constants
    public static int OPENING_PHASE = 0;
    public static int MIDDLEGAME_PHASE = 1;
    public static int ENDGAME_PHASE = 2;

    public static boolean isWithinBoard(int row,int col){
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
    public static boolean isWithinBoard(int index) { return (index >=0 && index < 64);}
    public static boolean isOwnPiece(int piece,int turn){
        if(piece == NO_PIECE) return false;
        return (turn == WHITE) == (piece <= 6);
    }
    public static boolean isSquareEmpty(long[] bitboards, int newIndex) {
        return (bitboards[logic.NO_PIECE] & (1L << newIndex)) != 0;
    }
    public static boolean isSquareOccupied(long targetBitboard, int newIndex) {
        return (targetBitboard & (1L << newIndex)) != 0;
    }
    public static boolean isOpponentPiece(int piece,int turn){
        if(piece == NO_PIECE) return false;
        return (turn != WHITE) == (piece <= 6);
    }
    public static boolean isPawnPromoting(int piece,int newRow){

        return (piece == W_PAWN || piece == B_PAWN) && (newRow == 0 || newRow == 7);
    }
    public static int getPieceValue(int piece) {
        if (piece == logic.W_PAWN || piece == logic.B_PAWN) return 100;
        if (piece == logic.W_KNIGHT || piece == logic.B_KNIGHT) return 300;
        if (piece == logic.W_BISHOP || piece == logic.B_BISHOP) return 300;
        if (piece == logic.W_ROOK || piece == logic.B_ROOK) return 500;
        if (piece == logic.W_QUEEN || piece == logic.B_QUEEN) return 900;
        return 0; // Empty square
    }
    public static void updatePieceValues(Board board, Move move){

        if(!move.isPromotingPiece) {
            int capturedPiece = board.grid[move.newRow][move.newCol];
            if (capturedPiece != logic.NO_PIECE) {
                if (logic.getPieceColor(capturedPiece) == logic.WHITE) {
                    board.whitePieceValues -= logic.getPieceValue(capturedPiece);
                } else {
                    board.blackPieceValues -= logic.getPieceValue(capturedPiece);
                }
            }
        }
        else {
            if (logic.getPieceColor(move.piece) == logic.WHITE) {
                // White pawn promotes
                board.whitePieceValues += logic.getPieceValue(move.promotedPiece) - logic.getPieceValue(move.piece);
            } else if (logic.getPieceColor(move.piece) == logic.BLACK) {
                // Black pawn promotes
                board.blackPieceValues += logic.getPieceValue(move.promotedPiece) - logic.getPieceValue(move.piece);
            }
        }
    }
    public static int getNewPiecePP(StackPane square,int turn) {
        int index = Integer.parseInt(square.getId());
        int newPiece = logic.NO_PIECE;

        if(turn == logic.WHITE){
            if     (index == 0) newPiece = logic.W_KNIGHT;
            else if(index == 1) newPiece = logic.W_BISHOP;
            else if(index == 2) newPiece = logic.W_QUEEN;
            else if(index == 3) newPiece = logic.W_ROOK;
        }
        else{
            if     (index == 0) newPiece = logic.B_KNIGHT;
            else if(index == 1) newPiece = logic.B_BISHOP;
            else if(index == 2) newPiece = logic.B_QUEEN;
            else if(index == 3) newPiece = logic.B_ROOK;
        }
        return newPiece;
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
    public static Board convertFENIntoBoard(String FEN){

        String[] parts = FEN.split(" ");
        String pieceParts = parts[0];
        String turnPart = parts[1];
        String castleParts = parts[2];
        String enPassantPart = parts[3];
        Board board = new Board();

        int row = 0;
        int col = 0;

        for(char ch : pieceParts.toCharArray()){

            if(Character.isDigit(ch)){
                col += Character.getNumericValue(ch);
            }
            else if(Character.isLetter(ch)){
                int piece = getPieceNumber(ch);
                board.grid[row][col] = piece;
                col++;
            }

            if(col == 8){
                row++;
                col = 0;
            }
        }
        board.currentTurn = (turnPart.equals("w")) ? logic.WHITE : logic.BLACK;
        // Handle castling rights
        board.canWhiteLeftCastled = castleParts.contains("Q");
        board.canWhiteRightCastled = castleParts.contains("K");
        board.canBlackLeftCastled = castleParts.contains("q");
        board.canBlackRightCastled = castleParts.contains("k");

        // set board.enPassantIndex
        if(!enPassantPart.equals("-")){
            int enPassantCol = (enPassantPart.charAt(0) - 'a');
            int enPassantRank = Character.getNumericValue(enPassantPart.charAt(1));
            int enPassantRow = (board.currentTurn == logic.WHITE) ?
                    8 - enPassantRank : enPassantRank - 1;
            board.enPassantIndex = enPassantRow * 8 + enPassantCol;
        }
        else {
            board.enPassantIndex = NO_EN_PASSANT;
        }
        return board;
    }
    private static int getPieceNumber(char piece) {
        // Map character to piece value
        return switch (piece) {
            case 'p' -> logic.B_PAWN;
            case 'r' -> logic.B_ROOK;
            case 'n' -> logic.B_KNIGHT;
            case 'b' -> logic.B_BISHOP;
            case 'q' -> logic.B_QUEEN;
            case 'k' -> logic.B_KING;
            case 'P' -> logic.W_PAWN;
            case 'R' -> logic.W_ROOK;
            case 'N' -> logic.W_KNIGHT;
            case 'B' -> logic.W_BISHOP;
            case 'Q' -> logic.W_QUEEN;
            case 'K' -> logic.W_KING;
            default -> logic.NO_PIECE;
        };
    }

    public static boolean validSquareClick(int[][] positions,int row,int col){

        for(int[] pos : positions){
            if(pos[0] == row && pos[1] == col) return true;
        }
        return false;
    }

    public static int[][] validKnightSquares(int row, int col,int[][] grid,int turn) {
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
            if (isWithinBoard(newRow,newCol) && !logic.isOwnPiece(grid[newRow][newCol],turn)) {

                validSquares[validSquareCount][0] = newRow;
                validSquares[validSquareCount][1] = newCol;
                validSquareCount++;
            }
        }

        return Arrays.copyOf(validSquares, validSquareCount);
    }
    public static int[][] validSquaresInDirection(int row, int col,int turn,int[][] grid, int[] rowOffsets, int[] colOffsets) {
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

            // If first occupied piece is opponent piece
            if (isWithinBoard(newRow, newCol) && logic.isOpponentPiece(grid[newRow][newCol],turn)) {
                validSquares[validSquareCount][0] = newRow;
                validSquares[validSquareCount][1] = newCol;
                validSquareCount++;
            }
        }

        return Arrays.copyOf(validSquares, validSquareCount);
    }
    public static int[][] validRookSquares(int row,int col,int turn,int[][] grid){

        // Define movement directions (row and column changes)
        int[] rowOffsets = {1, -1, 0, 0}; // Up, down, right, left
        int[] colOffsets = {0, 0, 1, -1};

        return logic.validSquaresInDirection(row,col,turn,grid,rowOffsets,colOffsets);
    }

    public static int[][] validBishopSquares(int row,int col,int turn,int[][] grid){

        // Define movement directions (row and column changes)
        int[] rowOffsets = {1, 1, -1, -1};
        int[] colOffsets = {1, -1, 1, -1};

        return logic.validSquaresInDirection(row,col,turn,grid,rowOffsets,colOffsets);
    }
    public static int[][] validQueenSquares(int row,int col,int turn,int[][] grid){

        // Define movement directions (row and column changes)
        int[] rowOffsets = {1, 1, -1, -1, 1,-1, 0, 0};
        int[] colOffsets = {1, -1, 1, -1, 0, 0, 1,-1};

        return logic.validSquaresInDirection(row,col,turn,grid,rowOffsets,colOffsets);
    }

    public static boolean isKingInCheck(int[][] grid,int turn,int player){

        int[] kingPos = logic.getKingPosition(grid,turn);
        int row = kingPos[0];
        int col = kingPos[1];
        boolean isKingCheck;

        isKingCheck = rookAndQueenCheck(grid,turn,row,col) || bishopAndQueenCheck(grid,turn,row,col)
                        || knightCheck(grid,turn,row,col) || pawnCheck(grid,player,turn,row,col)
                            || kingCheck(grid,turn,row,col);


        return isKingCheck;
    }
    public static boolean isCheckSquare(int[][] grid,int row,int col,int turn,int player){

        return rookAndQueenCheck(grid,turn,row,col) || bishopAndQueenCheck(grid,turn,row,col)
                || knightCheck(grid,turn,row,col) || pawnCheck(grid,player,turn,row,col)
                || kingCheck(grid,turn,row,col);
    }
    private static boolean rookAndQueenCheck(int[][] grid,int turn,int row,int col){
        for (int i = row + 1; i < 8; i++) {
            int piece = grid[i][col];
            if (piece != logic.NO_PIECE) {
                if (logic.isOpponentPiece(piece, turn)) {
                    if (piece == logic.W_ROOK || piece == logic.B_ROOK ||
                            piece == logic.W_QUEEN || piece == logic.B_QUEEN) {
                        return true;
                    }
                }
                break;
            }
        }
        for (int i = row - 1; i >= 0; i--) {
            int piece = grid[i][col];
            if (piece != logic.NO_PIECE) {
                if (logic.isOpponentPiece(piece, turn)) {
                    if (piece == logic.W_ROOK || piece == logic.B_ROOK ||
                            piece == logic.W_QUEEN || piece == logic.B_QUEEN) {
                        return true;
                    }
                }
                break;
            }
        }
        for(int i = col + 1; i < 8; i++){
            int piece = grid[row][i];
            if(piece != logic.NO_PIECE){
                if(logic.isOpponentPiece(piece,turn)){
                    if (piece == logic.W_ROOK || piece == logic.B_ROOK ||
                            piece == logic.W_QUEEN || piece == logic.B_QUEEN) {
                        return true;
                    }
                }
                break;
            }
        }
        for(int i = col - 1; i >= 0; i--){
            int piece = grid[row][i];
            if(piece != logic.NO_PIECE){
                if(logic.isOpponentPiece(piece,turn)){
                    if (piece == logic.W_ROOK || piece == logic.B_ROOK ||
                            piece == logic.W_QUEEN || piece == logic.B_QUEEN) {
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }
    private static boolean bishopAndQueenCheck(int[][] grid,int turn,int row,int col){
        // Check for diagonal checks (downward-right)
        for (int i = row + 1, j = col + 1; i < 8 && j < 8; i++, j++) {
            int piece = grid[i][j];
            if (piece != logic.NO_PIECE) {
                if (logic.isOpponentPiece(piece, turn)) {
                    if (piece == logic.W_BISHOP || piece == logic.B_BISHOP ||
                            piece == logic.W_QUEEN || piece == logic.B_QUEEN) {
                        return true;
                    }
                }
                break;
            }
        }
        // (downward-left)
        for (int i = row + 1, j = col - 1; i < 8 && j >= 0; i++, j--) {
            int piece = grid[i][j];
            if (piece != logic.NO_PIECE) {
                if (logic.isOpponentPiece(piece, turn)) {
                    if (piece == logic.W_BISHOP || piece == logic.B_BISHOP ||
                            piece == logic.W_QUEEN || piece == logic.B_QUEEN) {
                        return true;
                    }
                }
                break;
            }
        }
        // (upward-right)
        for (int i = row - 1, j = col + 1; i >= 0 && j < 8; i--, j++) {
            int piece = grid[i][j];
            if (piece != logic.NO_PIECE) {
                if (logic.isOpponentPiece(piece, turn)) {
                    if (piece == logic.W_BISHOP || piece == logic.B_BISHOP ||
                            piece == logic.W_QUEEN || piece == logic.B_QUEEN) {
                        return true;
                    }
                }
                break;
            }
        }
        // (upward-left)
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            int piece = grid[i][j];
            if (piece != logic.NO_PIECE) {
                if (logic.isOpponentPiece(piece, turn)) {
                    if (piece == logic.W_BISHOP || piece == logic.B_BISHOP ||
                            piece == logic.W_QUEEN || piece == logic.B_QUEEN) {
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }
    private static boolean knightCheck(int[][] grid,int turn,int row,int col){

        // Check for knight attacks
        int[][] knightOffsets = {{-2, -1}, {-1, -2}, {-2, 1}, {-1, 2}, {1, -2}, {2, -1}, {1, 2}, {2, 1}};
        for (int[] offset : knightOffsets) {
            int newRow = row + offset[0];
            int newCol = col + offset[1];

            if (isWithinBoard(newRow, newCol)) {
                int piece = grid[newRow][newCol];
                if (logic.isOpponentPiece(piece, turn) && (piece == logic.W_KNIGHT || piece == logic.B_KNIGHT)) {
                    return true;
                }
            }
        }
        return false;
    }
    private static boolean pawnCheck(int[][] grid,int player,int turn,int row,int col){

        // if turn == player then pawn attacks upward else downward diagonal
        int direction = (turn==player) ? -1 : 1;
        int newRow = row + direction;
        // Check diagonal left
        int newCol = col - 1;
        if (isWithinBoard(newRow, newCol)){
            if(logic.isOpponentPiece(grid[newRow][newCol], turn) && (grid[newRow][newCol] == W_PAWN || grid[newRow][newCol] == B_PAWN)) {
                return true;
            }
        }

        // Check diagonal right
        newRow = row + direction;
        newCol = col + 1;
        if (isWithinBoard(newRow, newCol)){
            return logic.isOpponentPiece(grid[newRow][newCol], turn) && (grid[newRow][newCol] == W_PAWN || grid[newRow][newCol] == B_PAWN);
        }


        return false;
    }
    private static boolean kingCheck(int[][] grid,int turn,int row,int col){

        int[][] kingOffsets = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        for (int[] offset : kingOffsets) {
            int newRow = row + offset[0];
            int newCol = col + offset[1];

            if (isWithinBoard(newRow, newCol)) {
                int piece = grid[newRow][newCol];
                if (logic.isOpponentPiece(piece, turn) && (piece == logic.W_KING || piece == logic.B_KING)) {
                    return true;
                }
            }
        }
        return false;
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
    public static boolean isCastle(int piece,int preCol,int newCol){
        return ((piece == W_KING || piece == B_KING) && (Math.abs(preCol - newCol) == 2));
    }
    public static boolean isCapture(Board board,Move move){
        return board.grid[move.newRow][move.newCol] != NO_PIECE;
    }
    public static int[][] copyGrid(int[][] grid){

        int[][] copy = new int[8][8];
        for(int i=0;i<8;i++){
            System.arraycopy(grid[i], 0, copy[i], 0, 8);
        }
        return copy;
    }
    public static int[][] copyOpponentGrid(int[][] grid){

        int[][] copy = new int[8][8];
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                copy[i][j] = grid[7-i][7-j];
            }
        }
        return copy;
    }
    public static void delay(int milliseconds, Runnable onFinish) {

        Platform.runLater(() -> {
            // Create a Timeline to handle the delay
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(milliseconds), event -> {
                // Execute the provided Runnable after the delay
                if (onFinish != null) {
                    onFinish.run();
                }
            }));
            timeline.setCycleCount(1);
            timeline.play();
        });
    }
    public static void display(int[][] grid){
        for (int i = 0; i < 8; i++) {
            for(int j=0;j<8;j++){
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }
    public static int[][] convertMoveToPosition(List<Move> moves){
        int[][] pos = new int[moves.size()][2];
        int i = 0;
        for(Move move : moves){
            pos[i][0] = move.newIndex / 8;
            pos[i][1] = move.newIndex % 8;
            i++;
        }
        return pos;
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

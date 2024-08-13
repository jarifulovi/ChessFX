package com.example.chessfx.Logic;


import com.example.chessfx.Logic.Abstract.logic;
import com.example.chessfx.Logic.Object.Board;
import com.example.chessfx.Logic.Object.Move;

public class GridLogic {

    private Board board;
    private PieceLogic pieceLogic;

    public GridLogic(int player){
        board = new Board();

        String FEN;
        if(player == logic.WHITE)
            FEN = logic.defaultWhitePlayerFEN;
        else
            FEN = logic.defaultBlackPlayerFEN;

        board = logic.convertFENIntoBoard(FEN);

        pieceLogic = new PieceLogic(player);
    }
    public int[][] getGrid(){
        return this.board.grid;
    }
    public Board getBoard(){
        return board;
    }
    public boolean isOwnPieceClick(int row,int col,int turn){

        return logic.isOwnPiece(board.grid[row][col],turn);
    }
    public int clickPiece(int row,int col){
        return board.grid[row][col];
    }
    public boolean isThreeFoldRepitionHappens(){
        return false;
    }
    public int[][] getValidPositions(int row,int col){

        return pieceLogic.allValidMoves(board,board.grid[row][col],row,col);
    }

    // For computer moves
    public int[][][][] getAllValidPositionsOnBoard(int turn) {

        int[][][][] allPositionGrid = new int[8][8][0][0];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int piece = board.grid[i][j];
                if (logic.isOwnPiece(piece, turn)) {
                    // Get valid moves for current piece
                    allPositionGrid[i][j] = pieceLogic.allValidMoves(board,piece, i, j);
                }
            }
        }
        return allPositionGrid;
    }

    public boolean noValidSquareForOpponent(int[][] grid, int opponent){

        int[][][][] allPositionGrid;

        allPositionGrid = getAllValidPositionsOnBoard(opponent);

        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                int piece = grid[i][j];
                if(logic.isOwnPiece(piece,opponent)){
                    if(allPositionGrid[i][j].length > 0) return false;
                }
            }
        }
        return true;
    }

    public void updateGrid(int player, Move move){
        // when a valid move is made by player or engine
        int turn = logic.getPieceColor(move.piece);
        board.update(move);

        // Set and reset en passant ( must call before update )
        pieceLogic.updateEnPassant(board,move.piece,move.preRow,move.newRow,move.preCol,move.newCol,turn);


        // Updating grid
        if(move.isPromotingPiece){
            board.grid[move.preRow][move.preCol] = logic.NO_PIECE;
            board.grid[move.newRow][move.newCol] = move.promotedPiece;
        }
        else {
            board.grid[move.preRow][move.preCol] = logic.NO_PIECE;
            board.grid[move.newRow][move.newCol] = move.piece;
        }


        // Update castling
        pieceLogic.updateCastling(board,player,move.piece,move.preRow,move.preCol,move.newRow,move.newCol);
        if(board.hasGreatPieceMaterialAdvantage(turn)) System.out.println("Endgame happens");
    }

}

package com.example.chessfx.Logic;


public class GridLogic {

    private int[][] grid;
    private PieceLogic pieceLogic;

    public GridLogic(int player){
        grid = new int[8][8];

        String FEN = "";
        if(player == logic.WHITE)
            FEN = logic.defaultWhitePlayerFEN;
        else
            FEN = logic.defaultBlackPlayerFEN;

        grid = logic.convertFENIntoGrid(FEN);

        pieceLogic = new PieceLogic(player,FEN);
    }
    public int[][] getGrid(){
        return this.grid;
    }
    public boolean isOwnPieceClick(int row,int col,int turn){

        return logic.isOwnPiece(grid[row][col],turn);
    }
    public int clickPiece(int row,int col){
        return grid[row][col];
    }
    public int[][] getValidPositions(int row,int col,int turn){

        return pieceLogic.allValidMoves(grid[row][col],row,col,grid);
    }

    // For computer moves
    public int[][][][] getAllValidPositionsOnBoard(int turn) {

        int[][][][] allPositionGrid = new int[8][8][0][0];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int piece = grid[i][j];
                if (logic.isOwnPiece(piece, turn)) {
                    // Get valid moves for current piece
                    allPositionGrid[i][j] = pieceLogic.allValidMoves(piece, i, j, grid);
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

    public void updateGrid(int player,int piece,int preRow,int preCol,int newRow,int newCol){
        // when a valid move is made by player or engine
        int turn = logic.getPieceColor(piece);

        // Set and reset en passant ( must call before update )
        pieceLogic.updateEnPassant(grid,piece,preRow,newRow,preCol,newCol,turn);


        // Updating grid
        grid[preRow][preCol] = logic.NO_PIECE;
        grid[newRow][newCol] = piece;



        // Update castling
        pieceLogic.updateCastling(grid,player,piece,preRow,preCol,newRow,newCol);
    }


    // For debugging
    public static void main(String[] args) {

        GridLogic gridLogic = new GridLogic(1);
        logic.display(gridLogic.grid);
    }

}

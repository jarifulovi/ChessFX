package com.example.chessfx.Logic;


public class GridLogic {

    private int[][] grid;
    private PieceLogic pieceLogic;

    public GridLogic(int player){
        grid = new int[8][8];
        grid = logic.setPieces(player);
        pieceLogic = new PieceLogic(player);
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
    public boolean isCheck(int player,int opponent,int piece,int row,int col){

        // Piece is current turn piece
        // Opponent is not current turn
        if(piece == logic.W_KING || piece == logic.B_KING){
            return false;
        }

        int[] kingPos = logic.getKingPosition(grid,opponent);

        if(piece == logic.W_PAWN || piece == logic.B_PAWN){
            int turn = logic.getPieceColor(piece);
            int[][] positions = logic.validPawnAttackSquares(turn,player,row,col);
            return logic.isWithinPosition(positions,kingPos);
        }
        if(piece == logic.W_KNIGHT || piece == logic.B_KNIGHT){
            int[][] positions = logic.validKnightSquares(row,col);
            return logic.isWithinPosition(positions,kingPos);
        }

        boolean diagonal = logic.isDiagonal(kingPos[0],kingPos[1],row,col);
        boolean straight = logic.isStraight(kingPos[0],kingPos[1],row,col);
        if(piece == logic.W_QUEEN || piece == logic.B_QUEEN){
            return diagonal || straight;
        }
        else if(piece == logic.W_BISHOP || piece == logic.B_BISHOP){
            return diagonal;
        }
        else if(piece == logic.W_ROOK || piece == logic.B_ROOK){
            return straight;
        }
        return false;
    }
    public void updateGrid(int piece,int preRow,int preCol,int newRow,int newCol){
        // when a valid move is made by player or engine
        int turn = logic.getPieceColor(piece);

        // Set and reset en passant ( must call before update )
        pieceLogic.updateEnPassant(grid,piece,preRow,newRow,preCol,newCol,turn);


        // Updating grid
        grid[preRow][preCol] = logic.NO_PIECE;
        grid[newRow][newCol] = piece;



        // Update castling
        pieceLogic.updateCastling(grid,piece,preRow,preCol,newRow,newCol);
    }


    // For debugging
    public static void main(String[] args) {

        GridLogic gridLogic = new GridLogic(1);
        logic.display(gridLogic.grid);
    }

}

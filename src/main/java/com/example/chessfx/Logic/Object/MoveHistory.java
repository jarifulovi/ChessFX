package com.example.chessfx.Logic.Object;

public class MoveHistory {

    private Move[] lastMoves;
    private int currentIndex;
    public int moveCount;

    public MoveHistory(MoveHistory moveHistory){
        this.lastMoves = new Move[6];
        for(int i=0;i<moveHistory.moveCount;i++){
            this.lastMoves[i] = new Move(moveHistory.lastMoves[i]);
        }
        this.moveCount = moveHistory.moveCount;
        this.currentIndex = moveHistory.currentIndex;
    }
    public MoveHistory(){
        lastMoves = new Move[6];
        currentIndex = -1;
        moveCount = 0;
    }
    public Move[] getLastMoves(){
        return lastMoves;
    }
    public void addMove(Move move){
        currentIndex = (currentIndex + 1) % 6;
        lastMoves[currentIndex] = new Move(move);
        if(moveCount < 6) moveCount++;
    }

    public boolean isThreeFoldRepition(){
        if(moveCount < 6) return false;

        int preIndex = ((currentIndex - 4) + 6) % 6;
        int opponentCurrentIndex = ((currentIndex - 1) + 6) % 6;
        int opponentPreIndex = ((opponentCurrentIndex - 4) + 6) % 6;
        return lastMoves[currentIndex].equals(lastMoves[preIndex]) &&
                lastMoves[opponentCurrentIndex].equals(lastMoves[opponentPreIndex]);
    }
}

package entities;

public class Board {
    private Field[][] board;
    private int ninjasOnBoardQuantity;

    public Board( int boardSize) {
        this.board = new Field[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j]=new Field();
            }
        }
    }

    public void placeNinja(int column, int row, Ninja ninja){
        board[row][column].setNinja(ninja);
        ninjasOnBoardQuantity++;
    }

    public void eliminateNinja(int column, int row){
        board[row][column].setNinja(null);
        ninjasOnBoardQuantity--;
    }

    public int getNinjasOnBoardQuantity() {
        return ninjasOnBoardQuantity;
    }

    public void setNinjasOnBoardQuantity(int ninjasOnBoard) {
        this.ninjasOnBoardQuantity = ninjasOnBoard;
    }

    public Field[][] getBoard() {
        return board;
    }
}

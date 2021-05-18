package entities;

public class Board {
    private Field[][] board;

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
    }

    public Field[][] getBoard() {
        return board;
    }
}

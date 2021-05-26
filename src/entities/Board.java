package entities;

public class Board {
    private Field[][] fields;
    private int boardSize;
    private int ninjasOnBoardQuantity;

    public Board( int boardSize) {
        this.boardSize = boardSize;
        this.fields = new Field[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                fields[i][j]=new Field();
            }
        }
    }

    public void placeNinja(int column, int row, Ninja ninja){
        fields[row][column].setNinja(ninja);
        fields[row][column].setTransitable(false);
        ninjasOnBoardQuantity++;
    }

    public void eliminateNinja(int column, int row){
        if (fields[row][column].getNinja() != null){
            fields[row][column].setNinja(null);
            fields[row][column].setTransitable(true);
            ninjasOnBoardQuantity--;
        }
    }

    public void clearBoard(){
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                eliminateNinja(i,j);
            }
        }
    }

    public int getNinjasOnBoardQuantity() {
        return ninjasOnBoardQuantity;
    }

    public void setNinjasOnBoardQuantity(int ninjasOnBoard) {
        this.ninjasOnBoardQuantity = ninjasOnBoard;
    }

    public Field[][] getFields() {
        return fields;
    }

    public int getBoardSize() {
        return boardSize;
    }
}

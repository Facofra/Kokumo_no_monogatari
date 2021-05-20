package entities;

import enums.GameMode;

import java.util.List;

public class Player {
    private String name;
    private String ip;
    private Board board;
    private GameMode gameMode;


    public Player(String name, String ip, GameMode gameMode, Board board) {
        this.name = name;
        this.ip = ip;
        this.gameMode = gameMode;
        this.board = board;
    }

    public void move(int direction){

    }

    public void attack(int row, char column, Player opponent){
        Field[][] opponentBoard = opponent.getBoard().getBoard();
        int columnInt = column - 65;
        if (opponentBoard[row][columnInt] == null ){
            opponentBoard[row][columnInt].setTransitable(false);
        } else{
            opponentBoard[row][columnInt].setNinja(null);
        }

    }

    public int getNinjasOnBoardQuantity(){
        return board.getNinjasOnBoardQuantity();
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public Board getBoard() {
        return board;
    }

    public GameMode getGameMode() {
        return gameMode;
    }
}

package entities;

import enums.GameMode;
import enums.NinjaType;

import java.util.List;

public class Player {
    private String name;
    private Board board;
    private  Board opponentBoard;
    private GameMode gameMode;


    public Player(String name, GameMode gameMode, Board board, Board opponentBoard, int numberOfNinjas) {
        this.name = name;
        this.gameMode = gameMode;
        this.board = board;
        this.opponentBoard = opponentBoard;
    }

    public int getNinjasOnBoardQuantity(){
        return board.getNinjasOnBoardQuantity();
    }

    public String getName() {
        return name;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Board getOpponentBoard() {
        return opponentBoard;
    }

    public Ninja getNinjaFromBoard(int row, int column){
        return board.getFields()[row][column].getNinja();
    }

    public boolean isCommanderAlive(){
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if (board.getFields()[i][j].getNinja() != null){
                    if (board.getFields()[i][j].getNinja().getNinjaType() == NinjaType.COMMANDER){
                        return board.getFields()[i][j].getNinja().getLives() > 0;
                    }
                }
            }
        }
        return false;
    }
}

package entities;

import enums.GameMode;
import enums.NinjaType;

import java.util.List;

public class Player {
    private String name;
    private String ip;
    private Board board;
    private  Board opponentBoardImage;
    private Ninja[] ninjas;
    private GameMode gameMode;


    public Player(String name, String ip, GameMode gameMode, Board board, Board opponentBoardImage, int numberOfNinjas) {
        this.name = name;
        this.ip = ip;
        this.gameMode = gameMode;
        this.board = board;
        this.opponentBoardImage = opponentBoardImage;
        this.ninjas = new Ninja[numberOfNinjas];
    }

    public void move(int direction){

    }

    public void attack(int row, int column, Player opponent){
        Field[][] opponentBoard = opponent.getBoard().getFields();
        if (opponentBoard[row][column] == null ){
            opponentBoard[row][column].setTransitable(false);
        } else{
            opponentBoard[row][column].setNinja(null);
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

    public Board getOpponentBoardImage() {
        return opponentBoardImage;
    }

    public Ninja[] getNinjas() {
        return ninjas;
    }

    public void setNinja(int index, Ninja ninja, int row, int column) {
        ninja.setColumnPosition(column);
        ninja.setRowPosition(row);
        ninjas[index] = ninja;
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

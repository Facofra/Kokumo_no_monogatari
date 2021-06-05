package managers;

import entities.Board;
import entities.Ninja;
import entities.Player;
import enums.GameMode;

public class PlayerManager {

    public void initializePlayer(Player[] players, int mode,int boardSize, String name, String ip, int numberOfNinjas){
        GameMode gameMode = mode == 1 ? GameMode.SERVER : GameMode.CLIENT;
        Board board = new Board(boardSize);
        Board opponentBoard = new Board(boardSize);
        Player player= new Player(name,ip, gameMode,board,opponentBoard,numberOfNinjas);

        if (player.getGameMode() == GameMode.SERVER){
            players[0] = player;
        }else{
            players[1] = player;
        }
    }

    public void placeNinjaOnBoard(int column, int row, Ninja ninja, Player player){
        player.getBoard().placeNinja(column,row,ninja);
    }

    public void eliminateNinjaFromBoard(int row, int column, Player player){
        player.getBoard().eliminateNinja(column,row);
    }

    public void killNinja(int row, int column, Player player){
        player.getBoard().killNinja(row,column);
    }

    public void clearBoard(Player player){
        player.getBoard().clearBoard();
    }

    public Ninja getNinjaFromBoard(int row, int column, Player player){
        return player.getBoard().getFields()[row][column].getNinja();
    }

    public boolean getNinjaHasActed(int row, int column, Player player){
        return getNinjaFromBoard(row,column,player).hasActed();
    }

    public void setNinjaHasActed(boolean hasActed, int row, int column, Player player){
        getNinjaFromBoard(row,column,player).setHasActed(hasActed);
    }

    public void setNinjaCanMove(boolean canMove, int row, int column, Player player){
        getNinjaFromBoard(row,column,player).setCanMove(canMove);
    }

    public boolean isTargetTransitable(int row, int column, Player player){
        return player.getOpponentBoard().getFields()[row][column].isTransitable();
    }

    public void setTargetTransitable(boolean isTransitable,int row, int column, Player player){
        player.getOpponentBoard().getFields()[row][column].setTransitable(isTransitable);
    }

    public void setTransitable(boolean isTransitable,int row, int column, Player player){
        player.getBoard().getFields()[row][column].setTransitable(isTransitable);
    }
}

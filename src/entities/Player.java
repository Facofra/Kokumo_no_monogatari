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

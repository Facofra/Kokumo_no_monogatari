package connection;

import entities.Board;
import entities.Player;

public class Message {
    private String ip;
    private String name;
    private Player player;
    private Board boardPlayer1;
    private Board boardPlayer2;
    private boolean playing=true;
    private boolean captainAttacked;

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isCaptainAttacked() {
        return captainAttacked;
    }

    public void setCaptainAttacked(boolean captainAttacked) {
        this.captainAttacked = captainAttacked;
    }

    public Board getBoardPlayer1() {
        return boardPlayer1;
    }

    public void setBoardPlayer1(Board boardPlayer1) {
        this.boardPlayer1 = boardPlayer1;
    }

    public Board getBoardPlayer2() {
        return boardPlayer2;
    }

    public void setBoardPlayer2(Board boardPlayer2) {
        this.boardPlayer2 = boardPlayer2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

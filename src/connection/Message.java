package connection;

import entities.Player;

public class Message {
//    acá van los atributos del mensaje que quiera mandar
    private String ip;
    private String name;
    private String[] atacks= new String[3];
    private boolean waiting;
    private Player player;

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

    public String[] getAtacks() {
        return atacks;
    }

    public void setAtacks(String[] atacks) {
        this.atacks = atacks;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

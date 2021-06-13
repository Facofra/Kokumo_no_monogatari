package connection;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Server {
    MessageHandler messageHandler = new MessageHandler();
    WaitingHandler waitingHandler = new WaitingHandler();
    HttpServer server;
    private String ip;
    private String playerName;
    private int port=8000;

    public void start() throws Exception {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", messageHandler);
        server.createContext("/waiting",waitingHandler );
        server.setExecutor(null);
        server.start();

    }
    public void sendMessage(String message){
        messageHandler.setMessage(message);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void stop(){
        server.stop(0);
    }

}

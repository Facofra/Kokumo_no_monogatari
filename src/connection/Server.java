package connection;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;

public class Server {
    MessageHandler messageHandler = new MessageHandler();
    HttpServer server;
    boolean yourTurn;

    public void start() throws Exception {
        server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", messageHandler);
        server.setExecutor(null);
        server.start();


    }
    public void sendMessage(String message){
        messageHandler.setMessage(message);
        yourTurn=true;
    }
}

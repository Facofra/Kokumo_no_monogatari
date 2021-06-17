package connection;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class MessageHandler implements HttpHandler {
    private String message="Bienvenido";
    private static String opponentIp;

    public void handle(HttpExchange t) throws IOException {
        String response = message;
        opponentIp=t.getRemoteAddress().getAddress().getHostAddress();

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public String getMessage() {
        return message;
    }

    public static String getOpponentIp() {
        return opponentIp;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}

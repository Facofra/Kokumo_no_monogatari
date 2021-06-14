package connection;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class WaitingHandler implements HttpHandler {
    private String message="Bienvenido";
    private static boolean waiting=true;

    public void handle(HttpExchange t) throws IOException {
        String response = message;
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();

        waiting=false;
    }

    public static boolean isWaiting() {
        return waiting;
    }

    public static void setWaiting(boolean waiting) {
        WaitingHandler.waiting = waiting;
    }
}

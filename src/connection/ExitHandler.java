package connection;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class ExitHandler implements HttpHandler {
    private String message="Exit";
    private static boolean exit=false;

    public void handle(HttpExchange t) throws IOException {
        String response = message;
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();

        exit=true;
    }

    public static boolean isExit() {
        return exit;
    }

    public static void setExit(boolean exit) {
        ExitHandler.exit = exit;
    }
}


import connection.Client;
import connection.Server;

import java.io.IOException;
public class Main {
    public static void main(String[] args) {
        try{
            Server.start();
            Client client = new Client();
            System.out.println(client.getJSON("http://192.168.0.8:8000/get",1000));

        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        final int boardSize=5;
        final int numberOfNinjas = 3;
        Game game = new Game(boardSize,numberOfNinjas);
        game.run();






    }
}

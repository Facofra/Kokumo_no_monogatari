
import connection.Client;
import connection.Server;

import java.io.IOException;
public class Main {
    public static void main(String[] args) {
//        esto del server es totalmente de prueba.
        Client client = new Client();
        Server server = new Server();
        try{
            server.start();
            System.out.println(client.recieveMessage());

            server.sendMessage("otra cosa mariposa");
            System.out.println(client.recieveMessage());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }


        final int boardSize=5;
        final int numberOfNinjas = 3;
        Game game = new Game(boardSize,numberOfNinjas);
        game.run();






    }
}

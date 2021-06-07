
import connection.Client;
import connection.Server;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        final int boardSize=5;
        final int numberOfNinjas = 3;
        Game game = new Game(boardSize,numberOfNinjas);
        game.run();

    }
}

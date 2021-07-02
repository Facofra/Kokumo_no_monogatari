package factories;

import Game.Game;
import connection.Client;
import connection.Server;
import entities.Player;
import entities.Screen;
import validators.Validator;

import java.util.Scanner;

public abstract class GameFactory {
    public static Game createGame(int boardSize, int numberOfNinjas){

        Scanner input = new Scanner(System.in);
        Player[] players = new Player[2];
        Validator validator = new Validator(boardSize, numberOfNinjas);
        Screen screen = new Screen(boardSize, numberOfNinjas,validator);
        Server server = new Server();
        Client client = new Client();

        return new Game(server, client, screen, players, input);
    }
}

import entities.*;
import enums.GameMode;
import enums.NinjaType;

import java.util.Scanner;

public class Game {
    private Screen screen;
    private Player[] players;
    private Scanner input;
    private Validator validator;
    private String lineReader;
    private int playerTurn;
    private final int boardSize;
    private final int numberOfNinjas;

    public Game(int boardSize, int numberOfNinjas) {
        input = new Scanner(System.in);
        players = new Player[2];
        this.boardSize= boardSize;
        this.numberOfNinjas = numberOfNinjas;
        this.validator = new Validator(boardSize, numberOfNinjas);
        screen = new Screen(boardSize, numberOfNinjas,validator);

    }

    public void run(){
        boolean playing = true;
        screen.configurePlayer(players);
        screen.ninjaPlacement(players[playerTurn]);
        nextTurn();

        screen.configurePlayer(players);
        screen.ninjaPlacement(players[playerTurn]);

        while (playing){
            screen.println("\n***** Turno de " + players[playerTurn].getName() + " *****");
            screen.playerAction(players,playerTurn);
            playing= ! checkDeadOpponent();

            nextTurn();
        }
        endGame();
    }

    public void nextTurn(){
        playerTurn = playerTurn == 1 ? 0 : 1;
    }
    public void endGame(){
        nextTurn();
        screen.println("\nEl juego terminó");
        screen.println("Ganador: *** "+ players[playerTurn].getName() + " ***");
    }
    private boolean checkDeadOpponent(){
        Player opponent = players[playerTurn == 1 ? 0 : 1];

        return opponent.getBoard().getNinjasOnBoardQuantity() == 0;
    }


    private void exit(){
        System.exit(0);
    }


}

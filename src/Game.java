import entities.*;
import enums.GameMode;

import java.util.Scanner;

public class Game {
    private Screen screen;
    private Player[] players;
    private int playerTurn;
    private Scanner input;
    private final int boardSize;
    private final int numberOfNinjas;
    private final String[] sprites;

    public Game(int boardSize, int numberOfNinjas, String[] sprites) {
        screen = new Screen();
        input = new Scanner(System.in);
        players = new Player[2];
        this.boardSize= boardSize;
        this.numberOfNinjas = numberOfNinjas;
        this.sprites = sprites;

    }

    public void run(){
        configurePlayer();
        ninjaPlacement();

//        boolean playing = true;
//        while (playing){
//
//            playing=false;
//        }
    }

    private void configurePlayer(){
        screen.println("(1) Servidor");
        screen.println("(2) Cliente");
        screen.print("Ejecutar como: ");
        int mode = Integer.valueOf( input.nextLine() );
        screen.print("Ingrese nombre de jugador: ");
        String name = input.nextLine();
        screen.print("Ingrese IP: ");
        String ip = input.nextLine();

        GameMode gameMode = mode == 1 ? GameMode.SERVER : GameMode.CLIENT;
        Board board = new Board(boardSize);
        Player player= new Player(name,ip, gameMode,board);

        if (player.getGameMode() == GameMode.SERVER){
            players[0] = player;
            playerTurn=0;
        }else{
            players[1] = player;
            playerTurn=1;
        }

    }

    public void ninjaPlacement(){
        while (players[playerTurn].getNinjasOnBoardQuantity() < numberOfNinjas){
            screen.renderBoard(players[playerTurn].getBoard());

            screen.println("Es momento de colocar tres ninjas y un capitán en el tablero.");
            screen.println("¿Donde colocar al capitán?.");
            screen.print("Ingrese columa: ");
            char column = input.nextLine().charAt(0) ;
            screen.print("Ingrese fila: ");
            int row = Integer.valueOf( input.nextLine() );
            players[playerTurn].getBoard().placeNinja(column-65,row,new Ninja("N1"));

        }
        screen.renderBoard(players[playerTurn].getBoard());

    }

    public void begin(){

    }

    public void nextTurn(){}
    public void endGame(){}
    public void playerAction(int action){}

    public Screen getScreen() {
        return screen;
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }


}

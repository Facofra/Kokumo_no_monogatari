import entities.*;
import enums.GameMode;

import java.util.Scanner;

public class Game {
    private Screen screen;
    private Player[] players;
    private int playerTurn;
    private Scanner input;
    private final int boardSize;

    public Game(int boardSize) {
        screen = new Screen();
        input = new Scanner(System.in);
        players = new Player[2];
        this.boardSize= boardSize;

    }

    public void run(){
        configurePlayer();
        boolean playing = true;

        while (playing){
            screen.render(players[0].getBoard());

            System.out.println("Es momento de colocar tres ninjas y un capitán en el tablero.");
            System.out.println("¿Donde colocar al capitán?.");
            System.out.print("Ingrese columa: ");
            char column = input.nextLine().charAt(0) ;
            System.out.print("Ingrese fila: ");
            int row = Integer.valueOf( input.nextLine() );
            players[0].getBoard().placeNinja(column-65,row,new Ninja("N1"));

            screen.render(players[0].getBoard());
            playing=false;
        }
    }

    private void configurePlayer(){
        System.out.println("(1) Servidor");
        System.out.println("(2) Cliente");
        System.out.print("Ejecutar como: ");
        int mode = Integer.valueOf( input.nextLine() );
        System.out.print("Ingrese nombre de jugador: ");
        String name = input.nextLine();
        System.out.print("Ingrese IP: ");
        String ip = input.nextLine();

        GameMode gameMode = mode == 1 ? GameMode.SERVER : GameMode.CLIENT;
        Board board = new Board(boardSize);
        Player player= new Player(name,ip, gameMode,board);

        if (player.getGameMode() == GameMode.SERVER){
            players[0] = player;
        }else{
            players[1] = player;
        }

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

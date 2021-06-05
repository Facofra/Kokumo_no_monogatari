import entities.*;
import enums.GameMode;
import enums.NinjaType;
import managers.PlayerManager;

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


//        borrar testing, descomentar lo de arriba
//        testing();

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

    private void testing(){
        //        seteo de jugadores para probar
        GameMode gameMode = GameMode.SERVER;
        Board board = new Board(boardSize);
        Board opponentBoard = new Board(boardSize);
        Player player= new Player("Servidor","1", gameMode,board,opponentBoard,numberOfNinjas);

        if (player.getGameMode() == GameMode.SERVER){
            players[0] = player;
        }else{
            players[1] = player;
        }

        gameMode = GameMode.CLIENT;
        Board newboard = new Board(boardSize);
        Board newopponentBoard = new Board(boardSize);
        Player newplayer= new Player("Cliente","2", gameMode,newboard,newopponentBoard,numberOfNinjas);

        if (newplayer.getGameMode() == GameMode.SERVER){
            players[0] = newplayer;
        }else{
            players[1] = newplayer;
        }

        //    colocacion de ninjas en tablero para cada uno
        PlayerManager playerManager = new PlayerManager();

        playerManager.placeNinjaOnBoard(0,0,new Ninja(NinjaType.COMMANDER),player);
        playerManager.placeNinjaOnBoard(0,1,new Ninja(NinjaType.NORMAL),player);
        playerManager.placeNinjaOnBoard(0,2,new Ninja(NinjaType.NORMAL),player);
        playerManager.placeNinjaOnBoard(1,0,new Ninja(NinjaType.COMMANDER),newplayer);
        playerManager.placeNinjaOnBoard(1,1,new Ninja(NinjaType.NORMAL),newplayer);
        playerManager.placeNinjaOnBoard(1,2,new Ninja(NinjaType.NORMAL),newplayer);
    }


}

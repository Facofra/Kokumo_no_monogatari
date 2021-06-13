import connection.*;
import entities.*;
import enums.GameMode;
import enums.NinjaType;
import managers.PlayerManager;
import validators.Validator;

import java.util.Scanner;

public class Game {
    private Server server;
    private Client client;
    private Screen screen;
    private Player[] players;
    private Scanner input;
    private Validator validator;
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
        this.server = new Server();
        this.client = new Client();

    }

    public void run(){
        boolean playing = true;
        Message message = new Message();
        String jsonMessage;

        screen.configurePlayer(players,server,client);

        if (players[0]==null){
            screen.ninjaPlacement(players[1]);
            message.setPlayer(players[1]);
            jsonMessage = MessageManager.toJson(message);
            server.sendMessage(jsonMessage);
            client.informEndTurn();

            WaitingHandler.setWaiting(true);
            screen.waitingScreen();

            jsonMessage = client.recieveMessage();
            message = MessageManager.jsonToMessage(jsonMessage);
            players[0] = message.getPlayer();
            WaitingHandler.setWaiting(true);
            screen.println("Se recibió información del host.");
            screen.waitingScreen();
            playerTurn=1;

//            recibir jugador con primero ataques de oponente
            jsonMessage = client.recieveMessage();
            message = MessageManager.jsonToMessage(jsonMessage);
            players[1] = message.getPlayer();
        }else{
            WaitingHandler.setWaiting(true);
            screen.ninjaPlacement(players[0]);
            screen.waitingScreen();
            jsonMessage = client.recieveMessage();
            message = MessageManager.jsonToMessage(jsonMessage);
            players[1] = message.getPlayer();

            message.setPlayer(players[0]);
            jsonMessage = MessageManager.toJson(message);
            server.sendMessage(jsonMessage);
            client.informEndTurn();
        }

        while (playing){
            screen.println("\n***** Tu Turno " + players[playerTurn].getName() + " *****");

            screen.playerAction(players,playerTurn);

            playing= ! checkDeadOpponent();
            if (playing){
                message.setPlayer(players[playerTurn==0?1:0]);
                jsonMessage = MessageManager.toJson(message);
                server.sendMessage(jsonMessage);
                client.informEndTurn();
                screen.waitingScreen();
            }

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

import connection.*;
import entities.*;
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
//            envío jugador al oponente avisando que terminé
            message.setPlayer(players[1]);
            jsonMessage = MessageManager.toJson(message);
            server.sendMessage(jsonMessage);
            client.informEndTurn();

            WaitingHandler.setWaiting(true);
            screen.waitingScreen();
//          recibo jugador oponente
            jsonMessage = client.recieveMessage();
            message = MessageManager.jsonToMessage(jsonMessage);
            players[0] = message.getPlayer();
            WaitingHandler.setWaiting(true);
            screen.println("Se recibió información del host.");
            screen.waitingScreen();
            playerTurn=1;

//            recibir boards con primeros ataques de oponente
            jsonMessage = client.recieveMessage();
            message = MessageManager.jsonToMessage(jsonMessage);
            players[0].setBoard(message.getBoardPlayer1());
            players[1].setBoard(message.getBoardPlayer2());
            if (message.isCaptainAttacked()){
                screen.println("\nEl capitán fue atacado !!!\n");
            }

        }else{
            WaitingHandler.setWaiting(true);
            screen.ninjaPlacement(players[0]);
            screen.waitingScreen();
//      recibo jugador oponente
            jsonMessage = client.recieveMessage();
            message = MessageManager.jsonToMessage(jsonMessage);
            players[1] = message.getPlayer();
//      envío mi jugador al oponente
            message.setPlayer(players[0]);
            jsonMessage = MessageManager.toJson(message);
            server.sendMessage(jsonMessage);
            client.informEndTurn();
        }

//        comienzan los turnos
        while (playing){
            screen.println("\n***** Tu Turno " + players[playerTurn].getName() + " *****");
            message.setCaptainAttacked(false);

            screen.playerAction(players,playerTurn,message);

            playing= ! checkDeadOpponent();
            if (playing){
//                mandar ataque hecho
                message.setBoardPlayer1(players[0].getBoard());
                message.setBoardPlayer2(players[1].getBoard());
                jsonMessage = MessageManager.toJson(message);
                server.sendMessage(jsonMessage);
                client.informEndTurn();
                screen.println("\n Fin de tu turno.\n");
                screen.waitingScreen();
//                recibir ataques hechos
                jsonMessage = client.recieveMessage();
                message = MessageManager.jsonToMessage(jsonMessage);
                players[0].setBoard(message.getBoardPlayer1());
                players[1].setBoard(message.getBoardPlayer2());
                if (message.isCaptainAttacked()){
                    screen.println("\nEl capitán fue atacado !!!\n");
                }
                playing= message.isPlaying();

            }

        }
        if (players[playerTurn].getBoard().getNinjasOnBoardQuantity() !=0){
            message.setPlaying(false);
            jsonMessage = MessageManager.toJson(message);
            server.sendMessage(jsonMessage);
            client.informEndTurn();
        }else{
            nextTurn();
        }
        endGame(playerTurn);
    }


    public void nextTurn(){
        playerTurn = playerTurn == 1 ? 0 : 1;
    }
    public void endGame(int winner){


        screen.println("\nEl juego terminó");
        screen.println("Ganador: *** "+ players[winner].getName() + " ***");
        try {
            Thread.sleep(2000);
        } catch (Exception ex) {}
        server.stop();
        screen.println("\nPresione enter para continuar\n.");
        input.nextLine();
    }
    private boolean checkDeadOpponent(){
        Player opponent = players[playerTurn == 1 ? 0 : 1];

        return opponent.getBoard().getNinjasOnBoardQuantity() == 0;
    }


    private void exit(){
        System.exit(0);
    }



}

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

        screen.configurePlayer(players,server,client);

        if (players[0]==null){
            screen.ninjaPlacement(players[1]);
//            envío jugador al oponente avisando que terminé
            message.setPlayer(players[1]);

            sendMessage(message);
            client.informEndTurn();

            WaitingHandler.setWaiting(true);
            screen.waitingScreen();
//          recibo jugador oponente

            message = recieveMessage(client);
            players[0] = message.getPlayer();
            WaitingHandler.setWaiting(true);
            screen.println("Se recibió información del host.");
            screen.waitingScreen();
            playerTurn=1;

//            recibir boards con primeros ataques de oponente

            message = recieveMessage(client);
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

            message = recieveMessage(client);
            players[1] = message.getPlayer();
//      envío mi jugador al oponente
            message.setPlayer(players[0]);

            sendMessage(message);
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

                sendMessage(message);
                client.informEndTurn();
                screen.println("\n Fin de tu turno.\n");
                screen.waitingScreen();
//                recibir ataques hechos

                message= recieveMessage(client);
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

            sendMessage(message);
            client.informEndTurn();
        }else{
            nextTurn();
        }
        endGame(playerTurn);
    }

    private Message recieveMessage(Client client){
        String jsonMessage;
        jsonMessage = client.recieveMessage();
        return MessageManager.jsonToMessage(jsonMessage);
    }
    private void sendMessage(Message message){
        String jsonMessage;
        jsonMessage = MessageManager.toJson(message);
        server.sendMessage(jsonMessage);
    }
    private void nextTurn(){
        playerTurn = playerTurn == 1 ? 0 : 1;
    }
    private void endGame(int winner){


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

}

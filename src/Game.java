import entities.*;
import enums.GameMode;
import enums.NinjaType;

import java.util.Scanner;

public class Game {
    private Screen screen;
    private Player[] players;
    private int playerTurn;
    private Scanner input;
    private final int boardSize;
    private final int numberOfNinjas;
    private String lineReader;
    private Validator validator;

    public Game(int boardSize, int numberOfNinjas) {
        screen = new Screen();
        input = new Scanner(System.in);
        players = new Player[2];
        this.boardSize= boardSize;
        this.numberOfNinjas = numberOfNinjas;
        this.validator = new Validator(boardSize, numberOfNinjas);

    }

    public void run(){
        boolean playing = true;
        configurePlayer();
        ninjaPlacement();

        while (playing){
            playerAction();

            playing=false;
        }
    }

    private void configurePlayer(){
        screen.println("(1) Servidor");
        screen.println("(2) Cliente");
        screen.println("(S) Salir");
        screen.print("Ejecutar como: ");
        lineReader = input.nextLine();

        if (lineReader.equals("s") || lineReader.equals("S")){
            exit();
        }
        else if (lineReader.equals("1") || lineReader.equals("2")){
            int mode = Integer.valueOf( lineReader);
            screen.print("Ingrese nombre de jugador: ");
            String name = input.nextLine();
            screen.print("Ingrese IP: ");
            String ip = input.nextLine();

            GameMode gameMode = mode == 1 ? GameMode.SERVER : GameMode.CLIENT;
            Board board = new Board(boardSize);
            Board opponentBoard = new Board(boardSize);
            Player player= new Player(name,ip, gameMode,board,opponentBoard,numberOfNinjas);

            if (player.getGameMode() == GameMode.SERVER){
                players[0] = player;
                playerTurn=0;
            }else{
                players[1] = player;
                playerTurn=1;
            }
        }else{
            screen.println("Input incorrecto, debe ser 1, 2 o S");
            screen.println();
            configurePlayer();
        }


    }

    public void ninjaPlacement(){
        int i = 0;
        int column;
        int row;

        screen.println("Es momento de colocar " + numberOfNinjas +" ninjas en el tablero.");
        screen.renderBoard(players[playerTurn].getBoard());
        screen.println("¿Donde colocar al capitán?");
        screen.print("Ingrese columa: ");
        lineReader = input.nextLine();

        while (! validator.validateColumn(lineReader)){
            screen.println("Error, debe colocar una columna válida");
            screen.print("Ingrese columa: ");
            lineReader = input.nextLine();
        }
        column = columnCharToInt( lineReader.charAt(0) );

        screen.print("Ingrese fila: ");
        lineReader = input.nextLine();
        while (! validator.validateRow(lineReader)){
            screen.println("Error, debe colocar una fila válida");
            screen.print("Ingrese fila: ");
            lineReader = input.nextLine();
        }

        row = Integer.valueOf( lineReader ) -1 ;

        players[playerTurn].getBoard().placeNinja(column,row,new Ninja(NinjaType.COMMANDER));

        players[playerTurn].setNinja(i,new Ninja(NinjaType.COMMANDER),row, column);
        i++;

        while (players[playerTurn].getNinjasOnBoardQuantity() < numberOfNinjas){
            screen.renderBoard(players[playerTurn].getBoard());

            screen.println("¿Donde colocar al ninja " + i +" ?");
            screen.print("Ingrese columa: ");
            lineReader = input.nextLine();

            while (! validator.validateColumn(lineReader)){
                screen.println("Error, debe colocar una columna válida");
                screen.print("Ingrese columa: ");
                lineReader = input.nextLine();
            }
            column = columnCharToInt( lineReader.charAt(0) );


            screen.print("Ingrese fila: ");
            lineReader = input.nextLine();
            while (! validator.validateRow(lineReader)){
                screen.println("Error, debe colocar una fila válida");
                screen.print("Ingrese fila: ");
                lineReader = input.nextLine();
            }

            row = Integer.valueOf( lineReader ) -1 ;

            if (players[playerTurn].getBoard().getFields()[row][column].getNinja() == null){
                players[playerTurn].getBoard().placeNinja(column,row,new Ninja(NinjaType.NORMAL));
                players[playerTurn].setNinja(i,new Ninja(NinjaType.NORMAL),row, column);
                i++;
            }else {
                screen.println("En ese lugar ya había un ninja, colocar nuevamente.");
            }
        }
        screen.renderBoard(players[playerTurn].getBoard());


        screen.print("Estás contento con las posiciones? (S/N): ");
        lineReader = input.nextLine();
        while (! validator.validateYesOrNo(lineReader)){
            screen.print("Error, debe colocar S o N: ");
            lineReader = input.nextLine();
        }

        char option = lineReader.charAt(0) ;
        if (option == 'N' || option == 'n'){
            players[playerTurn].getBoard().clearBoard();
            ninjaPlacement();
        }

    }

    public void nextTurn(){}
    public void endGame(){}

    public void playerAction(){
        Player actualPlayer = players[playerTurn];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (actualPlayer.getNinjaFromBoard(i,j) != null && ! actualPlayer.getNinjaFromBoard(i,j).hasActed()){
                    screen.renderBoard(players[playerTurn].getBoard());

                    screen.println("Ninja de Fila: " + (i+1) + " Columna: " + columnIntToChar(j));

                    screen.println("(M) Mover ");
                    screen.println("(A) Atacar ");
                    screen.println("(S) Salir ");
                    screen.print("¿Que desea hacer?: ");
                    lineReader = input.nextLine();

                    while (! validator.validatePlayerAction(lineReader)){
                        screen.println("Error, debe colocar una acción válida (M, A, S)");
                        screen.print("¿Que desea hacer?: ");
                        lineReader = input.nextLine();
                    }

                    if (lineReader.equals("S") || lineReader.equals("s")){
                        exit();
                    }
                    if (lineReader.equals("M") || lineReader.equals("m")){
                        if (actualPlayer.isCommanderAlive() && actualPlayer.getNinjaFromBoard(i,j).isCanMove() && validator.validateFreeSpace(i,j,actualPlayer.getBoard().getFields())){
                            playerMoves(i,j);
                        }else{
                            screen.println("El ninja no se puede mover, escoja otra opción");
                            j--;
                        }
                    }
                    if (lineReader.equals("A") || lineReader.equals("a")){
                        actualPlayer.getNinjaFromBoard(i,j).setCanMove(true);
                        actualPlayer.getNinjaFromBoard(i,j).setHasActed(true);
                        exit();
                    }
                }


            }

        }

//  resetear el hasActed a false, es una solución momentanea hasta no implementar guardar ninjas en array
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (actualPlayer.getNinjaFromBoard(i,j) != null){
                    actualPlayer.getNinjaFromBoard(i,j).setHasActed(false);
                }
            }
        }
    }

    private void playerMoves(int originRow, int originColumn){
        int destinationColumn;
        int destinationRow;
        Player player = players[playerTurn];

        screen.println("Indique coordenada a donde quiere mover el ninja");

        screen.print("Ingrese columa: ");
        lineReader = input.nextLine();

        while (! validator.validateColumn(lineReader)){
            screen.println("Error, debe colocar una columna válida");
            screen.print("Ingrese columa: ");
            lineReader = input.nextLine();
        }
        destinationColumn = columnCharToInt( lineReader.charAt(0) );

        screen.print("Ingrese fila: ");
        lineReader = input.nextLine();
        while (! validator.validateRow(lineReader)){
            screen.println("Error, debe colocar una fila válida");
            screen.print("Ingrese fila: ");
            lineReader = input.nextLine();
        }

        destinationRow = Integer.valueOf( lineReader ) -1 ;

        if (validator.validateMovement(originColumn, originRow, destinationColumn, destinationRow, player.getBoard().getFields())){

            Ninja ninjaAux = player.getNinjaFromBoard(originRow,originColumn);
            ninjaAux.setCanMove(false);
            ninjaAux.setHasActed(true);
            player.getBoard().eliminateNinja(originColumn,originRow);
            player.getBoard().placeNinja(destinationColumn,destinationRow,ninjaAux);
        }else{
            screen.println(validator.getMessage() + " Pruebe de nuevo.");
            playerMoves(originRow,originColumn);
        }

    }

    private void playerAttacks(){

    }

    public Screen getScreen() {
        return screen;
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    private int columnCharToInt(char columnChar){

        int columnInt = (int) columnChar;

        if (columnInt >= 97 && columnInt <= 122){
            columnInt-=32;
        }
        columnInt-= 65;

        return columnInt;
    }

    private char columnIntToChar (int columnInt){
        return (char) (columnInt + 65);
    }

    private void exit(){
        System.exit(0);
    }


}

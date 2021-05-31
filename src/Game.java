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

        configurePlayer();
        ninjaPlacement();

        while (playing){
            screen.println("\n***** Turno de " + players[playerTurn].getName() + " *****");
            playerAction();
            playing= ! checkDeadOpponent();

            nextTurn();
        }
        endGame();
    }

    private void configurePlayer(){
        screen.println("\n(1) Servidor");
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
            lineReader = input.nextLine();
            while (lineReader.equals("")){
                screen.print("No puede estar vació. Ingrese nombre: ");
                lineReader = input.nextLine();
            }
            String name = lineReader;
            screen.print("Ingrese IP: ");
            lineReader = input.nextLine();
            while (lineReader.equals("")){
                screen.print("No puede estar vació. Ingrese IP: ");
                lineReader = input.nextLine();
            }
            String ip = lineReader;

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
        Player actualPlayer = players[playerTurn];

        screen.println("\nEs momento de colocar " + numberOfNinjas +" ninjas en el tablero.");
        screen.renderBoard(actualPlayer.getBoard());
        screen.println("¿Donde colocar al capitán?");

        column = columnInput();
        row = rowInput() ;

        actualPlayer.getBoard().placeNinja(column,row,new Ninja(NinjaType.COMMANDER));

        actualPlayer.setNinja(i,new Ninja(NinjaType.COMMANDER),row, column);
        i++;

        while (actualPlayer.getNinjasOnBoardQuantity() < numberOfNinjas){
            screen.renderBoard(actualPlayer.getBoard());

            screen.println("¿Donde colocar al ninja " + i +" ?");

            column = columnInput();
            row = rowInput() ;

            if (actualPlayer.getNinjaFromBoard(row,column) == null){
                actualPlayer.placeNinaOnBoard(column,row,new Ninja(NinjaType.NORMAL));

                actualPlayer.setNinja(i,new Ninja(NinjaType.NORMAL),row, column);
                i++;
            }else {
                screen.println("En ese lugar ya había un ninja, colocar nuevamente.");
            }
        }
        screen.renderBoard(actualPlayer.getBoard());


        screen.print("Estás contento con las posiciones? (S/N): ");
        lineReader = input.nextLine();
        while (! validator.validateYesOrNo(lineReader)){
            screen.print("Error, debe colocar S o N: ");
            lineReader = input.nextLine();
        }

        char option = lineReader.charAt(0) ;
        if (option == 'N' || option == 'n'){
            actualPlayer.getBoard().clearBoard();
            ninjaPlacement();
        }

    }

    public void nextTurn(){
        playerTurn = playerTurn == 1 ? 0 : 1;
    }
    public void endGame(){
        nextTurn();
        screen.println("\nEl juego terminó");
        screen.println("Ganador: *** "+ players[playerTurn].getName() + " ***");
    }

    public void playerAction(){
        Player actualPlayer = players[playerTurn];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (actualPlayer.getNinjaFromBoard(i,j) != null && ! actualPlayer.getNinjaFromBoard(i,j).hasActed()){
                    screen.renderBoard(actualPlayer.getBoard());

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
                        if (validator.validateCanMove(actualPlayer,i,j)){
                            playerMoves(i,j);
                            screen.println("\nMovimiento realizado con éxito.");
                        }else{
                            screen.println(validator.getMessage());
                            screen.println("Escoja otra opción.");
                            j--;
                        }
                    }
                    if (lineReader.equals("A") || lineReader.equals("a")){
                        actualPlayer.getNinjaFromBoard(i,j).setCanMove(true);
                        actualPlayer.getNinjaFromBoard(i,j).setHasActed(true);
                        playerAttacks();
                        screen.println("\nAtaque realizado con exito");

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
        Player actualPlayer = players[playerTurn];

        screen.println("\nIndique coordenada a donde quiere mover el ninja");

        destinationColumn = columnInput();
        destinationRow = rowInput() ;

        if (validator.validateMovement(originColumn, originRow, destinationColumn, destinationRow, actualPlayer.getBoard().getFields())){

            Ninja ninjaAux = actualPlayer.getNinjaFromBoard(originRow,originColumn);
            ninjaAux.setCanMove(false);
            ninjaAux.setHasActed(true);
            actualPlayer.getBoard().eliminateNinja(originColumn,originRow);
            actualPlayer.getBoard().placeNinja(destinationColumn,destinationRow,ninjaAux);
        }else{
            screen.println(validator.getMessage() + " Pruebe de nuevo.");
            screen.renderBoard(players[playerTurn].getBoard());
            playerMoves(originRow,originColumn);
        }

    }

    private void playerAttacks(){
        Player actualPlayer = players[playerTurn];
        Player opponent = players[playerTurn == 1 ? 0 : 1];
        int row;
        int column;

        screen.renderBoard(actualPlayer.getOpponentBoard());
        screen.println("Ingrese coordenada a atacar.");
        column = columnInput();
        row = rowInput();

        while (! actualPlayer.getOpponentBoard().getFields()[row][column].isTransitable()){
            screen.println("Esa coordenada ya ha sido atacada.");
            screen.println("Ingrese otra coordenada a atacar.");
            column = columnInput();
            row = rowInput();
        }

        actualPlayer.getOpponentBoard().getFields()[row][column].setTransitable(false);

        if (opponent.getNinjaFromBoard(row,column) == null){
            opponent.getBoard().getFields()[row][column].setTransitable(false);
        }else {
            opponent.getNinjaFromBoard(row,column).recieveAttack();
            if (opponent.getNinjaFromBoard(row,column).getLives()==0){
                opponent.getBoard().killNinja(row,column);
            }
        }

//            AVISAR AL JUGADOR SI FALLÓ o ATINÓ?

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

    private boolean checkDeadOpponent(){
        Player opponent = players[playerTurn == 1 ? 0 : 1];

        return opponent.getBoard().getNinjasOnBoardQuantity() == 0;
    }

    private int columnInput(){
        screen.print("Ingrese columa: ");
        lineReader = input.nextLine();

        while (! validator.validateColumn(lineReader)){
            screen.println("Error, debe colocar una columna válida");
            screen.print("Ingrese columa: ");
            lineReader = input.nextLine();
        }
        return columnCharToInt( lineReader.charAt(0) );
    }

    private int rowInput(){
        screen.print("Ingrese fila: ");
        lineReader = input.nextLine();
        while (! validator.validateRow(lineReader)){
            screen.println("Error, debe colocar una fila válida");
            screen.print("Ingrese fila: ");
            lineReader = input.nextLine();
        }

        return Integer.valueOf( lineReader ) -1 ;
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

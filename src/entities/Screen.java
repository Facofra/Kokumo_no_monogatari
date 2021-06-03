package entities;

import enums.GameMode;
import enums.NinjaType;

import java.util.Scanner;

public class Screen {
    private Validator validator;
    private Scanner input;
    private String lineReader;
    private int boardSize;
    private int numberOfNinjas;

    public Screen(int boardSize, int numberOfNinjas, Validator validator) {
        this.boardSize = boardSize;
        this.numberOfNinjas = numberOfNinjas;
        input = new Scanner(System.in);
        this.validator = validator;
    }

    public void renderBoard(Board board){
        Field[][] gameBoard = board.getFields();
        int boardSize = gameBoard.length;
        println();
        print("   ");
        for (int i = 0; i < boardSize; i++) {
            char columnChar = (char) (i +65) ;
            print(columnChar + " ");
        }
        println();
        print("   ");
        for (int i = 0; i < boardSize; i++) {
            print("_" + " ");
        }
        println();

        for (int i = 0; i < boardSize; i++) {
            print((i+1) + "| ");
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j].getNinja() == null){
                    if (gameBoard[i][j].isTransitable()) {
                        print(". ");
                    }else {
                        print("x ");
                    }
                }else {
                    print(gameBoard[i][j].getNinja().getSprite() + " ");
                }
            }
            println();
        }
        println();
    }

    public void print(String text){
        System.out.print(text);
    }
    public void print(char text){
        System.out.print(text);
    }
    public void println(String text){
        System.out.println(text);
    }
    public void println(){
        System.out.println();
    }


    public void configurePlayer(Player[] players){
        println("\n(1) Servidor");
        println("(2) Cliente");
        println("(S) Salir");
        print("Ejecutar como: ");
        lineReader = input.nextLine();

        if (lineReader.equals("s") || lineReader.equals("S")){
            exit();

        }
        else if (lineReader.equals("1") || lineReader.equals("2")){
            int mode = Integer.valueOf( lineReader);
            print("Ingrese nombre de jugador: ");
            lineReader = input.nextLine();
            while (lineReader.equals("")){
                print("No puede estar vació. Ingrese nombre: ");
                lineReader = input.nextLine();
            }
            String name = lineReader;
            print("Ingrese IP: ");
            lineReader = input.nextLine();
            while (lineReader.equals("")){
                print("No puede estar vació. Ingrese IP: ");
                lineReader = input.nextLine();
            }
            String ip = lineReader;

            GameMode gameMode = mode == 1 ? GameMode.SERVER : GameMode.CLIENT;
            Board board = new Board(boardSize);
            Board opponentBoard = new Board(boardSize);
            Player player= new Player(name,ip, gameMode,board,opponentBoard,numberOfNinjas);

            if (player.getGameMode() == GameMode.SERVER){
                players[0] = player;
            }else{
                players[1] = player;
            }
        }else{
            println("Input incorrecto, debe ser 1, 2 o S");
            println();
            configurePlayer(players);
        }


    }
    public void ninjaPlacement(Player actualPlayer){
        int i = 0;
        int column;
        int row;

        println("\nEs momento de colocar " + numberOfNinjas +" ninjas en el tablero.");
        renderBoard(actualPlayer.getBoard());
        println("¿Donde colocar al capitán?");

        column = columnInput();
        row = rowInput() ;

        actualPlayer.getBoard().placeNinja(column,row,new Ninja(NinjaType.COMMANDER));

        actualPlayer.setNinja(i,new Ninja(NinjaType.COMMANDER),row, column);
        i++;

        while (actualPlayer.getNinjasOnBoardQuantity() < numberOfNinjas){
            renderBoard(actualPlayer.getBoard());

            println("¿Donde colocar al ninja " + i +" ?");

            column = columnInput();
            row = rowInput() ;

            if (actualPlayer.getNinjaFromBoard(row,column) == null){
                actualPlayer.placeNinaOnBoard(column,row,new Ninja(NinjaType.NORMAL));

                actualPlayer.setNinja(i,new Ninja(NinjaType.NORMAL),row, column);
                i++;
            }else {
                println("En ese lugar ya había un ninja, colocar nuevamente.");
            }
        }
        renderBoard(actualPlayer.getBoard());


        print("Estás contento con las posiciones? (S/N): ");
        lineReader = input.nextLine();
        while (! validator.validateYesOrNo(lineReader)){
            print("Error, debe colocar S o N: ");
            lineReader = input.nextLine();
        }

        char option = lineReader.charAt(0) ;
        if (option == 'N' || option == 'n'){
            actualPlayer.getBoard().clearBoard();
            ninjaPlacement(actualPlayer);
        }

    }
    public void playerAction(Player[] players, int playerTurn ){
        Player actualPlayer = players[playerTurn];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (actualPlayer.getNinjaFromBoard(i,j) != null && ! actualPlayer.getNinjaFromBoard(i,j).hasActed()){
                    renderBoard(actualPlayer.getBoard());

                    println("Ninja de Fila: " + (i+1) + " Columna: " + columnIntToChar(j));

                    println("(M) Mover ");
                    println("(A) Atacar ");
                    println("(S) Salir ");
                    print("¿Que desea hacer?: ");
                    lineReader = input.nextLine();

                    while (! validator.validatePlayerAction(lineReader)){
                        println("Error, debe colocar una acción válida (M, A, S)");
                        print("¿Que desea hacer?: ");
                        lineReader = input.nextLine();
                    }

                    if (lineReader.equals("S") || lineReader.equals("s")){
                        exit();
                    }
                    if (lineReader.equals("M") || lineReader.equals("m")){
                        if (validator.validateCanMove(actualPlayer,i,j)){
                            playerMoves(i,j,players[playerTurn]);
                            println("\nMovimiento realizado con éxito.");
                        }else{
                            println(validator.getMessage());
                            println("Escoja otra opción.");
                            j--;
                        }
                    }
                    if (lineReader.equals("A") || lineReader.equals("a")){
                        actualPlayer.getNinjaFromBoard(i,j).setCanMove(true);
                        actualPlayer.getNinjaFromBoard(i,j).setHasActed(true);
                        playerAttacks(players,playerTurn);
                        println("\nAtaque realizado con exito");

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
    private void playerMoves(int originRow, int originColumn, Player actualPlayer){
        int destinationColumn;
        int destinationRow;

        println("\nIndique coordenada a donde quiere mover el ninja");

        destinationColumn = columnInput();
        destinationRow = rowInput() ;

        if (validator.validateMovement(originColumn, originRow, destinationColumn, destinationRow, actualPlayer.getBoard().getFields())){

            Ninja ninjaAux = actualPlayer.getNinjaFromBoard(originRow,originColumn);
            ninjaAux.setCanMove(false);
            ninjaAux.setHasActed(true);
            actualPlayer.getBoard().eliminateNinja(originColumn,originRow);
            actualPlayer.getBoard().placeNinja(destinationColumn,destinationRow,ninjaAux);
        }else{
            println(validator.getMessage() + " Pruebe de nuevo.");
            renderBoard(actualPlayer.getBoard());
            playerMoves(originRow,originColumn,actualPlayer);
        }

    }
    private void playerAttacks(Player[] players, int playerTurn){
        Player actualPlayer = players[playerTurn];
        Player opponent = players[playerTurn == 1 ? 0 : 1];
        int row;
        int column;

        renderBoard(actualPlayer.getOpponentBoard());
        println("Ingrese coordenada a atacar.");
        column = columnInput();
        row = rowInput();

        while (! actualPlayer.getOpponentBoard().getFields()[row][column].isTransitable()){
            println("Esa coordenada ya ha sido atacada.");
            println("Ingrese otra coordenada a atacar.");
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


    private int columnInput(){
        print("Ingrese columa: ");
        lineReader = input.nextLine();

        while (! validator.validateColumn(lineReader)){
            println("Error, debe colocar una columna válida");
            print("Ingrese columa: ");
            lineReader = input.nextLine();
        }
        return columnCharToInt( lineReader.charAt(0) );
    }
    private int rowInput(){
        print("Ingrese fila: ");
        lineReader = input.nextLine();
        while (! validator.validateRow(lineReader)){
            println("Error, debe colocar una fila válida");
            print("Ingrese fila: ");
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

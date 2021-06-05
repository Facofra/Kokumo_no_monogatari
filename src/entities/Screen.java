package entities;

import enums.GameMode;
import enums.NinjaType;
import managers.PlayerManager;

import java.util.Scanner;

public class Screen {
    private Validator validator;
    private Scanner input;
    private String lineReader;
    private PlayerManager playerManager;
    private int boardSize;
    private int numberOfNinjas;

    public Screen(int boardSize, int numberOfNinjas, Validator validator) {
        this.boardSize = boardSize;
        this.numberOfNinjas = numberOfNinjas;
        input = new Scanner(System.in);
        this.validator = validator;
        this.playerManager = new PlayerManager();
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

//---------------------------- all screens -------------------------------------------------------------
    public void configurePlayer(Player[] players){
        println("\n(1) Servidor");
        println("(2) Cliente");
        println("(S) Salir");
        print("Ejecutar como: ");
        lineReader = input.nextLine();

        if (lineReader.equals("s") || lineReader.equals("S")){
            println("¿Seguro que desear salir? (S/N)");
            char confirm= confirmInput();
            if (confirm =='S' || confirm == 's'){
                exit();
            }else{
                configurePlayer(players);
            }

        }
        else if (lineReader.equals("1") || lineReader.equals("2")){
            int mode = Integer.valueOf( lineReader);

            String name = nameInput();
            String ip = ipInput();

            playerManager.initializePlayer(players,mode,boardSize,name,ip,numberOfNinjas);

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

        playerManager.placeNinjaOnBoard(column,row,new Ninja(NinjaType.COMMANDER),actualPlayer);

//        actualPlayer.setNinja(i,new Ninja(NinjaType.COMMANDER),row, column);
        i++;

        while (actualPlayer.getNinjasOnBoardQuantity() < numberOfNinjas){
            renderBoard(actualPlayer.getBoard());

            println("¿Donde colocar al ninja " + i +" ?");

            column = columnInput();
            row = rowInput() ;

            if (playerManager.getNinjaFromBoard(row,column,actualPlayer) == null){
                playerManager.placeNinjaOnBoard(column,row,new Ninja(NinjaType.NORMAL),actualPlayer);

//                actualPlayer.setNinja(i,new Ninja(NinjaType.NORMAL),row, column);
                i++;
            }else {
                println("En ese lugar ya había un ninja, colocar nuevamente.");
            }
        }
        renderBoard(actualPlayer.getBoard());


        print("Estás contento con las posiciones? (S/N): ");
        char confirm = confirmInput() ;

        if (confirm == 'N' || confirm == 'n'){
            playerManager.clearBoard(actualPlayer);
            ninjaPlacement(actualPlayer);
        }

    }
    public void playerAction(Player[] players, int playerTurn ){
        Player actualPlayer = players[playerTurn];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (playerManager.getNinjaFromBoard(i,j,actualPlayer) != null && ! playerManager.getNinjaHasActed(i,j,actualPlayer)){
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
//                        actualPlayer.getNinjaFromBoard(i,j).setCanMove(true);
                        playerManager.setNinjaCanMove(true,i,j,actualPlayer);
                        playerManager.setNinjaHasActed(true,i,j,actualPlayer);
//                        actualPlayer.getNinjaFromBoard(i,j).setHasActed(true);
                        playerAttacks(players,playerTurn);
                        println("\nAtaque realizado con exito");

                    }
                }
            }
        }

//  resetear el hasActed a false, es una solución momentanea.
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (playerManager.getNinjaFromBoard(i,j,actualPlayer) != null){
                    playerManager.setNinjaHasActed(false,i,j,actualPlayer);
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

            Ninja ninjaAux = playerManager.getNinjaFromBoard(originRow,originColumn,actualPlayer);
            ninjaAux.setCanMove(false);
            ninjaAux.setHasActed(true);
//            actualPlayer.getBoard().eliminateNinja(originColumn,originRow);
//            actualPlayer.getBoard().placeNinja(destinationColumn,destinationRow,ninjaAux);
            playerManager.eliminateNinjaFromBoard(originRow,originColumn,actualPlayer);
            playerManager.placeNinjaOnBoard(destinationColumn,destinationRow,ninjaAux,actualPlayer);
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

        while (! playerManager.isTargetTransitable(row,column,actualPlayer)){
            println("Esa coordenada ya ha sido atacada.");
            println("Ingrese otra coordenada a atacar.");
            column = columnInput();
            row = rowInput();
        }
        playerManager.setTargetTransitable(false,row,column,actualPlayer);

        if (playerManager.getNinjaFromBoard(row,column,opponent) == null){
            playerManager.setTransitable(false,row,column,opponent);
        }else {
            playerManager.getNinjaFromBoard(row,column,opponent).recieveAttack();
            if (playerManager.getNinjaFromBoard(row,column,opponent).getLives()==0){
                playerManager.killNinja(row,column,opponent);
            }
        }

//            AVISAR AL JUGADOR SI FALLÓ o ATINÓ?

    }



    private String ipInput(){
        print("Ingrese IP: ");
        lineReader = input.nextLine();
        while (lineReader.equals("")){
            print("No puede estar vació. Ingrese IP: ");
            lineReader = input.nextLine();
        }
        return lineReader;
    }
    private String nameInput(){
        print("Ingrese nombre de jugador: ");
        lineReader = input.nextLine();
        while (lineReader.equals("")){
            print("No puede estar vació. Ingrese nombre: ");
            lineReader = input.nextLine();
        }
        return lineReader;
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
    private char confirmInput(){
        lineReader = input.nextLine();
        while (! validator.validateYesOrNo(lineReader)){
            print("Error, debe colocar S o N: ");
            lineReader = input.nextLine();
        }

        return lineReader.charAt(0) ;
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
        println("Saliendo del juego...");
        System.exit(0);
    }
}

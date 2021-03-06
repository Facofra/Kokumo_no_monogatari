package entities;

import connection.*;
import enums.NinjaType;
import managers.PlayerManager;
import validators.Validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Screen {
    private Validator validator;
    private Scanner input;
    private String lineReader;
    private PlayerManager playerManager;
    private int boardSize;
    private int numberOfNinjas;
    private boolean goBack;

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
    public void println(String text){
        System.out.println(text);
    }
    public void println(){
        System.out.println();
    }

//---------------------------- all screens -------------------------------------------------------------
    public void configurePlayer(Player[] players, Server server, Client client){
        println("\n(1) Crear partida");
        println("(2) Unirse a partida");
        println("(S) Salir del juego");
        println();
        print("Elija opci??n: ");
        lineReader = input.nextLine();
        println();
        if (lineReader.equals("s") || lineReader.equals("S")){
            print("??Seguro que desear salir? (S/N): ");
            char confirm= confirmInput();
            if (confirm =='S' || confirm == 's'){
                exit();
            }else{
                configurePlayer(players,server,client);
            }

        }
        else if (lineReader.equals("1") || lineReader.equals("2")){
            int mode = Integer.valueOf( lineReader);

            String name = nameInput();
            server.setPlayerName(name);

            playerManager.initializePlayer(players,mode,boardSize,name,numberOfNinjas);
            if (mode == 1){
                server.setPort(8000);
                client.setPort(8001);
                try{
                    server.start();
                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
                serverScreen(server,client);
            }else{
                server.setPort(8001);
                client.setPort(8000);
                try{
                    server.start();
                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
                clientScreen(server,client);
            }
            if (goBack){
                goBack=false;
                try {
                    server.stop();
                } catch (Exception ex) {}
                configurePlayer(players,server,client);
            }

        }else{
            println("Input incorrecto, debe ser 1, 2 o S");
            println();
            configurePlayer(players,server,client);
        }


    }
    public void ninjaPlacement(Player actualPlayer){
        int i = 0;
        int column;
        int row;

        println("\nEs momento de colocar " + numberOfNinjas +" ninjas en el tablero.");
        renderBoard(actualPlayer.getBoard());
        println("??Donde colocar al capit??n?");

        column = columnInput();
        row = rowInput() ;

        playerManager.placeNinjaOnBoard(column,row,new Ninja(NinjaType.COMMANDER),actualPlayer);

        i++;

        while (actualPlayer.getNinjasOnBoardQuantity() < numberOfNinjas){
            renderBoard(actualPlayer.getBoard());

            println("??Donde colocar al ninja " + i +" ?");

            column = columnInput();
            row = rowInput() ;

            if (playerManager.getNinjaFromBoard(row,column,actualPlayer) == null){
                playerManager.placeNinjaOnBoard(column,row,new Ninja(NinjaType.NORMAL),actualPlayer);

                i++;
            }else {
                println("En ese lugar ya hab??a un ninja, colocar nuevamente.");
            }
        }
        renderBoard(actualPlayer.getBoard());


        print("Est??s contento con las posiciones? (S/N): ");
        char confirm = confirmInput() ;

        if (confirm == 'N' || confirm == 'n'){
            playerManager.clearBoard(actualPlayer);
            ninjaPlacement(actualPlayer);
        }

    }
    public void playerAction(Player[] players, int playerTurn , Message message, Client client){
        Player actualPlayer = players[playerTurn];
        List<String> attackMessages = new ArrayList<>();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (playerManager.getNinjaFromBoard(i,j,actualPlayer) != null && ! playerManager.getNinjaHasActed(i,j,actualPlayer)){
                    renderBoard(actualPlayer.getBoard());

                    println("Ninja de Fila: " + (i+1) + " Columna: " + columnIntToChar(j));

                    println("(M) Mover ");
                    println("(A) Atacar ");
                    println("(S) Salir del juego ");
                    print("??Que desea hacer?: ");
                    lineReader = input.nextLine();

                    while (! validator.validatePlayerAction(lineReader)){
                        println("\nError, debe colocar una acci??n v??lida (M, A, S)");
                        println("\nNinja de Fila: " + (i+1) + " Columna: " + columnIntToChar(j));
                        print("\n??Que desea hacer?: ");
                        lineReader = input.nextLine();
                    }

                    if (lineReader.equals("S") || lineReader.equals("s")){
                        print("??Seguro que desear salir? (S/N): ");
                        char confirm= confirmInput();
                        if (confirm =='S' || confirm == 's'){
                            informAndExit(client);
                        }else{
                            j--;
                        }
                    }
                    if (lineReader.equals("M") || lineReader.equals("m")){
                        if (validator.validateCanMove(actualPlayer,i,j)){
                            playerMoves(i,j,players[playerTurn]);
                            println("\nMovimiento realizado con ??xito.");
                        }else{
                            println(validator.getMessage());
                            println("Escoja otra opci??n.");
                            j--;
                        }
                    }
                    if (lineReader.equals("A") || lineReader.equals("a")){

                        playerManager.setNinjaCanMove(true,i,j,actualPlayer);
                        playerManager.setNinjaHasActed(true,i,j,actualPlayer);

                        playerAttacks(players,playerTurn,attackMessages);
                        println("\nAtaque realizado con exito");

                    }
                }
            }
        }
//  dar mensajes, y desmarcar casilla de capit??n si sigue vivo
        println();
        for (String attackMessage:attackMessages) {
            if (attackMessage.matches("[0-9]{2}")){
                int row = Integer.valueOf( attackMessage.substring(0,1) );
                int column = Integer.valueOf( attackMessage.substring(1,2) );
                playerManager.setTargetTransitable(true,row,column,actualPlayer);
                message.setCaptainAttacked(true);
            }else {
                System.out.println(attackMessage);
            }
        }
        attackMessages.clear();

//  resetear el hasActed a false, es una soluci??n momentanea.
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
            playerManager.eliminateNinjaFromBoard(originRow,originColumn,actualPlayer);
            playerManager.placeNinjaOnBoard(destinationColumn,destinationRow,ninjaAux,actualPlayer);
        }else{
            println(validator.getMessage() + " Pruebe de nuevo.");
            renderBoard(actualPlayer.getBoard());
            playerMoves(originRow,originColumn,actualPlayer);
        }

    }
    private void playerAttacks(Player[] players, int playerTurn, List<String> attackMessages){
        Player actualPlayer = players[playerTurn];
        Player opponent = players[playerTurn == 1 ? 0 : 1];
        int row;
        int column;

        println("\n  -- Tablero de ataque --");
        renderBoard(actualPlayer.getOpponentBoard());
        println("Ingrese coordenada a atacar.");
        column = columnInput();
        row = rowInput();

        while (! playerManager.isTargetTransitable(row,column,actualPlayer)){
            println("\nEsa coordenada ya ha sido atacada.\n");
            println("Ingrese otra coordenada a atacar.");
            column = columnInput();
            row = rowInput();
        }
        playerManager.setTargetTransitable(false,row,column,actualPlayer);

        if (playerManager.getNinjaFromBoard(row,column,opponent) == null){
            playerManager.setTransitable(false,row,column,opponent);
            attackMessages.add("- Atacaste a espacio vac??o");
        }else {
            playerManager.getNinjaFromBoard(row,column,opponent).recieveAttack();
            if (playerManager.getNinjaFromBoard(row,column,opponent).getLives()==0){
                playerManager.killNinja(row,column,opponent);
                attackMessages.add("- Mataste un ninja");
            }else{
                attackMessages.add("- Atacaste un ninja");
//                este mensaje no se dar?? al usuario, es para desmarcar donde hab??a un capit??n
                attackMessages.add(""+row+""+column);
            }
        }


    }
    private void clientScreen(Server server, Client client){

        if (validator.validateIp( client.getIpOpponent() ) ){
            print("\n?? Unirse a: " + client.getIpOpponent() + " ? (S/N): ");
            char confirm= confirmInput();
            println();
            if (confirm =='N' || confirm == 'n'){
                print("Ingrese IP a unirse: ");
                String ipOpponent = ipInput();
                client.setIpOpponent(ipOpponent);
            }

        }else{
            print("Ingrese IP a unirse: ");
            String ipOpponent = ipInput();
            client.setIpOpponent(ipOpponent);
        }

        try {
            if (client.recieveMessage().equals("ERROR URL")){
                println("\nTodav??a no hay partida creada en esa IP\n");
                goBack=true;
            }else{
                Message message = new Message();
                message.setPass("password");
                message.setName(server.getPlayerName());
                server.sendMessage(MessageManager.toJson(message));

                waitingForAcceptance(server,client);
            }

        } catch (Exception ex) {
            println("\nTodav??a no hay partida creada en esa IP\n");
            goBack=true;
        }



    }
    private void waitingForAcceptance(Server server, Client client){
        boolean waiting = true;

        while (waiting) {
            println("(1) Ver si host me acept??");
            println("(A) Atr??s");
            println("(S) salir del juego");
            print("\nElija una opci??n: ");
            lineReader = input.nextLine();
            println();

            if (lineReader.equals("1")){
                println("\nChequeando si host acept??, esto puede llevar un rato...");
                println();
                try {
                    String response = client.recieveMessage();
                    Message message = MessageManager.jsonToMessage(response);
                    if (message.getPass().equals("password")){
                        waiting=false;
                        goBack=false;
                        println("Host ha aceptado, nombre: " + message.getName());
                    }else{
                        println("Host todav??a no acept??");
                    }
                } catch (Exception ex) {
                    println("Host todav??a no acept??");
                }
            }
            else if (lineReader.equalsIgnoreCase("S")){
                print("??Seguro que desear salir? (S/N): ");
                char confirm= confirmInput();
                if (confirm =='S' || confirm == 's'){
                    exit();
                }
            }else if (lineReader.equalsIgnoreCase("A")){
                waiting=false;
                goBack=true;
            }
            else {
                println("Input inv??lido, debe ser 1 o S");
            }

        }
    }
    private void serverScreen(Server server, Client client){
        boolean waiting = true;
        while (waiting) {
            println("(1) Ver si oponente se conect??");
            println("(A) Atr??s");
            println("(S) salir del juego");
            print("\nElija una opci??n: ");
            lineReader = input.nextLine();

            if (lineReader.equals("1")){
                println("\nChequeando si oponente se conect??, esto puede llevar un rato...");
                println();
                try {
                    client.setIpOpponent(MessageHandler.getOpponentIp());
                    String response = client.recieveMessage();
                    Message message = MessageManager.jsonToMessage(response);
                    if (message.getPass().equals("password")){


                        println("Oponente conectado, nombre: " + message.getName());
                        println();
                        print("??Aceptarlo? (S/N): ");
                        char confirm = confirmInput();
                        if (confirm == 'S' || confirm == 's'){
                            waiting=false;
                            message.setPass("password");
                            message.setName(server.getPlayerName());
                            server.sendMessage(MessageManager.toJson(message));

                        }else{
                            println("\nHas negado la conexi??n");
                        }

                    }else{
                        println("Oponente todav??a no acept??");
                    }
                } catch (Exception ex) {
                    println("Oponente todav??a no conectado");
                }
            }
            else if (lineReader.equalsIgnoreCase("S")){
                print("??Seguro que desear salir? (S/N): ");
                char confirm= confirmInput();
                if (confirm =='S' || confirm == 's'){
                    exit();
                }
            }else if (lineReader.equalsIgnoreCase("A")){
                waiting=false;
                goBack=true;
            }
            else {
                println("Input inv??lido, debe ser 1 o S");
            }

        }
    }

    public void waitingScreen(){
        println("\nEsperando que el oponente termine... ");
        while (WaitingHandler.isWaiting()){
            try {
                Thread.sleep(2000);
            } catch (Exception ex) {
                println("Presione enter para ver si oponente termin??.");
                input.nextLine();
            }
        }

        if (ExitHandler.isExit()){
            println("\n Oponente se retir??. Ganaste por abandono. \n");
            exit();
        }

        WaitingHandler.setWaiting(true);
        println("Oponente ha terminado.\n");
    }

    private String ipInput(){
        lineReader = input.nextLine();

        while (! validator.validateIp(lineReader)){
            print("No es una IP valida. Ingrese IP: ");
            lineReader = input.nextLine();
        }
        println();
        return lineReader;
    }
    private String nameInput(){
        print("Ingrese su nombre de jugador: ");
        lineReader = input.nextLine();
        while (lineReader.length() < 3 || lineReader.length() > 20){
            println();
            print("El nombre debe tener m??nimo 3 caract??res y m??ximo 20 caract??tes.\nIngrese nombre: ");
            lineReader = input.nextLine();
        }
        println();
        return lineReader;
    }
    private int columnInput(){
        print("Ingrese columa: ");
        lineReader = input.nextLine();

        while (! validator.validateColumn(lineReader)){
            println("Error, debe colocar una columna v??lida");
            print("Ingrese columa: ");
            lineReader = input.nextLine();
        }
        return columnCharToInt( lineReader.charAt(0) );
    }
    private int rowInput(){
        print("Ingrese fila: ");
        lineReader = input.nextLine();
        while (! validator.validateRow(lineReader)){
            println("Error, debe colocar una fila v??lida");
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
    private void informAndExit(Client client){
        client.informExit();
        client.informEndTurn();
        exit();
    }
    public void clearScreen() {

        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

}

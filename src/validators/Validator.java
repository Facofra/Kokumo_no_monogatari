package validators;

import entities.Field;
import entities.Player;

public class Validator {
    private int boardSize;
    private int numberOfNinjas;
    private String message;

    public Validator(int boardSize, int numberOfNinjas) {
        this.boardSize = boardSize;
        this.numberOfNinjas = numberOfNinjas;
    }

    public boolean validateColumn(String lineReader){
        if (lineReader.length()!= 1){
            return false;
        }
        char columnCharacter = lineReader.charAt(0);
        int columnInt = (int) columnCharacter;


        if (columnInt >= 97 && columnInt <= 122){
            columnInt-=32;
        }
        columnInt-= 65;


        return columnInt>=0 && columnInt < boardSize;

    }

    public boolean validateRow(String lineReader){
        int row=-1;
        try {
            row = Integer.valueOf( lineReader );
        }catch (Exception ex){
            System.out.println();
        }

        return row > 0 && row <= boardSize;
    }

    public boolean validateYesOrNo(String lineReader){
        return lineReader.equals("S") || lineReader.equals("s") || lineReader.equals("N") || lineReader.equals("n");
    }

    public boolean validatePlayerAction(String lineReader){
        return lineReader.equals("S") || lineReader.equals("s") || lineReader.equals("A") || lineReader.equals("a") || lineReader.equals("M") || lineReader.equals("m");
    }

    public boolean validateMovement(int originColumn, int originRow, int destinationColumn, int destinationRow, Field[][] board){
        //   chequear si no hay ninja, y si es un espacio dentro de la matriz
        if (destinationColumn > boardSize || destinationRow > boardSize || destinationColumn<0 || destinationRow<0){
            message="La coordenada indicada no existe.";
            return false;
        }
        if (board[destinationRow][destinationColumn].getNinja() != null){
            message="En esa coordenada ya hay un ninja.";
            return false;
        }
        if (! board[destinationRow][destinationColumn].isTransitable()){
            message="Esa coordenada no es transitable.";
            return false;
        }
        if (Math.abs(originColumn-destinationColumn ) > 1 || Math.abs(originRow-destinationRow ) > 1  ){
            message="Esa coordenada no es adyacente al ninja.";
            return false;
        }

        return true;
    }

    public boolean validateCanMove(Player actualPlayer, int i, int j){
        if (! actualPlayer.isCommanderAlive()){
            message="Ninja no se puede mover, Comandante no está vivo.";
            return false;
        }
        if (! actualPlayer.getNinjaFromBoard(i,j).isCanMove()){
            message="Ninja no se puede mover, se ha movido el turno anterior.";
            return false;
        }
        if (! validateFreeSpace(i,j,actualPlayer.getBoard().getFields())){
            message="Ninja no se puede mover, no tiene espacio libre al rededor.";
            return false;
        }
        return true;
    }

    public boolean validateFreeSpace(int row, int column, Field[][] board){

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (row+i >= 0 && row+i < boardSize && column+j >= 0 && column+j < boardSize){
                    if (board[row+i][column+j].isTransitable()){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public String getMessage() {
        return message;
    }
}

package entities;

public class Screen {

    public void renderBoard(Board board){
        Field[][] gameBoard = board.getBoard();

        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j].getNinja() == null){
                    print("#");
                }else {
                    print(gameBoard[i][j].getNinja().getSprite());
                }
            }
            println();
        }
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


}
//          A   B   C   D   E
//        _____________________
//      1 |   |   |   |   |   |
//        |___|___|___|___|___|
//      2 |   |   |   |   |   |
//        |___|___|___|___|___|
//      3 |   |   |   |   |   |
//        |___|___|___|___|___|
//      4 |   |   |   |   |   |
//        |___|___|___|___|___|
//      5 |   |   |   |   |   |
//        |___|___|___|___|___|
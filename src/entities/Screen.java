package entities;

public class Screen {

    public void render(Board board){
        Field[][] gameBoard = board.getBoard();

        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j].getNinja() == null){
                    System.out.print("#");
                }else {
                    System.out.print(gameBoard[i][j].getNinja().getSprite());
                }
            }
            System.out.println();
        }
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
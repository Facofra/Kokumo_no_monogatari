

public class Main {
    public static void main(String[] args) {


        while (true){
            final int boardSize=5;
            final int numberOfNinjas = 3;
            Game game = new Game(boardSize,numberOfNinjas);
            game.run();
        }

    }

}

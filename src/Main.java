import java.io.IOException;
public class Main {
    public static void main(String[] args) {
        final int boardSize=5;
        final int numberOfNinjas = 3;
        final String[] sprites = {"C","N1","N2"};
        Game game = new Game(boardSize,numberOfNinjas,sprites);
        game.run();



    }
}

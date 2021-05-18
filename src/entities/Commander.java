package entities;


public class Commander extends Ninja {
    private int lives;

    public Commander(String sprite, int lives) {
        super(sprite);
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }
}

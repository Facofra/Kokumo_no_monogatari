package enums;

public enum NinjaType {
    NORMAL(1,'N'),COMMANDER(2,'C');
    private int lives;
    private char sprite;

    NinjaType(int lives, char sprite){
        this.lives=lives;
        this.sprite=sprite;
    }

    public int getLives() {
        return lives;
    }

    public char getSprite() {
        return sprite;
    }
}

package entities;

public class Ninja {
    private boolean canMove;
    private String sprite;

    public Ninja(String sprite) {
        this.sprite = sprite;
    }


    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public String getSprite() {
        return sprite;
    }
}

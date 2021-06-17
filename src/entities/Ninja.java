package entities;

import enums.NinjaType;

public class Ninja {
    private boolean canMove;
    private char sprite;
    private NinjaType ninjaType;
    private int lives;
    private boolean hasActed;

    public Ninja(NinjaType ninjaType) {
        this.ninjaType = ninjaType;
        this.lives = ninjaType.getLives();
        this.sprite = ninjaType.getSprite();
        this.canMove=true;
        this.hasActed = false;
    }


    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public char getSprite() {
        return sprite;
    }

    public NinjaType getNinjaType() {
        return ninjaType;
    }

    public int getLives() {
        return lives;
    }

    public void recieveAttack(){
        lives-=1;
    }

    public boolean hasActed() {
        return hasActed;
    }

    public void setHasActed(boolean hasActed) {
        this.hasActed = hasActed;
    }
}

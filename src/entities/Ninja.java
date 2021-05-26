package entities;

import enums.NinjaType;

public class Ninja {
    private boolean canMove;
    private char sprite;
    private NinjaType ninjaType;
    private int lives;
    private int rowPosition;
    private int columnPosition;
    private boolean hasActed;

    public Ninja(NinjaType ninjaType) {
        this.ninjaType = ninjaType;
        this.lives = ninjaType.getLives();
        this.sprite = ninjaType.getSprite();
        this.rowPosition=-1;
        this.columnPosition=-1;
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

    public int getRowPosition() {
        return rowPosition;
    }

    public void setRowPosition(int rowPosition) {
        this.rowPosition = rowPosition;
    }

    public int getColumnPosition() {
        return columnPosition;
    }

    public void setColumnPosition(int columnPosition) {
        this.columnPosition = columnPosition;
    }

    public NinjaType getNinjaType() {
        return ninjaType;
    }

    public int getLives() {
        return lives;
    }

    public boolean hasActed() {
        return hasActed;
    }

    public void setHasActed(boolean hasActed) {
        this.hasActed = hasActed;
    }
}

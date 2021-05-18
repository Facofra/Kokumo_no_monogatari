package entities;

public class Field {
    private Ninja ninja;
    private boolean isTransitable;

    public void setNinja(Ninja ninja) {
        this.ninja = ninja;
    }

    public void setTransitable(boolean transitable) {
        isTransitable = transitable;
    }

    public Ninja getNinja() {
        return ninja;
    }

    public boolean isTransitable() {
        return isTransitable;
    }
}

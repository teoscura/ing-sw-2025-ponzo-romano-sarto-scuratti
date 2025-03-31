package it.polimi.ingsw.model.rulesets;

public abstract class GameState {
    public abstract void gameLoop();
    public abstract boolean hasFinished();
    public abstract GameState getNext();
}

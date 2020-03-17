package battle;

public abstract class Control {
    Game game;
    int numPlayer;

    public Control(Game game, int numPlayer) {
        this.game = game;
        this.numPlayer = numPlayer;
    }

    public abstract void startOfTurn();
    public abstract void endOfTurn();
}

package battle;

import javax.swing.*;
import java.util.Random;
import java.util.logging.Logger;

public class Game {
    private static final Logger logger = Logger.getLogger("battle.JavaNavalBattle");
    private static final int SECONDS_ON_TURN = 10; //number of update per second.

    PlayerField[] playerFields;
    Control[] controls;

    int numUserPlayer;
    int numCurrentPlayer;

    long endTurnNanoTime;

    private GameState gameState;

    public Game(int cols, int rows, int countPlayers) {
        playerFields = new PlayerField[countPlayers];
        Ranges.setSize(new Coord(cols, rows));

        controls = new Control[countPlayers];
    }

    public void start() {
        Random random = new Random();
        numUserPlayer = random.nextInt(controls.length);

        for (int i = 0; i < controls.length; i++) {
            if (i == numUserPlayer) {
                controls[i] = new UserControl(this, i);
            } else {
                controls[i] = new AIControl(this, i);
            }
        }

        numCurrentPlayer = 0;
        controls[numCurrentPlayer].startOfTurn();

        int[] shipsCount = new int[4];
        shipsCount[0] = 4;
        shipsCount[1] = 3;
        shipsCount[2] = 2;
        shipsCount[3] = 1;

        PlayerFieldGenerator playerFieldGenerator = new PlayerFieldGenerator();

        for (int i = 0; i < playerFields.length; i++) {
            playerFields[i] = playerFieldGenerator.playerField(Ranges.getSize().x, Ranges.getSize().y, shipsCount);
        }

        gameState = GameState.PLAYED;
        resetTimeOnTurn();
    }

    public void update() {
        if (endTurnNanoTime - System.currentTimeMillis() <= 0) {
            turnPlayer();
        }
    }

    void initPanel(JPanel panel) {
        //this.panel = panel;
        UserControl userControl = (UserControl) controls[numUserPlayer];
        userControl.initPanel(panel);
    }

    public boolean isPlayerTurn() {
        return numUserPlayer == numCurrentPlayer;
    }

    public CellType getCellType(int numPlayer, Coord coord) {
        return playerFields[numPlayer].get(coord, this.numUserPlayer == numPlayer);
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getNumUserPlayer() {
        return numUserPlayer;
    }

    public int getNumEnemyPlayer(int numPlayer) {
        int numEnemyPlayer = numPlayer + 1;
        if (numEnemyPlayer >= playerFields.length) {
            return 0;
        }

        return numEnemyPlayer;
    }

    public long getRemainingTimeOnTurn() {
        return (endTurnNanoTime - System.currentTimeMillis()) / 1000;
    }

    public void wellAimedShot() {
        controls[numCurrentPlayer].endOfTurn();

        controls[numCurrentPlayer].startOfTurn();
        resetTimeOnTurn();
    }

    public void turnPlayer() {
        controls[numCurrentPlayer].endOfTurn();

        numCurrentPlayer++;
        if (numCurrentPlayer >= playerFields.length) {
            numCurrentPlayer = 0;
        }

        controls[numCurrentPlayer].startOfTurn();
        resetTimeOnTurn();
    }

    void checkWin() {

    }

    boolean isAllowToAttack(int numPlayer, Coord coord) {
        return playerFields[numPlayer].isAllowToAttack(coord);
    }

    HitType shotToCell(int numPlayer, Coord coord) {
        return playerFields[numPlayer].shotCell(coord);
    }

    void resetTimeOnTurn() {
        endTurnNanoTime = System.currentTimeMillis() + (1000 * SECONDS_ON_TURN);
    }
}

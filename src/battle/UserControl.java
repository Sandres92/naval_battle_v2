package battle;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

public class UserControl extends Control {
    private static final Logger logger = Logger.getLogger("battle.UserControl");

    boolean isMyTurn = false;

    public UserControl(Game game, int numPlayer) {

        super(game, numPlayer);
    }

    void initPanel(JPanel panel) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                makeShot(e.getX(), e.getY());
            }
        });
    }

    public void startOfTurn() {
        isMyTurn = true;
    }

    public void endOfTurn() {
        isMyTurn = false;
    }

    void makeShot(int x, int y) {
        if (!isMyTurn) {
            return;
        }

        Coord coordClick = getCoordClick(x, y);

        if (coordClick.x < 0) {
            return;
        }

        if (!game.isAllowToAttack(game.getNumEnemyPlayer(numPlayer), coordClick)) {
            return;
        }

        HitType hitType = game.shotToCell(game.getNumEnemyPlayer(numPlayer), coordClick);
        if (hitType == HitType.Hit || hitType == HitType.Destroy) {
            game.wellAimedShot();
            return;
        }

        game.turnPlayer();
    }

    Coord getCoordClick(int x, int y) {
        int startX = x - ((Ranges.getSize().x * Ranges.getImageSize()) + Ranges.getImageSize()) * game.getNumEnemyPlayer(numPlayer);
        int startY = y - Ranges.getImageSize();

        if (startX < 0 || startY < 0) {
            return new Coord(-1, -1);
        }

        Coord coord = new Coord(startX / Ranges.getImageSize(), startY / Ranges.getImageSize());

        if (coord.x >= Ranges.getSize().x || coord.y >= Ranges.getSize().y) {
            return new Coord(-1, -1);
        }

        return coord;
    }
}

package battle;

import java.util.*;
import java.util.logging.Logger;

public class AIControl extends Control {
    private static final Logger logger = Logger.getLogger("battle.AIControl");

    Coord firstHitCoord;
    Coord lastHitCoord;
    List<Coord> allowAttackedCoords;
    List<Coord> attackDirectionsToSerchLine = new ArrayList<>();
    Coord[] lineAttackDirection;
    int indexLineAttackDirection;

    Timer timer;

    public AIControl(Game game, int numPlayer) {
        super(game, numPlayer);

        allowAttackedCoords = new ArrayList<>();
        for (int i = 0; i < Ranges.getSize().x; i++) {
            for (int j = 0; j < Ranges.getSize().y; j++) {
                allowAttackedCoords.add(new Coord(i, j));
            }
        }

        firstHitCoord = new Coord(-1, -1);
        lastHitCoord = new Coord(-1, -1);
    }

    public void startOfTurn() {
        timer = new Timer();

        //время обдуманимания хода ботом
        //long secThink = 2;
        //TODO: в тестовом режиме бот не думает совсем
        long secThink = 0;
        timer.schedule(
                new TimerTask() {
                    public void run() {
                        makeShot();
                    }
                },
                secThink);
    }

    void makeShot() {
        timer.cancel();

        if (firstHitCoord.x < 0) {
            shotRandomCell();
            return;
        }

        shotOnDirectionLine();
    }

    void shotRandomCell() {
        Random random = new Random();

        Coord coordForAttack = new Coord(-1, -1);
        int allowAttackedCoordsSize = allowAttackedCoords.size();

        for (int i = 0; i < allowAttackedCoordsSize; i++) {
            int indexAllowAttackedCoords = random.nextInt(allowAttackedCoords.size());
            coordForAttack = allowAttackedCoords.get(indexAllowAttackedCoords);
            allowAttackedCoords.remove(indexAllowAttackedCoords);

            if (game.isAllowToAttack(game.getNumEnemyPlayer(numPlayer), coordForAttack)) {
                break;
            }
        }

        //такой ситуации случиться не должно
        if (coordForAttack.x < 0) {
            logger.warning("ИИ не нашёл координату дл атаки!!!!!!!!");
            allowAttackedCoords.clear();

            game.turnPlayer();

            return;
        }


        HitType hitType = game.shotToCell(game.getNumEnemyPlayer(numPlayer), coordForAttack);
        if (hitType == HitType.NotHit) {
            game.turnPlayer();
            return;
        }

        firstHitCoord = coordForAttack;

        List<Coord> attackDirectionTemp = new ArrayList<>(4);
        attackDirectionTemp.add(new Coord(-1, 0));
        attackDirectionTemp.add(new Coord(0, 1));
        attackDirectionTemp.add(new Coord(1, 0));
        attackDirectionTemp.add(new Coord(0, -1));

        attackDirectionsToSerchLine = new ArrayList<>(attackDirectionTemp.size());

        int attackDirectionTempSize = attackDirectionTemp.size();
        for (int i = 0; i < attackDirectionTempSize; i++) {
            int indexAttackDirectionTemp = random.nextInt(attackDirectionTemp.size());
            Coord directionAttack = attackDirectionTemp.get(indexAttackDirectionTemp);
            attackDirectionTemp.remove(indexAttackDirectionTemp);

            attackDirectionsToSerchLine.add(directionAttack);
        }

        game.wellAimedShot();
    }

    void shotOnDirectionLine() {
        if (lastHitCoord.x < 0) {
            searchAttackLine();
            return;
        }

        Coord coordForAttack = lastHitCoord.plus(lineAttackDirection[indexLineAttackDirection]);
        if (game.isAllowToAttack(game.getNumEnemyPlayer(numPlayer), coordForAttack)) {
            indexLineAttackDirection++;

            if (indexLineAttackDirection >= lineAttackDirection.length) {
                resetAttackDatas();
                makeShot();

                return;
            }

            shotOnDirectionLine();
            lastHitCoord = firstHitCoord;

            return;
        }

        HitType hitType = game.shotToCell(game.getNumEnemyPlayer(numPlayer), coordForAttack);
        if (hitType == HitType.NotHit) {
            lastHitCoord = firstHitCoord;
            indexLineAttackDirection++;

            game.turnPlayer();

            return;
        }

        if (hitType == HitType.Destroy) {
            resetAttackDatas();
            makeShot();

            return;
        }

        lastHitCoord = coordForAttack;

        game.wellAimedShot();
    }

    void searchAttackLine() {
        Random random = new Random();

        int attackDirectionsSize = attackDirectionsToSerchLine.size();
        Coord coordForAttack = firstHitCoord;
        Coord attackDirection = new Coord(0, 0);

        for (int i = 0; i < attackDirectionsSize; i++) {
            int indexAttackDirectionTemp = random.nextInt(attackDirectionsToSerchLine.size());
            Coord attackDirectionTemp = attackDirectionsToSerchLine.get(indexAttackDirectionTemp);

            attackDirectionsToSerchLine.remove(indexAttackDirectionTemp);

            coordForAttack = firstHitCoord.plus(attackDirectionTemp);
            attackDirection = attackDirectionTemp;
            if (game.isAllowToAttack(game.getNumEnemyPlayer(numPlayer), coordForAttack)) {
                break;
            }
        }

        allowAttackedCoords.remove(coordForAttack);

        HitType hitType = game.shotToCell(game.getNumEnemyPlayer(numPlayer), coordForAttack);
        if (hitType == HitType.Destroy) {

            resetAttackDatas();
            makeShot();

            return;
        }

        if (hitType == HitType.Hit) {
            // расмер массива 2 т.к. мы можем стрелять только по 2 направлениям, ввер-вниз, лево-право
            lineAttackDirection = new Coord[2];
            lineAttackDirection[0] = attackDirection;
            lineAttackDirection[1] = new Coord(attackDirection.x * -1, attackDirection.y * -1);
            indexLineAttackDirection = 0;

            lastHitCoord = coordForAttack;

            makeShot();

            return;
        }

        game.turnPlayer();
    }

    public void endOfTurn() {
        timer.cancel();
    }

    void resetAttackDatas() {
        firstHitCoord = new Coord(-1, -1);
        lastHitCoord = new Coord(-1, -1);
        attackDirectionsToSerchLine.clear();
    }
}

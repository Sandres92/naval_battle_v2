package battle;

import java.util.List;
import java.util.logging.Logger;

public class PlayerField {
    private static final Logger logger = Logger.getLogger("battle.PlayerField");

    Cell[][] cells;
    Ship[] ships;

    public PlayerField(Cell[][] cells, Ship[] ships) {
        this.cells = cells;
        this.ships = ships;

        for (int i = 0; i < ships.length; i++) {
            for (int j = 0; j < ships[i].getCellCoords().length; j++) {
                Coord coordTemp = ships[i].getCellCoords()[j];
                this.cells[coordTemp.x][coordTemp.y].setShip(i, ships[i].getCellType());
            }
        }
    }

    public CellType get(Coord coord, boolean isUser) {
        if (!Ranges.inRange(coord)) {
            return null;
        }

        if (isUser) {
            return cells[coord.x][coord.y].getCellTypePlayer();
        }

        return cells[coord.x][coord.y].getCellTypeEnemy();
    }

    public HitType shotCell(Coord coord) {
        Cell cellTemp = cells[coord.x][coord.y];
        cellTemp.setIsAttacked(true);

        if (!cellTemp.isShip()) {
            return HitType.NotHit;
        }

        Ship shipTemp = ships[cellTemp.getShipIndex()];
        shipTemp.addDamage();

        if(!shipTemp.getIsDestroyed()){
            return HitType.Hit;
        }

        for (int i = 0; i < shipTemp.getCellCoords().length; i++) {
            Coord coordTemp = shipTemp.getCellCoords()[i];
            this.cells[coordTemp.x][coordTemp.y].setIsDestroyed(true);
        }

        List<Coord> aroudCoord = Ranges.getCoordAround(shipTemp.getCellCoords());
        for (Coord coordTemp : aroudCoord) {
            this.cells[coordTemp.x][coordTemp.y].setIsAttacked(true);
        }

        return HitType.Destroy;
    }

    boolean isAllowToAttack(Coord coord) {
        if (!Ranges.inRange(coord)) {
            return false;
        }

        return cells[coord.x][coord.y].isAllowToAttack();
    }
}

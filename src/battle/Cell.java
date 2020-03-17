package battle;

import java.util.logging.Logger;

public class Cell {
    Coord coord;

    CellType cellType;

    int shipIndex;

    boolean isAttacked;
    boolean isDestroyed;

    private static final Logger logger = Logger.getLogger("battle.Cell");

    public Cell(Coord coord) {
        this.coord = coord;
        this.cellType = CellType.EMPTY;

        shipIndex = -1;
    }

    public void setShip(int shipIndex, CellType cellType) {
        this.cellType = cellType;
        this.shipIndex = shipIndex;
    }

    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }

    public void setIsAttacked(boolean isAttacked) {
        this.isAttacked = isAttacked;
    }

    public void setIsDestroyed(boolean isDestroyed) {
        this.isDestroyed = isDestroyed;
    }

    public void setShipIndex(int shipIndex) {
        this.shipIndex = shipIndex;
    }

    public CellType getCellTypePlayer() {
        if (isAttacked) {
            if (cellType.equals(CellType.EMPTY)) {
                return CellType.BOMB_MISSED;
            }

            return CellType.BOMB_HIT;
        }

        return cellType;
    }

    public CellType getCellTypeEnemy() {
        if (isAttacked) {
            if (cellType.equals(CellType.EMPTY)) {
                return CellType.BOMB_MISSED;
            }

            if (isDestroyed) {
                return cellType;
            }

            return CellType.BOMB_HIT;
        }

        return CellType.CLOSED;
    }

    public int getShipIndex() {
        return shipIndex;
    }

    public boolean isShip() {
        return shipIndex >= 0;
    }


    public boolean isAllowToAttack() {
        return !isAttacked && !isDestroyed;
    }
}

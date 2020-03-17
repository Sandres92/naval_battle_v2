package battle;

public class Ship {
    Coord[] cellCoords;
    CellType cellType;

    int countOfShipDecks;
    int damageCountOfShipDecks;

    public Ship(Coord[] cellCoords, int countOfShipDecks) {
        this.cellCoords = cellCoords;
        this.countOfShipDecks = countOfShipDecks;

        cellType = CellType.values()[countOfShipDecks];
    }

    public void addDamage() {
        damageCountOfShipDecks++;
    }

    public boolean getIsDestroyed() {
        return damageCountOfShipDecks >= countOfShipDecks;
    }

    public CellType getShipType() {
        return CellType.values()[countOfShipDecks];
    }

    public Coord[] getCellCoords() {
        return cellCoords;
    }

    public CellType getCellType() {
        return cellType;
    }
}

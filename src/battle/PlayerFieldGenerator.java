package battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class PlayerFieldGenerator {
    private static final Logger logger = Logger.getLogger("battle.JavaNavalBattle");

    List<Coord> allCells;
    int sizeX;
    int sizeY;

    PlayerField playerField(int sizeX, int sizeY, int[] shipsCount) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        allCells = new ArrayList<>();

        //int[] shipsCounеt; index - тип коробля. 0 - это однопалубник, 1 - двухпалубник, 2 - 3х палубник и т.д. Значение, это количество кораблей данного типа
        Cell[][] cells = new Cell[sizeX][];
        for (int i = 0; i < sizeX; i++) {
            cells[i] = new Cell[sizeY];

            for (int j = 0; j < sizeY; j++) {
                Coord cellCoord = new Coord(i, j);
                cells[i][j] = new Cell(cellCoord);

                allCells.add(cellCoord);
            }
        }

        int totalShips = 0;
        for (int value : shipsCount) {
            totalShips += value;
        }
        Ship[] ships = new Ship[totalShips];

        int indexShip = 0;
        for (int i = shipsCount.length - 1; i >=0 ; i--) {
            for (int j = 0; j < shipsCount[i]; j++) {
                ships[indexShip] = getShip(i + 1);
                indexShip++;
            }
        }

        logger.info("player was created");

        return new PlayerField(cells, ships);
    }

    Ship getShip(int countOfShipDecks) {
        Random random = new Random();
        Coord[] shipCoords = new Coord[0];

        for (int i = 0; i < allCells.size(); i++) {
            int index = random.nextInt(allCells.size());
            Coord startCoord = allCells.get(index);

            shipCoords = getCellsBySide(startCoord, new Coord(-1, 0), countOfShipDecks);
            if (shipCoords.length == countOfShipDecks) {
                break;
            }

            shipCoords = getCellsBySide(startCoord, new Coord(0, 1), countOfShipDecks);
            if (shipCoords.length == countOfShipDecks) {
                break;
            }

            shipCoords = getCellsBySide(startCoord, new Coord(1, 0), countOfShipDecks);
            if (shipCoords.length == countOfShipDecks) {
                break;
            }

            shipCoords = getCellsBySide(startCoord, new Coord(0, -1), countOfShipDecks);
            if (shipCoords.length == countOfShipDecks) {
                break;
            }
        }

        for (Coord shipCoord : shipCoords) {
            allCells.remove(shipCoord);
        }
        List<Coord> coordsAround = Ranges.getCoordAround(shipCoords);

        for (Coord coord : coordsAround) {
            allCells.remove(coord);
        }

        return new Ship(shipCoords, countOfShipDecks);
    }

    Coord[] getCellsBySide(Coord startCoord, Coord side, int countOfShipDecks) {
        List<Coord> allCoordsTemp = new ArrayList<>();
        for (Coord allCell : allCells) {
            allCoordsTemp.add(new Coord(allCell.x, allCell.y));
        }

        Coord[] tempCoords = new Coord[countOfShipDecks];
        tempCoords[0] = startCoord;

        Coord tempCoord = startCoord;

        for (int i = 1; i < countOfShipDecks; i++) {
            tempCoord = tempCoord.plus(side);

            if (!allCoordsTemp.contains(tempCoord)) {
                return new Coord[0];
            }

            if (tempCoord.x < 0 || tempCoord.y < 0 || tempCoord.x >= sizeX || tempCoord.y >= sizeY) {
                return new Coord[0];
            }

            tempCoords[i] = tempCoord;
        }

        return tempCoords;
    }
}

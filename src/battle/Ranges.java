package battle;

import java.util.ArrayList;

public class Ranges {
    private static Coord size;
    private static Coord[] directionCoord = new Coord[]{
            new Coord(-1, -1),
            new Coord(-1, 0),
            new Coord(-1, 1),
            new Coord(0, 1),
            new Coord(1, 1),
            new Coord(1, 0),
            new Coord(1, -1),
            new Coord(0, -1)
    };

    private static int imageSize;

    static void setSize(Coord sizeField) {
        size = sizeField;
    }

    static void setImageSize(int imageSizeArg) {
        imageSize = imageSizeArg;
    }

    public static Coord getSize() {
        return size;
    }

    public static int getImageSize() {
        return imageSize;
    }

    static boolean inRange(Coord coord) {
        return coord.x >= 0 && coord.x < size.x && coord.y >= 0 && coord.y < size.y;
    }

    static ArrayList<Coord> getCoordAround(Coord[] shipCoords) {
        ArrayList<Coord> listAround = new ArrayList<>();

        for (Coord shipCoord : shipCoords) {
            for (Coord directionCoord : directionCoord) {
                Coord tempCoord = shipCoord.plus(directionCoord);
                if (inRange(tempCoord) && !listAround.contains(tempCoord)) {
                    listAround.add(tempCoord);
                }
            }
        }

        return listAround;
    }
}

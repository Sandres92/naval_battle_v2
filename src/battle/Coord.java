package battle;

public class Coord {
    int x;
    int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Coord) {
            Coord equalsCoord = (Coord) o;

            return x == equalsCoord.x && y == equalsCoord.y;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Coord{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public Coord plus(Coord temp) {
        return new Coord(this.x + temp.x, this.y + temp.y);
    }

    public Coord minus(Coord temp) {
        return new Coord(this.x - temp.x, this.y - temp.y);
    }

    public Coord multiply(Coord temp) {
        return new Coord(this.x * temp.x, this.y * temp.y);
    }

    public Coord divide(Coord temp) {
        return new Coord(this.x / temp.x, this.y / temp.y);
    }
}

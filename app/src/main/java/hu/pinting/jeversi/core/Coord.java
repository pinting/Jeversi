package hu.pinting.jeversi.core;

public class Coord {
    public int x;
    public int y;

    /**
     * Create a new 2D coord.
     * @param x
     * @param y
     */
    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Default constructor.
     */
    public Coord() {
        this(0, 0);
    }
}

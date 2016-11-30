package hu.pinting.jeversi;

/**
 * Két dimenziós koordináta.
 */
public class Coord {
    public int x;
    public int y;

    /**
     * Új koordináta létrehozása a megadott paraméterekkel.
     * @param x
     * @param y
     */
    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Alapértelmezett konstruktor.
     */
    public Coord() {
        this(0, 0);
    }
}

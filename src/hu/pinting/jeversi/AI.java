package hu.pinting.jeversi;

public interface AI {
    void init(double difficulty);
    Coord get(Board board, Cell type);
}

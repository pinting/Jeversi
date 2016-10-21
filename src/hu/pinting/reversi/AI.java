package hu.pinting.reversi;

public interface AI {
    void init(double difficulty);
    Coord get(Board board, Cell type);
}

package hu.pinting.jeversi.core;

/**
 * Egy általános AI-t leíró interfész, amely bármilyen algoritmussal megvalósítható.
 */
public interface AI {
    /**
     * Init AI with the given difficulty.
     * @param difficulty Difficulty in percentage.
     */
    void init(double difficulty);

    /**
     * Find the best move on the table.
     * @param board Table to search on.
     * @param type Type of the cell to search with.
     * @return Null or a Coord.
     */
    Coord get(Board board, Cell type);
}

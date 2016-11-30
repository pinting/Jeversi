package hu.pinting.jeversi;

/**
 * Egy általános AI-t leíró interfész, amely bármilyen algoritmussal megvalósítható.
 */
public interface AI {
    /**
     * AI inicializálása egy adott nehézséggel, amit százalékban adunk meg.
     * @param difficulty
     */
    void init(double difficulty);

    /**
     * Legjobb lépés megtétele a táblán.
     * @param board Lépéshez a tábla.
     * @param type Cella típusa.
     * @return
     */
    Coord get(Board board, Cell type);
}

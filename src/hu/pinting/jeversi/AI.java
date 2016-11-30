package hu.pinting.jeversi;

/**
 * Egy általános AI-t leíró interfész, amely bármilyen algoritmussal megvalósítható.
 */
public interface AI {
    /**
     * AI inicializálása egy adott nehézséggel.
     * @param difficulty Nehézség százalékban.
     */
    void init(double difficulty);

    /**
     * Legjobb lépés megtétele a táblán.
     * @param board Lépéshez a tábla.
     * @param type Cella típusa.
     * @return Koordináta a táblán.
     */
    Coord get(Board board, Cell type);
}

package hu.pinting.jeversi.test;

import hu.pinting.jeversi.Board;
import hu.pinting.jeversi.Cell;
import hu.pinting.jeversi.Coord;
import hu.pinting.jeversi.SimpleAI;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * AI interfészt megvalósító SimpleAI tesztelésére szolgáló függvények.
 */
public class AITest {
    /**
     * AI lépésnek vizsgálata, hogy valid-e.
     * @throws Exception
     */
    @Test
    public void testMove() throws Exception {
        SimpleAI ai = new SimpleAI();
        Board board = new Board(8);

        board.init(Cell.WHITE);
        ai.init(1.0);

        Coord coord = ai.get(board, Cell.WHITE);

        assertTrue((coord.x == 4 && coord.y == 2) ||
                (coord.x == 5 && coord.y == 3) ||
                (coord.x == 2 && coord.y == 4) ||
                (coord.x == 3 && coord.y == 5));
    }

}
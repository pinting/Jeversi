package hu.pinting.jeversi.test;

import hu.pinting.jeversi.Cell;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Cell enum tesztelésére szolgáló függvények.
 */
public class CellTest {
    /**
     * Cella típusának számmá alakításának tesztje.
     * @throws Exception
     */
    @Test
    public void toNum() throws Exception {
        assertTrue(Cell.toNum(Cell.BLACK) == 1 &&
                Cell.toNum(Cell.WHITE) == -1 &&
                Cell.toNum(Cell.BLANK) == 0);
    }

    /**
     * Karakter cella típussá alakításának tesztje.
     * @throws Exception
     */
    @Test
    public void testFromByte() throws Exception {
        assertTrue(Cell.fromByte((byte)'1') == Cell.BLACK &&
                Cell.fromByte((byte)'2') == Cell.WHITE &&
                Cell.fromByte((byte)'0') == Cell.BLANK);
    }

    /**
     * Cella típus karakterré alakításának tesztelése.
     * @throws Exception
     */
    @Test
    public void testToByte() throws Exception {
        assertTrue(Cell.toByte(Cell.BLACK) == '1' &&
                Cell.toByte(Cell.WHITE) == '2' &&
                Cell.toByte(Cell.BLANK) == '0');
    }
}
package hu.pinting.jeversi.test;

import hu.pinting.jeversi.Board;
import hu.pinting.jeversi.Cell;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Board osztály tesztelésére szolgáló függvények.
 */
public class BoardTest {
    /**
     * Cellák megszámolásának tesztelése.
     * @throws Exception
     */
    @org.junit.Test
    public void testCount() throws Exception {
        Board board = new Board(8);

        board.init(Cell.WHITE);

        assertTrue(board.count(Cell.BLACK) == 2 && board.count(Cell.WHITE) == 2);
    }

    /**
     * Betöltés tesztelése.
     * @throws Exception
     */
    @org.junit.Test(expected=IOException.class)
    public void testLoadFailure() throws Exception {
        Board board = new Board(8);

        board.init(Cell.WHITE);
        board.load(new File("file_not_found_666_doom_end_foo_bar.bin"));
    }

    /**
     * Betöltés tesztelése.
     * @throws Exception
     */
    @org.junit.Test
    public void testLoad() throws Exception {
        Board board = new Board(8);

        board.init(Cell.WHITE);
        board.move(3, 5, Cell.WHITE);
        board.save(new File("test_save.bin"));
        board.load(new File("test_save.bin"));

        assertTrue(board.get(3, 3) == Cell.WHITE &&
                board.get(3, 4) == Cell.WHITE &&
                board.get(3, 5) == Cell.WHITE);
    }

    /**
     * Mentés tesztelése.
     * @throws Exception
     */
    @org.junit.Test
    public void testSave() throws Exception {
        Board board = new Board(8);

        board.init(Cell.WHITE);
        board.save(new File("test_save.bin"));
    }

    /**
     * Lépés tesztelése.
     * @throws Exception
     */
    @org.junit.Test
    public void testMove() throws Exception {
        Board board = new Board(8);

        board.init(Cell.WHITE);
        board.move(3, 5, Cell.WHITE);

        assertTrue(board.get(3, 3) == Cell.WHITE &&
                board.get(3, 4) == Cell.WHITE &&
                board.get(3, 5) == Cell.WHITE);
    }

    /**
     * Tábla másolásának tesztelése.
     * @throws Exception
     */
    @org.junit.Test
    public void testCopy() throws Exception {
        Board board = new Board(8);

        board.init(Cell.WHITE);
        Board copy = board.copy();

        for (int y = 0; y < board.size(); y++) {
            for(int x = 0; x < board.size(); x++) {
                assertFalse(board.get(x, y) != copy.get(x, y));
            }
        }
    }

    /**
     * Új tábla létrehozásának tesztelése
     * @throws Exception
     */
    @org.junit.Test
    public void testInit() throws Exception {
        Board board = new Board(8);

        board.init(Cell.WHITE);

        assertTrue(board.get(3, 3) == Cell.WHITE &&
                board.get(3, 4) == Cell.BLACK &&
                board.get(4, 3) == Cell.BLACK &&
                board.get(4, 4) == Cell.WHITE);
    }
}
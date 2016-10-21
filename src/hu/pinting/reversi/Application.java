package hu.pinting.reversi;

import java.io.File;
import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        Board board = new Board(8);

        board.init(Cell.BLACK);
        board.save(new File("test.save"));
    }
}

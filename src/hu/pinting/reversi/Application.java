package hu.pinting.reversi;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Application {
    public static void test() throws IOException {
        Board board = new Board(8);

        board.init(Cell.BLACK);
        board.save(new File("test.save"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Game();
            }
        });
    }
}

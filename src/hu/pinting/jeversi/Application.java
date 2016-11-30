package hu.pinting.jeversi;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Application {
    /**
     * Game JFrame megjelenítése.
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Game();
            }
        });
    }
}

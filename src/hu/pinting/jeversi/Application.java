package hu.pinting.jeversi;

import javax.swing.*;

/**
 * A főprogram megvalósítása, amely megjeleníti a Game JFrameet.
 */
public class Application {
    /**
     * Game JFrame megjelenítése.
     * @param args Konzol argumentumok.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Game());
    }
}

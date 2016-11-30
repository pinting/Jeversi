package hu.pinting.jeversi;

/**
 * Táblát érintő hibaüzenet.
 */
public class BoardException extends Exception {
    public BoardException(String message) {
        super(message);
    }
}

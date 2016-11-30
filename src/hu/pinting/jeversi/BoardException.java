package hu.pinting.jeversi;

/**
 * A táblát érintő hibák jelzéséért felelős osztály.
 */
public class BoardException extends Exception {
    public BoardException(String message) {
        super(message);
    }
}

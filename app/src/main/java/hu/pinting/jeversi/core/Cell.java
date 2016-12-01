package hu.pinting.jeversi.core;

public enum Cell {
    BLACK, // MAX
    WHITE, // MIN
    BLANK;

    /**
     * Check the negate of a cell.
     * @param cell
     * @return
     */
    public static Cell negate(Cell cell) {
        switch (cell) {
            case BLACK:
                return Cell.WHITE;
            default:
                return Cell.BLACK;
        }
    }

    /**
     * Convert the enum to string.
     * @param cell
     * @return
     */
    public static String toString(Cell cell) {
        switch (cell) {
            case BLACK:
                return "Black";
            case WHITE:
                return "White";
            default:
                return "";
        }
    }

    /**
     * Convert the enum to int.
     * @param cell
     * @return 1 if black, -1 if white.
     */
    public static int toNum(Cell cell) {
        switch (cell) {
            case BLACK:
                return 1;
            case WHITE:
                return -1;
            default:
                return 0;
        }
    }

    /**
     * Create an enum from number.
     * @param c
     * @return
     */
    public static Cell fromNum(int c) {
        switch (c) {
            case 1:
                return Cell.BLACK;
            case -1:
                return Cell.WHITE;
            default:
                return Cell.BLANK;
        }
    }
}

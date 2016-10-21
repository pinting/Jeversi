package hu.pinting.reversi;

public enum Cell {
    BLACK, // MAX
    WHITE, // MIN
    BLANK;

    public static Cell negate(Cell cell) {
        switch (cell) {
            case BLACK:
                return Cell.WHITE;
            default:
                return Cell.BLACK;
        }
    }

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

    public static Cell fromByte(byte c) {
        switch (c) {
            case 'b':
                return Cell.BLACK;
            case 'w':
                return Cell.WHITE;
            default:
                return Cell.BLANK;
        }
    }

    public static byte toByte(Cell type) {
        switch (type) {
            case BLACK:
                return 'b';
            case WHITE:
                return 'w';
            default:
                return 'b';
        }
    }
}

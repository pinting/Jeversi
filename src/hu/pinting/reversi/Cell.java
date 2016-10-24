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

    public static String toString(Cell cell) {
        switch (cell) {
            case BLACK:
                return "X";
            case WHITE:
                return "O";
            default:
                return "";
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
            case (byte) '1':
                return Cell.BLACK;
            case (byte) '2':
                return Cell.WHITE;
            default:
                return Cell.BLANK;
        }
    }

    public static byte toByte(Cell type) {
        switch (type) {
            case BLACK:
                return '1';
            case WHITE:
                return '2';
            default:
                return '0';
        }
    }
}

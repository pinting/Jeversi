package hu.pinting.jeversi;

public enum Cell {
    BLACK, // MAX
    WHITE, // MIN
    BLANK;

    /**
     * Átfordított cella lekérése.
     * @param cell Cella típusa.
     * @return Átfordított cella.
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
     * Az adott cella típusát reprezentáló szöved lekérése.
     * @param cell Cella típusa.
     * @return Szöveges megnevezés.
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
     * Cella számmá alakítása.
     * @param cell Cella típusa.
     * @return 1, ha fekete; 0, ha fehér, ellenben -1
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
     * Cella létrehozása karakterből - betöltéshez.
     * @param c Cellát leíró karakter.
     * @return Cella.
     */
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

    /**
     * Cella karakterré alakítása - lementéshez.
     * @param type Cella típusa.
     * @return Cellát leíró karakter.
     */
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

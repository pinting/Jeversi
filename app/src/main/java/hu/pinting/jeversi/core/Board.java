package hu.pinting.jeversi.core;

/**
 * Játékteret adó tábla
 */
public class Board {
    private Cell[][] board;

    /**
     * Create a new board.
     * @param size Size of the board (NxN).
     */
    public Board(int size) { init(size); }

    /**
     * Internal init to create a 2D array.
     * @param size Size of the board.
     */
    private void init(int size) {
        board = new Cell[size][size];
    }

    /**
     * Get the size of the board (NxN)
     * @return Size of the board.
     */
    public int size() {
        return board.length;
    }

    /**
     * Get the type of a cell.
     * @param x
     * @param y
     * @return
     */
    public Cell get(int x, int y) {
        if(x < 0 || y < 0 || x >= size() || y >= size()) {
            return null;
        }

        return board[y][x];
    }

    /**
     * Set the type of a cell.
     * @param x
     * @param y
     * @param type
     */
    public void set(int x, int y, Cell type) {
        if(get(x, y) == null) {
            return;
        }

        board[y][x] = type;
    }

    /**
     * Draw a line on the table (8 possible directions).
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param type Type to draw with.
     */
    private void drawLine(int x1, int y1, int x2, int y2, Cell type) {
        int stepX = x1 == x2 ? 0 : x1 < x2 ? 1 : -1;
        int stepY = y1 == y2 ? 0 : y1 < y2 ? 1 : -1;

        while(true)
        {
            set(x1, y1, type);

            if (x1 == x2 && y1 == y2)
            {
                return;
            }

            x1 += stepX;
            y1 += stepY;
        }
    }

    /**
     * Move on the cell by flipping enemy cells.
     * @param x
     * @param y
     * @param type Player type.
     * @param test Just test, disable actual movement.
     * @return Flipped cell count.
     */
    private int move(int x, int y, Cell type, boolean test)
    {
        int stepX, stepY, lastX, lastY, i;
        int count = 0;

        if(get(x, y) != Cell.BLANK)
        {
            return count;
        }

        for (stepX = -1; stepX <= 1; stepX += 1)
        {
            for (stepY = -1; stepY <= 1; stepY += 1)
            {
                if (stepY == 0 && stepX == 0)
                {
                    continue;
                }

                lastX = x;
                lastY = y;

                i = 0;

                while(true)
                {
                    lastX += stepX;
                    lastY += stepY;

                    // Check if the cell is an enemy cell
                    if (get(lastX, lastY) == Cell.negate(type))
                    {
                        i += value(lastX, lastY) + 1;
                        continue;
                    }

                    // Check if the cell is a player cell
                    if (get(lastX, lastY) == type)
                    {
                        // If there were no enemy cell before, break
                        if (i == 0)
                        {
                            break;
                        }

                        // If it was draw a line from the first to the last enemy cell
                        if (!test)
                        {
                            drawLine(x, y, lastX, lastY, type);
                        }

                        count += i;
                    }

                    // Break on empty cell
                    break;
                }
            }
        }

        return count;
    }

    /**
     * Make an actual movement (test = false).
     * @param x
     * @param y
     * @param type Player type.
     * @return Flipped cell count.
     */
    public int move(int x, int y, Cell type) {
        return move(x, y, type, false);
    }

    /**
     * Test a move (test = true).
     * @param x
     * @param y
     * @param type Player type.
     * @return Flipped cell count.
     */
    public int test(int x, int y, Cell type) {
        return move(x, y, type, true);
    }

    /**
     * Get the value of a cell.
     * @param x
     * @param y
     * @return Corners are worth more.
     */
    public int value(int x, int y) {
        if ((x == 0 && y == 0) ||
            (x == 0 && y == size() - 1) ||
            (x == size() - 1 && y == 0) ||
            (x == size() - 1 && y == size() - 1))
        {
            // Corner
            return 2;
        }

        if (x == 0 || y == size() - 1 || x == size() || y == 0)
        {
            // Wall
            return 1;
        }

        return 0;
    }

    /**
     * Count cells with the given type.
     * @param type
     * @return
     */
    public int count(Cell type) {
        int count = 0;

        for (int y = 0; y < size(); y++)
        {
            for (int x = 0; x < size(); x++)
            {
                if (get(x, y) == type)
                {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Count the possible moves.
     * @param type
     * @return
     */
    public int movesLeft(Cell type) {
        int count = 0;
        int x, y;

        for (y = 0; y < size(); y++)
        {
            for (x = 0; x < size(); x++)
            {
                if (test(x, y, type) > 0)
                {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Clone the board.
     * @return
     */
    public Board copy() {
        Board clone = new Board(size());

        for (int y = 0; y < size(); y++)
        {
            for (int x = 0; x < size(); x++)
            {
                clone.board[y][x] = get(x, y);
            }
        }

        return clone;
    }

    /**
     * Init the table with a given type.
     * @param type
     */
    public void init(Cell type) {
        for(int y = 0; y < size(); y++) {
            board[y] = new Cell[size()];

            for(int x = 0; x < size(); x++) {
                board[y][x] = Cell.BLANK;
            }
        }

        int c = size() / 2;

        set(c - 1, c - 1, type);
        set(c - 1, c, Cell.negate(type));
        set(c, c - 1, Cell.negate(type));
        set(c, c, type);
    }
}
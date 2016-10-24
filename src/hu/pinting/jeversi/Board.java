package hu.pinting.jeversi;

import java.io.*;

public class Board {
    private Cell[][] board;

    public Board(int size) { init(size); }

    private void init(int size) {
        board = new Cell[size][size];
    }

    public void load(File file) throws IOException, BoardException {
        byte[] save = new byte[(int) file.length()];

        DataInputStream stream = new DataInputStream(new FileInputStream(file));
        stream.readFully(save);
        stream.close();

        if(save.length != size() * size()) {
            if(save.length % Math.sqrt(save.length) == 0) {
                init((int)Math.sqrt(save.length));
            }
            else {
                throw new BoardException("Cannot process board size!");
            }
        }

        for(int y = 0; y < size(); y++) {
            for(int x = 0; x < size(); x++) {
                set(x, y, Cell.fromByte(save[y * size() + x]));
            }
        }
    }

    public void save(File file) throws IOException {
        byte save[] = new byte[size() * size()];

        for(int y = 0; y < size(); y++) {
            for(int x = 0; x < size(); x++) {
                save[y * size() + x] = Cell.toByte(get(x, y));
            }
        }

        FileOutputStream out = new FileOutputStream(file);
        out.write(save);
        out.close();
    }

    public int size() {
        return board.length;
    }

    public Cell get(int x, int y) {
        if(x < 0 || y < 0 || x >= size() || y >= size()) {
            return null;
        }

        return board[y][x];
    }

    private void set(int x, int y, Cell type) {
        if(get(x, y) == null) {
            return;
        }

        board[y][x] = type;
    }

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

                    // If it is not, then
                    if (get(lastX, lastY) == type)
                    {
                        // If there were no enemy cell(s) before, break it
                        if (i == 0)
                        {
                            break;
                        }

                        // Else, reverse them
                        if (!test)
                        {
                            drawLine(x, y, lastX, lastY, type);
                        }

                        count += i;
                    }

                    break;
                }
            }
        }

        return count;
    }

    public int move(int x, int y, Cell type) {
        return move(x, y, type, false);
    }

    public int test(int x, int y, Cell type) {
        return  move(x, y, type, true);
    }

    public int value(int x, int y) {
        if ((x == 0 && y == 0) ||
            (x == 0 && y == size() - 1) ||
            (x == size() - 1 && y == 0) ||
            (x == size() - 1 && y == size() - 1))
        {
            // If the cell is in the corner
            return 2;
        }

        if (x == 0 || y == size() - 1 || x == size() || y == 0)
        {
            // If the cell is next to the wall, but in the corner
            return 1;
        }

        return 0;
    }

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
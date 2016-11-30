package hu.pinting.jeversi;

import java.io.*;

/**
 * Játékteret adó tábla
 */
public class Board {
    private Cell[][] board;

    /**
     * Tábla létrehozása.
     * @param size
     */
    public Board(int size) { init(size); }

    /**
     * Belső inicializálás - két dimenziós tömb létrehozása a megadott méretben.
     * @param size Tábla mérete.
     */
    private void init(int size) {
        board = new Cell[size][size];
    }

    /**
     * Tábla betöltése text fájlból.
     * @param file Fájl elérési útvonala.
     * @throws IOException A fájlt nem lehet olvasni.
     * @throws BoardException Ha nem lehet feldolgozni a tartalmát.
     */
    @Deprecated
    public void loadText(File file) throws IOException, BoardException {
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

    /**
     * Tábla betöltése fájlból.
     * @param file Fájl elérési útvonala.
     * @throws BoardException Ha nem lehet feldolgozni a tábla tartalmát.
     */
    public void load(File file) throws BoardException, IOException {
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));
            board = (Cell[][])stream.readObject();
            stream.close();
        }
        catch (IOException e) {
            throw e;
        }
        catch(Exception e) {
            throw new BoardException("Cannot process board!");
        }
    }

    /**
     * Tábla mentése text fájlba.
     * @param file Fájl elérési útvonala.
     * @throws IOException Ha nem írható a fájl.
     */
    @Deprecated
    public void saveText(File file) throws IOException {
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

    /**
     * Tábla mentése fájlba.
     * @param file Fájl elérési útvonala.
     * @throws BoardException Ha nem írható a fájl.
     */
    public void save(File file) throws BoardException, IOException {
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
            stream.writeObject(board);
            stream.close();
        }
        catch (IOException e) {
            throw e;
        }
        catch(Exception e) {
            throw new BoardException("Cannot write to file!");
        }
    }

    /**
     * Tábla méretének lekérése - elég egy dimenzióban, hiszen NxN-es.
     * @return Tábla mérete.
     */
    public int size() {
        return board.length;
    }

    /**
     * Cella típusának lekérése.
     * @param x X koordináta.
     * @param y Y koordináta.
     * @return Cella típusa.
     */
    public Cell get(int x, int y) {
        if(x < 0 || y < 0 || x >= size() || y >= size()) {
            return null;
        }

        return board[y][x];
    }

    /**
     * Tábla egy cellájának módosítása.
     * @param x X koordináta.
     * @param y Y koordináta.
     * @param type Cella új értéke.
     */
    private void set(int x, int y, Cell type) {
        if(get(x, y) == null) {
            return;
        }

        board[y][x] = type;
    }

    /**
     * Vonal húzása a táblán 8 lehetséges irányban.
     * @param x1 Kiinduló pont X koordinátája.
     * @param y1 Kiinduló pont Y koordinátája.
     * @param x2 Végpont X koordinátája.
     * @param y2 Végpont Y koordinátája.
     * @param type Húzott vonalat alkotó cellák típusa.
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
     * Lépés a táblán az ellenséges cellák átfordításával.
     * @param x X koordináta.
     * @param y Y koordináta.
     * @param type Játékos típusa.
     * @param test Csak tesztelés.
     * @return
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

                    // Cella ellenőrzése, hogy ellenséges-e
                    if (get(lastX, lastY) == Cell.negate(type))
                    {
                        i += value(lastX, lastY) + 1;
                        continue;
                    }

                    // Cella ellenőrzése, hogy a sajátunk-e
                    if (get(lastX, lastY) == type)
                    {
                        // Ha nem voltak ellenséges cellák korábban break
                        if (i == 0)
                        {
                            break;
                        }

                        // Ha voltak
                        if (!test)
                        {
                            drawLine(x, y, lastX, lastY, type);
                        }

                        count += i;
                    }

                    // Üres cella
                    break;
                }
            }
        }

        return count;
    }

    /**
     * Lépés végrehajtása.
     * @param x
     * @param y
     * @param type Játékos típusa.
     * @return Az átfordított ellenséges cellák.
     */
    public int move(int x, int y, Cell type) {
        return move(x, y, type, false);
    }

    /**
     * Lépés által leütött ellenséges cellák megszámolása.
     * @param x
     * @param y
     * @param type Játékos típusa.
     * @return Az átfordított ellenséges cellák.
     */
    public int test(int x, int y, Cell type) {
        return  move(x, y, type, true);
    }

    /**
     * Cella értékének lekérése - ez alapján lép az AI.
     * @param x
     * @param y
     * @return Cella értéke.
     */
    public int value(int x, int y) {
        if ((x == 0 && y == 0) ||
            (x == 0 && y == size() - 1) ||
            (x == size() - 1 && y == 0) ||
            (x == size() - 1 && y == size() - 1))
        {
            // Ha a cella a sarokban van
            return 2;
        }

        if (x == 0 || y == size() - 1 || x == size() || y == 0)
        {
            // Ha a cella fal mellett van
            return 1;
        }

        return 0;
    }

    /**
     * Cellák megszámolása.
     * @param type Megszámolni kívánt cella.
     * @return Adott cellák száma.
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
     * Szabályszerű lépések lekérése.
     * @param type A játékos cella típusa.
     * @return Lépések száma.
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
     * Tábla klónozása.
     * @return A tábla másolata.
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
     * Tábla inicializálása egy adott cella típus szerint.
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
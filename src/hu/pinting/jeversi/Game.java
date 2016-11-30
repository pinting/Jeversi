package hu.pinting.jeversi;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;

/**
 * A játékteret megjelenítő Game JFrame.
 */
public class Game extends JFrame {
    public static final int CELL_SIZE = 50;
    public static final int GRID_WIDTH = 1;
    public static final int CELL_PADDING = CELL_SIZE / 6;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
    public static final int SYMBOL_STROKE_WIDTH = 5;

    private boolean disableMouse = false;
    private ArrayList<String> log = new ArrayList<>();
    private JFrame logFrame = null;

    private Cell player = Cell.WHITE;
    private double difficulty = 0.33;
    private int size = 8;

    private DrawCanvas canvas;
    private JMenuBar menu;
    private JLabel status;
    private Board board;
    private AI ai;

    /**
     * Játéktér megjelenítése Graphics2D segítségével.
     */
    class DrawCanvas extends JPanel {
        /**
         * Játéktér újrarajzolása repaint() esetén.
         * @param gfx Graphics objektum.
         */
        @Override
        public void paintComponent(Graphics gfx) {
            super.paintComponent(gfx);

            setBackground(Color.WHITE);
            gfx.setColor(Color.LIGHT_GRAY);

            for (int y = 1; y < board.size(); y++) {
                gfx.fillRect(0, CELL_SIZE * y - GRID_WIDTH / 2, CELL_SIZE * board.size(), GRID_WIDTH);
            }

            for (int x = 1; x < board.size(); x++) {
                gfx.fillRect(CELL_SIZE * x - GRID_WIDTH / 2, 0, GRID_WIDTH, CELL_SIZE * board.size());
            }

            Graphics2D gfx2d = (Graphics2D)gfx;

            gfx2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            gfx2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (int y = 0; y < board.size(); y++) {
                for (int x = 0; x < board.size(); x++) {
                    int x1 = x * CELL_SIZE + CELL_PADDING;
                    int y1 = y * CELL_SIZE + CELL_PADDING;

                    gfx2d.setColor(Color.BLACK);

                    if (board.get(x, y) == Cell.BLACK) {
                        int x2 = (x + 1) * CELL_SIZE - CELL_PADDING;
                        int y2 = (y + 1) * CELL_SIZE - CELL_PADDING;

                        gfx2d.fillOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
                    }
                    else if (board.get(x, y) == Cell.WHITE) {
                        gfx2d.drawOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
                    }
                }
            }
        }
    }

    /**
     * Ablak létrehozása.
     */
    public Game() {
        // Canvas létrehozása
        canvas = new DrawCanvas();
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClick(e);
            }
        });

        status = new JLabel(" ");
        status.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
        status.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));

        this.menu = new JMenuBar();

        // Game menu
        JMenu menu = new JMenu("Game");
        this.menu.add(menu);

        JMenuItem item = new JMenuItem("New");
        item.addActionListener(e -> init());
        menu.add(item);

        item = new JMenuItem("Load");
        item.addActionListener(e -> load());
        menu.add(item);

        item = new JMenuItem("Save");
        item.addActionListener(e -> save());
        menu.add(item);

        item = new JMenuItem("Log");
        item.addActionListener(e -> showLog());
        menu.add(item);

        // Difficulty menu
        menu = new JMenu("Difficulty");
        ButtonGroup group = new ButtonGroup();
        this.menu.add(menu);

        JRadioButtonMenuItem button = new JRadioButtonMenuItem("Easy");
        button.addItemListener(e -> changeDifficulty(0.33));
        button.setSelected(true);
        group.add(button);
        menu.add(button);

        button = new JRadioButtonMenuItem("Medium");
        button.addItemListener(e -> changeDifficulty(0.66));
        group.add(button);
        menu.add(button);

        button = new JRadioButtonMenuItem("Hard");
        button.addItemListener(e -> changeDifficulty(0.99));
        group.add(button);
        menu.add(button);

        // Player menu
        menu = new JMenu("Player");
        group = new ButtonGroup();
        this.menu.add(menu);

        button = new JRadioButtonMenuItem("White");
        button.addItemListener(e -> changePlayer(Cell.WHITE));
        button.setSelected(true);
        group.add(button);
        menu.add(button);

        button = new JRadioButtonMenuItem("Black");
        button.addItemListener(e -> changePlayer(Cell.BLACK));
        group.add(button);
        menu.add(button);

        // Size menu
        menu = new JMenu("Size");
        group = new ButtonGroup();
        this.menu.add(menu);

        button = new JRadioButtonMenuItem("4");
        button.addItemListener(e -> changeSize(4));
        group.add(button);
        menu.add(button);

        button = new JRadioButtonMenuItem("8");
        button.addItemListener(e -> changeSize(8));
        button.setSelected(true);
        group.add(button);
        menu.add(button);

        button = new JRadioButtonMenuItem("16");
        button.addItemListener(e -> changeSize(16));
        group.add(button);
        menu.add(button);

        // Container létrehozása
        Container container = getContentPane();

        container.setLayout(new BorderLayout());
        container.add(this.canvas, BorderLayout.CENTER);
        container.add(this.status, BorderLayout.PAGE_END);
        container.add(this.menu, BorderLayout.NORTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();

        setTitle("Jeversi");
        setResizable(false);
        setVisible(true);
    }

    /**
     * Tábla inicializálása az adott beállításokkal.
     */
    private void init() {
        board = new Board(size);
        board.init(player);

        ai = new SimpleAI();
        ai.init(difficulty);

        disableMouse = false;

        canvas.setPreferredSize(new Dimension(CELL_SIZE * board.size(), CELL_SIZE * board.size()));
        setSize(1, 1);

        revalidate();
        pack();

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(d.width / 2 - this.getSize().width / 2, d.height / 2 - this.getSize().height / 2);

        if(player == Cell.BLACK) {
            computerMove();
        }

        addStatus("You move as " + Cell.toString(player) + "!");
        repaint();
    }

    /**
     * Játékos színének változtatása.
     * @param player Fehér vagy fekete.
     */
    private void changePlayer(Cell player) {
        if(player != Cell.BLACK && player != Cell.WHITE) {
            return;
        }

        this.player = player;
        init();
    }

    /**
     * Tábla méretének megváltoztatása.
     * @param size NxN-es tábla N paramétere.
     */
    private void changeSize(int size) {
        this.size = size;
        init();
    }

    /**
     * Nehézség megváltoztatása.
     * @param difficulty Nehézség százalékban.
     */
    private void changeDifficulty(double difficulty) {
        this.difficulty = difficulty;
        init();
    }

    /**
     * Játék mentése fájlba.
     */
    private void save() {
        try {
            board.save(new File("save.bin"));
            addStatus("Game saved!");
        }
        catch (IOException e) {
            addStatus("IO error: " + e.getMessage());
        }
        catch (BoardException e) {
            addStatus("Board error: " + e.getMessage());
        }
    }

    /**
     * Játék betöltése fájlból.
     */
    private void load() {
        try {
            init();
            board.load(new File("save.bin"));
            addStatus("Game loaded!");
            repaint();
        }
        catch (IOException e) {
            addStatus("Failed to load!");
        }
        catch (BoardException e) {
            addStatus("Board error: " + e.getMessage());
        }
    }

    /**
     * Számítógép EGY lépésének kiszámolása és megtétele.
     * @return Sikerült lépni?
     */
    private boolean computerMove() {
        Coord coord = ai.get(board, Cell.negate(player));

        if(coord != null) {
            board.move(coord.x, coord.y, Cell.negate(player));
            return true;
        }

        return false;
    }

    /**
     * Számítógép lépésének/lépéseinek kiszámolása és elhelyezése a táblán.
     */
    private void computerAttack() {
        if(board.movesLeft(Cell.negate(player)) == 0) {
            addStatus(Cell.toString(Cell.negate(player)) + " passed!");
        }
        else {
            computerMove();
        }

        if(board.movesLeft(player) == 0) {
            addStatus(Cell.toString(player) + " passed!");
            computerMove();
        }

        // Nyertes kiszámolása
        if(board.movesLeft(player) == 0 && board.movesLeft(Cell.negate(player)) == 0) {
            if(board.count(player) > board.count(Cell.negate(player))) {
                addStatus(Cell.toString(player) + " won!");
            }
            else if(board.count(player) < board.count(Cell.negate(player))) {
                addStatus(Cell.toString(Cell.negate(player)) + " won!");
            }
            else {
                addStatus("Tie!");
            }
        }
        else {
            disableMouse = false;
        }

        repaint();
    }

    /**
     * Játéktéren kattintás eventje.
     * @param e Egér pozícióját tartalmazó MouseEvent.
     */
    private void onClick(MouseEvent e) {
        if(disableMouse) {
            return;
        }

        int mouseX = e.getX();
        int mouseY = e.getY();

        int y = mouseY / CELL_SIZE;
        int x = mouseX / CELL_SIZE;

        if(board.test(x, y, player) <= 0) {
            addStatus("Invalid move!");
            return;
        }

        board.move(x, y, player);
        repaint();

        disableMouse = true;

        Timer timer = new Timer(1000, event -> computerAttack());
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Új üzenet megjelenítése és hozzáadása a loghoz.
     * @param message Az új üzenet.
     */
    private void addStatus(String message) {
        status.setText(message);

        message = new SimpleDateFormat("HH:mm:ss - ").format(new Date()) + message;

        try {
            // Duplikációk elkerülése
            if(message.compareTo(log.get(log.size() - 1)) != 0) {
                log.add(message);
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            log.add(message);
        }
    }

    /**
     * Üzenetnapló megjelenítése.
     */
    private void showLog() {
        if(logFrame != null) {
            return;
        }

        logFrame = new JFrame("Log");

        JList list = new JList(log.toArray());
        JScrollPane scrollPane = new JScrollPane(list);

        logFrame.add(scrollPane);
        logFrame.pack();
        logFrame.setLocationRelativeTo(null);
        logFrame.setVisible(true);
        logFrame.setSize(260, 240);

        logFrame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                logFrame = null;
            }
        });
    }
}
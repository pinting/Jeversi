package hu.pinting.reversi;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

public class Game extends JFrame {
    public static final int CELL_SIZE = 50;
    public static final int GRID_WIDTH = 1;
    public static final int CELL_PADDING = CELL_SIZE / 6;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
    public static final int SYMBOL_STROKE_WIDTH = 5;

    private Cell player = Cell.WHITE;
    private double difficulty = 0.33;
    private int size = 8;

    private DrawCanvas canvas;
    private JMenuBar menu;
    private JLabel status;
    private Board board;
    private AI ai;

    class DrawCanvas extends JPanel {
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

            for (int y = 0; y < board.size(); y++) {
                for (int x = 0; x < board.size(); x++) {
                    int x1 = x * CELL_SIZE + CELL_PADDING;
                    int y1 = y * CELL_SIZE + CELL_PADDING;

                    if (board.get(x, y) == Cell.BLACK) {
                        gfx2d.setColor(Color.RED);

                        int x2 = (x + 1) * CELL_SIZE - CELL_PADDING;
                        int y2 = (y + 1) * CELL_SIZE - CELL_PADDING;

                        gfx2d.drawLine(x1, y1, x2, y2);
                        gfx2d.drawLine(x2, y1, x1, y2);
                    }
                    else if (board.get(x, y) == Cell.WHITE) {
                        gfx2d.setColor(Color.BLUE);
                        gfx2d.drawOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
                    }
                }
            }
        }
    }

    public Game() {
        canvas = new DrawCanvas();
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                int y = mouseY / CELL_SIZE;
                int x = mouseX / CELL_SIZE;

                if (board.test(x, y, player) > 0) {
                    board.move(x, y, player);
                    computerMove();
                }
                else {
                    status.setText("Invalid move!");
                }

                if(board.movesLeft(player) == 0 && board.movesLeft(Cell.negate(player)) == 0) {
                    status.setText(Cell.toString(player) + " won!");
                }

                repaint();
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

        // Difficulty menu
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

        button = new JRadioButtonMenuItem("12");
        button.addItemListener(e -> changeSize(12));
        group.add(button);
        menu.add(button);

        // Create container and add to JFrame
        Container container = getContentPane();

        container.setLayout(new BorderLayout());
        container.add(this.canvas, BorderLayout.CENTER);
        container.add(this.status, BorderLayout.PAGE_END);
        container.add(this.menu, BorderLayout.NORTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();

        setTitle("Reversi");
        setResizable(false);
        setVisible(true);
    }

    private void init() {
        board = new Board(size);
        board.init(player);

        ai = new SimpleAI();
        ai.init(difficulty);

        Dimension window = new Dimension(CELL_SIZE * board.size(), CELL_SIZE * board.size());

        canvas.setPreferredSize(window);
        pack();

        if(player == Cell.BLACK) {
            computerMove();
        }

        repaint();
    }

    private void changePlayer(Cell player) {
        this.player = player;
        init();
    }

    private void changeSize(int size) {
        this.size = size;
        init();
    }

    private void changeDifficulty(double difficulty) {
        this.difficulty = difficulty;
        init();
    }

    private void computerMove() {
        Coord coord = ai.get(board, Cell.negate(player));

        if(coord != null) {
            board.move(coord.x, coord.y, Cell.negate(player));
        }
    }

    private void save() {
        try {
            board.save(new File("save.bin"));
        }
        catch (IOException e) {
            status.setText("Failed to save!");
        }
    }

    private void load() {
        try {
            board.load(new File("save.bin"));
            init();
        }
        catch (IOException e) {
            status.setText("Failed to load!");
        }
        catch (BoardException e) {
            status.setText(e.getMessage());
        }
    }
}
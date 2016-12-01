package hu.pinting.jeversi.core;

import java.util.Random;

public class SimpleAI implements AI {
    private Random random = new Random();
    private Coord result;
    private int maxLevel;

    /**
     * Random quess between 0 or 1.
     * @return
     */
    private boolean quess() {
        return random.nextInt(1) == 0 ? false : true;
    }

    /**
     * Get the best move recursively.
     * @param board Board to test moves on.
     * @param type Type to test movement.
     * @param level Levels to check.
     * @param init Init value, do not search bellow this (internal, first 0).
     * @param alpha Min cut alpha value (internal, first -INF).
     * @param beta Max cut beta value (internal, first +INF).
     * @return Value of the level.
     */
    private int calculate(Board board, Cell type, int level, int init, int alpha, int beta) {
        if (level == 0)
        {
            return init;
        }

        int bestX = -1;
        int bestY = -1;

        boolean doCut = false;

        for (int y = 0; y < board.size(); y++)
        {
            for (int x = 0; x < board.size(); x++)
            {
                int value = init + Cell.toNum(type) * board.test(x, y, type);

                if (value == init)
                {
                    continue;
                }

                // Make the move on a new board
                Board next = board.copy();
                next.move(x, y, type);

                if(next.count(Cell.BLANK) == 0)
                {
                    int count = next.count(type);
                    int countInv = next.count(Cell.negate(type));

                    if (count > countInv)
                    {
                        value = Cell.toNum(type) * Integer.MAX_VALUE / 2;
                    }
                    else if (count == countInv)
                    {
                        value = Cell.toNum(Cell.negate(type)) * Integer.MAX_VALUE / 3;
                    }
                    else
                    {
                        value = Cell.toNum(Cell.negate(type)) * Integer.MAX_VALUE / 2;
                    }
                }
                else
                {
                    value = calculate(next, Cell.negate(type), level - 1, value, alpha, beta);
                }

                // Max
                if (type == Cell.BLACK && value > alpha || (quess() && value == alpha))
                {
                    alpha = value;
                    bestX = x;
                    bestY = y;
                }

                // Min
                else if (type == Cell.WHITE && value < beta || (quess() && value == beta))
                {
                    beta = value;
                    bestX = x;
                    bestY = y;
                }

                // Cut
                if (alpha >= beta)
                {
                    doCut = true;
                    break;
                }
            }

            if (doCut)
            {
                break;
            }
        }

        if (bestX >= 0 && bestY >= 0)
        {
            result = new Coord(bestX, bestY);
        }

        return type == Cell.BLACK ? alpha : beta;
    }

    /**
     * Init the AI with a difficulty.
     * @param difficulty Difficulty in percentage.
     */
    @Override
    public void init(double difficulty) {
        this.maxLevel = (int)(difficulty * 5.0);
    }

    /**
     * Get the best move on the board.
     * @param board Table to search on.
     * @param type Type of the cell to search with.
     * @return
     */
    @Override
    public Coord get(Board board, Cell type) {
        result = null;

        calculate(board, type, maxLevel, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

        if(result != null) {
            return new Coord(result.x, result.y);
        }

        return null;
    }
}

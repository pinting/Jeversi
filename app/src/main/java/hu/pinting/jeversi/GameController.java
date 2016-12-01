package hu.pinting.jeversi;

import android.content.Context;

import hu.pinting.jeversi.core.AI;
import hu.pinting.jeversi.core.Board;
import hu.pinting.jeversi.core.Cell;
import hu.pinting.jeversi.core.Coord;
import hu.pinting.jeversi.core.SimpleAI;

public class GameController {
    private Settings settings;
    private Board board;
    private AI ai;

    public GameController(Settings s) {
        settings = s;

        reset();
    }

    /**
     * Reset the state of the controller.
     */
    public void reset() {
        board = new Board(settings.getSize());
        board.init(settings.getPlayer());

        ai = new SimpleAI();
        ai.init(settings.getDifficulty());

        if(settings.getPlayer() == Cell.BLACK) {
            moveEnemy();
        }
    }

    /**
     * Get board
     * @return
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Move the player on the board.
     * @param context Android Context.
     * @param x
     * @param y
     * @return
     */
    public boolean movePlayer(Context context, int x, int y) {
        if(this.board.test(x, y, settings.getPlayer()) <= 0) {
            return false;
        }

        this.board.move(x, y, settings.getPlayer());

        return true;
    }

    /**
     * Move an actual enemy cell on the board.
     * @return
     */
    private boolean moveEnemy() {
        Coord coord = ai.get(board, Cell.negate(settings.getPlayer()));

        if(coord != null) {
            board.move(coord.x, coord.y, Cell.negate(settings.getPlayer()));
            return true;
        }

        return false;
    }

    /**
     * Make the enemy move (with passes, with everything). Also calculate the end.
     * @param context Android Context.
     * @return
     */
    public boolean moveEnemy(Context context) {
        final Cell p = settings.getPlayer();
        
        if(board.movesLeft(Cell.negate(p)) == 0) {
            Utils.showMessage(context,
                    p == Cell.BLACK ?
                            context.getResources().getString(R.string.black_pass) :
                            context.getResources().getString(R.string.white_pass));
        }
        else {
            moveEnemy();
        }

        if(board.movesLeft(p) == 0) {
            Utils.showMessage(context,
                    Cell.negate(p) == Cell.BLACK ?
                            context.getResources().getString(R.string.black_pass) :
                            context.getResources().getString(R.string.white_pass));

            moveEnemy();
        }

        if (board.movesLeft(p) != 0 || board.movesLeft(Cell.negate(p)) != 0) {
            return true;
        }

        // Calculate the winner
        if(board.count(p) > board.count(Cell.negate(p))) {
            Utils.showDialog(context,
                    p == Cell.BLACK ?
                            context.getResources().getString(R.string.black_won) :
                            context.getResources().getString(R.string.white_won));
        }
        else if(board.count(p) < board.count(Cell.negate(p))) {
            Utils.showDialog(context,
                    Cell.negate(p) == Cell.BLACK ?
                            context.getResources().getString(R.string.black_won) :
                            context.getResources().getString(R.string.white_won));
        }
        else {
            Utils.showDialog(context, context.getResources().getString(R.string.tie));
        }

        return false;

    }
}

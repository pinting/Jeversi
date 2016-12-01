package hu.pinting.jeversi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import hu.pinting.jeversi.core.Cell;
import hu.pinting.jeversi.sqlite.PersistentDataHelper;

public class GameView extends View {
    private GameController controller;
    private PersistentDataHelper data;
    private Settings settings;

    private Context context;
    private Paint paint;

    private int symbolSize;
    private int cellSize;

    private boolean disableMouse;

    public GameView(Context c, AttributeSet attrs) {
        super(c, attrs);

        context = c;

        settings = new Settings();
        settings.load(context);

        paint = new Paint();
        paint.setAntiAlias(true);

        controller = new GameController(settings);
    }

    /**
     * Set the data helper.
     * @param dataHelper A PersistentDataHelper object.
     */
    public void setData(PersistentDataHelper dataHelper) {
        data = dataHelper;
        data.restoreBoard(controller.getBoard());
    }

    /**
     * Reset the state of the game controller.
     */
    public void resetController() {
        settings.load(context);
        controller.reset();

        if(data != null) {
            data.restoreBoard(controller.getBoard());
        }

        invalidate();
    }

    /**
     * Resize the canvas to fit view.
     */
    private void resizeCanvas() {
        int w = getWidth();
        int h = getHeight();
        int size = w < h ? w : h;

        cellSize = size / settings.getSize();
        symbolSize = cellSize / 3;
    }

    /**
     * A click on the game view.
     * @param e Position of the mouse.
     */
    private void onClick(MotionEvent e) {
        if (disableMouse) {
            return;
        }

        int x = (int)(e.getX() / cellSize);
        int y = (int)(e.getY() / cellSize);

        if(!controller.movePlayer(context, x, y)) {
            return;
        }

        if(data != null) {
            data.saveBoard(controller.getBoard());
        }

        invalidate();

        Handler handler = new Handler();
        disableMouse = true;

        handler.postDelayed(new Runnable() {
            public void run() {
                controller.moveEnemy(context);

                if(data != null) {
                    data.saveBoard(controller.getBoard());
                }

                disableMouse = false;
                
                invalidate();
            }
        }, 500);
    }

    /**
     * Redraw the game view on case of an invalidate()
     * @param canvas Canvas to draw on.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int white = Color.parseColor("#ffffff");
        final int green = Color.parseColor("#d4f7d4");
        final int red = Color.parseColor("#ff0000");
        final int blue = Color.parseColor("#0000ff");
        final int gray = Color.parseColor("#808080");
        final int size = settings.getSize();
        final int grid = 2;

        resizeCanvas();

        // Draw playable area background
        paint.setColor(white);

        canvas.drawRect(0, 0, cellSize * size, cellSize * size, paint);

        // Draw cells
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int x1 = x * cellSize;
                int y1 = y * cellSize;
                int x2 = (x + 1) * cellSize;
                int y2 = (y + 1) * cellSize;

                if(!disableMouse && controller.getBoard().test(x, y, settings.getPlayer()) > 0) {
                    paint.setColor(green);
                    canvas.drawRect(x1, y1, x2, y2, paint);
                }

                if (controller.getBoard().get(x, y) == Cell.BLACK) {
                    paint.setColor(red);
                    canvas.drawCircle(x1 + cellSize / 2, y1 + cellSize / 2, symbolSize, paint);
                }
                else if (controller.getBoard().get(x, y) == Cell.WHITE) {
                    paint.setColor(blue);
                    canvas.drawCircle(x1 + cellSize / 2, y1 + cellSize / 2, symbolSize, paint);
                }
            }
        }

        // Draw grids
        paint.setColor(gray);

        for (int y = 0; y <= size; y++) {
            canvas.drawRect(0,
                    cellSize * y - grid / 2,
                    cellSize * size,
                    cellSize * y - grid / 2 + grid,
                    paint);
        }

        for (int x = 0; x <= size; x++) {
            canvas.drawRect(cellSize * x - grid / 2,
                    0,
                    grid + cellSize * x - grid / 2,
                    cellSize * size,
                    paint);
        }
    }

    /**
     * Executed on touch event.
     * @param e Informations about the touch.
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                onClick(e);
                break;
        }

        return true;
    }
}
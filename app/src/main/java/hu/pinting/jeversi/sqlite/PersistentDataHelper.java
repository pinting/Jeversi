package hu.pinting.jeversi.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import hu.pinting.jeversi.core.Board;
import hu.pinting.jeversi.core.Cell;
import hu.pinting.jeversi.sqlite.table.BoardTable;

public class PersistentDataHelper {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private String[] boardColumns = {
        BoardTable.Columns._id.name(),
        BoardTable.Columns.pos_x.name(),
        BoardTable.Columns.pos_y.name(),
        BoardTable.Columns.cell.name()
    };

    public PersistentDataHelper(final Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void saveBoard(Board board) {
        clearBoard();

        for(int y = 0; y < board.size(); y++) {
            for(int x = 0; x < board.size(); x++) {
                final ContentValues values = new ContentValues();

                values.put(BoardTable.Columns.pos_x.name(), x);
                values.put(BoardTable.Columns.pos_y.name(), y);
                values.put(BoardTable.Columns.cell.name(), Cell.toNum(board.get(x, y)));

                database.insert(BoardTable.TABLE_BOARD, null, values);
            }
        }
    }

    public void restoreBoard(Board board) {
        final Cursor cursor = database.query(BoardTable.TABLE_BOARD, boardColumns, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int pos_x = cursor.getInt(BoardTable.Columns.pos_x.ordinal());
            int pos_y = cursor.getInt(BoardTable.Columns.pos_y.ordinal());
            int cell = cursor.getInt(BoardTable.Columns.cell.ordinal());

            board.set(pos_x, pos_y, Cell.fromNum(cell));

            cursor.moveToNext();
        }

        cursor.close();
    }

    public void clearBoard() {
        database.delete(BoardTable.TABLE_BOARD, null, null);
    }
}